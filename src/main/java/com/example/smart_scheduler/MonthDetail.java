package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MonthDetail {
    String Id;
    @FXML
    Label year_label;
    @FXML
    Label month_label;
    @FXML
    Label day_label;
    @FXML
    ListView content_listview;
    @FXML
    TextArea memo_area;
    @FXML
    Button back_button;
    @FXML
    Button save_button;
    @FXML
    Button delete_button;


    public void initData(String clickyear, String clickmonth, String clickday) {
        year_label.setText(clickyear);
        month_label.setText(clickmonth);
        day_label.setText(clickday);
    }
    @FXML
    private void backButtonAction(ActionEvent event) {
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_button.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
    }
    @FXML
    private void saveButtonAction(ActionEvent event) {
        Id = primary();
        String year = year_label.getText();
        String month = month_label.getText();
        String day = day_label.getText();
        String memo = memo_area.getText();

        /*try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://hbr2024.dothome.co.kr/save_monthschedule.php";

            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 요청 데이터 설정
            String postData = "Id=" + Id + "&start=" + start + "&end=" + end + "&memo=" + memo;
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

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("day_save.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    private void deleteButtonAction(ActionEvent event) {
        Id = primary();

        /*try {
            String serverURL = "http://hbr2024.dothome.co.kr/delete_day.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "Id=" + Id + "&start=" + start + "&end=" + end;
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

                FXMLLoader loader = new FXMLLoader(getClass().getResource("day_delete.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);

                Stage failStage = new Stage();
                failStage.setScene(scene);
                failStage.show();
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    public String primary() {
        String primaryid = null;

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/priget.php";
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
                // 서버 응답 처리
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder(); // response 초기화 추가
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // 데이터 파싱 (단순한 문자열을 ,로 분리하여 사용)
                String[] data = response.toString().split(",");
                if (data.length > 0) {
                    primaryid = data[0];
                    System.out.println(primaryid);
                } else {
                    System.out.println("데이터가 존재하지 않습니다.");
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(primaryid);

        return primaryid;
    }
}
