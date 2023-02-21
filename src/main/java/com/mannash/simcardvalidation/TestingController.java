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

    public int counter = 0;
    public void changeImage() throws InterruptedException, IOException {



        if(img_test_button.getImage().getUrl().contains("startTest.gif")) {

            if(this.counter % 2 == 0){
                Image testingImage = new Image("/com/mannash/javafxapplication/fxml/images/testInProgress.gif");
                Image newImage = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
                Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/ok.jpg");
                Image doneButton = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event ->
                        img_status.setImage(testingImage)), new KeyFrame(Duration.seconds(10), event ->
                        input_iccid.setText("8991000904448610811F")), new KeyFrame(Duration.seconds(15), event ->
                        img_test_status_1.setImage(newImage)), new KeyFrame(Duration.seconds(25), event ->
                        img_test_status_2.setImage(newImage)), new KeyFrame(Duration.seconds(37), event ->
                        img_test_status_3.setImage(newImage)), new KeyFrame(Duration.seconds(67), event ->
                        img_test_status_4.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
                        img_test_status_5.setImage(newImage)), new KeyFrame(Duration.seconds(77), event ->
                        img_status.setImage(okImage)), new KeyFrame(Duration.seconds(77), event ->
                        img_test_button.setImage(doneButton))
                );
                timeline.play();
            }
            else{
                Image testingImage = new Image("/com/mannash/javafxapplication/fxml/images/testInProgress.gif");
                Image newImage = new Image("/com/mannash/javafxapplication/fxml/images/green_check.png");
                Image crossImage = new Image("/com/mannash/javafxapplication/fxml/images/crossicon.png");
                Image okImage = new Image("/com/mannash/javafxapplication/fxml/images/nok.png");
                Image doneButton = new Image("/com/mannash/javafxapplication/fxml/images/done.gif");
                Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event ->
                        img_status.setImage(testingImage)), new KeyFrame(Duration.seconds(10), event ->
                        input_iccid.setText("8991000904448610860F")), new KeyFrame(Duration.seconds(15), event ->
                        img_test_status_1.setImage(newImage)), new KeyFrame(Duration.seconds(25), event ->
                        img_test_status_2.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
                        img_test_status_3.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
                        img_test_status_4.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
                        img_test_status_5.setImage(crossImage)), new KeyFrame(Duration.seconds(25), event ->
                        img_status.setImage(okImage)), new KeyFrame(Duration.seconds(25), event ->
                        img_test_button.setImage(doneButton))
                );
                timeline.play();

            }

            counter++;

        }else{
            loadTestingWindow();
        }
    }

    public void loadTestingWindow() throws IOException {
        Parent testingPage = FXMLLoader.load(getClass().getResource("/com/mannash/javafxapplication/fxml/Testing-view.fxml"));
        Stage primaryStage = (Stage) img_test_button.getScene().getWindow();
        Scene scene = new Scene(testingPage);

        primaryStage.setScene(scene);
        primaryStage.show();

    }


}
