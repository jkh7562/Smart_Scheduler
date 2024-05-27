package com.example.smart_scheduler;

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
import java.net.MalformedURLException;
import java.net.URL;

public class DayprtyController {

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
            String serverURL = "http://hbr2024.dothome.co.kr/prtydayget.php";
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

    @FXML
    private void content1saveButtonAction(ActionEvent event) {
        // 현재 선택된 아이템의 인덱스를 가져옵니다.
        int selectedIndex = list.getSelectionModel().getSelectedIndex();

        // 선택된 아이템이 없는 경우 -1을 반환하므로 체크합니다.
        if (selectedIndex != -1) {
            // 선택된 아이템의 텍스트 값을 가져옵니다.
            String selectedItemText = (String) list.getSelectionModel().getSelectedItem();

            // 가져온 값으로 원하는 작업을 수행합니다.
            System.out.println("선택된 아이템의 텍스트 값: " + selectedItemText);
            content1_label.setText(selectedItemText);
        } else {
            // 선택된 아이템이 없을 때 처리할 내용을 여기에 추가합니다.
            System.out.println("아이템이 선택되지 않았습니다.");
        }
    }

    @FXML
    private void content2saveButtonAction(ActionEvent event) {
        // 현재 선택된 아이템의 인덱스를 가져옵니다.
        int selectedIndex = list.getSelectionModel().getSelectedIndex();

        // 선택된 아이템이 없는 경우 -1을 반환하므로 체크합니다.
        if (selectedIndex != -1) {
            // 선택된 아이템의 텍스트 값을 가져옵니다.
            String selectedItemText = (String) list.getSelectionModel().getSelectedItem();

            // 가져온 값으로 원하는 작업을 수행합니다.
            System.out.println("선택된 아이템의 텍스트 값: " + selectedItemText);
            content2_label.setText(selectedItemText);
        } else {
            // 선택된 아이템이 없을 때 처리할 내용을 여기에 추가합니다.
            System.out.println("아이템이 선택되지 않았습니다.");
        }
    }
    @FXML
    private void content3saveButtonAction(ActionEvent event) {
        // 현재 선택된 아이템의 인덱스를 가져옵니다.
        int selectedIndex = list.getSelectionModel().getSelectedIndex();

        // 선택된 아이템이 없는 경우 -1을 반환하므로 체크합니다.
        if (selectedIndex != -1) {
            // 선택된 아이템의 텍스트 값을 가져옵니다.
            String selectedItemText = (String) list.getSelectionModel().getSelectedItem();

            // 가져온 값으로 원하는 작업을 수행합니다.
            System.out.println("선택된 아이템의 텍스트 값: " + selectedItemText);
            content3_label.setText(selectedItemText);
        } else {
            // 선택된 아이템이 없을 때 처리할 내용을 여기에 추가합니다.
            System.out.println("아이템이 선택되지 않았습니다.");
        }
    }

    @FXML
    private void deleteButtonAction(ActionEvent event) {
        Id = primary();
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/deleteprtyday.php";
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
                System.out.println("Response: " + response.toString());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("day_alldelete.fxml"));
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

    @FXML
    private void saveButtonAction(ActionEvent event) {
        Id = primary();
        String content1 = content1_label.getText();
        String content2 = content2_label.getText();
        String content3 = content3_label.getText();
        System.out.println(Id);
        System.out.printf(content1);
        System.out.println();
        System.out.printf(content2);
        System.out.println();
        System.out.printf(content3);
        System.out.println();
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/saveprtyday.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "Id=" + Id + "&content1=" + content1 + "&content2=" + content2 + "&content3=" + content3;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
