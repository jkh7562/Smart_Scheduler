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
    private ImageView weather;
    @FXML
    private Label hightemp;
    @FXML
    private Label lowtemp;
    @FXML
    private Label weatherLabel;
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

    private static String weatherPic;

    @FXML
    public void initialize() {
        // 화면이 로드되고 초기화된 후에 호출되는 메소드

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





        String baseTime = secondClosestTime.format(timeFormatter);
        System.out.println(baseTime);


        // getApiResponse 함수 호출하여 현재 시간의 데이터 가져오기
        try {
            String responseData = getApiResponse(baseDate, baseTime);//전체 단기예보 데이터
            System.out.println("----------------------------------지금 부터는 최고 최저기온 API------------------------------------");
            String responseTM = getApiTM(baseDate, "0200"); //TMX 와 TMN 을 위한 데이터
            // 가져온 데이터를 파싱하여 출력
            parseResponse(responseData);
            System.out.println("SKY 값 =" + SKY);
            parseTM(responseTM);

            //최고, 최저 기온업데이트
            System.out.println("TMX 값"+TMX +"TMN값" + TMN);
            updateTemperature(TMX,TMN);

            System.out.println("pop 값: "+popint);
            // 날씨 이미지 업데이트
            if(popint >= 40){
                itememg1 = true;
            }
            int nowpopInt = Integer.parseInt(POP);
            if (nowpopInt >= 40) {
                SKY = "2";
            }

            updateWeatherImage(SKY);
            System.out.println("SKY 변수의 데이터 타입: " + SKY.getClass().getName());



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getApiResponse(String baseDate, String baseTime) throws IOException {  // 현재 시간을 기반한 api 데이터
        System.out.println(baseTime);
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=d7Mf01XLXPGGPlYQ8Itqst7R%2FFwiXNOaxoRkW39DZwzgXLW7SnyZ85l73m%2BOcmsY%2FXYWYQYjMQnbYJmNLHtD%2Fg%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("500", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); /*06시 발표(정시단위) */
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
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=d7Mf01XLXPGGPlYQ8Itqst7R%2FFwiXNOaxoRkW39DZwzgXLW7SnyZ85l73m%2BOcmsY%2FXYWYQYjMQnbYJmNLHtD%2Fg%3D%3D"); //*Service Key*//*
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


                //System.out.println("Category: " + category + ", Forecast Value: " + fcstValue +" 예정 날짜: "+ fcstdate +" 예보 시간: "+ fcsttime );

                //System.out.println(fcstdate + fcsttime);

                // 원하는 작업 수행
                if(fcstdate.equals((formattedDate))){
                    if (category.equals("POP")) {
                        // "fcstValue" 값을 가져와 정수로 변환
                        String popfcstValue = item.getString("fcstValue");
                        int popValue = Integer.parseInt(popfcstValue);

                        // 현재까지의 최댓값보다 크면 갱신
                        highestPOPValue = Math.max(highestPOPValue, popValue);
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
                    } //System.out.println("Category: " + category + ", Forecast Value: " + fcstValue +" 예정 날짜: "+ fcstdate +" 예보 시간: "+ fcsttime );
                }
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class UVapi //자외선 지수
    {
        public static void main(String[] args) throws IOException
        {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/LivingWthrIdxServiceV4/getUVIdxV4"); /*URL*/
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=서비스키"); /*Service Key*/
            urlBuilder.append("&" + URLEncoder.encode("serviceKey","UTF-8") + "=" + URLEncoder.encode("d7Mf01XLXPGGPlYQ8Itqst7R%2FFwiXNOaxoRkW39DZwzgXLW7SnyZ85l73m%2BOcmsY%2FXYWYQYjMQnbYJmNLHtD%2Fg%3D%3D", "UTF-8")); /*공공데이터포털에서 받은 인증키*/
            urlBuilder.append("&" + URLEncoder.encode("areaNo","UTF-8") + "=" + URLEncoder.encode("1100000000", "UTF-8")); /*서울지점*/
            urlBuilder.append("&" + URLEncoder.encode("time","UTF-8") + "=" + URLEncoder.encode("2022071118", "UTF-8")); /*2022년7월11일18시*/
            urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); /*xml, json 선택(미입력시 xml)*/
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
        }
    }



    @FXML  //날씨 사진 바꾸기
    public void updateWeatherImage(String SKY) {
        System.out.println(SKY);
        String imagePath;
        String itemImg1, itemImg2, itemImg3;
        switch (SKY){
            case "1":
                imagePath = "001lighticons-02.png"; // 맑은 날씨 이미지 경로
                itemImg1 = "001lighticons-18.png"; //추천 아이템 1
                itemImg2 = "001lighticons-18.png";  //추천 아이템 2
                itemImg3 = "001lighticons-18.png";  //추천아이템 3
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("맑음");
                break;
            case "2":
                imagePath = "001lighticons-18.png"; // 비 오는 날씨 이미지 경로
                itemImg1 = "um.png"; //추천 아이템 1
                itemImg2 = "001lighticons-18.png";  //추천 아이템 2
                itemImg3 = "001lighticons-18.png";  //추천아이템 3
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("비");
                item1.setImage(new Image(itemImg1));
                item2.setImage(new Image(itemImg2));
                item3.setImage(new Image(itemImg3));
                break;
            case "3":
                imagePath = "001lighticons-08.png"; // 구름 많음
                itemImg1 = "001lighticons-18.png"; //추천 아이템 1
                itemImg2 = "001lighticons-18.png";  //추천 아이템 2
                itemImg3 = "001lighticons-18.png";  //추천아이템 3
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("흐림");
                item1.setImage(new Image(itemImg1));
                item2.setImage(new Image(itemImg2));
                item3.setImage(new Image(itemImg3));
                break;
            case "4":
                imagePath = "001lighticons-14.png"; // 흐림 날씨 이미지 경로
                itemImg1 = "001lighticons-18.png"; //추천 아이템 1
                itemImg2 = "001lighticons-18.png";  //추천 아이템 2
                itemImg3 = "001lighticons-18.png";  //추천아이템 3
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("구름");
                item1.setImage(new Image(itemImg1));
                item2.setImage(new Image(itemImg2));
                item3.setImage(new Image(itemImg3));
                break;
            case "5":
                imagePath = "001lighticons-08.png"; // 비와 눈이 같이 오는 날씨 이미지 경로
                itemImg1 = "001lighticons-18.png"; //추천 아이템 1
                itemImg2 = "001lighticons-18.png";  //추천 아이템 2
                itemImg3 = "001lighticons-18.png";  //추천아이템 3
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("진눈깨비");
                item1.setImage(new Image(itemImg1));
                item2.setImage(new Image(itemImg2));
                item3.setImage(new Image(itemImg3));
                break;
            case "6":
                imagePath = "001lighticons-24.png"; // 눈 오는 날씨 이미지 경로
                itemImg1 = "001lighticons-18.png"; //추천 아이템 1
                itemImg2 = "001lighticons-18.png";  //추천 아이템 2
                itemImg3 = "001lighticons-18.png";  //추천아이템 3
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("눈");
                item1.setImage(new Image(itemImg1));
                item2.setImage(new Image(itemImg2));
                item3.setImage(new Image(itemImg3));
                break;
            default:
                // 아무것도 해당되지 않을 때의 기본 이미지 설정
                imagePath = "001lighticons-08.png"; // 기본 이미지 경로
                itemImg1 = "001lighticons-18.png"; //추천 아이템 1
                itemImg2 = "001lighticons-18.png";  //추천 아이템 2
                itemImg3 = "001lighticons-18.png";  //추천아이템 3
                weather.setImage(new Image(imagePath));
                weatherLabel.setText("알수없음");
                item1.setImage(new Image(itemImg1));
                item2.setImage(new Image(itemImg2));
                item3.setImage(new Image(itemImg3));
                break;
        }
    }

    @FXML  //최저, 최고기온 업데이트
    public void updateTemperature(String TMX, String TMN)
    {
        hightemp.setText(TMX);
        lowtemp.setText(TMN);
    }

    @FXML//비가 올 확률이 40% 이상인 경우 우산아이템 추천
    public void updateWeatherItem1(){
        String itemImg1;
        if(popint>=40){
            itemImg1 = "um.png";
            item1.setImage(new Image(itemImg1));
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
}