package com.example.smart_scheduler;

import javafx.application.Platform;
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
import javafx.scene.layout.HBox;
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
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainController {


    @FXML
    private Button work_button;
    @FXML
    private Label nowtmp;
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

    String Id;

    @FXML
    Label label_08, label_09, label_10, label_11, label_12, label_13, label_14, label_15, label_16, label_17, label_18, label_19, label_20, label_21, label_22;
    @FXML
    HBox hbox08, hbox09, hbox10, hbox11, hbox12, hbox13, hbox14, hbox15, hbox16, hbox17, hbox18, hbox19, hbox20, hbox21, hbox22;

    private Map<String, String> contentColorMap = new HashMap<>();
    private Random random = new Random();

    private void updateHBoxBackground(String start, String end, String content) {
        // 시간을 HBox와 Label에 매핑하기 위한 로직 추가
        HBox[] hboxes = {hbox08, hbox09, hbox10, hbox11, hbox12, hbox13, hbox14, hbox15, hbox16, hbox17, hbox18, hbox19, hbox20, hbox21, hbox22};
        Label[] labels = {label_08, label_09, label_10, label_11, label_12, label_13, label_14, label_15, label_16, label_17, label_18, label_19, label_20, label_21, label_22};
        int[] times = {8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22};

        // 콘텐츠별 색상 설정
        String color = contentColorMap.get(content);
        if (color == null) {
            color = getRandomBrightColor();
            contentColorMap.put(content, color);
        }

        // 시작과 끝 시간을 정수로 변환
        int startTime = Integer.parseInt(start);
        int endTime = Integer.parseInt(end);

        // HBox 배경색 및 Label 텍스트 업데이트
        for (int i = 0; i < times.length; i++) {
            if (times[i] >= startTime && times[i] <= endTime) {
                hboxes[i].setStyle("-fx-background-color: " + color + ";");
                labels[i].setText(content);
            }
        }
    }

    private String getRandomBrightColor() {
        // 밝은 색상 생성 로직 (RGB 값 중 두 개를 높게 설정)
        int r = random.nextInt(156) + 100; // 100-255
        int g = random.nextInt(156) + 100; // 100-255
        int b = random.nextInt(156) + 100; // 100-255
        return String.format("rgb(%d, %d, %d)", r, g, b);
    }

    @FXML
    public void initialize() {
        Id = primary();

        try {
            String serverURL = "http://hbr2024.dothome.co.kr/getday.php";
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

                // JSON 파싱
                JSONArray jsonArray = new JSONArray(response.toString());
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String start = jsonObject.getString("start");
                    String end = jsonObject.getString("end");
                    String content = jsonObject.getString("content");

                    // 데이터 출력
                    System.out.println("Start: " + start);
                    System.out.println("End: " + end);
                    System.out.println("Content: " + content);

                    // HBox 배경색 및 Label 텍스트 업데이트
                    updateHBoxBackground(start, end, content);
                }

            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 현재 시간 가져오기
        LocalTime now = LocalTime.now();



        // 날짜를 YYYYMMDD 형식으로 변환
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String baseDate = today.format(dateFormatter);

        // 시간을 HHmm 형식으로 변환 (0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300 이 아니면 가까운 값으로)
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
        LocalTime[] availableTimes = {LocalTime.of(2, 0), LocalTime.of(5, 0), LocalTime.of(8, 0),
                LocalTime.of(11, 0), LocalTime.of(14, 0), LocalTime.of(17, 0),
                LocalTime.of(20, 0)};//, LocalTime.of(23, 0)
        LocalTime closestTime = availableTimes[0]; // 초기화
        LocalTime secondClosestTime = availableTimes[0]; // 두 번째로 가까운 시간 초기화
        long minDifference = Long.MAX_VALUE; // 최소값을 찾기 위해 최대값으로 초기화
        long secondMinDifference = Long.MAX_VALUE; // 두 번째 최소값을 찾기 위해 최대값으로 초기화


// 현재 시간과 가장 가까운 시간을 찾음
        for (LocalTime availableTime : availableTimes) {
            long difference = Duration.between(now, availableTime).abs().toMinutes();
            if (difference < minDifference) {
                secondMinDifference = minDifference; // 기존 최소값을 두 번째 최소값으로 설정
                secondClosestTime = closestTime; // 기존 가장 가까운 시간을 두 번째로 가까운 시간으로 설정
                minDifference = difference; // 새로운 최소값으로 설정
                closestTime = availableTime; // 새로운 가장 가까운 시간으로 설정
            } else if (difference < secondMinDifference) {
                secondMinDifference = difference; // 두 번째 최소값을 업데이트
                secondClosestTime = availableTime; // 두 번째로 가까운 시간을 업데이트
            }
        }

        // System.out.println("가장 가까운 이전 시간: " + closestTime.format(timeFormatter));
        System.out.println("두 번째로 가까운 이전 시간: " + secondClosestTime.format(timeFormatter));


        System.out.println("가장 가까운 이전 시간: " + closestTime.format(timeFormatter));





        String baseTime = closestTime.format(timeFormatter);
        System.out.println(baseTime);

        //자외선 지수 api 를 위한 시간 값
        LocalDateTime Now = LocalDateTime.now();

        // 원하는 형식의 포맷터 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHH");

        // 포맷터를 사용하여 날짜와 시간을 지정된 형식으로 출력
        String formattedDateTime = Now.format(formatter);

        //미세먼지를 위한 시간값
        DateTimeFormatter dustdateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dustdate = Now.format(dustdateformatter);


        if(SKY == null){
            baseTime = secondClosestTime.format(timeFormatter);
        }

        // getApiResponse 함수 호출하여 현재 시간의 데이터 가져오기
        try {
            String responseData = getApiResponse(baseDate, baseTime);//전체 단기예보 데이터
            System.out.println("----------------------------------지금 부터는 최고 최저기온 API------------------------------------");
            String responseTM = getApiTM(baseDate, "0200"); //TMX 와 TMN 을 위한 데이터
            System.out.println("----------------------------------지금 부터는 최고 자외선 API------------------------------------");
            String responseUV = UVapi(formattedDateTime);
            System.out.println("----------------------------------지금 부터는 미세먼지 API------------------------------------");
            String responsedust = dustapi(dustdate);
            // 가져온 데이터를 파싱하여 출력
            parseResponse(responseData);
            System.out.println("SKY 값 =" + SKY);
            parseTM(responseTM);
            parseUV(responseUV);
            parseDust(responsedust);


            //최고, 최저 기온업데이트
            System.out.println("TMX 값"+TMX +"TMN값" + TMN);
            updateTemperature(TMX,TMN);

            System.out.println("pop 값: "+popint);
            //오늘 비가 올 확률 애 따른 판단
            if(popint >= 40){
                itememg1 = true;
                SKY ="2";
            }else {
                itememg1 = false;
            }



            // 날씨 이미지 업데이트
            updateWeatherImage(SKY);
            //System.out.println("SKY 변수의 데이터 타입: " + SKY.getClass().getName());

            //자외선 지수 텍스트 업데이트
            updateUVtext(UV);

            //미세먼지 텍스트 업데이트
            updateDUSTText(Dust);
            //현재 온도 텍스트 업데이트
            updateNowtmp(TMP);

            System.out.println(Time + "Time 변수");



        } catch (IOException e) {
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

    @FXML
    private void plusButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("day_add.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonAction(ActionEvent event) {
        Id = primary();
        try {
            String serverURL = "http://hbr2024.dothome.co.kr/delete_allday.php";
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

                FXMLLoader loader = new FXMLLoader(getClass().getResource("day_alldelete.fxml"));
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

    public static int convertTimeToMinutes(String timeString) {
        int hours = Integer.parseInt(timeString.substring(0, 2));
        int minutes = Integer.parseInt(timeString.substring(2, 4));
        return hours * 60 + minutes;
    }

    private static String getApiResponse(String baseDate, String baseTime) throws IOException {  // 현재 시간을 기반한 api 데이터
        System.out.println(baseTime);
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=Hf9Z%2B7fF9N0l2bEfuOAvHN1sxacStyg1c2OdJHO6XE80dGsvI3ms50Wg5pz638TSwTPVy5z%2FPoIe2ex1dCjvyQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode("0200", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode("61", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode("110", "UTF-8")); /*예보지점의 Y 좌표값*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        // 반환 값을 추가합니다.
        return sb.toString();
    }

    //최고, 최소 기온을 위한 api 정보
    private static String getApiTM(String baseDate, String baseTime) throws IOException{
        System.out.println(baseTime);
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=Hf9Z%2B7fF9N0l2bEfuOAvHN1sxacStyg1c2OdJHO6XE80dGsvI3ms50Wg5pz638TSwTPVy5z%2FPoIe2ex1dCjvyQ%3D%3D"); //*Service Key*//*
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); //*페이지번호*//*
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); //*한 페이지 결과 수*//*
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); //*요청자료형식(XML/JSON) Default: XML*//*
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); //*‘21년 6월 28일 발표*//*
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode("0200", "UTF-8")); //*06시 발표(정시단위) *//*
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode("61", "UTF-8")); //*예보지점의 X 좌표값*//*
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode("110", "UTF-8")); //*예보지점의 Y 좌표값*//*
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());

        // 반환 값을 추가합니다.
        return sb.toString();
    }


    private static void parseResponse(String responseData) { //api 데이터 정보들 중에서 현재 시간에 해당하는 데이터를 변수에 할당
        try {

            // 현재 날짜와 시간을 받아오기
            LocalDate today = LocalDate.now();
            LocalTime now = LocalTime.now();
            //시간을 정시로만 받기
            LocalTime roundedTime = now.withMinute(0).withSecond(0).withNano(0);

            //날짜 출력
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            // 시간을 "HHmm" 형식의 4자리 숫자로 포맷팅하기
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
            // String formattedTime = now.format(formatter);



            LocalTime closestTime = null;
            long minDifference = Long.MAX_VALUE;

            // 현재 시간과 가장 가까운 정시를 찾음
            for (int i = 0; i < 24; i++) {
                LocalTime currentHour = LocalTime.of(i, 0);
                long difference = Math.abs(Duration.between(now, currentHour).toMinutes());
                if (difference < minDifference) {
                    minDifference = difference;
                    closestTime = currentHour;
                }
            }
// 가장 가까운 정시를 출력
            System.out.println("가장 가까운 정시: " + closestTime.format(formatter));
            LocalTime roundedCurrentTime = closestTime.withMinute(0).withSecond(0).withNano(0);
            LocalTime twoHoursBefore = roundedCurrentTime.minusHours(2);
            Time = twoHoursBefore.format(formatter);

            // 변수에 할당
            String formattedDate = today.format(dateFormatter);
            String closestTimeString = closestTime.format(formatter);
            System.out.println("변수에 할당된 값: " + formattedDate);
            System.out.println("변수에 할당된 값: " + closestTimeString);


            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray itemList = items.getJSONArray("item");

            int highestPOPValue = Integer.MIN_VALUE;// POP 카테고리의 가장 높은 값을 저장할 변수

            // 필요한 정보 추출
            for (int i = 0; i < itemList.length(); i++) {
                JSONObject item = itemList.getJSONObject(i);
                String basetime = item.getString("baseTime");
                String fcsttime = item.getString("fcstTime");
                String fcstdate = item.getString("fcstDate");
                String category = item.getString("category");
                String fcstValue = item.getString("fcstValue");


                // 원하는 작업 수행
                if(fcstdate.equals((formattedDate))){
                    if (category.equals("POP")) {
                        // "fcstValue" 값을 가져와 정수로 변환
                        String popfcstValue = item.getString("fcstValue");
                        int popValue = Integer.parseInt(popfcstValue);

                        // 현재까지의 최댓값보다 크면 갱신
                        //highestPOPValue = Math.max(highestPOPValue, popValue);
                        if (fcsttime.compareTo(Time) >= 0) {
                            highestPOPValue = Math.max(highestPOPValue, popValue);
                        }

                    }
                    if (fcsttime.equals(closestTimeString)){
                        switch (category) {
                            case "TMP":
                                TMP = fcstValue;
                                break;
                            case "UUU":
                                UUU = fcstValue;
                                break;
                            case "VVV":
                                VVV = fcstValue;
                                break;
                            case "VEC":
                                VEC = fcstValue;
                                break;
                            case "WSD":
                                WSD = fcstValue;
                                break;
                            case "SKY":
                                SKY = fcstValue;
                                break;
                            case "PTY":
                                PTY = fcstValue;
                                break;
                            case "POP":
                                POP = fcstValue;
                                break;
                            case "WAV":
                                WAV = fcstValue;
                                break;
                            case "PCP":
                                PCP = fcstValue;
                                break;
                            case "REH":
                                REH = fcstValue;
                                break;
                            case "SNO":
                                SNO = fcstValue;
                                break;
                            case "TMX":
                                TMX = fcstValue; //최고 온도
                                break;
                            case "TMN":
                                TMN = fcstValue;//최저 온도
                                break;
                            default:
                                // 다른 카테고리일 경우 아무 작업도 수행하지 않음
                                break;
                        }
                        System.out.println("Category: " + category + ", Forecast Value: " + fcstValue +" 예정 날짜: "+ fcstdate +" 예보 시간: "+ fcsttime );
                    }
                }

                // 받은 모든 데이터 출력
                //System.out.println("Category: " + category + ", Forecast Value: " + fcstValue +" 예정 날짜: "+ fcstdate +" 예보 시간: "+ fcsttime );
            } popint = highestPOPValue;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //최대/최소 기온값 파싱
    private static void parseTM(String responseData) { //api 데이터 정보들 중에서 현재 시간에 해당하는 데이터를 변수에 할당
        try {

            // 현재 날짜와 시간을 받아오기
            LocalDate today = LocalDate.now();

            //날짜 출력
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            // 변수에 할당
            String formattedDate = today.format(dateFormatter);
            System.out.println("변수에 할당된 값: " + formattedDate);


            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray itemList = items.getJSONArray("item");


            // 필요한 정보 추출
            for (int i = 0; i < itemList.length(); i++) {
                JSONObject item = itemList.getJSONObject(i);
                String basetime = item.getString("baseTime");
                String fcsttime = item.getString("fcstTime");
                String fcstdate = item.getString("fcstDate");
                String category = item.getString("category");
                String fcstValue = item.getString("fcstValue");



                if (fcstdate.equals((formattedDate))) {
                    switch (category) {
                        case "TMX":
                            TMX = fcstValue;
                            break;
                        case "TMN":
                            TMN = fcstValue;
                            break;
                    }
                }
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String UVapi(String formattedDateTime) throws IOException{
        //public static void main(String[] args) throws IOException

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/LivingWthrIdxServiceV4/getUVIdxV4"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=Hf9Z%2B7fF9N0l2bEfuOAvHN1sxacStyg1c2OdJHO6XE80dGsvI3ms50Wg5pz638TSwTPVy5z%2FPoIe2ex1dCjvyQ%3D%3D"); /*Service Key*/
        //urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=" + URLEncoder.encode("Hf9Z%2B7fF9N0l2bEfuOAvHN1sxacStyg1c2OdJHO6XE80dGsvI3ms50Wg5pz638TSwTPVy5z%2FPoIe2ex1dCjvyQ%3D%3D", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
        urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("4420033000", "UTF-8")); /*아산지점*/
        urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode(formattedDateTime, "UTF-8")); /*2022년7월11일18시*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml, json 선택(미입력시 xml)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;

        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
        {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else
        {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null)
        {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        return sb.toString();
    }
    private static void parseUV(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray itemList = items.getJSONArray("item");

            // h0부터 h24까지의 값을 비교하여 가장 높은 값을 찾음
            int highestValue = Integer.MIN_VALUE;
            for (int i = 0; i < itemList.length(); i++) {
                JSONObject item = itemList.getJSONObject(i);
                for (int j = 0; j <= 24; j++) {
                    String key = "h" + j;
                    if (item.has(key)) {
                        int value = item.getInt(key);
                        highestValue = Math.max(highestValue, value);
                    }
                }
            }

            // 가장 높은 값을 변수에 저장
            UV = highestValue;
            System.out.println("가장 높은 값: " + UV);
        }catch (JSONException e) {
            e.printStackTrace();

        }
    }
    //미세먼지 api 정보 받아오기
    public static String dustapi(String dustdate) throws IOException{
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getMinuDustFrcstDspth"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=Hf9Z%2B7fF9N0l2bEfuOAvHN1sxacStyg1c2OdJHO6XE80dGsvI3ms50Wg5pz638TSwTPVy5z%2FPoIe2ex1dCjvyQ%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 또는 json*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수(조회 날짜로 검색 시 사용 안함)*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호(조회 날짜로 검색 시 사용 안함)*/
        urlBuilder.append("&" + URLEncoder.encode("searchDate","UTF-8") + "=" + URLEncoder.encode("2024-05-11", "UTF-8")); /*통보시간 검색(조회 날짜 입력이 없을 경우 한달동안 예보통보 발령 날짜의 리스트 정보를 확인)*/
        urlBuilder.append("&" + URLEncoder.encode("InformCode","UTF-8") + "=" + URLEncoder.encode("PM10", "UTF-8")); /*통보코드검색(PM10, PM25, O3)*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());
        return sb.toString();
    }


    public static void parseDust(String responseData) {
        // 예시 데이터
        //String jsonResponse = "{ \"response\": { \"body\": { \"items\": [ { \"informData\": \"2024-05-08\", \"informGrade\": \"서울 : 좋음,제주 : 좋음,전남 : 좋음,전북 : 좋음,광주 : 좋음,경남 : 좋음,경북 : 좋음,울산 : 좋음,대구 : 좋음,부산 : 좋음,충남 : 나쁨,충북 : 좋음,세종 : 좋음,대전 : 좋음,영동 : 좋음,영서 : 좋음,경기남부 : 좋음,경기북부 : 좋음,인천 : 좋음\" } ] } } }";

        // JSON 파싱
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            // 첫 번째 항목의 informGrade 값 추출
            String informGrade = jsonObject.getJSONObject("response")
                    .getJSONObject("body")
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .getString("informGrade");

            // informGrade에서 충남의 값을 찾음
            String[] grades = informGrade.split(",");
            String chungnamGrade = "";
            for (String grade : grades) {
                // "충남 :"으로 시작하는지 확인
                if (grade.trim().startsWith("충남 :")) {
                    chungnamGrade = grade.split(":")[1].trim();
                    break;
                }
            }

            // 충남의 informGrade 값을 출력
            System.out.println("충남의 informGrade 값은: " + chungnamGrade);
            Dust = chungnamGrade;
        } catch (JSONException e) {
            System.err.println("JSON 예외 발생: " + e.getMessage());

        }

    }


    @FXML  //날씨 사진 바꾸기
    public void updateWeatherImage(String SKY) {
        System.out.println(SKY);
        String imagePath;
        String itemImg1, itemImg2, itemImg3;
        if(SKY==null){
            SKY = "7";
        }
        switch (SKY){
            case "1":
                imagePath = "001lighticons-02.png"; // 맑은 날씨 이미지 경로
                updateWeatherItem1();//비가 올 경우 우산 추천
                updateWeatherItem2();  //자외선이 쎌 경우 썬크림 추천
                updateWeatherItem3();// 미세먼지 나쁘면 마스크 추천
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("맑음");
                break;
            case "2":
                imagePath = "001lighticons-18.png"; // 비 오는 날씨 이미지 경로
                itemImg1 = "um.png"; //추천 아이템 1
                updateWeatherItem2();  //자외선이 쎌 경우 썬크림 추천
                updateWeatherItem3();// 미세먼지 나쁘면 마스크 추천
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("비");
                item1.setImage(new Image(itemImg1));
                break;
            case "3":
                imagePath = "cloudy.png"; // 구름 많음
                updateWeatherItem1();//오늘 비가 올경우 우산 추천
                updateWeatherItem2();  //자외선이 쎌 경우 썬크림 추천
                updateWeatherItem3();// 미세먼지 나쁘면 마스크 추천
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("구름");
                break;
            case "4":
                imagePath = "001lighticons-08.png"; // 흐림 날씨 이미지 경로
                updateWeatherItem1(); //추천 아이템 1
                updateWeatherItem2();  //자외선이 쎌 경우 썬크림 추천
                updateWeatherItem3();// 미세먼지 나쁘면 마스크 추천
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("흐림");
                break;
            case "5":
                imagePath = "cloudy.png"; // 비와 눈이 같이 오는 날씨 이미지 경로
                updateWeatherItem1();
                updateWeatherItem2();  //자외선이 쎌 경우 썬크림 추천
                updateWeatherItem3();// 미세먼지 나쁘면 마스크 추천
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("진눈깨비");
                break;
            case "6":
                imagePath = "001lighticons-24.png"; // 눈 오는 날씨 이미지 경로
                updateWeatherItem1();
                updateWeatherItem2();  //자외선이 쎌 경우 썬크림 추천
                updateWeatherItem3();// 미세먼지 나쁘면 마스크 추천
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("눈");
                break;
            default:
                // 아무것도 해당되지 않을 때의 기본 이미지 설정
                item1.setImage(null);
                item2.setImage(null);
                item3.setImage(null);
                weather.setImage(null);
                weatherLabel.setText("오류");
                break;
        }
    }

    @FXML  // 최저, 최고기온 업데이트
    public void updateTemperature(String TMX, String TMN) {
        if (TMX == null || TMX.trim().isEmpty()) {
            hightemp.setText("오류");
        } else {
            hightemp.setText(TMX);
        }
        if (TMN == null || TMN.trim().isEmpty()) {
            lowtemp.setText("오류");
        } else {
            lowtemp.setText(TMN);
        }
    }

    @FXML // 자외선 지수 텍스트 업데이트
    public void updateDUSTText(String Dust) {
        if (Dust == null || Dust.trim().isEmpty()) {
            dust.setText("오류");
        } else {
            dust.setText(Dust);
        }
    }

    @FXML
    public void updateNowtmp(String TMP) {
        if (TMP == null || TMP.trim().isEmpty()) {
            nowtmp.setText("오류");
        } else {
            nowtmp.setText(TMP);
        }
    }





    @FXML
    public void updateUVtext(int u) {
        if (u >= 11) {
            uv.setText("위험");
        } else if (u <= 10 && u >= 8) {
            uv.setText("매우 높음");
        } else if (u <= 7 && u >= 6) {
            uv.setText("높음");
        } else if (u <= 5 && u >= 3) {
            uv.setText("보통");
        } else if (u <= 2 && u >= 0) {
            uv.setText("낮음");
        } else {
            // UV 값이 없는 경우
            uv.setText("오류");
        }
    }

    @FXML//비가 올 확률이 40% 이상인 경우 우산아이템 추천
    public void updateWeatherItem1(){
        String itemImg1;
        if(itememg1){
            itemImg1 = "um.png";
            item1.setImage(new Image(itemImg1));
            itemLabel1.setText("우산");
        }else {
            item1.setImage(null);
            itemLabel1.setText("");
        }
    }

    @FXML//자외선 지수가 5 이상이면 선크림 추천
    public void updateWeatherItem2(){
        String itemImg2;
        if(UV >= 5){
            itemImg2 = "sun.png";
            item2.setImage(new Image(itemImg2));
            itemLabel2.setText("썬크림");
        }else {
            item2.setImage(null);
            itemLabel2.setText("");
        }
    }
    @FXML
    public void updateWeatherItem3(){
        String itemImg3;
        String dd;
        dd = Dust;
        if(dd.equals("나쁨")){
            itemImg3 = "mask.png";
            item3.setImage(new Image(itemImg3));
            itemLabel3.setText("마스크");
        }else {
            item3.setImage(null);
            itemLabel3.setText(" ");
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