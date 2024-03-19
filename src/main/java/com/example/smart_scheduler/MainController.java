package com.example.smart_scheduler;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainController {
    public static void main(String[] args) throws IOException {
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 현재 시간 가져오기
        LocalTime now = LocalTime.of(20, 59); // 오늘 8시 59분

        // 날짜를 YYYYMMDD 형식으로 변환
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String baseDate = today.format(dateFormatter);

        // 시간을 HHmm 형식으로 변환
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
        String baseTime = now.format(timeFormatter);

        // getApiResponse 함수 호출하여 현재 시간의 데이터 가져오기
        String responseData = getApiResponse(baseDate, baseTime);

        // 가져온 데이터를 파싱하여 출력
        parseResponse(responseData);
    }

    // getApiResponse 함수는 주어진 baseDate와 baseTime에 해당하는 데이터를 가져옵니다.
    private static String getApiResponse(String baseDate, String baseTime) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=d7Mf01XLXPGGPlYQ8Itqst7R%2FFwiXNOaxoRkW39DZwzgXLW7SnyZ85l73m%2BOcmsY%2FXYWYQYjMQnbYJmNLHtD%2Fg%3D%3D"); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("12", "UTF-8")); /*한 페이지 결과 수*/
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

    // JSON 응답을 파싱하여 필요한 정보를 추출하는 메서드
    private static void parseResponse(String responseData) {
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject response = jsonObject.getJSONObject("response");
            JSONObject body = response.getJSONObject("body");
            JSONObject items = body.getJSONObject("items");
            JSONArray itemList = items.getJSONArray("item");

            // 필요한 정보 추출
            for (int i = 0; i < itemList.length(); i++) {
                JSONObject item = itemList.getJSONObject(i);
                String category = item.getString("category");
                String fcstValue = item.getString("fcstValue");

                // 원하는 작업 수행
                // 예를 들어, 날씨 정보 출력
                System.out.println("Category: " + category + ", Forecast Value: " + fcstValue);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
