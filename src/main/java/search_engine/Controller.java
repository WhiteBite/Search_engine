package search_engine;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import lombok.SneakyThrows;
import search_engine.algorithm.HistorySearch;
import search_engine.algorithm.ReportFind;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class Controller {
    private HashMap<TreeItem<File>, Tab> openTabs = new HashMap<>();
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

    public void resetValue() {
        filterExt.setText("");
        searchWord.setText("");
    }

    @FXML
    private void initialize() {
        //TODO
        treeView = new FileSystemTree();

        Config.setInWord(true);
        checkInWord.setOnAction(actionEvent -> Config.setInWord(checkInWord.isSelected()));

        //Event Button Search
        btnFind.setOnAction(event -> {
            newFind();
            if (Config.isRoot()) {
                String finalSFilterExt = filterExt.getText();
                String finalSearchW = searchWord.getText();
              /*  Platform.runLater(new Runnable() {

                    @SneakyThrows
                    @Override
                    public void run() {
                        fileView.setRoot(treeView.filterChanged(finalSFilterExt, finalSearchW));
                    }
                });;*/
                new Thread(() -> {
                    Platform.runLater(() -> {

                        try {
                            fileView.setRoot(treeView.filterChanged(finalSFilterExt, finalSearchW));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                }).start();

           /*     CompletableFuture.runAsync(() -> {
                    try {
                        Date today = new Date();
                        fileView.setRoot(treeView.filterChanged(finalSFilterExt, finalSearchW));
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }
                });*/

            }
        });


        final DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        btnDirChooser.setOnAction(event -> {
            File dir = directoryChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());
            if (dir != null) {
                Config.setRoot(true);
                treeView.setRootFolder(dir.getAbsolutePath());
                treeView.createTree();
                filterExt.setText(".log");
                System.out.println("dir.getAbsolutePath() = " + dir.getAbsolutePath());
            }
        });


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
                // Tab tab;
                if (!openTabs.containsKey(newValue)) {
                    ObservableList<String> lines = FXCollections.observableArrayList();
                    ListView<String> listView = new ListView<>(lines);
                    // CompletableFuture.runAsync(() -> new LoaderDoc().loadDoc(newValue, listView));
                    new Thread(() -> {

                        new LoaderDoc().loadDoc(newValue, listView);

                    }).start();
                    //Thread thread = new Thread(new LoaderDoc(selectedItem, listView)); //отправляю сюда listView, чтоб он наполнился данными из файла.
                    //  thread.start();
                    Tab tab = new Tab(newValue.getValue().getName());
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


    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");
        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
}
