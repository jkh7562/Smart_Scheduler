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

import java.io.*;
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

    String userName;
    String userID;
    String userPhone;
    String userPass;
    String userCPass;
    boolean checkid = false;


    @FXML
    private void initialize() {
        // login_button에 대한 엔터 키 핸들러 등록
        signup_button.setDefaultButton(true);
    }

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
                    if(userID.isEmpty()){
                        return;
                    }// 추가: 이미 존재하는 아이디인지 확인
                    else if (isUserExist(userID)) {
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
            String serverURL = "http://hbr2024.dothome.co.kr/CheckUser.php"; // 아이디 확인용 서버 URL

            // POST 데이터 설정
            String postData = "userID=" + userID;

            // URL 및 연결 설정
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 요청 메소드를 POST로 설정
            connection.setRequestMethod("POST");

            // 데이터 전송을 위한 설정
            connection.setDoOutput(true);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.writeBytes(postData);
                wr.flush();
            }

            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 서버 응답을 읽어옴
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line);
                }
                String response = responseBuilder.toString();
                System.out.println("서버 응답 : " + response); // 서버 응답 출력
                in.close();

                return response.trim().equals("true");
            } else {
                // 에러 처리
                System.out.println("서버 연결 실패. HTTP Error Code: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @FXML
    private void signupButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("signup_button")) {
                try {
                    // 사용자 정보를 서버로 보내기
                    userName = name_textfield.getText();
                    userPhone = phone_textfield.getText();
                    userID = id_textfield.getText();
                    if(pass_textfield.getText().equals(cpass_textfield.getText())) {
                        userPass = pass_textfield.getText();
                    }
                    else{
                        System.out.println("비밀번호가 일치하지 않습니다.");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("passfail.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();
                    }
                    if(userName.isEmpty()){
                        return;
                    }
                    else if(userPhone.isEmpty()){
                        return;
                    }
                    else if(userID.isEmpty()){
                        return;
                    }
                    else if(userPass.isEmpty()){
                        return;
                    }// 추가: 이미 존재하는 아이디인지 확인
                    else if (isUserExist(userID)) {
                        System.out.println("이미 존재하는 아이디입니다.");
                        checkid = false;
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("checkfail.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();

                    }
                    else{
                        try {
                            if(checkid){
                                // 서버의 PHP 스크립트 URL로 설정
                                String serverURL = "http://hbr2024.dothome.co.kr/Register.php"; // 서버 URL

                                URL url = new URL(serverURL);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                connection.setDoOutput(true);
                                String postData = "userName=" + userName + "&phoneNumber=" + userPhone + "&userID=" + userID + "&userPass=" + userPass;
                                OutputStream os = connection.getOutputStream();
                                os.write(postData.getBytes("UTF-8"));
                                os.close();

                                int responseCode = connection.getResponseCode();
                                if (responseCode == HttpURLConnection.HTTP_OK) {
                                    // 서버 응답 처리
                                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                    String response;
                                    while ((response = in.readLine()) != null) {
                                        System.out.println(response);
                                    }
                                    in.close();


                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("sign_cpt.fxml"));
                                    Parent root = loader.load();
                                    Scene scene = new Scene(root);

                                    Stage failStage = new Stage();
                                    failStage.setScene(scene);
                                    failStage.show();
                                } else {
                                    // 에러 처리
                                    System.out.println("HTTP Error Code: " + responseCode);
                                }

                                connection.disconnect();
                            }else{
                                System.out.println("아이디 중복체크가 완료되지 않았습니다.");
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("signfail.fxml"));
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
                } catch (Exception e) {
                e.printStackTrace();
            }
            }
        }
    }

}
