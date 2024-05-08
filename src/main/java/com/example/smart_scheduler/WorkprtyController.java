package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WorkprtyController {

    @FXML
    ImageView back_image;
    @FXML
    Button save_button;
    @FXML
    Button delete_button;
    @FXML
    Label content1_label;
    @FXML
    Label content2_label;
    @FXML
    Label content3_label;
    @FXML
    Button content1_delete;
    @FXML
    Button content2_delete;
    @FXML
    Button content3_delete;
    @FXML
    Button content1_save;
    @FXML
    Button content2_save;
    @FXML
    Button content3_save;
    @FXML
    ListView list;

    @FXML
    private void backimageClicked(MouseEvent event) {
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
    }

    /*@FXML
    private void saveButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("save_button")) {

            }
        }
    }*/
}
