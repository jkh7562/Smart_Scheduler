package com.example.smart_scheduler;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimeCalculator {

    @FXML
    private TextField name_textfield;
    @FXML
    private TextField phone_textfield;
    @FXML
    private TextField id_textfield;
    @FXML
    private Label Time;
    @FXML
    private Label busname;
    @FXML
    private Label buslabel;
    @FXML
    private Label CN;
    @FXML
    ImageView back_image;

    private static String startpoint;
    private static String endpoint;
    private static String StartX;
    private static String EndX;
    private static String StartY;
    private static String EndY;
    private static String data1; // 입력 받은 좌표
    private static String noTime = null;
    private static boolean YN; //버스 정보 있는지 확인하는 용
    private static boolean Switch;
    private static int WalkSectionTime;
    private static String arrivalTimeString;
    private static boolean triger1; //CN 라벨의 표시 유무 판단
    private static int AlltotalTime;
    private static boolean aa; // 올바른 총시간 트리거
    private static String printTime;
    private static int usebusArr;
    private static int calTime;

    private static int minTotalTime;
    private static int minTotalDistance;
    private static String minFirstStartStation;
    private static String minBusNo;
    private static String minBusLocalBlID;
    private static String minBusID;
    private static String minLastEndStation;
    private static String StartTime;
    private static int arrtime; // 버스 도착까지 남은 시간
    public static LocalTime NowTime;

    private static String ArrivingBus;
    private static String localStationID; // localStationID를 저장하기 위한 변수

    private static final String API_KEY = "1686f682262a3e2f11756c853e1aee5f";
    private static final String API_URL = "https://dapi.kakao.com/v2/local/search/address.json";
    private static final String ODSAY_API_KEY = "7F2IfEnzOMbsNdMyJ5T5hH7fIctobRDZBOXFzpKMaAo";
    private static final String PUBLIC_DATA_API_KEY = "Hf9Z%2B7fF9N0l2bEfuOAvHN1sxacStyg1c2OdJHO6XE80dGsvI3ms50Wg5pz638TSwTPVy5z%2FPoIe2ex1dCjvyQ%3D%3D";
    private static final String PUBLIC_DATA_API_URL = "http://apis.data.go.kr/1613000/ArvlInfoInqireService/getSttnAcctoArvlPrearngeInfoList";

    @FXML
    private void handleBusButtonAction(ActionEvent event) throws IOException {
        YN = true;
        executeCommonLogic(false, "bus");
        findValidBusRoute();
        busname(true);
        updatebuslabel(true);

        // arrtime 값이 올바르게 설정된 후 출력
        System.out.println("워크 섹션: " + WalkSectionTime + " arrTime: " + arrtime/60);
        usebusArr = arrtime / 60;
        calTime = AlltotalTime - WalkSectionTime + usebusArr;
        boolean isGreaterThan = usebusArr > WalkSectionTime;
        System.out.println("arrtime / 60 > WalkSectionTime: " + isGreaterThan);
        System.out.println("AllTime: " + AlltotalTime);
    }

    @FXML
    private void handleWalkButtonAction(ActionEvent event) throws IOException {
        executeCommonLogic(true, "walk");
        if (noTime == null) {
            updateTimeWalk(StartTime,true);
        } else {
            updateTimeWalk(noTime,true);
            noTime = null;
        }
        busname(false);
        updatebuslabel(false);
    }

    @FXML
    private void handleBikeButtonAction(ActionEvent event) throws IOException {
        executeCommonLogic(true, "bike");
        if (noTime == null) {
            updateTimeWalk(StartTime,true);
        } else {
            updateTimeWalk(noTime,true);
            noTime = null;
        }
        busname(false);
        updatebuslabel(false);
    }


    @FXML
    private void busname(boolean bus) {
        if (bus) {
            busname.setText(minBusNo);
        } else {
            busname.setText("");
        }
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
    private void updateTimeWalk(String time, boolean can) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime arrivalTime = LocalTime.parse(arrivalTimeString, formatter);
        LocalTime finalLocalTime = LocalTime.parse(printTime, formatter);
        LocalTime now = LocalTime.now();

        // printTime과 현재 시간 비교
        if (finalLocalTime.isAfter(now)) {
            CN.setText(""); // finalLocalTime이 현재 시간보다 이후일 경우
        } else if (finalLocalTime.isBefore(now)) {
            CN.setText("도착할 수 없는 시간대입니다."); // finalLocalTime이 현재 시간보다 이전일 경우
        }

        // 현재 시간과 비교
        if (!can) {
            if (arrivalTime.isBefore(now)) {
                CN.setText("도착할 수 없는 시간대입니다.");
            } else {
                CN.setText("도착 버스 정보가 없습니다.");
            }
        }

        Time.setText(time);
    }


    private void executeCommonLogic(boolean calculateDistance, String mode) throws IOException {
        startpoint = name_textfield.getText();
        endpoint = phone_textfield.getText();
        arrivalTimeString = id_textfield.getText().trim();

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

    private void parseAndPrintData(String jsonData, boolean calculateDistance, String mode, String arrivalTimeString) {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray paths = result.getJSONArray("path");
        aa = true;
        AlltotalTime = Integer.MAX_VALUE; // 초기화

        minTotalTime = Integer.MAX_VALUE;
        minTotalDistance = Integer.MAX_VALUE;

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
                if (aa == true && AlltotalTime > totalTime) {
                    AlltotalTime = totalTime; // 총 이동시간 업데이트
                }

                if (totalTime < minTotalTime) {
                    minTotalTime = totalTime;
                    minTotalDistance = totalDistance;
                    minFirstStartStation = info.getString("firstStartStation");
                    minLastEndStation = info.getString("lastEndStation");

                    minBusNo = null;
                    minBusLocalBlID = null;
                    minBusID = null;
                    Switch = true;

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
                            ArrivingBus = subPath.getJSONArray("lane").getJSONObject(0).optString("busNo", "정보 없음");
                        } else if (trafficType == 3) {
                            System.out.println("도보: " + subPath.getInt("sectionTime") + "분");
                            if (Switch == true) {
                                WalkSectionTime = subPath.getInt("sectionTime");
                                Switch = false;
                            }
                        }
                    }
                    System.out.println();

                    // 선택된 경로의 모든 항목 이름과 값을 출력
                    System.out.println("선택된 경로의 정보:");
                    Iterator<String> keys = info.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        System.out.println(key + ": " + info.get(key));
                    }
                    System.out.println();
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

    private static String calculateDepartureTimeWithTotalTimeCal(int totalTimeCal, String inputTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            LocalTime currentTime = LocalTime.parse(inputTime, formatter);
            LocalTime departureTime = currentTime.minusMinutes(totalTimeCal);

            System.out.printf("입력 받은 시간: %s\n", currentTime.format(formatter));
            System.out.printf("출발 시간: %s\n", departureTime.format(formatter));

            return departureTime.format(formatter);
        } catch (DateTimeParseException e) {
            System.out.println("잘못된 시간 형식입니다. 올바른 형식은 HH:mm입니다.");
            return null;
        }
    }

    private void calculateDepartureTime(String arrivalTimeString, double travelTimeInMinutes) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            // 초 단위 입력값(arrTime)을 시간과 분으로 변환
            System.out.println("arrtime 0인지 확인용:"+arrtime);
            int hours = arrtime / 3600;
            int minutes = (arrtime % 3600) / 60;
            String timeFormatted = String.format("%02d:%02d", hours, minutes);
            LocalTime arrivalTime = LocalTime.parse(arrivalTimeString, formatter);
            LocalTime departureTime = arrivalTime.minusMinutes((long) travelTimeInMinutes);
            System.out.println("계산된 걸리는 시간:"+calTime);
            System.out.println("계산할때 사용할 총 시간:"+AlltotalTime);
            System.out.println("계산할때 사용할 버스 정류장까지 걸어가는 시간:"+WalkSectionTime);
            System.out.println("계산할때 사용하는 버스 도착 시간:"+ usebusArr);
            System.out.println("총시간:"+calculateDepartureTimeWithTotalTimeCal(calTime,arrivalTimeString));
            printTime = calculateDepartureTimeWithTotalTimeCal(calTime,arrivalTimeString);

            // 출발 시간 출력
            System.out.printf("출발 시간: %s\n", departureTime.format(formatter));

            // StartTime을 형식에 맞게 저장
            StartTime = departureTime.format(formatter);
            NowTime = LocalTime.now();

            // WalkSectionTime을 분 단위로 비교하여 계산
            if (arrtime / 60 >= WalkSectionTime) {
                updateTimeWalk(printTime, YN);
                LocalTime finalLocalTime = LocalTime.parse(printTime, formatter);
                LocalTime arrivalLocalTime = LocalTime.parse(arrivalTimeString, formatter);

                if (finalLocalTime.isBefore(NowTime)) {
                    triger1 = false;  // finalTime이 arrivalTime보다 이전 시간일 경우
                } else if (finalLocalTime.isAfter(NowTime)) {
                    triger1 = true;  // finalTime이 arrivalTime보다 이후 시간일 경우
                }
            } else if (arrtime / 60 <= WalkSectionTime){
                updateTimeWalk(StartTime, YN);
                LocalTime StartLocalTime = LocalTime.parse(StartTime, formatter);
                LocalTime arrivalLocalTime = LocalTime.parse(arrivalTimeString, formatter);

                if (StartLocalTime.isBefore(NowTime)) {
                    triger1 = false;  // finalTime이 arrivalTime보다 이전 시간일 경우
                } else if (StartLocalTime.isAfter(NowTime)) {
                    triger1 = true;  // finalTime이 arrivalTime보다 이후 시간일 경우
                }
            }

        } catch (DateTimeParseException e) {
            System.out.println("잘못된 시간 형식입니다. 올바른 형식은 HH:mm입니다.");
        }
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
                        localStationID = station.optString("localStationID", "정보 없음");
                        System.out.println("버스 로컬 블 ID: " + busLocalBlID + ", 버스 번호: " + busNo);
                        System.out.println("localStationID: " + localStationID);
                        ArrivingBus = busNo;
                    }
                }
            }
        } catch (JSONException e) {
            System.out.println("JSON 데이터 파싱 오류: " + e.getMessage());
        }
    }

    private static void getBusArrivalInfo(String nodeId, String routeId) {
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
                    YN = false;
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
                                        System.out.println("arrtime: " + arrtime); // 여기서 arrtime 값 출력
                                        busFound = true;
                                        aa = true;
                                        break;
                                    }
                                }
                            } else if (item instanceof JSONObject) {
                                JSONObject itemObj = (JSONObject) item;
                                if (itemObj.getString("routeid").equals(minBusLocalBlID)) {
                                    arrtime = itemObj.getInt("arrtime");
                                    System.out.println("arrtime: " + arrtime); // 여기서 arrtime 값 출력
                                    busFound = true;
                                    aa = true;
                                }
                            }
                        }
                    } else if (items instanceof JSONArray) {
                        JSONArray itemArray = (JSONArray) items;
                        for (int i = 0; i < itemArray.length(); i++) {
                            JSONObject item = itemArray.getJSONObject(i);
                            if (item.getString("routeid").equals(minBusLocalBlID)) {
                                arrtime = item.getInt("arrtime");
                                System.out.println("arrtime: " + arrtime); // 여기서 arrtime 값 출력
                                busFound = true;
                                aa = false;
                                break;
                            }
                        }
                    }

                    if (!busFound) {
                        findAlternateBusRoute();
                    }
                }
            } else {
                System.out.println("JSON 응답을 받지 못했습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void findAlternateBusRoute() throws IOException {
        System.out.println("찾는 버스가 없습니다. 대체 경로를 찾습니다.");
        // Use alternate path if no suitable non-subway path was found
        JSONObject jsonObject = new JSONObject(data1);
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray paths = result.getJSONArray("path");

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

                JSONArray subPathsAlt = path.getJSONArray("subPath");
                for (int j = 0; j < subPathsAlt.length(); j++) {
                    JSONObject subPath = subPathsAlt.getJSONObject(j);
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
                        System.out.println("도보: " + subPath.getInt("sectionTime") + "분");
                    }
                }
                System.out.println();

                if (localStationID != null && minBusID != null) {
                    getBusArrivalInfo(localStationID, minBusID);
                } else {
                    System.out.println("버스 도착 정보가 없습니다.");
                    YN = false;
                }

                alternateFound = true;
                break;
            }
        }

        if (!alternateFound) {
            System.out.println("버스 도착 정보가 없습니다.");
            YN = false;
        }
    }

    private static void findValidBusRoute() throws IOException {
        ODBSserch(minFirstStartStation);
        if (localStationID != null && minBusID != null) {
            getBusArrivalInfo(localStationID, minBusID);
        } else {
            findAlternateBusRoute();
        }
    }

    @FXML
    private void backimageClicked(MouseEvent event) {
        // 현재 창(Stage)을 가져옵니다.
        Stage currentStage = (Stage) back_image.getScene().getWindow();
        // 현재 창을 닫습니다.
        currentStage.close();
    }
}
