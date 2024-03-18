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

public class MainController {
    public static void main(String[] args) throws IOException {
        // 오늘 날짜 가져오기
        LocalDate today = LocalDate.now();

        // 현재 시간 가져오기
        LocalTime now = LocalTime.now();

        // 날짜를 YYYYMMDD 형식으로 변환
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String baseDate = today.format(dateFormatter);

        // 시간을 HHmm 형식으로 변환
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmm");
        String baseTime = now.format(timeFormatter);

        // getApiResponse 함수 호출하여 현재 시간의 데이터 가져오기
        String responseData = getApiResponse(baseDate, baseTime);

        // 가져온 데이터를 처리하고 출력
        System.out.println(responseData);
    }

    // getApiResponse 함수는 주어진 baseDate와 baseTime에 해당하는 데이터를 가져옵니다.
    private static String getApiResponse(String baseDate, String baseTime) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=d7Mf01XLXPGGPlYQ8Itqst7R%2FFwiXNOaxoRkW39DZwzgXLW7SnyZ85l73m%2BOcmsY%2FXYWYQYjMQnbYJmNLHtD%2Fg%3D%3D");
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("61", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("110", "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        StringBuilder sb = new StringBuilder();
        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            conn.disconnect();
        }
        return sb.toString();
    }
}
