package search_engine;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import search_engine.algorithm.HistorySearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

//import javafx.scene.control.*;

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
    TextArea reportInfo;
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
                new Thread(() -> {
                    try {
                        fileView.setRoot(treeView.filterChanged(finalSFilterExt, finalSearchW));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

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
                try {
                    treeView.createTree();
                    filterExt.setText(".log");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("dir.getAbsolutePath() = " + dir.getAbsolutePath());
            }
        });


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
                    CompletableFuture.runAsync(() -> new LoaderDoc(newValue, listView).run());
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
                    if (!HistorySearch.history.isEmpty()) {
                        String q = HistorySearch.history.get(tmpF).getResult().toString();
                        textAreaReport.setText(q);
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
