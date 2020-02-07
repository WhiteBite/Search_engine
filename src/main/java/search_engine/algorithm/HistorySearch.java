package search_engine.algorithm;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HistorySearch {
    @Getter
    @Setter
    public static Map<Path, ReportFind> history = new HashMap<>();

}
