package search_engine;

import lombok.Getter;
import lombok.Setter;



public class Config {
    @Getter
    @Setter
    private static boolean inWord;
    @Getter
    @Setter
    private static boolean isRoot;

    @Getter
    private static final String EMPTY_FILE_MSG = "File is Empty";
    @Getter
    private static final String SEARCH_MSG = "Идёт поиск";
/*    @Getter
    private static final String EMPTY_FILE_MSG = "File is Empty";*/


    Config() {
    }
}
