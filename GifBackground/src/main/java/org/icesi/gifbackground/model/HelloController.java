package org.icesi.gifbackground.model;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    Stage stage;

    @FXML
    private Canvas canvas;
    @FXML
    private AnchorPane mainPane;

    @FXML
    public Button game1Button;

    @FXML
    public Button game2Button;

    private GraphicsContext graphicsContext;
    private Image gifImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.graphicsContext = canvas.getGraphicsContext2D();


        // Load the background GIF
        gifImage = new Image(getClass().getResource("/animations/background/back.gif").toExternalForm());

        // Thread to update the background GIF
        new Thread(() -> {
            while (true) {
                Platform.runLater(() -> {
                    graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                    graphicsContext.drawImage(gifImage, 0, 0, gifImage.getWidth(), gifImage.getHeight(),
                            0, 0, canvas.getWidth(), canvas.getHeight());
                });
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        addButton1Image();
        addButton2Image();
    }

    private void addButton1Image() {
        // Configure the first button with an image
        ImageView button1ImageView = new ImageView(new Image(getClass().getResourceAsStream("/assets/button1.png")));
        button1ImageView.setFitWidth(120);
        button1ImageView.setFitHeight(90);
        game1Button.setGraphic(button1ImageView);
        game1Button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }

    private void addButton2Image() {
        // Configure the second button with an image
        ImageView button2ImageView = new ImageView(new Image(getClass().getResourceAsStream("/assets/button2.png")));
        button2ImageView.setFitWidth(120);
        button2ImageView.setFitHeight(120);
        game2Button.setGraphic(button2ImageView);
        game2Button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
    }

    public void onGame2ButtonClick(ActionEvent actionEvent) {
        // Hide and disable buttons when the second game button is clicked
        game1Button.setVisible(false);
        game2Button.setVisible(false);

        game1Button.setDisable(true);
        game2Button.setDisable(true);
        try {
            // Load the FXML for the new screen
            FXMLLoader loader3 = new FXMLLoader(getClass().getResource("/org/icesi/gifbackground/Game2.fxml"));
            Scene scene3 = new Scene(loader3.load());

            // Get the current stage
            Stage stage3 = (Stage) game1Button.getScene().getWindow();

            // Set the position of the window
            stage3.setX(350);
            stage3.setY(0);

            // Change the scene
            stage3.setScene(scene3);
            scene3.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            stage3.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onGame1ButtonClick(ActionEvent actionEvent) {
        // Hide and disable buttons when the first game button is clicked
        game1Button.setVisible(false);
        game2Button.setVisible(false);

        game1Button.setDisable(true);
        game2Button.setDisable(true);

        try {
            // Load the FXML for the new screen
            FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/org/icesi/gifbackground/Game1.fxml"));
            Scene scene2 = new Scene(loader2.load());

            // Get the current stage
            Stage stage2 = (Stage) game1Button.getScene().getWindow();

            // Set the position of the window
            stage2.setX(350);
            stage2.setY(0);

            // Change the scene
            stage2.setScene(scene2);
            scene2.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            stage2.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
