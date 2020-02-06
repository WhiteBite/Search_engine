package SEngine.AlgorithmOfFind;

import java.util.ArrayList;
import java.util.List;


public class ReportFind {
    private long timeStart;
    private long timeStop;
    private List<Match> matchArr;
    public  boolean isFound =false;

    public ReportFind(long timeFind, List<Match> matchArr) {
        this.timeStart = timeFind;
        this.matchArr = matchArr;
        matchArr = new ArrayList<>();
    }

    public ReportFind() {
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
        matchArr.add(match);
    }

    public void addMatch(int numRow, StringBuilder row) {
        addMatch(new Match(numRow, row));
    }

    public void Show() {
        for (var x : matchArr) {
            System.out.println(x.getNum_row() + "  " + x.getRow());
        }
        System.out.println("Search time:" + (timeStop - timeStart));
    }

    public StringBuilder getResult() {
        StringBuilder result = new StringBuilder();
        if (matchArr.isEmpty())
            result.append("Not Found");
        else {
            isFound =true;
            result.append("Ok\n");
            for (var x : matchArr) {
                result.append(x.getNum_row()).append(" ").append(x.getRow());
            }
        }
        result.append("Search time:").append(timeStop - timeStart);
        return result;
    }

    void StopTime() {
        timeStop = System.currentTimeMillis();
    }
}
