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
    private Pane[][] panes = new Pane[WorkController.DayOfWeek.values().length][END_HOUR - START_HOUR + 1];

    String Id;

    class WorkItem {
        String week;
        String start;
        String end;
        String content;

        public WorkItem(String week, String start, String end, String content) {
            this.week = week;
            this.start = start;
            this.end = end;
            this.content = content;
        }
    }

    @FXML
    private void backimageClicked(MouseEvent event) {
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
    }
    private WorkController.DayOfWeek convertToDayOfWeek(String koreanDay) {
        switch (koreanDay) {
            case "월":
                return WorkController.DayOfWeek.Mon;
            case "화":
                return WorkController.DayOfWeek.Tue;
            case "수":
                return WorkController.DayOfWeek.Wed;
            case "목":
                return WorkController.DayOfWeek.Thu;
            case "금":
                return WorkController.DayOfWeek.Fri;
            case "토":
                return WorkController.DayOfWeek.Sat;
            case "일":
                return WorkController.DayOfWeek.Sun;
            default:
                throw new IllegalArgumentException("Invalid day of the week: " + koreanDay);
        }
    }
    private List<WorkController.WorkItem> workItems = new ArrayList<>();
    private void updateUI() {
        // 이미 생성된 모든 팬의 내용을 클리어합니다.
        for (Pane[] paneRow : panes) {
            for (Pane pane : paneRow) {
                pane.getChildren().clear();
            }
        }

        Random rand = new Random(); // Random 객체 생성

        // GridPane에 작업 항목을 표시하기 위한 UI 업데이트 로직
        for (WorkController.WorkItem item : workItems) {
            WorkController.DayOfWeek dayOfWeek = convertToDayOfWeek(item.week);
            int startHour = Integer.parseInt(item.start.split(":")[0]);
            int endHour = Integer.parseInt(item.end.split(":")[0]);
            int startHourIndex = startHour - START_HOUR;
            int duration = endHour - startHour;

            // duration이 1 미만인 경우에는 1로 설정합니다.
            if (duration < 1) {
                duration = 1;
            }

            // 해당 요일과 시간에 맞는 Pane 찾기
            Pane pane = panes[dayOfWeek.ordinal()][startHourIndex];

            VBox vbox;
            if (!pane.getChildren().isEmpty() && pane.getChildren().get(0) instanceof VBox) {
                // 이미 VBox가 있으면 사용
                vbox = (VBox) pane.getChildren().get(0);
            } else {
                // VBox가 없으면 새로 생성
                vbox = new VBox();
                vbox.setAlignment(Pos.CENTER_LEFT); // VBox 내의 항목을 왼쪽 정렬합니다.
                pane.getChildren().add(vbox); // Pane에 VBox 추가
            }

            // Pane 내에 표시할 Label 생성
            Label label = new Label(item.content);
            vbox.getChildren().add(label);

            // RGB 값이 128~255 범위인 밝은 색상 코드 생성
            int r = rand.nextInt(128) + 128; // 128~255
            int g = rand.nextInt(128) + 128; // 128~255
            int b = rand.nextInt(128) + 128; // 128~255
            String colorCode = String.format("#%02X%02X%02X", r, g, b);

            // 생성된 색상 코드로 Pane의 배경색 설정
            pane.setStyle("-fx-background-color: " + colorCode + ";");

            // 일정이 있는 경우에만 경계선을 없애도록 설정
            if (!item.content.isEmpty()) {
                pane.setStyle(pane.getStyle() + "-fx-border-width: 0;");
            }

            // Duration 처리 (여러 시간에 걸쳐있는 경우)
            GridPane.setRowSpan(pane, duration);
        }
    }
    private String convertToKoreanDay(WorkController.DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case Mon:
                return "월";
            case Tue:
                return "화";
            case Wed:
                return "수";
            case Thu:
                return "목";
            case Fri:
                return "금";
            case Sat:
                return "토";
            case Sun:
                return "일";
            default:
                throw new IllegalArgumentException("Invalid day of the week: " + dayOfWeek);
        }
    }
    private Pane getPane(WorkController.DayOfWeek day, int hour) {
        String paneId = String.format("%s%02d", day.toString().substring(0, 3), hour);
        Pane pane = (Pane) gridPane.lookup("#" + paneId);
        if (pane == null) {
            System.out.println("Pane not found for ID: " + paneId);
        }
        return pane;
    }
    @FXML
    private void initialize() {
        Id = primary();
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
                    int id = jsonArray.getInt(i);

                    //여기부터
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
