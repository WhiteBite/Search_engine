package search_engine;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


public class Config {
    @Getter
    @Setter
    private static boolean inWord;
    @Getter
    @Setter
    private static boolean isRoot;



    Config() {
    }
}
