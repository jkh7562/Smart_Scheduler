package com.example.smart_scheduler;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProjectController {

    @FXML
    private Label year_label;

    @FXML
    private Label month_label;
    @FXML
    private Pane pan01, pan11, pan21, pan31, pan41, pan51, pan61, pan02, pan12, pan22, pan32, pan42, pan52, pan62, pan03, pan13, pan23, pan33, pan43, pan53, pan63, pan04, pan14, pan24, pan34, pan44, pan54, pan64, pan05, pan15, pan25, pan35, pan45, pan55, pan65, pan06, pan16, pan26, pan36, pan46, pan56, pan66;
    @FXML
    private Label day01_label, day11_label, day21_label, day31_label, day41_label, day51_label, day61_label, day02_label, day12_label, day22_label, day32_label, day42_label, day52_label, day62_label, day03_label, day13_label, day23_label, day33_label, day43_label, day53_label, day63_label, day04_label, day14_label, day24_label, day34_label, day44_label, day54_label, day64_label, day05_label, day15_label, day25_label, day35_label, day45_label, day55_label, day65_label, day06_label, day16_label, day26_label, day36_label, day46_label, day56_label, day66_label;
    @FXML
    private VBox vbox01, vbox11, vbox21, vbox31, vbox41, vbox51, vbox61, vbox02, vbox12, vbox22, vbox32, vbox42, vbox52, vbox62, vbox03, vbox13, vbox23, vbox33, vbox43, vbox53, vbox63, vbox04, vbox14, vbox24, vbox34, vbox44, vbox54, vbox64, vbox05, vbox15, vbox25, vbox35, vbox45, vbox55, vbox65, vbox06, vbox16, vbox26, vbox36, vbox46, vbox56, vbox66;
    @FXML
    private Pane _10_gauge, _20_gauge, _30_gauge, _40_gauge, _50_gauge, _60_gauge, _70_gauge, _80_gauge, _90_gauge, _100_gauge;
    @FXML
    Button work_button;
    @FXML
    Button main_button;
    @FXML
    Label project_label;
    @FXML
    Button team_button;
    private int currentYear;
    private int currentMonth;

    private Label[] dayLabels;
    private Pane[][] panes;
    private  VBox [][] vboxs;
    private String clickyear, clickmonth, clickday;

    @FXML
    private Button[][] dayButtons = new Button[6][7];

    @FXML
    Button manage_button;

    String Id;
    String teamname;
    int year;
    int month;

    // 다른 필요한 필드 및 메서드들을 추가할 수 있습니다.

    // 예시로 몇 가지 초기화 메서드를 추가합니다.
    @FXML
    private void initialize() {
        dayLabels = new Label[]{
                day01_label, day11_label, day21_label, day31_label, day41_label, day51_label,
                day61_label, day02_label, day12_label, day22_label, day32_label, day42_label,
                day52_label, day62_label, day03_label, day13_label, day23_label, day33_label,
                day43_label, day53_label, day63_label, day04_label, day14_label, day24_label,
                day34_label, day44_label, day54_label, day64_label, day05_label, day15_label,
                day25_label, day35_label, day45_label, day55_label, day65_label, day06_label,
                day16_label, day26_label, day36_label, day46_label, day56_label, day66_label
        };
        panes = new Pane[][]{
                {pan01, pan11, pan21, pan31, pan41, pan51, pan61},
                {pan02, pan12, pan22, pan32, pan42, pan52, pan62},
                {pan03, pan13, pan23, pan33, pan43, pan53, pan63},
                {pan04, pan14, pan24, pan34, pan44, pan54, pan64},
                {pan05, pan15, pan25, pan35, pan45, pan55, pan65},
                {pan06, pan16, pan26, pan36, pan46, pan56, pan66}
        };
        vboxs = new VBox[][]{
                {vbox01, vbox11, vbox21, vbox31, vbox41, vbox51, vbox61},
                {vbox02, vbox12, vbox22, vbox32, vbox42, vbox52, vbox62},
                {vbox03, vbox13, vbox23, vbox33, vbox43, vbox53, vbox63},
                {vbox04, vbox14, vbox24, vbox34, vbox44, vbox54, vbox64},
                {vbox05, vbox15, vbox25, vbox35, vbox45, vbox55, vbox65},
                {vbox06, vbox16, vbox26, vbox36, vbox46, vbox56, vbox66}
        };

        for (int row = 0; row < vboxs.length; row++) {
            for (int col = 0; col < vboxs[row].length; col++) {
                VBox vbox = vboxs[row][col];
                vbox.setOnMouseClicked(event -> handleVBoxClick(vbox));
            }
        }

        LocalDate localDate = LocalDate.now();
        currentYear = localDate.getYear();
        currentMonth = localDate.getMonthValue();

        updateDayLabels(currentYear, currentMonth);

        year_label.setText(Integer.toString(currentYear));
        month_label.setText(Integer.toString(currentMonth));

        Id = primary();

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/get_teamname.php";
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

                // 서버에서 받은 teamname 출력
                teamname = response.toString();
                System.out.println("Teamname: " + teamname);

            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (IOException e) {
            // IOException이 발생한 경우에 대한 예외 처리
            e.printStackTrace();
            // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
        } catch (Exception e) {
            // 그 외의 예외에 대한 예외 처리
            e.printStackTrace();
            // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
        }


        try {
            // POST 요청을 보낼 PHP 서버의 URL
            String serverURL = "http://hbr2024.dothome.co.kr/updateproject.php";

            // URL 객체 생성
            URL url = new URL(serverURL);

            // HTTP 연결 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // POST 데이터 생성
            String postData = "teamname=" + teamname;

            // 데이터를 서버로 전송
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            // 서버로부터 응답 코드 받기
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                String responseData = response.toString();
                // 데이터가 비어 있는지 확인
                if (!responseData.isEmpty()) {
                    try {
                        // 서버 응답 데이터가 JSON 형식인지 확인
                        if (responseData.startsWith("[")) {
                            // JSON 형식일 경우 JSONArray로 변환
                            JSONArray jsonArray = new JSONArray(responseData);

                            Map<String, Color> contentColorMap = new HashMap<>();

// 각 콘텐츠에 대해 색상 매핑
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String projectname = jsonObject.getString("projectname");
                                project_label.setText(projectname);
                                String content = jsonObject.getString("content");

                                // 콘텐츠에 대한 색상이 매핑되어 있는지 확인
                                if (!contentColorMap.containsKey(content)) {
                                    // 랜덤한 밝은색 계열의 색상 생성
                                    Color color = getRandomBrightColor();
                                    contentColorMap.put(content, color);
                                }
                            }

// 각 날짜에 대해 콘텐츠와 색상을 매핑하여 적용
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");
                                String startDate = jsonObject.getString("startdate");
                                String endDate = jsonObject.getString("enddate");

                                LocalDate startLocalDate = LocalDate.parse(startDate);
                                LocalDate endLocalDate = LocalDate.parse(endDate);

                                year = Integer.parseInt(year_label.getText());
                                month = Integer.parseInt(month_label.getText());

                                if (startLocalDate.getYear() == year && startLocalDate.getMonthValue() == month) {
                                    for (LocalDate date = startLocalDate; date.isBefore(endLocalDate.plusDays(1)); date = date.plusDays(1)) {
                                        int year = date.getYear();
                                        int month = date.getMonthValue();
                                        int day = date.getDayOfMonth();

                                        // 해당 날짜에 맞는 content 라벨을 생성하여 content를 적용시켜주고 팬의 배경색을 적용
                                        for (int row = 0; row < 6; row++) {
                                            for (int col = 0; col < 7; col++) {
                                                Label dayLabel = dayLabels[row * 7 + col];
                                                String labelText = dayLabel.getText();
                                                if (!labelText.isEmpty() && Integer.parseInt(labelText) == day) {
                                                    // 해당하는 vbox를 가져와서 content 라벨 생성 및 추가
                                                    VBox vbox = vboxs[row][col];
                                                    Label contentLabel = new Label(content);
                                                    contentLabel.setFont(new Font("Arial", 12));
                                                    // 해당하는 콘텐츠에 맞는 색상 적용
                                                    Color color = contentColorMap.get(content);
                                                    contentLabel.setTextFill(Color.BLACK); // 글씨색은 검정색으로 지정
                                                    vbox.getChildren().add(contentLabel);
                                                    // 배경색을 해당 콘텐츠의 색상으로 지정
                                                    vbox.setStyle("-fx-background-color: " + toRGBCode(color) + ";");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // JSON 형식이 아닌 경우에 대한 처리
                            System.out.println("Response data is not in JSON format.");
                            // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                        }
                    } catch (JSONException e) {
                        // JSON 형식이 잘못된 경우에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    } catch (Exception e) {
                        // 그 외의 예외에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    }
                } else {
                    System.out.println("Response data is empty.");
                    // 처리할 작업을 추가하세요 (예: 기본 값을 설정하거나 사용자에게 메시지 출력)
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/projectcpt_get.php";
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

                // JSON 응답을 처리하여 각 content에 대해 disableMatchingVBoxes를 호출
                String jsonResponse = response.toString();
                JSONArray jsonArray = new JSONArray(jsonResponse);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String content = jsonArray.getString(i);
                    disableMatchingVBoxes(content);
                }


            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleVBoxClick(VBox vbox) {
        // Check if the VBox has 2 or more children
        if (vbox.getChildren().size() >= 2) {
            // Get the last child of the VBox
            Node lastChild = vbox.getChildren().get(vbox.getChildren().size() - 1);
            if (lastChild instanceof Label) {
                Label lastLabel = (Label) lastChild;
                String content = lastLabel.getText();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("project_complete.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();

                    project_cptController project_cptController = loader.getController();
                    project_cptController.initData(content, teamname);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //disableMatchingVBoxes(content);
            }
        }
    }

    private void disableMatchingVBoxes(String content) {
        for (int row = 0; row < vboxs.length; row++) {
            for (int col = 0; col < vboxs[row].length; col++) {
                VBox vbox = vboxs[row][col];
                // Check if the VBox has 2 or more children
                if (vbox.getChildren().size() >= 2) {
                    // Get the last child of the VBox
                    Node lastChild = vbox.getChildren().get(vbox.getChildren().size() - 1);
                    if (lastChild instanceof Label) {
                        Label lastLabel = (Label) lastChild;
                        if (lastLabel.getText().equals(content)) {
                            // Disable the VBox
                            vbox.setDisable(true);
                        }
                    }
                }
            }
        }
    }

    private String toRGBCode(Color color) {
        // Color 객체의 RGB 값을 255로 곱하여 정수 값으로 변환한 후 HEX 문자열로 변환
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);

        // HEX 문자열로 변환한 RGB 값을 결합하여 RGB 코드 생성
        String rgbCode = String.format("#%02X%02X%02X", r, g, b);

        return rgbCode;
    }

    private Color getRandomBrightColor() {
        // 랜덤한 밝은색 계열의 RGB 값을 생성
        double red = Math.random() * 0.5 + 0.5; // 0.5 ~ 1.0
        double green = Math.random() * 0.5 + 0.5; // 0.5 ~ 1.0
        double blue = Math.random() * 0.5 + 0.5; // 0.5 ~ 1.0

        // RGB 값을 기반으로 Color 객체 생성
        Color color = new Color(red, green, blue, 1.0); // 마지막 매개변수는 알파값(투명도)로, 여기서는 1.0으로 설정하여 완전히 불투명하도록 합니다.

        return color;
    }

    @FXML
    private void boxMouseClick(MouseEvent event) {
        // 클릭한 상자의 행 및 열 값을 얻음
        Pane pane = (Pane) event.getSource();

        // GridPane의 행과 열 제약 조건을 이용하여 인덱스를 추정
        int rowIndex = GridPane.getRowIndex(pane) == null ? 0 : GridPane.getRowIndex(pane);
        int colIndex = GridPane.getColumnIndex(pane) == null ? 0 : GridPane.getColumnIndex(pane);

        Label dayLabel = dayLabels[rowIndex * 7 + colIndex-7]; // 1차원 배열을 2차원 배열로 변경
        String date = (String) pane.getUserData();
        System.out.println("클릭한 상자의 위치: 행 = " + colIndex + ", 열 = " + rowIndex);
        System.out.println("날짜 레이블의 값: " + dayLabel.getText());


        String dayLabelText = dayLabel.getText();
        if (!dayLabelText.isEmpty()) {
            clickday = dayLabel.getText();
            System.out.println("날짜 레이블의 값: " + clickday);
            clickyear = String.valueOf(currentYear);
            clickmonth = String.valueOf(currentMonth);
        }
    }

    private void updateDayLabels(int year, int month) {
        for (Label label : dayLabels) {
            setDayLabel(label, ""); // 텍스트를 빈 문자열로 설정하여 일자 정보를 없앰
        }
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        boolean isLeapYear = firstDayOfMonth.isLeapYear();
        int daysInFebruary = isLeapYear ? 29 : 28;

        int startDayOfWeek = (firstDayOfMonth.getDayOfWeek().getValue()) % 7;

        // 해당 월의 일수를 구합니다.
        int daysInMonth;
        switch (month) {
            case 2:
                daysInMonth = daysInFebruary;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysInMonth = 30;
                break;
            default:
                daysInMonth = 31;
        }

        // 1주차의 시작 부분을 살펴봅니다.
        int dayIndex = startDayOfWeek; // 0부터 시작하도록 조정

        // 1주차의 시작 부분부터 해당 월의 일수만큼 날짜 레이블에 숫자를 표시합니다.
        for (int day = 1; day <= daysInMonth; day++) {
            setDayLabel(dayLabels[dayIndex], Integer.toString(day));
            dayIndex++;
        }
    }

    private void setDayLabel(Label label, String text) {
        if (label != null) {
            label.setText(text);
        }
    }

    private void setDayLabelsVisibility(boolean visible, Label[] dayLabels) {
        for (Label label : dayLabels) {
            if (label != null) {
                label.setVisible(visible);
            }
        }
    }

    @FXML
    private void onPreviousMonthButtonClick(ActionEvent event) {

        try {
            // POST 요청을 보낼 PHP 서버의 URL
            String serverURL = "http://hbr2024.dothome.co.kr/updateproject.php";

            // URL 객체 생성
            URL url = new URL(serverURL);

            // HTTP 연결 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // POST 데이터 생성
            String postData = "teamname=" + teamname;

            // 데이터를 서버로 전송
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            // 서버로부터 응답 코드 받기
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                String responseData = response.toString();
                // 데이터가 비어 있는지 확인
                if (!responseData.isEmpty()) {
                    try {
                        // 서버 응답 데이터가 JSON 형식인지 확인
                        if (responseData.startsWith("[")) {
                            // JSON 형식일 경우 JSONArray로 변환
                            JSONArray jsonArray = new JSONArray(responseData);

                            Map<String, Color> contentColorMap = new HashMap<>();

// 각 콘텐츠에 대해 색상 매핑
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");

                                // 콘텐츠에 대한 색상이 매핑되어 있는지 확인
                                if (!contentColorMap.containsKey(content)) {
                                    // 랜덤한 밝은색 계열의 색상 생성
                                    Color color = getRandomBrightColor();
                                    contentColorMap.put(content, color);
                                }
                            }

// 각 날짜에 대해 콘텐츠와 색상을 매핑하여 적용
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");
                                String startDate = jsonObject.getString("startdate");
                                String endDate = jsonObject.getString("enddate");

                                LocalDate startLocalDate = LocalDate.parse(startDate);
                                LocalDate endLocalDate = LocalDate.parse(endDate);

                                // 시작일부터 종료일까지의 각 날짜에 대해 처리
                                for (LocalDate date = startLocalDate; date.isBefore(endLocalDate.plusDays(1)); date = date.plusDays(1)) {
                                    int year = date.getYear();
                                    int month = date.getMonthValue();
                                    int day = date.getDayOfMonth();

                                    for (int row = 0; row < 6; row++) {
                                        for (int col = 0; col < 7; col++) {
                                            Label dayLabel = dayLabels[row * 7 + col];
                                            String labelText = dayLabel.getText();
                                            if (!labelText.isEmpty() && Integer.parseInt(labelText) == day) {
                                                // 해당하는 vbox를 찾아서 마지막에 생성된 라벨을 삭제
                                                VBox vbox = vboxs[row][col];
                                                // 해당 VBox에서 자식 요소의 개수 확인
                                                ObservableList<Node> children = vbox.getChildren();
                                                int numChildren = children.size();

                                                // 자식 요소가 2개 이상인 경우에만 마지막 요소 삭제
                                                if (numChildren > 1) {
                                                    // 마지막 요소 제거
                                                    Node lastChild = children.get(numChildren - 1);
                                                    vbox.getChildren().remove(lastChild);
                                                }

                                                // 배경색을 초기화
                                                vbox.setStyle("-fx-background-color: transparent;");
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // JSON 형식이 아닌 경우에 대한 처리
                            System.out.println("Response data is not in JSON format.");
                            // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                        }
                    } catch (JSONException e) {
                        // JSON 형식이 잘못된 경우에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    } catch (Exception e) {
                        // 그 외의 예외에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    }
                } else {
                    System.out.println("Response data is empty.");
                    // 처리할 작업을 추가하세요 (예: 기본 값을 설정하거나 사용자에게 메시지 출력)
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 현재 월을 이전 월로 감소시킴
        currentMonth--;
        if (currentMonth < 1) {
            // 현재 월이 1월보다 작으면 연도를 감소시키고 12월로 설정
            currentMonth = 12;
            currentYear--;
        }

        for (Label label : dayLabels) {
            setDayLabel(label, ""); // 텍스트를 빈 문자열로 설정하여 일자 정보를 없앰
        }

        // 연도와 월을 기반으로 달력을 갱신
        updateDayLabels(currentYear, currentMonth);

        // 레이블에 연도와 월을 업데이트
        year_label.setText(Integer.toString(currentYear));
        month_label.setText(Integer.toString(currentMonth));

        setDayLabelsVisibility(true, dayLabels);
        try {
            // POST 요청을 보낼 PHP 서버의 URL
            String serverURL = "http://hbr2024.dothome.co.kr/updateproject.php";

            // URL 객체 생성
            URL url = new URL(serverURL);

            // HTTP 연결 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // POST 데이터 생성
            String postData = "teamname=" + teamname;

            // 데이터를 서버로 전송
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            // 서버로부터 응답 코드 받기
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                String responseData = response.toString();
                // 데이터가 비어 있는지 확인
                if (!responseData.isEmpty()) {
                    try {
                        // 서버 응답 데이터가 JSON 형식인지 확인
                        if (responseData.startsWith("[")) {
                            // JSON 형식일 경우 JSONArray로 변환
                            JSONArray jsonArray = new JSONArray(responseData);

                            Map<String, Color> contentColorMap = new HashMap<>();

// 각 콘텐츠에 대해 색상 매핑
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");

                                // 콘텐츠에 대한 색상이 매핑되어 있는지 확인
                                if (!contentColorMap.containsKey(content)) {
                                    // 랜덤한 밝은색 계열의 색상 생성
                                    Color color = getRandomBrightColor();
                                    contentColorMap.put(content, color);
                                }
                            }

// 각 날짜에 대해 콘텐츠와 색상을 매핑하여 적용
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");
                                String startDate = jsonObject.getString("startdate");
                                String endDate = jsonObject.getString("enddate");

                                LocalDate startLocalDate = LocalDate.parse(startDate);
                                LocalDate endLocalDate = LocalDate.parse(endDate);

                                year = Integer.parseInt(year_label.getText());
                                month = Integer.parseInt(month_label.getText());

                                if (startLocalDate.getYear() == year && startLocalDate.getMonthValue() == month) {
                                    for (LocalDate date = startLocalDate; date.isBefore(endLocalDate.plusDays(1)); date = date.plusDays(1)) {
                                        int year = date.getYear();
                                        int month = date.getMonthValue();
                                        int day = date.getDayOfMonth();

                                        // 해당 날짜에 맞는 content 라벨을 생성하여 content를 적용시켜주고 팬의 배경색을 적용
                                        for (int row = 0; row < 6; row++) {
                                            for (int col = 0; col < 7; col++) {
                                                Label dayLabel = dayLabels[row * 7 + col];
                                                String labelText = dayLabel.getText();
                                                if (!labelText.isEmpty() && Integer.parseInt(labelText) == day) {
                                                    // 해당하는 vbox를 가져와서 content 라벨 생성 및 추가
                                                    VBox vbox = vboxs[row][col];
                                                    Label contentLabel = new Label(content);
                                                    contentLabel.setFont(new Font("Arial", 12));
                                                    // 해당하는 콘텐츠에 맞는 색상 적용
                                                    Color color = contentColorMap.get(content);
                                                    contentLabel.setTextFill(Color.BLACK); // 글씨색은 검정색으로 지정
                                                    vbox.getChildren().add(contentLabel);
                                                    // 배경색을 해당 콘텐츠의 색상으로 지정
                                                    vbox.setStyle("-fx-background-color: " + toRGBCode(color) + ";");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // JSON 형식이 아닌 경우에 대한 처리
                            System.out.println("Response data is not in JSON format.");
                            // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                        }
                    } catch (JSONException e) {
                        // JSON 형식이 잘못된 경우에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    } catch (Exception e) {
                        // 그 외의 예외에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    }
                } else {
                    System.out.println("Response data is empty.");
                    // 처리할 작업을 추가하세요 (예: 기본 값을 설정하거나 사용자에게 메시지 출력)
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNextMonthButtonClick(ActionEvent event) {

        try {
            // POST 요청을 보낼 PHP 서버의 URL
            String serverURL = "http://hbr2024.dothome.co.kr/updateproject.php";

            // URL 객체 생성
            URL url = new URL(serverURL);

            // HTTP 연결 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // POST 데이터 생성
            String postData = "teamname=" + teamname;

            // 데이터를 서버로 전송
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            // 서버로부터 응답 코드 받기
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                String responseData = response.toString();
                // 데이터가 비어 있는지 확인
                if (!responseData.isEmpty()) {
                    try {
                        // 서버 응답 데이터가 JSON 형식인지 확인
                        if (responseData.startsWith("[")) {
                            // JSON 형식일 경우 JSONArray로 변환
                            JSONArray jsonArray = new JSONArray(responseData);

                            Map<String, Color> contentColorMap = new HashMap<>();

// 각 콘텐츠에 대해 색상 매핑
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");

                                // 콘텐츠에 대한 색상이 매핑되어 있는지 확인
                                if (!contentColorMap.containsKey(content)) {
                                    // 랜덤한 밝은색 계열의 색상 생성
                                    Color color = getRandomBrightColor();
                                    contentColorMap.put(content, color);
                                }
                            }

// 각 날짜에 대해 콘텐츠와 색상을 매핑하여 적용
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");
                                String startDate = jsonObject.getString("startdate");
                                String endDate = jsonObject.getString("enddate");

                                LocalDate startLocalDate = LocalDate.parse(startDate);
                                LocalDate endLocalDate = LocalDate.parse(endDate);

                                // 시작일부터 종료일까지의 각 날짜에 대해 처리
                                for (LocalDate date = startLocalDate; date.isBefore(endLocalDate.plusDays(1)); date = date.plusDays(1)) {
                                    int year = date.getYear();
                                    int month = date.getMonthValue();
                                    int day = date.getDayOfMonth();

                                    for (int row = 0; row < 6; row++) {
                                        for (int col = 0; col < 7; col++) {
                                            Label dayLabel = dayLabels[row * 7 + col];
                                            String labelText = dayLabel.getText();
                                            if (!labelText.isEmpty() && Integer.parseInt(labelText) == day) {
                                                // 해당하는 vbox를 찾아서 마지막에 생성된 라벨을 삭제
                                                VBox vbox = vboxs[row][col];
                                                // 해당 VBox에서 자식 요소의 개수 확인
                                                ObservableList<Node> children = vbox.getChildren();
                                                int numChildren = children.size();

                                                // 자식 요소가 2개 이상인 경우에만 마지막 요소 삭제
                                                if (numChildren > 1) {
                                                    // 마지막 요소 제거
                                                    Node lastChild = children.get(numChildren - 1);
                                                    vbox.getChildren().remove(lastChild);
                                                }

                                                // 배경색을 초기화
                                                vbox.setStyle("-fx-background-color: transparent;");
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // JSON 형식이 아닌 경우에 대한 처리
                            System.out.println("Response data is not in JSON format.");
                            // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                        }
                    } catch (JSONException e) {
                        // JSON 형식이 잘못된 경우에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    } catch (Exception e) {
                        // 그 외의 예외에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    }
                } else {
                    System.out.println("Response data is empty.");
                    // 처리할 작업을 추가하세요 (예: 기본 값을 설정하거나 사용자에게 메시지 출력)
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 현재 월을 다음 월로 증가시킴
        currentMonth++;
        if (currentMonth > 12) {
            // 현재 월이 12월보다 크면 연도를 증가시키고 1월로 설정
            currentMonth = 1;
            currentYear++;
        }

        for (Label label : dayLabels) {
            setDayLabel(label, ""); // 텍스트를 빈 문자열로 설정하여 일자 정보를 없앰
        }

        // 연도와 월을 기반으로 달력을 갱신
        updateDayLabels(currentYear, currentMonth);

        // 레이블에 연도와 월을 업데이트
        year_label.setText(Integer.toString(currentYear));
        month_label.setText(Integer.toString(currentMonth));

        setDayLabelsVisibility(true, dayLabels);

        try {
            // POST 요청을 보낼 PHP 서버의 URL
            String serverURL = "http://hbr2024.dothome.co.kr/updateproject.php";

            // URL 객체 생성
            URL url = new URL(serverURL);

            // HTTP 연결 생성
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // POST 데이터 생성
            String postData = "teamname=" + teamname;

            // 데이터를 서버로 전송
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            // 서버로부터 응답 코드 받기
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 응답 데이터 읽기
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                String responseData = response.toString();
                // 데이터가 비어 있는지 확인
                if (!responseData.isEmpty()) {
                    try {
                        // 서버 응답 데이터가 JSON 형식인지 확인
                        if (responseData.startsWith("[")) {
                            // JSON 형식일 경우 JSONArray로 변환
                            JSONArray jsonArray = new JSONArray(responseData);

                            Map<String, Color> contentColorMap = new HashMap<>();

// 각 콘텐츠에 대해 색상 매핑
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");

                                // 콘텐츠에 대한 색상이 매핑되어 있는지 확인
                                if (!contentColorMap.containsKey(content)) {
                                    // 랜덤한 밝은색 계열의 색상 생성
                                    Color color = getRandomBrightColor();
                                    contentColorMap.put(content, color);
                                }
                            }

// 각 날짜에 대해 콘텐츠와 색상을 매핑하여 적용
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String content = jsonObject.getString("content");
                                String startDate = jsonObject.getString("startdate");
                                String endDate = jsonObject.getString("enddate");

                                LocalDate startLocalDate = LocalDate.parse(startDate);
                                LocalDate endLocalDate = LocalDate.parse(endDate);

                                year = Integer.parseInt(year_label.getText());
                                month = Integer.parseInt(month_label.getText());

                                if (startLocalDate.getYear() == year && startLocalDate.getMonthValue() == month) {
                                    for (LocalDate date = startLocalDate; date.isBefore(endLocalDate.plusDays(1)); date = date.plusDays(1)) {
                                        int year = date.getYear();
                                        int month = date.getMonthValue();
                                        int day = date.getDayOfMonth();

                                        // 해당 날짜에 맞는 content 라벨을 생성하여 content를 적용시켜주고 팬의 배경색을 적용
                                        for (int row = 0; row < 6; row++) {
                                            for (int col = 0; col < 7; col++) {
                                                Label dayLabel = dayLabels[row * 7 + col];
                                                String labelText = dayLabel.getText();
                                                if (!labelText.isEmpty() && Integer.parseInt(labelText) == day) {
                                                    // 해당하는 vbox를 가져와서 content 라벨 생성 및 추가
                                                    VBox vbox = vboxs[row][col];
                                                    Label contentLabel = new Label(content);
                                                    contentLabel.setFont(new Font("Arial", 12));
                                                    // 해당하는 콘텐츠에 맞는 색상 적용
                                                    Color color = contentColorMap.get(content);
                                                    contentLabel.setTextFill(Color.BLACK); // 글씨색은 검정색으로 지정
                                                    vbox.getChildren().add(contentLabel);
                                                    // 배경색을 해당 콘텐츠의 색상으로 지정
                                                    vbox.setStyle("-fx-background-color: " + toRGBCode(color) + ";");
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            // JSON 형식이 아닌 경우에 대한 처리
                            System.out.println("Response data is not in JSON format.");
                            // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                        }
                    } catch (JSONException e) {
                        // JSON 형식이 잘못된 경우에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    } catch (Exception e) {
                        // 그 외의 예외에 대한 예외 처리
                        e.printStackTrace();
                        // 예외 발생 시 사용자에게 메시지 출력 등의 작업 추가 가능
                    }
                } else {
                    System.out.println("Response data is empty.");
                    // 처리할 작업을 추가하세요 (예: 기본 값을 설정하거나 사용자에게 메시지 출력)
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void workButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Work.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) work_button.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // 사용자에게 오류 메시지 표시
        }
    }

    @FXML
    private void mainButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) main_button.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // 사용자에게 오류 메시지 표시
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
    private void manageButtonAction(ActionEvent event) {
        Id = primary();
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
                // Id가 존재하는지 여부에 따라 특정 행동 수행
                if (!serverResponse.equals("Id does not exist in the team table.")) {
                    try {
                        String serverURL1 = "http://hbr2024.dothome.co.kr/checkproject.php";
                        URL url1 = new URL(serverURL1);
                        HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                        connection1.setRequestMethod("POST");
                        connection1.setDoOutput(true);

                        String postData1 = "teamname=" + teamname;
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
                            System.out.println("Response: " + response1.toString());

                            String serverResponse1 = response1.toString();
                            if (serverResponse1.equals("Teamname exists in the project table.")) {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("project_management.fxml"));
                                    Parent root = loader.load();
                                    Scene scene = new Scene(root);
                                    Stage stage = new Stage();
                                    stage.setScene(scene);
                                    stage.show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("project_name.fxml"));
                                    Parent root = loader.load();
                                    Scene scene = new Scene(root);
                                    Stage stage = new Stage();
                                    stage.setScene(scene);
                                    stage.show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("HTTP Error Code: " + responseCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("project_teamname.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
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

    @FXML
    private void teamButtonClick(ActionEvent event) {
        Id = primary();
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
                // Id가 존재하는지 여부에 따라 특정 행동 수행
                if (!serverResponse.equals("Id does not exist in the team table.")) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("project_team.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("project_teamname.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
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
}
