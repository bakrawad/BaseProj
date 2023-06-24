package com.example.baseproj;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Driver extends Application {


     Stage stage1 = new Stage();
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("Sign-In.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Hotel Reservation System");
        stage.setScene(scene);
        stage.show();
        stage1 = stage;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("CopyRight");
        alert.setContentText("تم إنجاز هذا المشروع بواسطة بكر عوض و محمد فحل \n" +
                "باشراف الدكتور الفاضل مروان بشارات ");
        alert.show();
//        HelloController helloController = new HelloController();
//        helloController.SignInBT(new ActionEvent(),stage);
    }







    public static void main(String[] args) {


        launch();
    }
    public Stage getStage1() {
        return stage1;
    }


}