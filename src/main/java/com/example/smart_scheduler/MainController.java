package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    Button work_button;

    @FXML
    private void workButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("work_button")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Work.fxml"));
                    Parent root = loader.load();
                    Stage currentStage = (Stage) work_button.getScene().getWindow();
                    currentStage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                    // 사용자에게 오류 메시지 표시
                }
            }
        }
    }

}
