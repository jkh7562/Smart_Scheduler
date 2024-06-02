package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class deleteusercptController {

    @FXML
    private Button okay_button;

    @FXML
    private void initialize() {
        okay_button.setDefaultButton(true);
    }

    @FXML
    public void okayButtonAction(ActionEvent event) {
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_screen.fxml"));
                    Parent root = loader.load();

                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/delete.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "&tableName=pri";
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                System.out.println("Response: " + response.toString());


            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
