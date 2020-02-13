package search_engine.algorithm;

import lombok.Getter;
@Getter
public class Match {

    private Integer numRow;
    private StringBuilder row;

    Match(int numRow, StringBuilder row) {
        this.numRow = numRow;
        this.row = row;
    }
}
