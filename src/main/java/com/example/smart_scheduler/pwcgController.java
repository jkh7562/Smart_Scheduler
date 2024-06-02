package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class pwcgController {
    @FXML
    private ImageView back_image;
    @FXML
    private PasswordField pass_textfield;
    @FXML
    private PasswordField chgepass_textfield;
    @FXML
    private PasswordField cfpass_textfield;
    @FXML
    private Button enter_button;

    private String Id;

    public String primary() {
        String primaryid = null;

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/priget.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "tableName=pri";
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
                String[] data = response.toString().split(",");
                if (data.length > 0) {
                    primaryid = data[0];
                } else {
                    System.out.println("No data available.");
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return primaryid;
    }

    @FXML
    private void backimageClicked(MouseEvent event) {
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        currentStage.close();
    }

    @FXML
    private void enterButtonAction(ActionEvent event) {
        String Id = primary();
        String pass = pass_textfield.getText();
        String chgepass = chgepass_textfield.getText();
        String cfpass = cfpass_textfield.getText();

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
                System.out.println("Response: " + serverResponse);

                if ("Password match".equals(serverResponse)) {
                    if (chgepass.equals(cfpass)) {
                        changePassword(Id, chgepass);
                    } else {
                        loadFXML("/com/example/smart_scheduler/passfail.fxml");
                    }
                } else {
                    loadFXML("/com/example/smart_scheduler/passfail.fxml");
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changePassword(String Id, String newPassword) {
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/chgePW.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "Id=" + Id + "&pass=" + newPassword;
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("chgePW_cpt.fxml"));
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
