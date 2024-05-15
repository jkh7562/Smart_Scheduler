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
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        String content = content_textfield.getText();
        LocalDate selectedStartDate = start_datepicker.getValue();
        LocalDate selectedEndDate = end_datepicker.getValue();

        String startdate = null;
        String enddate = null;

        if (selectedStartDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            startdate = selectedStartDate.format(formatter);
        }

        if (selectedEndDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            enddate = selectedEndDate.format(formatter);
        }
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/projectadd.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "teamname=" + teamname + "&projectname=" + projectname + "&content=" + content + "&startdate=" + startdate + "&enddate=" + enddate;
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
