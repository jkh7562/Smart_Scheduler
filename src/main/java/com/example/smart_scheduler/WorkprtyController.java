package com.example.smart_scheduler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.json.JSONArray;
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

    String Id;

    @FXML
    private void initialize() {
        String Id = primary();
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/prtyworkget.php";
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

                JSONObject jsonResponse = new JSONObject(response.toString());
                boolean searchSuccess = jsonResponse.getBoolean("success");

                if (searchSuccess) {
                    System.out.println("검색 성공");

                    JSONArray userDataArray = jsonResponse.getJSONArray("userData");

                    ObservableList<String> items = FXCollections.observableArrayList();

                    for (int i = 0; i < userDataArray.length(); i++) {
                        JSONObject userData = userDataArray.getJSONObject(i);

                        // Assuming there is a key "content" in userData JSON object
                        items.add(userData.getString("content"));
                    }

                    list.setItems(items);
                }
                in.close();
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backimageClicked(MouseEvent event) {
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
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

    /*@FXML
    private void saveButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("save_button")) {

            }
        }
    }*/
}
