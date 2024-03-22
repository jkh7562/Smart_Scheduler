package com.example.smart_scheduler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView; // WebView를 위한 import 문
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class NaverController {

    @FXML
    private ImageView back_image;
    @FXML
    private WebView naver_webview;

    String stateToken = UUID.randomUUID().toString();

    @FXML
    private void initialize() {
        // 로그인 URL에 상태 토큰을 실제 생성된 토큰으로 치환합니다.
        String redirectURI = URLEncoder.encode("http://hbr2024.dothome.co.kr/Login.php", StandardCharsets.UTF_8);
        String loginUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=pZ6UdKaBdDdwuWxFjtbW&state="
                + stateToken
                + "&redirect_uri="
                + redirectURI;
        naver_webview.getEngine().load(loginUrl);
    }

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
}
