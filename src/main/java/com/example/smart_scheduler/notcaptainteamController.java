package com.example.smart_scheduler;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class notcaptainteamController {

    @FXML
    private ImageView back_image;
    @FXML
    private Label team_label;
    @FXML
    private Label id_label;
    @FXML
    private ListView<String> team_listview;
    @FXML
    private ListView<String> search_listview;
    @FXML
    private TextField search_textfield;
    @FXML
    private Button search_button;

    private String Id;
    private ObservableList<String> searchList;

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
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // 데이터 파싱
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return primaryid;
    }

    @FXML
    private void initialize() {
        Id = primary();
        id_label.setText(Id);

        searchList = FXCollections.observableArrayList();
        search_listview.setItems(searchList);

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/team.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "Id=" + Id;
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
                String serverResponse = response.toString();
                System.out.println("Response: " + serverResponse);

                String teamname = serverResponse.trim();
                team_label.setText(teamname);
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String teamname = team_label.getText();

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/getteam.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "teamname=" + teamname;
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

                // JSON 파싱 및 ListView에 추가
                JSONArray jsonArray = new JSONArray(response.toString());
                Platform.runLater(() -> {
                    team_listview.getItems().clear(); // 기존 항목을 초기화
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String userID = jsonObject.getString("userID");
                        String userName = jsonObject.getString("userName");
                        String displayText = userName + " - " + userID;
                        team_listview.getItems().add(displayText);
                    }
                });


            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchButtonClick(ActionEvent event) {
        String searchid = search_textfield.getText();
        searchList.clear();
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/teamsearch.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + searchid;
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
                // JSON 파싱
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String userID = jsonObject.getString("userID");
                    String userName = jsonObject.getString("userName");
                    String displayText = userName + " - " + userID;
                    searchList.add(displayText);
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
