package com.example.smart_scheduler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TimeCalculater {
    //카카오부분
    private static final String API_KEY = "1686f682262a3e2f11756c853e1aee5f"; // 발급받은 REST API 키를 여기에 입력하세요
    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    public static void main(String[] args) throws IOException {

        // ODsay Api Key 정보
        String apiKey = "7F2IfEnzOMbsNdMyJ5T5hH7fIctobRDZBOXFzpKMaAo";

        String urlInfo = "https://api.odsay.com/v1/api/searchPubTransPathT?SX=126.9027279&SY=37.5349277&EX=126.9145430&EY=37.5499421&apiKey=" + URLEncoder.encode(apiKey, "UTF-8");

        // http 연결
        URL url = new URL(urlInfo);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(conn.getInputStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();
        conn.disconnect();

        // 결과 출력
        System.out.println(sb.toString());

        //카카오 부분
        String address = "선문로 221번길 70";
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String jsonResponse = getCoordinatesFromAddress(encodedAddress);
        System.out.println(jsonResponse);

    }
    //카카오 부분
    private static String getCoordinatesFromAddress(String address) {
        OkHttpClient client = new OkHttpClient();
        String url = API_URL + "?query=" + address;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "KakaoAK " + API_KEY)
                .build();

        System.out.println("Request URL: " + url); // 디버깅을 위해 추가

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseData = response.body().string();
            System.out.println("Response Data: " + responseData); // 디버깅을 위해 추가
            JsonObject jsonObject = JsonParser.parseString(responseData).getAsJsonObject();
            return jsonObject.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }








    //카카오 api 부분----------------------------------------------------------------------------------------------------------
    /*private static final String API_KEY = "1686f682262a3e2f11756c853e1aee5f"; // 발급받은 REST API 키를 여기에 입력하세요
    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json";

    public static void main(String[] args) {
        String address = "선문로 221번길 70";
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String jsonResponse = getCoordinatesFromAddress(encodedAddress);
        System.out.println(jsonResponse);
    }

    private static String getCoordinatesFromAddress(String address) {
        OkHttpClient client = new OkHttpClient();
        String url = API_URL + "?query=" + address;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "KakaoAK " + API_KEY)
                .build();

        System.out.println("Request URL: " + url); // 디버깅을 위해 추가

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            String responseData = response.body().string();
            System.out.println("Response Data: " + responseData); // 디버깅을 위해 추가
            JsonObject jsonObject = JsonParser.parseString(responseData).getAsJsonObject();
            return jsonObject.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
