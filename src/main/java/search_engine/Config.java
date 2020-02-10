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
    private static final String STATUS_MSG = "Статус: ";
    @Getter
    private static final String EMPTY_FILE_MSG = "File is Empty";
    @Getter
    private static final String SEARCH_MSG = "Идёт поиск";
    @Getter
    private static final String READY_MSG = "Готов к работе";
    @Getter
    private static final String FILTERING_MSG = "Идёт фильтрация";
    @Getter
    private static final String OPEN_DOC_MSH = "Открываю файл";


    Config() {
    }
}
