package com.example.smart_scheduler;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class meetingtime {
    @FXML
    ImageView back_image;
    @FXML
    Label teamname_label;
    @FXML
    private GridPane gridPane; // 그리드 팬에 대한 참조
    enum DayOfWeek {Mon, Tue, Wed, Thu, Fri, Sat, Sun}
    private final int START_HOUR = 8;
    private final int END_HOUR = 22;
    private Pane[][] panes = new Pane[DayOfWeek.values().length][END_HOUR - START_HOUR + 1];

    String Id;

    @FXML
    private void backimageClicked(MouseEvent event) {
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
    }

    private DayOfWeek convertToDayOfWeek(String koreanDay) {
        switch (koreanDay) {
            case "월":
                return DayOfWeek.Mon;
            case "화":
                return DayOfWeek.Tue;
            case "수":
                return DayOfWeek.Wed;
            case "목":
                return DayOfWeek.Thu;
            case "금":
                return DayOfWeek.Fri;
            case "토":
                return DayOfWeek.Sat;
            case "일":
                return DayOfWeek.Sun;
            default:
                throw new IllegalArgumentException("Invalid day of the week: " + koreanDay);
        }
    }

    private Pane getPane(DayOfWeek day, int hour) {
        String paneId = String.format("%s%02d", day.toString().substring(0, 3), hour);
        Pane pane = (Pane) gridPane.lookup("#" + paneId);
        if (pane == null) {
            pane = new Pane(); // Create a new pane if not found
            pane.setId(paneId); // Set ID
            gridPane.add(pane, day.ordinal(), hour - START_HOUR); // Add to gridPane
            panes[day.ordinal()][hour - START_HOUR] = pane; // Update panes array
        }
        return pane;
    }

    @FXML
    private void initialize() {
        Id = primary();

        for (DayOfWeek day : DayOfWeek.values()) {
            for (int hour = START_HOUR; hour <= END_HOUR; hour++) {
                Pane pane = getPane(day, hour);
                panes[day.ordinal()][hour - START_HOUR] = pane;
            }
        }

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/teamnameget.php";
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

                String teamName = response.toString(); // 받은 teamname 값을 변수에 저장
                teamname_label.setText(teamName);

            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String teamname = teamname_label.getText();

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/teamuserget.php";
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
                String jsonResponse = response.toString();
                System.out.println("Response: " + response.toString());

                // JSON 데이터 파싱
                JSONArray jsonArray = new JSONArray(jsonResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String id = jsonArray.getString(i);

                    try {
                        String serverURL1 = "http://hbr2024.dothome.co.kr/setrcmd.php";
                        URL url1 = new URL(serverURL1);
                        HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                        connection1.setRequestMethod("POST");
                        connection1.setDoOutput(true);

                        String postData1 = "id=" + id;
                        OutputStream os1 = connection1.getOutputStream();
                        os1.write(postData1.getBytes("UTF-8"));
                        os1.close();

                        int responseCode1 = connection1.getResponseCode();
                        if (responseCode1 == HttpURLConnection.HTTP_OK) {
                            BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                            StringBuilder response1 = new StringBuilder();
                            String inputLine1;

                            while ((inputLine1 = in1.readLine()) != null) {
                                response1.append(inputLine1);
                            }

                            in1.close();
                            String jsonResponse1 = response1.toString();
                            System.out.println("Response: " + jsonResponse1);

                            // JSON 데이터 파싱
                            JSONArray jsonArray1 = new JSONArray(jsonResponse1);
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject = jsonArray1.getJSONObject(j);
                                String week = jsonObject.getString("week");
                                String start = jsonObject.getString("start");
                                String end = jsonObject.getString("end");

                                DayOfWeek dayOfWeek = convertToDayOfWeek(week);
                                int startHour = Integer.parseInt(start.split(":")[0]);
                                int endHour = Integer.parseInt(end.split(":")[0]);

                                for (int hour = startHour; hour <= endHour; hour++) {
                                    int hourIndex = hour - START_HOUR;
                                    // 해당 Pane 가져오기
                                    Pane pane = panes[dayOfWeek.ordinal()][hourIndex]; // 'startHourIndex' 대신에 'hourIndex'를 사용합니다.

                                    // Pane의 배경색 업데이트
                                    pane.setStyle("-fx-background-color: " + "#a9a9a9");
                                }
                            }
                        } else {
                            System.out.println("HTTP Error Code: " + responseCode1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
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
}
