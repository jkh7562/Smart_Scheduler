package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

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
    private Button login_button;
    @FXML
    private ImageView naver_image;
    @FXML
    private ImageView google_image;
    @FXML
    private ImageView kakao_image;

    @FXML
    private void initialize() {
        // login_button에 대한 엔터 키 핸들러 등록
        login_button.setDefaultButton(true);
    }

    @FXML
    private void signupLabelClicked(MouseEvent event) {
        loadScene("membership_system.fxml", event);
    }

    @FXML
    private void idfinderLabelClicked(MouseEvent event) {
        loadScene("find_Id.fxml", event);
    }

    @FXML
    private void passfinderLabelClicked(MouseEvent event) {
        loadScene("find_Password.fxml", event);
    }

    private void loadScene(String fxmlFile, MouseEvent event) {
        if (event.getSource() instanceof Label) {
            Label clickedLabel = (Label) event.getSource();
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                Parent root = loader.load();
                Stage currentStage = (Stage) clickedLabel.getScene().getWindow();
                currentStage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        // Your existing logic for handling login action
    }

    @FXML
    private void naverimageClicked(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView clickedImageView = (ImageView) event.getSource();
            if (clickedImageView.getId().equals("naver_image")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("naver_login.fxml"));
                    Parent root = loader.load();
                    Stage currentStage = (Stage) clickedImageView.getScene().getWindow();
                    currentStage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
