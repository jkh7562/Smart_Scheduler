package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ChecksuccessController {
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
            }
        }
    }
}
