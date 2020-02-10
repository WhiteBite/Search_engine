package search_engine;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import search_engine.algorithm.HistorySearch;
import search_engine.algorithm.ReportFind;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private HashMap<TreeItem<File>, Tab> openTabs = new HashMap<>();
    private Thread findThread;
    @FXML
    CheckBox checkInWord;
    @FXML
    TabPane tabPane;
    @FXML
    Button btnFind;
    @FXML
    TreeView<File> fileView;
    @FXML
    TextField searchWord;
    @FXML
    TextField filterExt;
    @FXML
    Button btnDirChooser;
    @FXML
    Label currentDir;
    @FXML
    Label btnStatus;

    private FileSystemTree treeView;

    private void newFind() {
        makeOldTab();
        HistorySearch.history.clear();
    }

    private void makeOldTab() {
        for (var item : openTabs.entrySet()) {
            item.getValue().setText("OLD_" + item.getValue().getText());
        }
    }


    private void InitBtnFind() {
        //Event Button Search
        btnFind.setOnAction(event -> {
            newFind();
            if (Config.isRoot()) {

                String finalSFilterExt = filterExt.getText();
                String finalSearchW = searchWord.getText();
                if (findThread != null && findThread.isAlive()){
                    findThread.interrupt();
                    StatusController.getStatusMsg().remove(Config.getSEARCH_MSG());
                    StatusController.ResetStatus(btnStatus);
                }
                findThread = new Thread(() -> {
                    try {
                        StatusController.getStatusMsg().add(Config.getSEARCH_MSG());
                        StatusController.ResetStatus(btnStatus);
                        var q = treeView.filterChanged(finalSFilterExt, finalSearchW);
                        Platform.runLater(() -> fileView.setRoot(q));
                        StatusController.getStatusMsg().remove(Config.getSEARCH_MSG());
                        StatusController.ResetStatus(btnStatus);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                findThread.setName("findThread");
                findThread.setDaemon(true);
                findThread.start();
            }
        });
    }

    private void InitDirectoryChooser() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        btnDirChooser.setOnAction(event -> {
            File dir = directoryChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());
            if (dir != null) {
                Config.setRoot(true);
                treeView.setRootFolder(dir.getAbsolutePath());
                treeView.createTree();
                filterExt.setText(".log");
                currentDir.setText(dir.getAbsolutePath());
                System.out.println("dir.getAbsolutePath() = " + dir.getAbsolutePath());
            }
        });
    }

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");
        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    @FXML
    private void initialize() {

        treeView = new FileSystemTree();

        Config.setInWord(true);
        checkInWord.setOnAction(actionEvent -> Config.setInWord(checkInWord.isSelected()));

        InitBtnFind();
        InitDirectoryChooser();


        //Listener FilterExt
        filterExt.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                TreeItem<File> q = treeView.filterChanged(newValue, "");
                fileView.setRoot(q);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fileView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null)
                return;
            System.out.println("Selected File : " + newValue.getValue().getAbsolutePath());
            if (newValue.getValue().isFile()) {
                if (!openTabs.containsKey(newValue)) {
                    ObservableList<String> lines = FXCollections.observableArrayList();
                    ListView<String> listView = new ListView<>(lines);
                    // CompletableFuture.runAsync(() -> new LoaderDoc().loadDoc(newValue, listView));
                    new Thread(() -> {
                        StatusController.getStatusMsg().add(Config.getOPEN_DOC_MSH());
                        StatusController.ResetStatus(btnStatus);
                        new LoaderDoc().loadDoc(newValue, listView);
                        StatusController.getStatusMsg().remove(Config.getOPEN_DOC_MSH());
                        StatusController.ResetStatus(btnStatus);
                    }).start();
                    Tab tab = new Tab(newValue.getValue().getName());
                    tab.setOnClosed(event -> openTabs.values().remove(tab)
                    );
                    tabPane.getTabs().add(tab);
                    openTabs.put(newValue, tab);
                    TextArea textAreaReport = new TextArea();

                    //insert in tab
                    VBox tmp = new VBox(listView, textAreaReport);
                    tab.setContent(tmp);
                    Path tmpF = Paths.get(newValue.getValue().getPath());

                    ReportFind reportFind = HistorySearch.history.get(tmpF);
                    if (!HistorySearch.history.isEmpty() && reportFind != null) {
                        textAreaReport.setText(reportFind.getResult().toString());
                    }
                }
                tabPane.getSelectionModel().select(openTabs.get(newValue)); //open current tab
            }
        });
    }
}
