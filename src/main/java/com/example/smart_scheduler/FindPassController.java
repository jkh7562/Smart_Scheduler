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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;

public class FindPassController {
    @FXML
    private ImageView back_image;
    @FXML
    private TextField id_textfield;
    @FXML
    private TextField phone_textfield;
    @FXML
    private Button enter_button;
    @FXML
    private Label error_label;

    @FXML
    private void backimageClicked(MouseEvent event){
        if(event.getSource() instanceof ImageView){
            ImageView clickedImageView = (ImageView) event.getSource();
            if (clickedImageView.getId().equals("back_image")){
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
        }
    }
    @FXML
    private void enterButtonAction(ActionEvent event) {
        if(event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("enter_button")) {
                String userID = id_textfield.getText();
                String phoneNumber = phone_textfield.getText();

                try {
                    // 서버의 PHP 스크립트 URL로 설정
                    String serverURL = "http://hbr2024.dothome.co.kr/PassFinder.php";

                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "userID=" + userID + "&phoneNumber=" + phoneNumber;
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
                        boolean PassfindSuccess = jsonResponse.getBoolean("success");

                        if (PassfindSuccess) {
                            String userPass = generateRandomString(10);
                            error_label.setText("");
                            try {
                                // 서버의 PHP 스크립트 URL로 설정
                                String serverURL1 = "http://hbr2024.dothome.co.kr/save_password.php";

                                URL url1 = new URL(serverURL1);
                                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                                connection1.setRequestMethod("POST");
                                connection1.setDoOutput(true);

                                // PHP 서버에 전달할 데이터 설정
                                String postData1 = "userID=" + userID + "&userPass=" + userPass;
                                OutputStream os1 = connection1.getOutputStream();
                                os1.write(postData1.getBytes("UTF-8"));
                                os1.close();

                                // 서버 응답 처리
                                // (이 부분은 생략합니다)
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("findpasssuccess.fxml"));
                                Parent root = loader.load();
                                Scene scene = new Scene(root);
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                                FindpasssuccessController findpasssuccessController = loader.getController();
                                findpasssuccessController.initData(userPass);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
    public static String generateRandomString(int length) {
        final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            sb.append(characters.charAt(randomIndex));
        }
        return sb.toString();
    }
}
