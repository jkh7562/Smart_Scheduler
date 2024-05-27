package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class projectaddController {
    @FXML
    ImageView back_image;
    @FXML
    TextField content_textfield;
    @FXML
    DatePicker start_datepicker;
    @FXML
    DatePicker end_datepicker;
    @FXML
    Button save_button;
    String teamname;
    String projectname;

    public void initData(String teamname, String projectname) {
        this.teamname = teamname;
        this.projectname = projectname;
    }

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("project_management.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        String content = content_textfield.getText();
        LocalDate selectedStartDate = start_datepicker.getValue();
        LocalDate selectedEndDate = end_datepicker.getValue();

        String startdate = null;
        String enddate = null;
        int date = 0;

        if (selectedStartDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startdate = selectedStartDate.format(formatter);
        }

        if (selectedEndDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            enddate = selectedEndDate.format(formatter);

            date = (int) ChronoUnit.DAYS.between(selectedStartDate, selectedEndDate);
            date++;
        }
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/projectadd.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "teamname=" + teamname + "&projectname=" + projectname + "&content=" + content + "&startdate=" + startdate + "&enddate=" + enddate + "&date=" + date;
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

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("projectadd_cpt.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
