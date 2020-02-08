package search_engine.algorithm;

import search_engine.Config;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


public class Myr {

    public static ReportFind run(String grepFor, Path path) throws IOException {
        System.out.println("Myr thread run!");
        return searchFor(grepFor,path);
    }
    private static final int MAPSIZE = 4 * 1024; // 4K - make this * 1024 to 4MB in a real system.

    private static ReportFind searchFor(String grepFor, Path path) throws IOException {
        ReportFind reportFind = new ReportFind();
        if (grepFor.isEmpty()) {
            reportFind.setFound(true);
            return reportFind;
        }
        reportFind.setPath(path.toString());
        final byte[] toSearch = grepFor.getBytes(StandardCharsets.UTF_8);
        int padding = 1; // need to scan 1 character ahead in case it is a word boundary.
        int lineCount = 0;
        boolean inWord = false;
        boolean scanToLineEnd = false;
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {
            final long length = channel.size();
            int pos = 0;
            int posL = 0;
            while (pos < length) {
                long remaining = length - pos;
                // int conversion is safe because of a safe MAPSIZE.. Assume a reaosnably sized tosearch.
                int tryMap = MAPSIZE + toSearch.length + padding;
                int toMap = (int) Math.min(tryMap, remaining);
                // different limits depending on whether we are the last mapped segment.
                int limit = tryMap == toMap ? MAPSIZE : (toMap - toSearch.length);
                MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, pos, toMap);
               // System.out.println("Mapped from " + pos + " for " + toMap);

                pos += (tryMap == toMap) ? MAPSIZE : toMap;
                for (int i = 0; i < limit; i++) {
                    final byte b = buffer.get(i);
                    if (scanToLineEnd) {
                        if (b == '\n') {
                            scanToLineEnd = false;
                            inWord = false;
                            lineCount++;
                            posL = i;
                        }
                    } else if (b == '\n') {
                        lineCount++;
                        posL = i;
                        inWord = false;
                    } else if (b == '\r' || b == ' ') {
                        inWord = false;
                    } else {
                        if (!Config.isInWord())
                            inWord = false;
                        if (!inWord) {
                            if (wordMatch(buffer, i, toMap, toSearch)) {
                                //add match

                                reportFind.addMatch(new Match(lineCount + 1, getString(buffer, posL + 1)));
                                scanToLineEnd = true;
                            } else {
                                inWord = true;
                            }
                        }
                    }
                }
            }
        }
        reportFind.StopTime();
        reportFind.Show();
        HistorySearch.history.put(path, reportFind);
        return reportFind;
    }

    private static boolean wordMatch(MappedByteBuffer buffer, int pos, int toMap, byte[] toSearch) {
        //assume at valid word start.
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i] != buffer.get(pos + i)) {
                return false;
            }
        }

        if (Config.isInWord()) {
            byte nxt = (pos + toSearch.length) == toMap ? (byte) ' ' : buffer.get(pos + toSearch.length);
            return nxt == ' ' || nxt == '\n' || nxt == '\r';
        }
        return true;
    }

    private static StringBuilder getString(MappedByteBuffer buffer, int pos) {
        //assume at valid word start.

        char tmp = ' ';
        StringBuilder row = new StringBuilder();
        try {
            for (int i = 0; tmp != '\n' && pos + i <= MAPSIZE && pos + i < buffer.capacity(); i++) {
                tmp = (char) buffer.get(pos + i);
                row.append(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return row;
    }
}