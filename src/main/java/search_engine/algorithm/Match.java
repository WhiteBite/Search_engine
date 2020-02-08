package search_engine.algorithm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Match {
    private int numRow;
    private StringBuilder row;

    Match(int numRow, StringBuilder row) {
        this.numRow = numRow;
        this.row = row;
    }
}
