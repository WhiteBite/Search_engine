package search_engine.algorithm;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public class HistorySearch {
    @Getter
    @Setter
    public static Map<Path, ReportFind> history = new LinkedHashMap<>();

    public static ReportFind getElementByIndex(int index) {
        return (ReportFind) history.values().toArray()[index];
    }

    public static void fillTable(TableView<Match> table, TreeItem<File> newValue) {
        TableColumn<Match, Integer> numRow = new TableColumn<>("No");
        numRow.setCellValueFactory(new PropertyValueFactory<>("numRow"));
        TableColumn<Match, String> row = new TableColumn<>("String");
        row.setCellValueFactory(new PropertyValueFactory<>("row"));
        table.getColumns().addAll(numRow, row);


        Path tmpF = Paths.get(newValue.getValue().getPath());
        ReportFind reportFind = HistorySearch.history.get(tmpF);
        if (!HistorySearch.history.isEmpty() && reportFind != null) {
            table.getItems().addAll(reportFind.getMatchArr());
        }
    }
}
