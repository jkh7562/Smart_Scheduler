package com.example.smart_scheduler;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

public class projectmanageController {
    @FXML
    ImageView back_image;
    @FXML
    Button change_button;
    @FXML
    Label project_label;
    @FXML
    Label teamname_label;
    @FXML
    Label id_label;
    @FXML
    Button save_button;
    @FXML
    Button alldelete_button;
    @FXML
    Button delete_button;
    @FXML
    ListView content_listview;

    String Id;
    String teamname;

    public void initData(String project) {
        project_label.setText(project);
    }

    @FXML
    private void initialize() {
        Id = primary();
        id_label.setText(Id);
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
                teamname_label.setText(teamname);

            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
                    // 데이터를 JSON 배열로 변환하여 처리
                    try {
                        ObservableList<Node> items = content_listview.getItems();
                        content_listview.setItems(items);

                        JSONArray jsonArray = new JSONArray(responseData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String projectName = jsonObject.getString("projectname");
                            project_label.setText(projectName);

                            // content, startdate, enddate 가져오기
                            String content = jsonObject.getString("content");
                            String startDate = jsonObject.getString("startdate");
                            String endDate = jsonObject.getString("enddate");
                            // 리스트뷰 아이템 생성
                            Label listItemLabel = new Label("계획 :  " + content + "      " + "시작일자 : " + startDate + "      " + "종료일자 : " + endDate);
                            // 글꼴 크기 설정
                            listItemLabel.setFont(new Font(15));

                            // ListView에 아이템 추가
                            items.add(listItemLabel);
                        }
                    } catch (JSONException e) {
                        // 서버에서 반환된 데이터가 JSON 배열 형식이 아닌 경우 처리
                        System.out.println("Invalid JSON format from the server: " + responseData);
                    }
                } else {
                    System.out.println("Response data is empty.");
                }
            } else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // 데이터가 없는 경우 처리
                System.out.println("Data not found in the database.");
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateContentTableView(JSONArray contentArray, JSONArray startDateArray, JSONArray endDateArray) {
        // content_tableview 업데이트 로직 작성
        for (int i = 0; i < contentArray.length(); i++) {
            String content = contentArray.getString(i);
            String startDate = startDateArray.getString(i);
            String endDate = endDateArray.getString(i);

            System.out.println("Updating content_tableview with content: " + content + ", startDate: " + startDate + ", endDate: " + endDate);
        }
    }

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

    @FXML
    private void changeButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("project_name.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) change_button.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // 사용자에게 오류 메시지 표시
        }
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        String projectname = project_label.getText();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("project_add.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) save_button.getScene().getWindow();
            currentStage.setScene(new Scene(root));

            projectaddController projectaddController = loader.getController();
            projectaddController.initData(teamname, projectname);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void alldeleteButtonAction(ActionEvent event) {
        String projectname = project_label.getText();
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/alldelete_project.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "projectname=" + projectname;
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

                // 현재 창을 닫음
                Stage currentStage = (Stage) alldelete_button.getScene().getWindow();
                currentStage.close();

                // 다른 열려 있는 창들을 닫음
                Stage[] openStages = Stage.getWindows().toArray(new Stage[0]);
                for (Stage stage : openStages) {
                    if (stage != currentStage) {
                        stage.close();
                    }
                }
                try {
                    // 로그인 화면을 로드하고 컨트롤러를 설정하여 새 창으로 열기
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main_Project.fxml"));
                    Parent root = loader.load();

                    // 새로운 스테이지를 만들어서 로그인 창을 열기
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.show();
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
