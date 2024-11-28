package org.icesi.gifbackground.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/icesi/gifbackground/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setScene(scene);
        scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        stage.setX(480); // Set the window's horizontal position
        stage.setY(180);   // Set the window's vertical position

        stage.setTitle("Fishing Minigame");

        stage.show();
        stage.setResizable(false);


    }

    public static void main(String[] args) {
        launch();
    }
}