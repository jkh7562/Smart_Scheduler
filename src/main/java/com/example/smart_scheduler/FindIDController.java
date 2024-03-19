package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
        String userPhone = phone_textfield.getText().trim(); // 공백 제거
        if (userPhone.startsWith("0")) {
            userPhone = "+82" + userPhone.substring(1);
        }
        String verificationCode = generateVerificationCode();

        try {
            URL url = new URL(apiEndpoint.trim()); // 공백 제거
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey.trim()); // 공백 제거

            String postData = String.format("{\"sender\":\"%s\",\"to\":\"%s\",\"content\":\"Your verification code is: %s\"}", sender, userPhone, verificationCode);
            conn.setDoOutput(true);

            try (OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8")) {
                osw.write(postData);
                osw.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // 여기에 성공적으로 SMS를 보냈음을 알리는 코드를 추가하세요.
                    showAlert(AlertType.INFORMATION, "SMS Sent", "Verification code sent successfully!");
                }
            } else {
                // 여기에 실패했음을 알리는 코드를 추가하세요.
                showAlert(AlertType.ERROR, "SMS Failed", "Failed to send verification code!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
        }
    }

    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
