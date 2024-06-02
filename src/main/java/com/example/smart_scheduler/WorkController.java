package com.example.smart_scheduler;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class WorkController {

    @FXML
    private GridPane gridPane; // 그리드 팬에 대한 참조

    @FXML
    private Pane Mon08, Mon09, Mon10, Mon11, Mon12, Mon13, Mon14, Mon15, Mon16, Mon17, Mon18, Mon19, Mon20, Mon21, Mon22;
    @FXML
    private Pane Tue08, Tue09, Tue10, Tue11, Tue12, Tue13, Tue14, Tue15, Tue16, Tue17, Tue18, Tue19, Tue20, Tue21, Tue22;
    @FXML
    private Pane Wen08, Wen09, Wen10, Wen11, Wen12, Wen13, Wen14, Wen15, Wen16, Wen17, Wen18, Wen19, Wen20, Wen21, Wen22;
    @FXML
    private Pane Thu08, Thu09, Thu10, Thu11, Thu12, Thu13, Thu14, Thu15, Thu16, Thu17, Thu18, Thu19, Thu20, Thu21, Thu22;
    @FXML
    private Pane Fri08, Fri09, Fri10, Fri11, Fri12, Fri13, Fri14, Fri15, Fri16, Fri17, Fri18, Fri19, Fri20, Fri21, Fri22;
    @FXML
    private Pane Sat08, Sat09, Sat10, Sat11, Sat12, Sat13, Sat14, Sat15, Sat16, Sat17, Sat18, Sat19, Sat20, Sat21, Sat22;
    @FXML
    private Pane Sun08, Sun09, Sun10, Sun11, Sun12, Sun13, Sun14, Sun15, Sun16, Sun17, Sun18, Sun19, Sun20, Sun21, Sun22;
    @FXML
    private Button plus_button;
    @FXML
    private Button main_button;
    @FXML
    Button delete_button;
    @FXML
    Button prty_button;
    @FXML
    Label content1_label;
    @FXML
    Label content2_label;
    @FXML
    Label content3_label;
    String content1;
    String content2;
    String content3;

    //유저팬 변수
    @FXML
    Pane user_pane;
    @FXML
    Label id_label;
    @FXML
    Button user_button;
    @FXML
    Label name_label;
    @FXML
    Button logout_button;
    @FXML
    Button pwcg_button;
    @FXML
    Button secession_button;

    @FXML
    Button project_button;

    private String Id;

    // 팬을 요일과 시간에 따라 구분하기 위한 변수 정의
    enum DayOfWeek {Mon, Tue, Wed, Thu, Fri, Sat, Sun}

    private final int START_HOUR = 8;
    private final int END_HOUR = 22;

    // WorkItem 클래스 정의
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

    // 각 요일과 시간에 대한 팬 배열 정의
    private Pane[][] panes = new Pane[DayOfWeek.values().length][END_HOUR - START_HOUR + 1];

        // FXML에서 정의한 각 팬에 대한 초기화 메서드
    @FXML
    private void initialize() {
        Id = primary();
        id_label.setText(Id);

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/usernameget.php";
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

                String userName = response.toString();
                name_label.setText(userName);


            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 각 요일과 시간에 대한 팬을 배열에 할당
        for (DayOfWeek day : DayOfWeek.values()) {
            for (int hour = START_HOUR; hour <= END_HOUR; hour++) {
                Pane pane = getPane(day, hour);
                pane.setOnMouseClicked(event -> handlePaneClick(pane)); // 이벤트 핸들러에 팬을 전달합니다.
                pane.setPickOnBounds(true); // 팬의 경계를 클릭 가능하게 만듭니다.
                panes[day.ordinal()][hour - START_HOUR] = pane;
            }
        }
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/getprtywork.php";
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

                // 응답에서 쉼표로 구분된 값을 가져와서 각각의 변수에 할당
                String[] contents = response.toString().split(",");
                if (contents.length >= 3) { // 적어도 3개의 값이 있어야 함
                    String content1 = contents[0];
                    String content2 = contents[1];
                    String content3 = contents[2];

                    // 가져온 값을 각각의 라벨에 설정
                    content1_label.setText(content1);
                    content2_label.setText(content2);
                    content3_label.setText(content3);
                } else {
                    // 가져온 값이 3개 미만인 경우에는 해당 값으로 라벨을 설정하고 나머지 라벨은 비움
                    for (int i = 0; i < contents.length; i++) {
                        switch (i) {
                            case 0:
                                content1_label.setText(contents[i]);
                                break;
                            case 1:
                                content2_label.setText(contents[i]);
                                break;
                            case 2:
                                content3_label.setText(contents[i]);
                                break;
                        }
                    }
                    // 3개 미만인 경우에는 나머지 라벨을 비움
                    for (int i = contents.length; i < 3; i++) {
                        switch (i) {
                            case 1:
                                content2_label.setText("");
                                break;
                            case 2:
                                content3_label.setText("");
                                break;
                        }
                    }
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        fetchWorkItemsAsync();
    }
    private void fetchWorkItemsAsync() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                fetchWorkItems();
                return null;
            }
        };
        new Thread(task).start();
    }

    private List<WorkItem> workItems = new ArrayList<>();

    private void fetchWorkItems() {
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/getwork.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String Id = primary(); // 여기에 Id를 할당하거나 다른 방식으로 Id를 설정해야 합니다.
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

                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    WorkItem item = new WorkItem(
                            jsonObject.getString("week"),
                            jsonObject.getString("start"),
                            jsonObject.getString("end"),
                            jsonObject.getString("content")
                    );
                    workItems.add(item);
                }
                // 로그 추가: 파싱된 데이터 개수 출력
                System.out.println("Fetched " + workItems.size() + " work items.");

                // UI 업데이트는 JavaFX 스레드에서 실행
                Platform.runLater(() -> updateUI());
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void updateUI() {
        // 이미 생성된 모든 팬의 내용을 클리어합니다.
        for (Pane[] paneRow : panes) {
            for (Pane pane : paneRow) {
                pane.getChildren().clear();
            }
        }

        Random rand = new Random(); // Random 객체 생성

        // GridPane에 작업 항목을 표시하기 위한 UI 업데이트 로직
        for (WorkItem item : workItems) {
            DayOfWeek dayOfWeek = convertToDayOfWeek(item.week);
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


    // 클릭한 팬의 요일과 시간을 확인하는 메서드
    private void handlePaneClick(Pane clickedPane) {
        int columnIndex = GridPane.getColumnIndex(clickedPane) != null ? GridPane.getColumnIndex(clickedPane) : 0;
        int rowIndex = GridPane.getRowIndex(clickedPane) != null ? GridPane.getRowIndex(clickedPane) : 0;

        columnIndex -= 1;
        // 요일과 시간 계산
        DayOfWeek weektmp = DayOfWeek.values()[columnIndex]; // 열 인덱스에 해당하는 요일
        int hour = rowIndex + START_HOUR; // 행 인덱스에 해당하는 시간

        // 클릭한 팬의 요일과 시간 출력
        System.out.println("Clicked pane represents " + weektmp + " at " + hour + ":00");
        try {
            String week = convertToKoreanDay(weektmp);
            System.out.printf(Id);
            System.out.printf(week);
            System.out.printf("%d",hour);
            System.out.println();
            System.out.println();

            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://hbr2024.dothome.co.kr/get_workschedule.php";

            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 요청 데이터 설정
            String postData = "Id=" + Id + "&week=" + week + "&hour=" + hour;
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

                // JSON 파싱
                JSONObject jsonResponse = new JSONObject(response.toString());
                String day = jsonResponse.getString("week");
                String startTime = jsonResponse.getString("start");
                String endTime = jsonResponse.getString("end");
                String content = jsonResponse.getString("content");
                String memo = jsonResponse.optString("memo","");

                System.out.printf(day);
                System.out.printf(startTime);
                System.out.printf(endTime);
                System.out.printf(content);
                System.out.printf(memo);
                System.out.println();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Work_detail.fxml"));
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedPane.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();

                    // 새로 열린 창에 데이터를 전달합니다.
                    WorkDetail workDetailController = loader.getController();
                    workDetailController.initData(day, startTime, endTime, content, memo);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertToKoreanDay(DayOfWeek dayOfWeek) {
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


    // 요일과 시간에 따라 해당하는 팬을 반환하는 메서드
    private Pane getPane(DayOfWeek day, int hour) {
        String paneId = String.format("%s%02d", day.toString().substring(0, 3), hour);
        Pane pane = (Pane) gridPane.lookup("#" + paneId);
        if (pane == null) {
            System.out.println("Pane not found for ID: " + paneId);
        }
        return pane;
    }

    @FXML
    private void plusButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Work_add.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
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

    @FXML
    private void deleteButtonAction(ActionEvent event) {
        Id = primary();
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/delete_allwork.php";
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

                FXMLLoader loader = new FXMLLoader(getClass().getResource("work_alldelete.fxml"));
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

    @FXML
    private void prtyButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Work_prty.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // 사용자에게 오류 메시지 표시
        }
    }

    @FXML
    private void projectButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Project.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) project_button.getScene().getWindow();
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
    private void userButtonAction(ActionEvent event) {
        if (user_pane.isDisable()) {
            user_pane.setDisable(false);
            user_pane.setVisible(true);
        } else {
            user_pane.setDisable(true);
            user_pane.setVisible(false);
        }
    }
    @FXML
    private void logoutButtonAction(ActionEvent event) {
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/delete.php";
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
                System.out.println("Response: " + response.toString());

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Login_screen.fxml"));
                    Parent root = loader.load();
                    Stage currentStage = (Stage) logout_button.getScene().getWindow();
                    currentStage.setScene(new Scene(root));
                } catch (IOException e) {
                    e.printStackTrace();
                    // 사용자에게 오류 메시지 표시
                }


            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void pwcgButtonAction(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Password_change.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root);

        // 새로운 Stage를 생성하여 로그인 실패 창을 표시
        Stage failStage = new Stage();
        failStage.setScene(scene);
        failStage.show();
    }
    @FXML
    private void secessionButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Membership_Withdrawal.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) secession_button.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // 사용자에게 오류 메시지 표시
        }
    }
}
