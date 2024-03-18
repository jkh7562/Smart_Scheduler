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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MembershipController {

    @FXML
    private ImageView back_image;
    @FXML
    private TextField name_textfield;
    @FXML
    private TextField phone_textfield;
    @FXML
    private TextField id_textfield;
    @FXML
    private TextField pass_textfield;
    @FXML
    private TextField cpass_textfield;
    @FXML
    private Button check_button;
    @FXML
    private Button signup_button;

    String userID;
    boolean checkid = false;

    @FXML
    private void backimageClicked(MouseEvent event){
        if(event.getSource() instanceof ImageView){
            ImageView clickedImageView = (ImageView) event.getSource();
            if (clickedImageView.getId().equals("back_image")){
                try {
                    // FXML 파일을 로드합니다.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_screen.fxml"));
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

    @FXML
    private void checkButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("check_button")) {
                try {
                    // 사용자 정보를 서버로 보내기
                    userID = id_textfield.getText();

                    // 추가: 이미 존재하는 아이디인지 확인
                    if (isUserExist(userID)) {
                        System.out.println("이미 존재하는 아이디입니다.");
                        checkid = false;
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("checkfail.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();

                    }else{
                        System.out.println("생성 가능한 아이디입니다.");
                        checkid = true;
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("checksuccess.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private boolean isUserExist(String userID) {
        try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://jkh75622.dothome.co.kr/CheckUser.php"; // 아이디 확인용 서버 URL
            String params = "userID=" + userID; // 전달할 파라미터

            URL url = new URL(serverURL + "?" + params);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 서버 응답을 읽어옴
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = in.readLine();
                in.close();

                // 서버에서 "true" 문자열을 반환하면 이미 존재하는 아이디
                return "true".equals(response.trim());
            } else {
                // 에러 처리
                System.out.println("HTTP Error Code: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
