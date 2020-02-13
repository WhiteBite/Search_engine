package search_engine.algorithm;

import lombok.Getter;
import lombok.Setter;
//TODO Make it with lombok
@Getter
public class Match {

    private Integer numRow;
    private StringBuilder row;

    Match(int numRow, StringBuilder row) {
        this.numRow = numRow;
        this.row = row;
    }
}
