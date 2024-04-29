package com.example.smart_scheduler;

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
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WorkaddController {
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
    private Button search_button;
    @FXML
    private TextField content_textfield;
    @FXML
    private Label rcmdfail_label;
    @FXML
    private HBox rcmd_hbox;
    public static String Id;
    public static String week;
    public static String start;
    public static String end;
    public static String content;
    public static int time;


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
                Id = primary();
                week = week_button.getText();
                start = ftime_button.getText();
                end = etime_button.getText();
                content = content_textfield.getText();
                time = Integer.parseInt(end) - Integer.parseInt(start);

                try {
                    // 서버에 데이터를 보내고 응답을 받는 부분
                    String serverURL = "http://hbr2024.dothome.co.kr/workadd.php";
                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    System.out.println(Id);
                    System.out.println(week);
                    // POST 데이터 설정
                    String postData = "Id=" + Id + "&week=" + week + "&start=" + start + "&end=" + end + "&content=" + content + "&time=" + time;
                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.close();

                    // 서버 응답 처리
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }

                    String serverResponse = response.toString(); // 서버 응답 저장


                    if (serverResponse.equals("overlap")) {
                        System.out.println("겹치는 일정이 있습니다.");
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("work_fail.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();

                    } else if (serverResponse.equals("saved")) {
                        System.out.println("일정이 저장되었습니다.");
                        try {
                            // 서버에 데이터를 보내고 응답을 받는 부분
                            String serverURL1 = "http://hbr2024.dothome.co.kr/rcmdworkadd.php";
                            URL url1 = new URL(serverURL1);
                            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                            connection1.setRequestMethod("POST");
                            connection1.setDoOutput(true);
                            System.out.println(Id);
                            System.out.println(week);
                            // POST 데이터 설정
                            String postData1 = "Id=" + Id + "&week=" + week + "&start=" + start + "&end=" + end + "&content=" + content + "&time=" + time;
                            OutputStream os1 = connection1.getOutputStream();
                            os1.write(postData1.getBytes("UTF-8"));
                            os1.close();

                            // 서버 응답 처리
                            BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                            StringBuilder response1 = new StringBuilder();
                            String inputLine1;
                            while ((inputLine1 = in1.readLine()) != null) {
                                response.append(inputLine1);
                            }

                            connection.disconnect(); // 연결 종료
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                    } else {
                        System.out.println("알 수 없는 서버 응답: " + serverResponse);
                        // 예상치 못한 서버 응답이 있을 때 처리할 내용 추가
                    }

                    connection.disconnect(); // 연결 종료
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @FXML
    private void searchButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("search_button")) {
                Id = primary();
                content = content_textfield.getText();
                if(content == ""){

                }else{

                }
            }
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
}
