package SEngine.AlgorithmOfFind;

public class Match {
    int getNum_row() {
        return numRow;
    }

    public void setNum_row(int num_row) {
        this.numRow = num_row;
    }

    StringBuilder getRow() {
        return row;
    }

    public void setRow(StringBuilder row) {
        this.row = row;
    }

    private int numRow;
    private StringBuilder row;

    Match(int numRow, StringBuilder row) {
        this.numRow = numRow;
        this.row = row;
    }
}
