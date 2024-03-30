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

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String userID = id_textfield.getText();
        String userPass = pass_textfield.getText();
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("login_button")) {
                try {
                    String serverURL = "http://hbr2024.dothome.co.kr/Login.php";
                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "userID=" + userID + "&userPass=" + userPass;
                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        System.out.println("Server Response: " + response.toString());

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        boolean loginSuccess = jsonResponse.getBoolean("success");

                        if (loginSuccess) {
                            System.out.println("로그인 성공");

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                            Parent root = loader.load();

                            // 현재 창을 닫음
                            Stage stage = (Stage) clickedButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();

                            connection.disconnect();

                            try{

                                String serverURL1 = "http://hbr2024.dothome.co.kr/primary.php"; // 서버 URL

                                URL url1 = new URL(serverURL1);
                                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                                connection1.setRequestMethod("POST");
                                connection1.setDoOutput(true);

                                String postData1 = "priid=" + userID;
                                OutputStream os1 = connection1.getOutputStream();
                                os1.write(postData1.getBytes("UTF-8"));
                                os1.close();

                                int responseCode1 = connection1.getResponseCode();
                                if (responseCode1 == HttpURLConnection.HTTP_OK) {
                                    // 서버 응답 처리
                                    BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                                    String response1;
                                    while ((response1 = in1.readLine()) != null) {
                                        System.out.println(response1);
                                    }
                                    in1.close();

                                }else {
                                    System.out.println("HTTP Error Code: " + responseCode1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            System.out.println("로그인 실패");

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginfail.fxml"));
                            Parent root = loader.load();
                            Scene scene = new Scene(root);

                            // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                            Stage failStage = new Stage();
                            failStage.setScene(scene);
                            failStage.show();
                        }

                    } else {
                        System.out.println("HTTP Error Code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @FXML
    private void naverimageClicked(MouseEvent event){
        if(event.getSource() instanceof ImageView){
            ImageView clickedImageView = (ImageView) event.getSource();
            if (clickedImageView.getId().equals("naver_image")){
                try {
                    // FXML 파일을 로드합니다.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("naver_login.fxml"));
                    Parent root = loader.load();

                    // 현재 Stage를 가져옵니다.
                    Stage currentStage = (Stage) clickedImageView.getScene().getWindow();

                    // 현재 Stage의 Scene을 새로운 Scene으로 교체합니다.
                    currentStage.setScene(new Scene(root));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}