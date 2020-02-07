package search_engine;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class Controller {
    // private HashSet<TreeItem<File>> openTabs = new HashSet<TreeItem<File>>();
    private HashMap<TreeItem<File>, Tab> openTabs = new HashMap<>();
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
    boolean isRoot = false;
    FileSystemTree treeView = new FileSystemTree();

    @FXML
    private void initialize() {
        btnFind.setOnAction(event -> {
            System.out.println("BtnFind Clicked!");
            if (isRoot) {
                try {
                    String filtExt = "";
                    String searchW = "";
                    if (filterExt.getText() != null)
                        filtExt = "";
                    if (searchWord.getText() != null)
                        searchW = "";
                    fileView.setRoot(treeView.filterChanged(filtExt, searchW));
                    //   reportInfo.setText(Myr.searchFor(searchWord.getText(), Paths.get("C:\\test_papka\\1\\2\\log - Copy.log")).getResult().toString());
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
                //fileView.setRoot(treeView.filterChanged(newValue, searchWord.getText()));
                fileView.setRoot(treeView.filterChanged(newValue, ""));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        fileView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                TreeItem<File> selectedItem = (TreeItem<File>) newValue;
                System.out.println("Selected File : " + selectedItem.getValue().getAbsolutePath());
                if (selectedItem.getValue().isFile()) {
                    // Tab tab;
                    if (!openTabs.containsKey(selectedItem)) {
                        Tab tab = new Tab(selectedItem.getValue().getName());
                        tabPane.getTabs().add(tab);
                        openTabs.put(selectedItem, tab);
                        TextArea textAreaText = new TextArea();
                        TextArea textAreaReport = new TextArea();
                        VBox tmp = new VBox(textAreaText, textAreaReport);
                        tab.setContent(tmp);
                        Path tmpF = Paths.get(selectedItem.getValue().getPath());
                        if (!HistorySearch.history.isEmpty()) {
                            String q = HistorySearch.history.get(tmpF).getResult().toString();
                            textAreaReport.setText(q);
                        }
                    }
                    tabPane.getSelectionModel().select(openTabs.get(selectedItem)); //open current tab
                    // do what ever me want
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
