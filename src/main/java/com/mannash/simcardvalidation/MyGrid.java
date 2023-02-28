package com.mannash.simcardvalidation;

import javafx.application.Application;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
public class MyGrid extends Application {

    @FXML
    private GridPane mainGridPane;

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Load the FXML file and retrieve a reference to the MyGrid controller
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/MyGrid.fxml"));

        // Load the card FXML file and add it to the mainGridPane
        FXMLLoader cardLoader = new FXMLLoader(getClass().getResource("/com/mannash/javafxapplication/fxml/GridElement.fxml"));
        Parent card = cardLoader.load();
        GridPane.setConstraints(card, 0, 0);
        mainGridPane.getChildren().add(card);

        // Create a scene with the GridPane as the root node
        Scene scene = new Scene(mainGridPane);

        // Set the stage title and show the stage
        primaryStage.setTitle("My Grid");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
