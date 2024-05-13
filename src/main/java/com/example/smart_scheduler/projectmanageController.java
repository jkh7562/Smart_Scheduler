package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class projectmanageController {
    @FXML
    ImageView back_image;
    @FXML
    private void backimageClicked(MouseEvent event) {
        // 현재 창을 닫음
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        currentStage.close();

        // 다른 열려 있는 창들을 닫음
        Stage[] openStages = Stage.getWindows().toArray(new Stage[0]);
        for (Stage stage : openStages) {
            if (stage != currentStage) {
                stage.close();
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Project.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
