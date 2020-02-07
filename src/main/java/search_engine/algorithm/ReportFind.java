package search_engine.algorithm;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class ReportFind {
    private long timeStart;
    private long timeStop;
    private List<Match> matchArr;
    public boolean isFound;
    @Getter
    @Setter
    private String path;

    public ReportFind(long timeFind, List<Match> matchArr) {
        isFound = false;
        this.timeStart = timeFind;
        this.matchArr = matchArr;
        matchArr = new ArrayList<>();
    }

    ReportFind() {
        isFound = false;
        matchArr = new ArrayList<>();
        timeStart = System.currentTimeMillis();
    }

    public long getTimeFind() {
        return timeStart;
    }

    public void setTimeFind(long timeFind) {
        this.timeStart = timeFind;
    }

    public List<Match> getMatchArr() {
        return matchArr;
    }

    public void setMatchArr(List<Match> matchArr) {
        this.matchArr = matchArr;
    }

    void addMatch(Match match) {
        isFound = true;
        matchArr.add(match);
    }

    public void addMatch(int numRow, StringBuilder row) {
        addMatch(new Match(numRow, row));
    }

    void Show() {
        System.out.println("Path: " + getPath());
        for (var x : matchArr) {
            System.out.println(x.getNum_row() + "  " + x.getRow());
        }
        System.out.println("Search time:" + (timeStop - timeStart));
    }

    public StringBuilder getResult() {
        StringBuilder result = new StringBuilder();
        result.append("Path: ").append(getPath());
        if (!matchArr.isEmpty()) {
            for (var x : matchArr) {
                result.append(x.getNum_row()).append(" ").append(x.getRow());
            }
            result.append("Search time:").append(timeStop - timeStart).append("ms");
        }
        return result;
    }

    void StopTime() {
        timeStop = System.currentTimeMillis();
    }
}
