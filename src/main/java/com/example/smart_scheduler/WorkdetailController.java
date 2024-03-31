package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WorkdetailController {
    @FXML
    private MenuButton week_button;
    @FXML
    private MenuButton ftime_button;
    @FXML
    private MenuButton etime_button;
    @FXML
    private ImageView back_image;

    @FXML
    private void weekSelection(ActionEvent event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem selectedMenuWItem = (MenuItem) event.getSource();
            week_button.setText(selectedMenuWItem.getText());
        }
    }
    @FXML
    private void handleTimeSelection(ActionEvent event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem selectedMenuItem = (MenuItem) event.getSource();
            ftime_button.setText(selectedMenuItem.getText());
        }
    }
    @FXML
    private void handleETimeSelection(ActionEvent event) {
        if (event.getSource() instanceof MenuItem) {
            MenuItem selectedMenuEItem = (MenuItem) event.getSource();
            etime_button.setText(selectedMenuEItem.getText());
        }
    }
    @FXML
    private void backimageClicked(MouseEvent event) {
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
    }


}
