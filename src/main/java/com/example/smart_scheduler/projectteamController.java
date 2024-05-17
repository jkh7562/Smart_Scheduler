package com.example.smart_scheduler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class projectteamController {
    @FXML
    ImageView back_image;
    @FXML
    Button teamdelete_button;
    @FXML
    Label team_label;
    @FXML
    Label id_label;
    @FXML
    ListView team_listview;
    @FXML
    ListView search_listview;
    @FXML
    TextField search_textfield;
    @FXML
    Button search_button;
    @FXML
    Button delete_button;
    @FXML
    Button add_button;

    @FXML
    private void backimageClicked(MouseEvent event) {
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
    }
}
