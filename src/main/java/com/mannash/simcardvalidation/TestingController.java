package com.mannash.simcardvalidation;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestingController {

    @FXML private ImageView img_test_status_1;
    @FXML private ImageView img_test_status_2;
    @FXML private ImageView img_test_status_3;
    @FXML private ImageView img_test_status_4;
    @FXML private ImageView img_test_status_5;

    @FXML private ImageView img_test_status;
    @FXML private ImageView img_test_button;

    @FXML private ImageView img_reset_button;
    @FXML private TextField input_iccid;

    @FXML private ImageView img_status;


    public void changeImage() throws InterruptedException {
//        PauseTransition delay = new PauseTransition(Duration.seconds(2));
//        delay.setOnFinished(event -> label.setText("Delayed Hello World"));

        Image testingImage = new Image("/com/mannash/javafxapplication/fxml/images/testing-in-progress.gif");
        Image newImage = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
        Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
        Timeline timeline = new Timeline( new KeyFrame(Duration.seconds(5), event ->
                img_status.setImage(testingImage)), new KeyFrame(Duration.seconds(10), event ->
                input_iccid.setText("8991000904192380835F")), new KeyFrame(Duration.seconds(15), event ->
                img_test_status_1.setImage(newImage)),  new KeyFrame(Duration.seconds(25), event ->
                img_test_status_2.setImage(newImage)), new KeyFrame(Duration.seconds(37), event ->
                img_test_status_3.setImage(newImage)), new KeyFrame(Duration.seconds(67), event ->
                img_test_status_4.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
                img_test_status_5.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
                img_status.setImage(okImage))
        );

        timeline.play();
    }

    public void loadTestingWindow() throws IOException {
        Parent testingPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(testingPage);

        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
