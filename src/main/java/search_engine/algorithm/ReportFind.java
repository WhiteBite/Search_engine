package search_engine.algorithm;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class ReportFind {
    private long timeStart;
    private long timeStop;
    private List<Match> matchArr;
    private boolean isFound;
    private String path;


    ReportFind() {
        isFound = false;
        matchArr = new ArrayList<>();
        timeStart = System.currentTimeMillis();
    }



    void addMatch(Match match) {
        isFound = true;
        matchArr.add(match);
    }


    void show() {
        System.out.println("Path: " + path);
        for (var x : matchArr) {
            System.out.println(x.getNumRow() + "  " + x.getRow());
        }
        System.out.println("Search time:" + (timeStop - timeStart));
    }

    public StringBuilder getResult() {
        StringBuilder result = new StringBuilder();
        result.append("Path: ").append(path).append("\n");
        if (!matchArr.isEmpty()) {
            for (var x : matchArr) {
                result.append(x.getNumRow()).append(": ").append(x.getRow());
            }
            result.append("Search time:").append(timeStop - timeStart).append("ms");
        }
        return result;
    }

    void stopTime() {
        timeStop = System.currentTimeMillis();
    }
}
