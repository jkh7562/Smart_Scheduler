package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.*;
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
    private Button enter_button;
    @FXML
    private Label error_label;

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
    private void enterButtonAction(ActionEvent event) {
        if(event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("enter_button")) {
                String userName = name_textfield.getText();
                String phoneNumber = phone_textfield.getText();

                try {
                    // 서버의 PHP 스크립트 URL로 설정
                    String serverURL = "http://hbr2024.dothome.co.kr/IdFinder.php";

                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "userName=" + userName + "&phoneNumber=" + phoneNumber;
                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        // 서버 응답 처리
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        System.out.println("Server Response: " + response.toString());

                        // JSON 파싱
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        boolean IDfindSuccess = jsonResponse.getBoolean("success");

                        if (IDfindSuccess) {
                            // 데이터의 id 값을 얻어옴
                            String userID = jsonResponse.getString("userID");
                            error_label.setText("");

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("findidsuccess.fxml"));
                                Parent root = loader.load();
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                                FindidsuccessController findidsuccessController = loader.getController();
                                findidsuccessController.initData(userID);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            // 여기에서 userId 값을 사용하여 필요한 작업 수행
                            System.out.println("사용자 ID: " + userID);
                        } else {
                            error_label.setText("정보를 잘못 입력하셨습니다.");
                            // 응답이 없거나 비어있는 경우
                            System.out.println("데이터가 존재하지 않습니다.");
                        }
                    } else {
                        // 에러 처리
                        System.out.println("HTTP Error Code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
