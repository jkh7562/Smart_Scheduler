package com.example.smart_scheduler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
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

    // 상태 토큰을 생성합니다.
    String stateToken = UUID.randomUUID().toString();

    @FXML
    private void initialize() {
        // 로그인 URL에 상태 토큰과 리다이렉트 URI를 실제 값으로 치환합니다.
        String redirectURI = URLEncoder.encode("http://hbr2024.dothome.co.kr/Naver_Login.php", StandardCharsets.UTF_8);
        String loginUrl = "https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=pZ6UdKaBdDdwuWxFjtbW&state="
                + stateToken
                + "&redirect_uri="
                + redirectURI;

        // WebView를 사용하여 네이버 로그인 페이지를 로드합니다.
        naver_webview.getEngine().load(loginUrl);
    }

    @FXML
    private void backimageClicked(MouseEvent event) {
        // 사용자가 뒤로가기 이미지를 클릭했을 때 로그인 화면으로 돌아가는 기능을 구현합니다.
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_screen.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) back_image.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // 오류가 발생했을 경우 사용자에게 메시지를 표시합니다.
        }
    }
}
