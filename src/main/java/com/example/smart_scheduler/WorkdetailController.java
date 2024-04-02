package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private Button add_button;
    @FXML
    private TextField content_textfield;

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
    @FXML
    private void addButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("add_button")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("work_cpt.fxml"));
                Parent root = null;
                try {
                    root = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Stage newStage = new Stage();
                newStage.setScene(new Scene(root));
                newStage.show();
            }
        }

        String week = week_button.getText();
        String start = ftime_button.getText();
        String end = etime_button.getText();
        String content = content_textfield.getText();
        int time = Integer.parseInt(end) - Integer.parseInt(start);

        try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://hbr2024.dothome.co.kr/IdFinder.php";

            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userName=" + userName + "&phoneNumber=" + phoneNumber;
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {

                // 서버 응답 처리
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                System.out.println("Server Response: " + response.toString());

            } else {
                // 에러 처리
                System.out.println("HTTP Error Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

}
