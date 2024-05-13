package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class teamname_cptController {
    @FXML
    Button okay_button;

    @FXML
    private void initialize() {
        // login_button에 대한 엔터 키 핸들러 등록
        okay_button.setDefaultButton(true);
    }

    @FXML
    private void okayButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("okay_button")) {
                // 현재 창을 닫음
                Stage currentStage = (Stage) clickedButton.getScene().getWindow();
                currentStage.close();

                // 다른 열려 있는 창들을 닫음
                Stage[] openStages = Stage.getWindows().toArray(new Stage[0]);
                for (Stage stage : openStages) {
                    if (stage != currentStage) {
                        stage.close();
                    }
                }

                try {
                    // 로그인 화면을 로드하고 컨트롤러를 설정하여 새 창으로 열기
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_screen.fxml"));
                    Parent root = loader.load();

                    // 새로운 스테이지를 만들어서 로그인 창을 열기
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
