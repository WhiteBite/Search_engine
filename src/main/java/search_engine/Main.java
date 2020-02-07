package search_engine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;


public class Main extends Application {
    boolean isRoot = false;

    @SneakyThrows
    public void start(Stage primaryStage) throws IOException {
        initRootLayout(primaryStage);

    }

    public void initRootLayout(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/main.fxml"));
        AnchorPane anchorPane = loader.load();
        Controller controller = loader.getController();
       // controller.setMain(this);
        Scene scene = new Scene(anchorPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}