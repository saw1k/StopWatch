package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(final Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/views/mainView.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        primaryStage.setResizable(false);
        primaryStage.setAlwaysOnTop(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}


