package search_engine.algorithm;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
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

    public static void fillTable(TableView<Match> table, TreeItem<File> TreeItem) {
        TableColumn<Match, Integer> numRow = new TableColumn<>("No");
        numRow.setCellValueFactory(new PropertyValueFactory<>("numRow"));
        TableColumn<Match, String> rowInfo = new TableColumn<>("String");
        rowInfo.setCellValueFactory(new PropertyValueFactory<>("row"));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().addAll(numRow, rowInfo);


        Path tmpF = Paths.get(TreeItem.getValue().getPath());
        ReportFind reportFind = HistorySearch.history.get(tmpF);
        if (!HistorySearch.history.isEmpty() && reportFind != null) {
            table.getItems().addAll(reportFind.getMatchArr());
        }



    }
}
