package SEngine.AlgorithmOfFind;

import java.util.ArrayList;
import java.util.List;



class ReportFind {
    private long timeStart;
    private long timeStop;
    private List<Match> matchArr;

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
    void Show() {
        for (var x : matchArr) {
            System.out.println( x.getNum_row() + "  " + x.getRow());
        }
        System.out.println("Search time:" + (timeStop-timeStart) );
    }
    public void StopTime(){
        timeStop = System.currentTimeMillis();
    }
}
