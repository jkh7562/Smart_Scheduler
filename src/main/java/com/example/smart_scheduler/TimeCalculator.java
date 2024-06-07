package com.example.smart_scheduler;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimeCalculator extends Application {

    @FXML
    private TextField name_textfield;
    @FXML
    private TextField phone_textfield;
    @FXML
    private TextField id_textfield;
    @FXML
    private Label Time;
    @FXML
    private Label buslabel;
    @FXML
    private Label busname;

    private static String startpoint;
    private static String endpoint;
    private static String StartX;
    private static String EndX;
    private static String StartY;
    private static String EndY;
    private static String data1; // 입력 받은 좌표
    private static String noTime = null;

    private static int minTotalTime;
    private static int minTotalDistance;
    private static String minFirstStartStation;
    private static String minBusNo;
    private static String minBusLocalBlID;
    private static String minBusID;
    private static String minLastEndStation;
    private static String StartTime;
    private static int arrtime; // 버스 도착까지 남은 시간

    private static String ArrivingBus;
    private static String localStationID; // localStationID를 저장하기 위한 변수
    private static JSONArray paths; // paths 변수를 클래스 멤버 변수로 선언
    private static List<RouteInfo> routeInfos = new ArrayList<>(); // 경로 정보를 저장하는 리스트

    private static final String API_KEY = "1686f682262a3e2f11756c853e1aee5f";
    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String ODSAY_API_KEY = "7F2IfEnzOMbsNdMyJ5T5hH7fIctobRDZBOXFzpKMaAo";
    private static final String PUBLIC_DATA_API_KEY = "Hf9Z%2B7fF9N0l2bEfuOAvHN1sxacStyg1c2OdJHO6XE80dGsvI3ms50Wg5pz638TSwTPVy5z%2FPoIe2ex1dCjvyQ%3D%3D";
    private static final String PUBLIC_DATA_API_URL = "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";

    @FXML
    private void handleBusButtonAction(ActionEvent event) throws IOException {
        executeCommonLogic(false, "bus");
        findValidBusRoute();
        updatebuslabel(true);
        System.out.println("버스 정보 담은거:" + RouteInfo.busNo);
    }

    @FXML
    private void handleWalkButtonAction(ActionEvent event) throws IOException {
        executeCommonLogic(true, "walk");
        if (noTime == null) {
            updateTimeWalk(StartTime);
        } else {
            updateTimeWalk(noTime);
            noTime = null;
        }
        updatebuslabel(false);
    }

    @FXML
    private void handleBikeButtonAction(ActionEvent event) throws IOException {
        executeCommonLogic(true, "bike");
        if (noTime == null) {
            updateTimeWalk(StartTime);
        } else {
            updateTimeWalk(noTime);
            noTime = null;
        }
        updatebuslabel(false);
    }

    @FXML
    private void backimageClicked(MouseEvent event) {
        // 뒤로 가기 이미지 클릭 시 처리할 로직 추가
        System.out.println("뒤로 가기 이미지가 클릭되었습니다.");
    }

    @FXML //버스 이름 업데이트
    private void updatebusname() {
        busname.setText(minBusNo);
    }

    @FXML
    private void updatebuslabel(boolean bus) {
        if (bus) {
            buslabel.setText("탑승할 버스:");
        } else {
            buslabel.setText("TimeCalculator");
        }
    }

    @FXML
    private void updateTimeWalk(String time) {
        Time.setText(time);
    }

    private void executeCommonLogic(boolean calculateDistance, String mode) throws IOException {
        startpoint = name_textfield.getText();
        endpoint = phone_textfield.getText();
        String arrivalTimeString = id_textfield.getText().trim();

        if (startpoint.isEmpty() || endpoint.isEmpty() || arrivalTimeString.isEmpty()) {
            System.out.println("출발지, 도착지, 도착 시간을 모두 입력해주세요.");
            noTime = "형식에 맞게 입력하세요";
            return;
        }

        StartX = getCoordinates(startpoint, "x");
        StartY = getCoordinates(startpoint, "y");
        EndX = getCoordinates(endpoint, "x");
        EndY = getCoordinates(endpoint, "y");

        if (StartX != null && StartY != null && EndX != null && EndY != null) {
            data1 = searchPublicTransportPath(StartX, StartY, EndX, EndY);
            parseAndPrintData(data1, calculateDistance, mode, arrivalTimeString);
        } else {
            System.out.println("좌표를 검색하는데 실패했습니다.");
            noTime = "잘못된 주소입니다.";
        }
    }

    private String getCoordinates(String address, String coordinateType) {
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

    private String getCoordinatesFromPlace(String place) {
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

    private String searchPublicTransportPath(String startX, String startY, String endX, String endY) throws IOException {
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

    private void parseAndPrintData(String jsonData, boolean calculateDistance, String mode, String arrivalTimeString) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject result = jsonObject.getJSONObject("result");
        paths = result.getJSONArray("path"); // paths 변수를 초기화

        minTotalTime = Integer.MAX_VALUE;
        minTotalDistance = Integer.MAX_VALUE;
        routeInfos.clear(); // Clear previous route infos

        for (int i = 0; i < paths.length(); i++) {
            JSONObject path = paths.getJSONObject(i);
            JSONObject info = path.getJSONObject("info");

            int totalTime = info.getInt("totalTime");
            int totalDistance = info.getInt("totalDistance");

            boolean hasSubway = false;
            JSONArray subPaths = path.getJSONArray("subPath");
            for (int j = 0; j < subPaths.length(); j++) {
                JSONObject subPath = subPaths.getJSONObject(j);
                int trafficType = subPath.getInt("trafficType");

                if (trafficType == 1) {
                    hasSubway = true;
                    break;
                }
            }

            if (!hasSubway) {
                RouteInfo routeInfo = new RouteInfo();
                routeInfo.totalTime = totalTime;
                routeInfo.totalDistance = totalDistance;
                routeInfo.firstStartStation = info.getString("firstStartStation");
                routeInfo.lastEndStation = info.getString("lastEndStation");

                for (int j = 0; j < subPaths.length(); j++) {
                    JSONObject subPath = subPaths.getJSONObject(j);
                    int trafficType = subPath.getInt("trafficType");

                    if (trafficType == 2) {
                        if (routeInfo.busNo == null) {
                            JSONArray lanes = subPath.getJSONArray("lane");
                            JSONObject lane = lanes.getJSONObject(0);
                            routeInfo.busNo = lane.optString("busNo", "정보 없음");
                            routeInfo.busLocalBlID = lane.optString("busLocalBlID", "정보 없음");
                            routeInfo.busID = lane.optString("busID", "정보 없음");
                        }
                        routeInfo.busTime = subPath.getInt("sectionTime");
                    } else if (trafficType == 3) {
                        if (routeInfo.walkTimeToBusStop == 0) {
                            routeInfo.walkTimeToBusStop = subPath.getInt("sectionTime");
                        } else {
                            routeInfo.walkTimeFromBusStop = subPath.getInt("sectionTime");
                        }
                    }
                }

                routeInfos.add(routeInfo);

                if (totalTime < minTotalTime) {
                    minTotalTime = totalTime;
                    minTotalDistance = totalDistance;
                    minFirstStartStation = routeInfo.firstStartStation;
                    minLastEndStation = routeInfo.lastEndStation;
                    minBusNo = routeInfo.busNo;
                    minBusLocalBlID = routeInfo.busLocalBlID;
                    minBusID = routeInfo.busID;
                }
            }
        }

        if (calculateDistance) {
            double speed = mode.equals("walk") ? 1.39 : 4.17; // m/s
            double timeInSeconds = minTotalDistance / speed;
            double timeInMinutes = timeInSeconds / 60;
            System.out.printf("최소 총 거리: %d m\n", minTotalDistance);
            System.out.printf("총 시간: %.2f 분\n", timeInMinutes);
            calculateDepartureTime(arrivalTimeString, timeInMinutes);
        } else {
            System.out.printf("총 시간: %d 분\n", minTotalTime);
            calculateDepartureTime(arrivalTimeString, minTotalTime);
        }

        System.out.println("원본 JSON 데이터: " + jsonData);
    }

    private void calculateDepartureTime(String arrivalTimeString, double travelTimeInMinutes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime arrivalTime = LocalTime.parse(arrivalTimeString, formatter);
            LocalTime departureTime = arrivalTime.minusMinutes((long) travelTimeInMinutes);
            System.out.printf("출발 시간: %s\n", departureTime.format(formatter));
            StartTime = departureTime.format(formatter);
            updateTimeWalk(StartTime);
        } catch (DateTimeParseException e) {
            System.out.println("잘못된 시간 형식입니다. 올바른 형식은 HH:mm입니다.");
            noTime = "형식에 맞게 입력하세요";
        }
    }

    private void calculateDepartureTimeWithMinTotalTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime currentTime = LocalTime.now();
        LocalTime estimatedDepartureTime = currentTime.plusMinutes(minTotalTime);

        System.out.printf("출발 시간: %s\n", estimatedDepartureTime.format(formatter));
        StartTime = estimatedDepartureTime.format(formatter);
        updateTimeWalk(StartTime);
    }

    private String ODBSserch(String stationName) throws IOException {
        if (stationName == null || stationName.isEmpty()) {
            System.out.println("정류장 이름이 올바르지 않습니다.");
            return null;
        }

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

    private void parseBusStationData(String jsonData) {
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
                        localStationID = station.optString("localStationID", "정보 없음");
                        System.out.println("버스 로컬 블 ID: " + busLocalBlID + ", 버스 번호: " + busNo);
                        System.out.println("localStationID: " + localStationID);
                        ArrivingBus = busLocalBlID;
                    }
                }
            }
        } catch (JSONException e) {
            System.out.println("JSON 데이터 파싱 오류: " + e.getMessage());
        }
    }

    private void getBusArrivalInfo(String nodeId, String routeId) {
        OkHttpClient client = new OkHttpClient();
        try {
            StringBuilder urlBuilder = new StringBuilder(PUBLIC_DATA_API_URL);
            urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + PUBLIC_DATA_API_KEY);
            urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=1");
            urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=10");
            urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=json");
            urlBuilder.append("&" + URLEncoder.encode("cityCode", "UTF-8") + "=34040");
            urlBuilder.append("&" + URLEncoder.encode("nodeId", "UTF-8") + "=" + URLEncoder.encode(nodeId, "UTF-8"));
            urlBuilder.append("&" + URLEncoder.encode("routeId", "UTF-8") + "=" + URLEncoder.encode(routeId, "UTF-8"));

            URL url = new URL(urlBuilder.toString());
            System.out.println("Request URL: " + url); // 디버그 로그

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

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

            System.out.println("Response: " + sb.toString()); // 디버그 로그

            if (sb.toString().trim().startsWith("{")) {
                JSONObject jsonObject = new JSONObject(sb.toString());
                if (!jsonObject.has("response") || jsonObject.getJSONObject("response").getJSONObject("body").isNull("items")) {
                    System.out.println("도착하는 버스가 없습니다.");
                } else {
                    Object items = jsonObject.getJSONObject("response").getJSONObject("body").get("items");
                    boolean busFound = false;
                    if (items instanceof JSONObject) {
                        JSONObject itemObject = (JSONObject) items;
                        if (itemObject.has("item")) {
                            Object item = itemObject.get("item");
                            if (item instanceof JSONArray) {
                                JSONArray itemArray = (JSONArray) item;
                                for (int i = 0; i < itemArray.length(); i++) {
                                    JSONObject itemObj = itemArray.getJSONObject(i);
                                    if (itemObj.getString("routeid").equals(minBusLocalBlID)) {
                                        arrtime = itemObj.getInt("arrtime");
                                        System.out.println("arrtime: " + arrtime);
                                        busFound = true;
                                        break;
                                    }
                                }
                            } else if (item instanceof JSONObject) {
                                JSONObject itemObj = (JSONObject) item;
                                if (itemObj.getString("routeid").equals(minBusLocalBlID)) {
                                    arrtime = itemObj.getInt("arrtime");
                                    System.out.println("arrtime: " + arrtime);
                                    busFound = true;
                                }
                            }
                        }
                    } else if (items instanceof JSONArray) {
                        JSONArray itemArray = (JSONArray) items;
                        for (int i = 0; i < itemArray.length(); i++) {
                            JSONObject item = itemArray.getJSONObject(i);
                            if (item.getString("routeid").equals(minBusLocalBlID)) {
                                arrtime = item.getInt("arrtime");
                                System.out.println("arrtime: " + arrtime);
                                busFound = true;
                                break;
                            }
                        }
                    }

                    if (!busFound) {
                        findAlternateBusRoute();
                    } else {
                        calculateFinalDepartureTime(arrtime);
                    }
                }
            } else {
                System.out.println("JSON 응답을 받지 못했습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateFinalDepartureTime(int arrtime) {
        for (int i = 0; i < paths.length(); i++) {
            JSONObject path = paths.getJSONObject(i);
            JSONObject info = path.getJSONObject("info");

            boolean hasSubway = false;
            JSONArray subPaths = path.getJSONArray("subPath");
            int walkTimeToBusStop = 0;

            for (int j = 0; j < subPaths.length(); j++) {
                JSONObject subPath = subPaths.getJSONObject(j);
                int trafficType = subPath.getInt("trafficType");

                if (trafficType == 1) {
                    hasSubway = true;
                    break;
                }

                if (trafficType == 3 && minBusNo != null) {
                    walkTimeToBusStop = subPath.getInt("sectionTime");
                    break;
                }
            }

            if (!hasSubway && minBusNo != null) {
                if (walkTimeToBusStop > arrtime) {
                    calculateDepartureTimeWithMinTotalTime();
                } else {
                    int adjustedTotalTime = minTotalTime - walkTimeToBusStop + arrtime / 60;
                    calculateAdjustedDepartureTime(adjustedTotalTime);
                }
                return;
            }
        }
    }

    private void calculateAdjustedDepartureTime(int adjustedTotalTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime currentTime = LocalTime.now();
        LocalTime estimatedDepartureTime = currentTime.plusMinutes(adjustedTotalTime);

        System.out.printf("출발 시간: %s\n", estimatedDepartureTime.format(formatter));
        StartTime = estimatedDepartureTime.format(formatter);
        updateTimeWalk(StartTime);
    }

    private void findAlternateBusRoute() throws IOException {
        System.out.println("찾는 버스가 없습니다. 대체 경로를 찾습니다.");
        // Use alternate path if no suitable non-subway path was found
        JSONObject jsonObject = new JSONObject(data1);
        JSONObject result = jsonObject.getJSONObject("result");
        paths = result.getJSONArray("path");

        boolean alternateFound = false;

        for (int i = 0; i < paths.length(); i++) {
            JSONObject path = paths.getJSONObject(i);
            JSONObject info = path.getJSONObject("info");

            int totalTime = info.getInt("totalTime");

            boolean hasSubway = false;
            JSONArray subPaths = path.getJSONArray("subPath");
            for (int j = 0; j < subPaths.length(); j++) {
                JSONObject subPath = subPaths.getJSONObject(j);
                int trafficType = subPath.getInt("trafficType");

                if (trafficType == 1) {
                    hasSubway = true;
                    break;
                }
            }

            if (totalTime > minTotalTime && !hasSubway) {
                minTotalTime = totalTime;
                minTotalDistance = info.getInt("totalDistance");
                minFirstStartStation = info.getString("firstStartStation");
                minLastEndStation = info.getString("lastEndStation");

                minBusNo = null;
                minBusLocalBlID = null;
                minBusID = null;

                for (int j = 0; j < subPaths.length(); j++) {
                    JSONObject subPath = subPaths.getJSONObject(j);
                    int trafficType = subPath.getInt("trafficType");

                    if (trafficType == 2) {
                        if (minBusNo == null) {
                            JSONArray lanes = subPath.getJSONArray("lane");
                            JSONObject lane = lanes.getJSONObject(0);
                            minBusNo = lane.optString("busNo", "정보 없음");
                            minBusLocalBlID = lane.optString("busLocalBlID", "정보 없음");
                            minBusID = lane.optString("busID", "정보 없음");
                        }
                        System.out.println("버스: " + subPath.getJSONArray("lane").getJSONObject(0).optString("busNo", "정보 없음") + " (" + subPath.getInt("sectionTime") + "분)");
                    } else if (trafficType == 3) {
                        if (RouteInfo.sectionTime == 0) {
                            RouteInfo.sectionTime = subPath.getInt("sectionTime");
                        }
                        System.out.println("도보: " + subPath.getInt("sectionTime") + "분");
                    }
                }
                System.out.println();

                if (localStationID != null && minBusID != null) {
                    getBusArrivalInfo(localStationID, minBusID);
                } else {
                    System.out.println("버스 도착 정보가 없습니다.");
                }

                alternateFound = true;
                break;
            }
        }

        if (!alternateFound) {
            System.out.println("버스 도착 정보가 없습니다.");
        }
    }

    private void findValidBusRoute() throws IOException {
        if (minFirstStartStation == null || minFirstStartStation.isEmpty()) {
            System.out.println("최초 시작 정류장이 올바르지 않습니다.");
            return;
        }
        ODBSserch(minFirstStartStation);
        if (localStationID != null && minBusID != null) {
            getBusArrivalInfo(localStationID, minBusID);
        } else {
            findAlternateBusRoute();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TimeCalculator.fxml"));
        primaryStage.setTitle("Time Calculator");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // 경로 정보를 저장하는 클래스
    private static class RouteInfo {
        int totalTime;
        int totalDistance;
        String firstStartStation;
        String lastEndStation;
        static String busNo;
        String busLocalBlID;
        String busID;
        int busTime;
        int walkTimeToBusStop;
        int walkTimeFromBusStop;
        static int sectionTime;
    }
}
