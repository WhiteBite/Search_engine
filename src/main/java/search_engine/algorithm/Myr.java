package search_engine.algorithm;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class Myr {

    public static void main(String[] args) throws IOException {
        String grepfor = "SEX";
        Path path = Paths.get("C:\\test_papka\\1\\2\\log - Copy.log");
        searchFor(grepfor, path).Show();
    }

    private static final int MAPSIZE = 4 * 1024; // 4K - make this * 1024 to 4MB in a real system.

    public static ReportFind searchFor(String grepFor, Path path) throws IOException {
        ReportFind reportFind = new ReportFind();
        if (grepFor.isEmpty()) {
            reportFind.isFound = true;
            return reportFind;
        }

        //ReportFind reportFind = new ReportFind();
        reportFind.setPath(path.toString());
        final byte[] toSearch = grepFor.getBytes(StandardCharsets.UTF_8);
        int padding = 1; // need to scan 1 character ahead in case it is a word boundary.
        int lineCount = 0; 
        boolean inWord = false; 
        boolean scanToLineEnd = false; 
        try (FileChannel channel = FileChannel.open(path, StandardOpenOption.READ)) {

            final long length = channel.size();
            int pos = 0;
            while (pos < length) {
                long remaining = length - pos;
                // int conversion is safe because of a safe MAPSIZE.. Assume a reaosnably sized tosearch.
                int tryMap = MAPSIZE + toSearch.length + padding;
                int toMap = (int) Math.min(tryMap, remaining);
                // different limits depending on whether we are the last mapped segment.
                int limit = tryMap == toMap ? MAPSIZE : (toMap - toSearch.length);
                MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, pos, toMap);
                System.out.println("Mapped from " + pos + " for " + toMap);
                pos += (tryMap == toMap) ? MAPSIZE : toMap;
                for (int i = 0; i < limit; i++) {
                    final byte b = buffer.get(i);
                    if (scanToLineEnd) {
                        if (b == '\n') {
                            scanToLineEnd = false;
                            inWord = false;
                            lineCount++;
                        }
                    } else if (b == '\n') {
                        lineCount++;
                        inWord = false;
                    } else if (b == '\r' || b == ' ') {
                        inWord = false;
                    } else if (!inWord) {
                        if (wordMatch(buffer, i, toMap, toSearch)) {
                            //add match
                            reportFind.addMatch(new Match(lineCount, getString(buffer, i)));
                            scanToLineEnd = true;
                        } else {
                            inWord = true;
                        }
                    }
                }
            }
        }
        reportFind.StopTime();
        reportFind.Show();
        HistorySearch.history.put(path,reportFind);
        return reportFind;
    }

    private static boolean wordMatch(MappedByteBuffer buffer, int pos, int toMap, byte[] toSearch) {
        //assume at valid word start.
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i] != buffer.get(pos + i)) {
                return false;
            }
        }
        byte nxt = (pos + toSearch.length) == toMap ? (byte) ' ' : buffer.get(pos + toSearch.length);
        return nxt == ' ' || nxt == '\n' || nxt == '\r';
    }

    private static StringBuilder getString(MappedByteBuffer buffer, int pos) {
        //assume at valid word start.
        char tmp = ' ';
        StringBuilder row = new StringBuilder();
        for (int i = 0; tmp != '\n'; i++) {
            tmp = (char) buffer.get(pos + i);
            row.append(tmp);
        }
        return row;
    }
}