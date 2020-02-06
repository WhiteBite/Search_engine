package SEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import SEngine.AlgorithmOfFind.Myr;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class Main extends Application {
    boolean isRoot = false;

    public void start(Stage primaryStage) {
        initRootLayout(primaryStage);

    }

    public void initRootLayout(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/main.fxml"));
            AnchorPane anchorPane = loader.load();
            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);
            primaryStage.show();
            Button btnFind = (Button) scene.lookup("#btnFind");
            TreeView fileViewScene = (TreeView) scene.lookup("#fileView");
            TextField searchWord = (TextField) scene.lookup("#searchWord");
            TextArea reportInfo = (TextArea) scene.lookup("#reportInfo");
            TextField filter = (TextField) scene.lookup("#filter");
            FileSystemTree treeView = new FileSystemTree();


            btnFind.setOnAction(event -> {
                System.out.println("BtnFind Clicked!");
                if (isRoot) {
                    try {
                        reportInfo.setText(Myr.searchFor(searchWord.getText(), Paths.get("C:\\test_papka\\1\\2\\log - Copy.log")).getResult().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Button btnDirChooser = (Button) scene.lookup("#btnDirChooser");
            final DirectoryChooser directoryChooser = new DirectoryChooser();
            configuringDirectoryChooser(directoryChooser);
            btnDirChooser.setOnAction(event -> {
                File dir = directoryChooser.showDialog(primaryStage);
                if (dir != null) {
                    isRoot = true;
                    treeView.setRootFolder(dir.getAbsolutePath());
                    try {
                        treeView.createTree();
                        filter.setText(".log");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //   fileViewScene.setRoot(new SimpleFileTreeItem(new File(dir.getAbsolutePath())));
                    System.out.println("dir.getAbsolutePath() = " + dir.getAbsolutePath());
                }
            });
            filter.textProperty().addListener((observable, oldValue, newValue) -> {
                fileViewScene.setRoot(treeView.filterChanged(newValue));

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        // Set title for DirectoryChooser
        directoryChooser.setTitle("Select Some Directories");
        // Set Initial Directory
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }
}