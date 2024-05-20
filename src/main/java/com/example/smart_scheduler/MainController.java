package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainController {


    @FXML
    private Button work_button;
    @FXML
    private Label nowtemp;
    @FXML
    private ImageView weather;
    @FXML
    private Label hightemp;
    @FXML
    private Label lowtemp;
    @FXML
    private Label weatherLabel;
    @FXML
    private Label uv;
    @FXML
    private Label dust;
    @FXML
    private ImageView item1;
    @FXML
    private ImageView item2;
    @FXML
    private ImageView item3;
    @FXML
    private Label itemLabel1;
    @FXML
    private Label itemLabel2;
    @FXML
    private Label itemLabel3;
    @FXML
    private Button project_button;


    private static int UV;
    private static String TMP;
    private static String UUU;
    private static String VVV;
    private static String VEC;
    private static String WSD;
    private static String SKY;
    private static String PTY;
    private static String POP;
    private static String WAV;
    private static String PCP;
    private static String REH;
    private static String SNO;
    private static String TMX;
    private static String TMN;
    private static int popint;
    private static boolean itememg1;
    private static String Dust;
    private static String Time;


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
}