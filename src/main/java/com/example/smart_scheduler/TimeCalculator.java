package com.example.smart_scheduler;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimeCalculator {
    private static String startpoint;
    private static String endpoint;
    private static String StartX;
    private static String EndX;
    private static String StartY;
    private static String EndY;
    private static String data1;

    private static int minTotalTime;
    private static String minFirstStartStation;
    private static String minBusNo;
    private static String minBusLocalBlID;
    private static String minBusID;
    private static String minLastEndStation;

    private static String ArrivingBus;

    private static final String API_KEY = "1686f682262a3e2f11756c853e1aee5f";
    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String ODSAY_API_KEY = "7F2IfEnzOMbsNdMyJ5T5hH7fIctobRDZBOXFzpKMaAo";
    private static final String ODSAY_BSsearch_API_URL = "https://api.odsay.com/v1/api/searchStation";

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("출발지를 입력하세요: ");
        startpoint = scanner.nextLine();
        System.out.print("도착지를 입력하세요: ");
        endpoint = scanner.nextLine();
        scanner.close();

        StartX = getCoordinates(startpoint, "x");
        StartY = getCoordinates(startpoint, "y");
        EndX = getCoordinates(endpoint, "x");
        EndY = getCoordinates(endpoint, "y");

        if (StartX != null && StartY != null && EndX != null && EndY != null) {
            data1 = searchPublicTransportPath(StartX, StartY, EndX, EndY);
            parseAndPrintData(data1);
            ODBSserch(minFirstStartStation);
        } else {
            System.out.println("좌표를 검색하는데 실패했습니다.");
        }

        System.out.println("최소 버스 로컬 블 ID: " + minBusLocalBlID);
        System.out.println("최소 버스 번호: " + minBusNo);
        System.out.println("최소 버스 ID: " + minBusID);
        System.out.println(ArrivingBus + " 출발지: " + minFirstStartStation);
    }

    private static String getCoordinates(String address, String coordinateType) {
        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
        String jsonResponse = getCoordinatesFromPlace(encodedAddress);

        if (jsonResponse != null) {
            JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
            JsonArray documents = jsonObject.getAsJsonArray("documents");
            if (documents.size() > 0) {
                JsonObject addressInfo = documents.get(0).getAsJsonObject();
                return coordinateType.equals("x") ? addressInfo.get("x").getAsString() : addressInfo.get("y").getAsString();
            } else {
                System.out.println("검색 결과가 없습니다: " + address);
                return null;
            }
        } else {
            System.out.println("API 요청에 실패했습니다: " + address);
            return null;
        }
    }

    private static String getCoordinatesFromPlace(String place) {
        OkHttpClient client = new OkHttpClient();
        String url = API_URL + "?query=" + place;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "KakaoAK " + API_KEY)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String searchPublicTransportPath(String startX, String startY, String endX, String endY) throws IOException {
        String urlTemplate = "https://api.odsay.com/v1/api/searchPubTransPathT?SX=%s&SY=%s&EX=%s&EY=%s&apiKey=%s";
        String encodedApiKey = URLEncoder.encode(ODSAY_API_KEY, "UTF-8");
        String urlInfo = String.format(urlTemplate, startX, startY, endX, endY, encodedApiKey);

        URL url = new URL(urlInfo);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        bufferedReader.close();
        conn.disconnect();

        return sb.toString();
    }

    private static void parseAndPrintData(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray paths = result.getJSONArray("path");

        minTotalTime = Integer.MAX_VALUE;

        for (int i = 0; i < paths.length(); i++) {
            JSONObject path = paths.getJSONObject(i);
            JSONObject info = path.getJSONObject("info");

            int totalTime = info.getInt("totalTime");

            if (totalTime < minTotalTime) {
                minTotalTime = totalTime;
                minFirstStartStation = info.getString("firstStartStation");
                minLastEndStation = info.getString("lastEndStation");

                JSONArray subPaths = path.getJSONArray("subPath");
                for (int j = 0; j < subPaths.length(); j++) {
                    JSONObject subPath = subPaths.getJSONObject(j);
                    int trafficType = subPath.getInt("trafficType");

                    if (trafficType == 1) {
                        System.out.println("지하철: " + subPath.getString("startName") + " -> " + subPath.getString("endName") + " (" + subPath.getInt("sectionTime") + "분)");
                    } else if (trafficType == 2) {
                        if (minBusNo == null) { // 가장 처음 나오는 버스 정보를 저장
                            JSONArray lanes = subPath.getJSONArray("lane");
                            JSONObject lane = lanes.getJSONObject(0);
                            minBusNo = lane.optString("busNo", "정보 없음");
                            minBusLocalBlID = lane.optString("busLocalBlID", "정보 없음");
                            minBusID = lane.optString("busID", "정보 없음");
                        }
                        System.out.println("버스: " + minBusNo + " (" + subPath.getInt("sectionTime") + "분)");
                    } else if (trafficType == 3) {
                        System.out.println("도보: " + subPath.getInt("sectionTime") + "분");
                    }
                }
                System.out.println();
            }
        }

        System.out.println("원본 JSON 데이터: " + jsonData);
    }

    private static String ODBSserch(String stationName) throws IOException {
        System.out.println("정류장 검색 ---------");
        String encodedStationName = URLEncoder.encode(stationName, StandardCharsets.UTF_8);
        String urlTemplate = "https://api.odsay.com/v1/api/searchStation?apiKey=%s&stationName=%s&CID=3060&stationClass=1";
        String urlInfo = String.format(urlTemplate, ODSAY_API_KEY, encodedStationName);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(urlInfo)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            String responseBody = response.body().string();
            System.out.println(responseBody);

            parseBusStationData(responseBody);
            return responseBody;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void parseBusStationData(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);
        try {
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray stations = result.getJSONArray("station");

            for (int i = 0; i < stations.length(); i++) {
                JSONObject station = stations.getJSONObject(i);
                JSONArray busInfoArray = station.getJSONArray("businfo");

                for (int j = 0; j < busInfoArray.length(); j++) {
                    JSONObject busInfo = busInfoArray.getJSONObject(j);
                    String busLocalBlID = busInfo.optString("busLocalBlID", "정보 없음");

                    if (busLocalBlID.equals(minBusLocalBlID)) {
                        String busNo = busInfo.optString("busNo", "정보 없음");
                        System.out.println("버스 로컬 블 ID: " + busLocalBlID + ", 버스 번호: " + busNo);
                        ArrivingBus = busNo;
                    }
                }
            }
        } catch (JSONException e) {
            System.out.println("JSON 데이터 파싱 오류: " + e.getMessage());
        }
    }
}
