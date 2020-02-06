package SEngine;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
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
            btnFind.setText("Say Hello World.");
            TreeView fileViewScene = (TreeView) scene.lookup("#fileView");
            fileViewScene.setRoot(new SimpleFileTreeItem(new File("C:\\test_papka\\")));


            btnFind.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("BtnFind Clicked");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}