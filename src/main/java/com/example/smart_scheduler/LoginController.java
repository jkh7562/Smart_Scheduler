package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private Label signup_label;
    @FXML
    private Label idfinder_label;
    @FXML
    private Label passfinder_label;
    @FXML
    private TextField id_textfield;
    @FXML
    private TextField pass_textfield;

    @FXML
    private void signupLabelClicked(MouseEvent event){
        if(event.getSource() instanceof Label){
            Label clickedLabel = (Label) event.getSource();
            if (clickedLabel.getId().equals("signup_label")){
                try {
                    // FXML 파일을 로드합니다.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("membership_system.fxml"));
                    Parent root = loader.load();

                    // 현재 Stage를 가져옵니다.
                    Stage currentStage = (Stage) clickedLabel.getScene().getWindow();

                    // 현재 Stage의 Scene을 새로운 Scene으로 교체합니다.
                    currentStage.setScene(new Scene(root));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void idfinderLabelClicked(MouseEvent event){
        if(event.getSource() instanceof Label){
            Label clickedLabel = (Label) event.getSource();
            if (clickedLabel.getId().equals("idfinder_label")){
                try {
                    // FXML 파일을 로드합니다.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("find_Id.fxml"));
                    Parent root = loader.load();

                    // 현재 Stage를 가져옵니다.
                    Stage currentStage = (Stage) clickedLabel.getScene().getWindow();

                    // 현재 Stage의 Scene을 새로운 Scene으로 교체합니다.
                    currentStage.setScene(new Scene(root));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void passfinderLabelClicked(MouseEvent event){
        if(event.getSource() instanceof Label){
            Label clickedLabel = (Label) event.getSource();
            if (clickedLabel.getId().equals("passfinder_label")){
                try {
                    // FXML 파일을 로드합니다.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("find_Password.fxml"));
                    Parent root = loader.load();

                    // 현재 Stage를 가져옵니다.
                    Stage currentStage = (Stage) clickedLabel.getScene().getWindow();

                    // 현재 Stage의 Scene을 새로운 Scene으로 교체합니다.
                    currentStage.setScene(new Scene(root));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}