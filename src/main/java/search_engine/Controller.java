package search_engine;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import search_engine.algorithm.HistorySearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Controller {
    // private HashSet<TreeItem<File>> openTabs = new HashSet<TreeItem<File>>();
    private HashMap<TreeItem<File>, Tab> openTabs = new HashMap<>();
    @FXML
    CheckBox checkInWord;
    @FXML
    TabPane tabPane;
    @FXML
    Button btnFind;
    @FXML
    TreeView fileView;
    @FXML
    TextField searchWord;
    @FXML
    TextArea reportInfo;
    @FXML
    TextField filterExt;
    @FXML
    Button btnDirChooser;
    boolean isRoot = false; //TODO move it on Config
    FileSystemTree treeView = new FileSystemTree();


    void newFind() {
        makeOldTab();
        HistorySearch.history.clear();
    }

    void makeOldTab() {
        for (var item : openTabs.entrySet()) {
            item.getValue().setText("OLD_" + item.getValue().getText());
        }
    }

    @FXML
    private void initialize() {

        checkInWord.setOnAction(actionEvent -> {
            Config.setInWorld(checkInWord.isSelected());
        });

        btnFind.setOnAction(event -> {
            newFind();
            if (isRoot) {
                try {
                    String filtertExt = "";
                    String searchW = "";
                    if (filterExt.getText() != null)
                        filtertExt = filterExt.getText();
                    if (searchWord.getText() != null)
                        searchW = searchWord.getText();
                    fileView.setRoot(treeView.filterChanged(filtertExt, searchW));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        final DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        btnDirChooser.setOnAction(event -> {
            File dir = directoryChooser.showDialog(((Node) event.getTarget()).getScene().getWindow());
            if (dir != null) {
                isRoot = true;
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
                fileView.setRoot(treeView.filterChanged(newValue, ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        fileView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                if (newValue == null)
                    return;
                TreeItem<File> selectedItem = (TreeItem<File>) newValue;


                System.out.println("Selected File : " + selectedItem.getValue().getAbsolutePath());
                if (selectedItem.getValue().isFile()) {
                    // Tab tab;
                    if (!openTabs.containsKey(selectedItem)) {
                        ObservableList<String> lines = FXCollections.observableArrayList();
                        ListView<String> listView = new ListView<>(lines);
                        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                        listView.setCellFactory(TextFieldListCell.forListView());
                        listView.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
                            @Override
                            public void handle(ListView.EditEvent<String> t) {
                                listView.getItems().set(t.getIndex(), t.getNewValue());
                            }
                        });
                        listView.setEditable(true); // change
                        listView.getItems().clear();
                        int numRom = 0;
                        try {
                            BufferedReader in = new BufferedReader(new FileReader(selectedItem.getValue().getAbsolutePath()));
                            String s;
                            while ((s = in.readLine()) != null) {
                                numRom++;
                                listView.getItems().add(numRom + ": " + s);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Tab tab = new Tab(selectedItem.getValue().getName());
                        tabPane.getTabs().add(tab);
                        openTabs.put(selectedItem, tab);
                        TextArea textAreaReport = new TextArea();

                        //insert in tab
                        VBox tmp = new VBox(listView, textAreaReport);
                        tab.setContent(tmp);
                        Path tmpF = Paths.get(selectedItem.getValue().getPath());
                        if (!HistorySearch.history.isEmpty()) {
                            String q = HistorySearch.history.get(tmpF).getResult().toString();
                            textAreaReport.setText(q);
                        }
                    }
                    tabPane.getSelectionModel().select(openTabs.get(selectedItem)); //open current tab

                }
            }
        });
    }


    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");
        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        // directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
}
