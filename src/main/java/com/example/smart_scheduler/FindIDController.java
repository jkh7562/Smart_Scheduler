package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class FindIDController {

    @FXML
    private ImageView back_image;
    @FXML
    private TextField name_textfield;
    @FXML
    private TextField phone_textfield;
    @FXML
    private Button auth_button;
    @FXML
    private TextField auth_textfield;
    @FXML
    private Button enter_button;

    // 설정 파일 또는 환경 변수에서 불러오세요.
    private String apiKey = System.getenv("API_KEY");
    private String sender = System.getenv("SENDER_PHONE");
    private String apiEndpoint = System.getenv("API_ENDPOINT");

    @FXML
    private void backimageClicked(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_screen.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) back_image.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // 사용자에게 오류 메시지 표시
        }
    }

    @FXML
    private void authButtonAction(ActionEvent event) {
        String userPhone = phone_textfield.getText();
        if (userPhone.startsWith("0")) {
            userPhone = "+82" + userPhone.substring(1);
        }
        String verificationCode = generateVerificationCode();
        System.out.println("API Endpoint: " + apiEndpoint);
        try {
            URL url = new URL(apiEndpoint);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);

            String postData = String.format("{\"sender\":\"%s\",\"to\":\"%s\",\"content\":\"인증번호: %s\"}", sender, userPhone, verificationCode);
            conn.setDoOutput(true);

            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            osw.write(postData);
            osw.flush();
            osw.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // SMS 전송 성공 메시지 처리
            } else {
                // 오류 처리
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 처리
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
