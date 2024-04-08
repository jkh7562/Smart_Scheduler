package com.example.smart_scheduler;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class ProjectController {
    @FXML
    private GridPane calendar;
    @FXML
    private Pane pan01, pan11, pan21, pan31, pan41, pan51, pan61, pan02, pan12, pan22, pan32, pan42, pan52, pan62, pan03, pan13, pan23, pan33, pan43, pan53, pan63, pan04, pan14, pan24, pan34, pan44, pan54, pan64, pan05, pan15, pan25, pan35, pan45, pan55, pan65, pan06, pan16, pan26, pan36, pan46, pan56, pan66;
    @FXML
    private Label day01_label, day11_label, day21_label, day31_label, day41_label, day51_label, day61_label, day02_label, day12_label, day22_label, day32_label, day42_label, day52_label, day62_label, day03_label, day13_label, day23_label, day33_label, day43_label, day53_label, day63_label, day04_label, day14_label, day24_label, day34_label, day44_label, day54_label, day64_label, day05_label, day15_label, day25_label, day35_label, day45_label, day55_label, day65_label, day06_label, day16_label, day26_label, day36_label, day46_label, day56_label, day66_label;
    @FXML
    private Label year_label, month_label;
    @FXML
    private Button previous_button;

    // A map to hold day Labels for easy access
    private Map<Integer, Label> dayLabels = new HashMap<>();
    private Map<Integer, Pane> dayPanes = new HashMap<>();
    private YearMonth currentMonth;

    @FXML
    private void initialize() {
        mapLabelsAndPanes();
        currentMonth = YearMonth.now(); // Initialize currentMonth
        fillCalendar();
    }

    private void mapLabelsAndPanes() {
        dayLabels.put(1, day01_label); dayPanes.put(1, pan01);
        dayLabels.put(2, day11_label); dayPanes.put(2, pan11);
        dayLabels.put(3, day21_label); dayPanes.put(3, pan21);
        dayLabels.put(4, day31_label); dayPanes.put(4, pan31);
        dayLabels.put(5, day41_label); dayPanes.put(5, pan41);
        dayLabels.put(6, day51_label); dayPanes.put(6, pan51);
        dayLabels.put(7, day61_label); dayPanes.put(7, pan61);
        dayLabels.put(8, day02_label); dayPanes.put(8, pan02);
        dayLabels.put(9, day12_label); dayPanes.put(9, pan12);
        dayLabels.put(10, day22_label); dayPanes.put(10, pan22);
        dayLabels.put(11, day32_label); dayPanes.put(11, pan32);
        dayLabels.put(12, day42_label); dayPanes.put(12, pan42);
        dayLabels.put(13, day52_label); dayPanes.put(13, pan52);
        dayLabels.put(14, day62_label); dayPanes.put(14, pan62);
        dayLabels.put(15, day03_label); dayPanes.put(15, pan03);
        dayLabels.put(16, day13_label); dayPanes.put(16, pan13);
        dayLabels.put(17, day23_label); dayPanes.put(17, pan23);
        dayLabels.put(18, day33_label); dayPanes.put(18, pan33);
        dayLabels.put(19, day43_label); dayPanes.put(19, pan43);
        dayLabels.put(20, day53_label); dayPanes.put(20, pan53);
        dayLabels.put(21, day63_label); dayPanes.put(21, pan63);
        dayLabels.put(22, day04_label); dayPanes.put(22, pan04);
        dayLabels.put(23, day14_label); dayPanes.put(23, pan14);
        dayLabels.put(24, day24_label); dayPanes.put(24, pan24);
        dayLabels.put(25, day34_label); dayPanes.put(25, pan34);
        dayLabels.put(26, day44_label); dayPanes.put(26, pan44);
        dayLabels.put(27, day54_label); dayPanes.put(27, pan54);
        dayLabels.put(28, day64_label); dayPanes.put(28, pan64);
        dayLabels.put(29, day05_label); dayPanes.put(29, pan05);
        dayLabels.put(30, day15_label); dayPanes.put(30, pan15);
        dayLabels.put(31, day25_label); dayPanes.put(31, pan25);
        dayLabels.put(32, day35_label); dayPanes.put(32, pan35);
        dayLabels.put(33, day45_label); dayPanes.put(33, pan45);
        dayLabels.put(34, day55_label); dayPanes.put(34, pan55);
        dayLabels.put(35, day65_label); dayPanes.put(35, pan65);
        dayLabels.put(36, day06_label); dayPanes.put(36, pan06);
        dayLabels.put(37, day16_label); dayPanes.put(37, pan16);
        dayLabels.put(38, day26_label); dayPanes.put(38, pan26);
        dayLabels.put(39, day36_label); dayPanes.put(39, pan36);
        dayLabels.put(40, day46_label); dayPanes.put(40, pan46);
        dayLabels.put(41, day56_label); dayPanes.put(41, pan56);
        dayLabels.put(42, day66_label); dayPanes.put(42, pan66);
    }

    private void fillCalendar() {
        // Set year and month labels
        year_label.setText(String.valueOf(currentMonth.getYear()));
        month_label.setText(String.format("%02d", currentMonth.getMonthValue())); // 월을 숫자 형태로 설정

        clearLabels();

        LocalDate firstOfMonth = currentMonth.atDay(1);
        int dayOfWeekIndex = firstOfMonth.getDayOfWeek().getValue() % 7;
        int daysInMonth = currentMonth.lengthOfMonth();

        // 날짜 레이블 설정 및 이벤트 핸들러 추가 로직 구현...
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = firstOfMonth.plusDays(day - 1);
            Pane pane = dayPanes.get(day);
            Label label = dayLabels.get(day);
            if (pane != null && label != null) {
                label.setText(String.valueOf(date.getDayOfMonth()));
                pane.setOnMouseClicked(event -> System.out.println("Date clicked: " + date));
            }
        }
    }

    private void clearLabels() {
        dayLabels.values().forEach(label -> label.setText(""));
    }

    @FXML
    private void handlePreviousAction() {
        currentMonth = currentMonth.minusMonths(1);
        clearLabels(); // 이전 달의 날짜 레이블을 지웁니다.
        fillCalendar(); // 새로운 달로 캘린더를 다시 그립니다.
    }


}
