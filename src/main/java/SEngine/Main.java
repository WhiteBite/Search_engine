package SEngine;
import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import kek.sample.Controller;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        initRootLayout(primaryStage);
   /*     try {
            *//* Create a new MenuBar. *//*
            MenuBar menu = new MenuBar();
            *//* Create new sub menus. *//*
            Menu menuFile = new Menu("File");
            Menu menuHelp = new Menu("Help");
            MenuItem about = new MenuItem("About");
            about.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    *//*
                     * Implement dialog to be prompted when users asks for
                     * details.
                     *//*
                }
            });
            menuHelp.getItems().add(about);

            *//* Adding all sub menus at ones to a MenuBar. *//*
            menu.getMenus().addAll(menuFile, menuHelp);

            *//* Create a button. *//*
            Button btn = new Button();
            btn.setText("Say Hello World.");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!");
                }
            });

            *//* Create a new ToolBar. *//*
            ToolBar tools = new ToolBar(btn);

            *//* Create a new VBox and add the menu as well as the tools. *//*
            VBox menus = new VBox();
            menus.getChildren().addAll(menu, tools);

            *//*
             * Adding a TreeView to the very left of the application window.
             *//*
            TreeView<File> fileView = new TreeView<File>(
                    new SimpleFileTreeItem(new File("C:\\")));

            *//* Creating a SplitPane and adding the fileView. *//*
            SplitPane splitView = new SplitPane();
            splitView.getItems().add(fileView);
            splitView.getItems().add(new HTMLEditor());

            *//* Create a root node as BorderPane. *//*
            BorderPane root = new BorderPane();

            *//* Adding the menus as well as the content pane to the root node. *//*
            root.setTop(menus);
            root.setCenter(splitView);

            *//*
             * Setting the root node of a scene as well as the applications CSS
             * file. CSS file has to be in same src directory as this class. The
             * path is always relative.
             *//*
            Scene scene = new Scene(root, 800, 600);
//            scene.getStylesheets().add(
//                    getClass().getResource("application.css").toExternalForm());

            *//* Adding a scene to the stage. *//*
            primaryStage.setScene(scene);
            primaryStage.setTitle("FileRex");

            *//* Lift the curtain :0). *//*
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    public void initRootLayout(Stage primaryStage){
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxml/main.fxml"));
            AnchorPane anchorPane = loader.load();

            Scene scene = new Scene(anchorPane);
            primaryStage.setScene(scene);
            primaryStage.show();

            Button btn = (Button) scene.lookup("#btn");
            btn.setText("Say Hello World.");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!");
                }
            });

        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}