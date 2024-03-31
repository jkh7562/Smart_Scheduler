package com.example.smart_scheduler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class WorkController {

    @FXML
    private GridPane gridPane; // 그리드 팬에 대한 참조

    @FXML
    private Pane Mon08,Mon09,Mon10,Mon11,Mon12,Mon13,Mon14,Mon15,Mon16,Mon17,Mon18,Mon19,Mon20,Mon21,Mon22;
    @FXML
    private Pane Tue08,Tue09,Tue10,Tue11,Tue12,Tue13,Tue14,Tue15,Tue16,Tue17,Tue18,Tue19,Tue20,Tue21,Tue22;
    @FXML
    private Pane Wen08,Wen09,Wen10,Wen11,Wen12,Wen13,Wen14,Wen15,Wen16,Wen17,Wen18,Wen19,Wen20,Wen21,Wen22;
    @FXML
    private Pane Thu08,Thu09,Thu10,Thu11,Thu12,Thu13,Thu14,Thu15,Thu16,Thu17,Thu18,Thu19,Thu20,Thu21,Thu22;
    @FXML
    private Pane Fri08,Fri09,Fri10,Fri11,Fri12,Fri13,Fri14,Fri15,Fri16,Fri17,Fri18,Fri19,Fri20,Fri21,Fri22;
    @FXML
    private Pane Sat08,Sat09,Sat10,Sat11,Sat12,Sat13,Sat14,Sat15,Sat16,Sat17,Sat18,Sat19,Sat20,Sat21,Sat22;
    @FXML
    private Pane Sun08,Sun09,Sun10,Sun11,Sun12,Sun13,Sun14,Sun15,Sun16,Sun17,Sun18,Sun19,Sun20,Sun21,Sun22;
    @FXML
    private Button plus_button;
    @FXML
    private Button main_button;


    // 팬을 요일과 시간에 따라 구분하기 위한 변수 정의
    enum DayOfWeek { Mon, Tue, Wed, Thu, Fri, Sat, Sun }

    private final int START_HOUR = 8;
    private final int END_HOUR = 22;

    // 각 요일과 시간에 대한 팬 배열 정의
    private Pane[][] panes = new Pane[DayOfWeek.values().length][END_HOUR - START_HOUR + 1];

    // FXML에서 정의한 각 팬에 대한 초기화 메서드
    @FXML
    private void initialize() {
        // 각 요일과 시간에 대한 팬을 배열에 할당
        for (DayOfWeek day : DayOfWeek.values()) {
            for (int hour = START_HOUR; hour <= END_HOUR; hour++) {
                Pane pane = getPane(day, hour);
                pane.setOnMouseClicked(event -> handlePaneClick(pane)); // 이벤트 핸들러에 팬을 전달합니다.
                pane.setPickOnBounds(true); // 팬의 경계를 클릭 가능하게 만듭니다.
                panes[day.ordinal()][hour - START_HOUR] = pane;
            }
        }
    }

    // 클릭한 팬의 요일과 시간을 확인하는 메서드
    private void handlePaneClick(Pane clickedPane) {
        int columnIndex = GridPane.getColumnIndex(clickedPane) != null ? GridPane.getColumnIndex(clickedPane) : 0;
        int rowIndex = GridPane.getRowIndex(clickedPane) != null ? GridPane.getRowIndex(clickedPane) : 0;

        columnIndex -= 1;
        // 요일과 시간 계산
        DayOfWeek day = DayOfWeek.values()[columnIndex]; // 열 인덱스에 해당하는 요일
        int hour = rowIndex + START_HOUR; // 행 인덱스에 해당하는 시간

        // 클릭한 팬의 요일과 시간 출력
        System.out.println("Clicked pane represents " + day + " at " + hour + ":00");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Work_detail.fxml"));
            Parent root = loader.load();
            Stage currentStage = (Stage) plus_button.getScene().getWindow();
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

}