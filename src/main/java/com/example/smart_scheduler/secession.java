package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class secession {

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
    Label name2_label;

    @FXML
    Button back_button;
    @FXML
    Button out_button;
    @FXML
    PasswordField pass_textfield;

    String Id;

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
    public void initialize() {

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
                name2_label.setText(userName);


            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    @FXML
    private void backButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) back_button.getScene().getWindow();
            currentStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            // 사용자에게 오류 메시지 표시
        }
    }

    @FXML
    private void outButtonAction(ActionEvent event) {
        Id = primary();
        String pass = pass_textfield.getText();

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/passcheck.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "Id=" + Id + "&pass=" + pass;
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
                System.out.println("Response: " + response.toString());

                if ("Password match".equals(serverResponse)) {
                    try {
                        String serverURL1 = "http://hbr2024.dothome.co.kr/deleteuser.php";
                        URL url1 = new URL(serverURL1);
                        HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                        connection1.setRequestMethod("POST");
                        connection1.setDoOutput(true);

                        String postData1 = "Id=" + Id;
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

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("deleteuser_cpt.fxml"));
                            Parent root = loader.load();
                            Scene scene = new Scene(root);

                            // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                            Stage failStage = new Stage();
                            failStage.setScene(scene);
                            failStage.show();

                        } else {
                            System.out.println("HTTP Error Code: " + responseCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    loadFXML("/com/example/smart_scheduler/passfail.fxml");
                }


            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
