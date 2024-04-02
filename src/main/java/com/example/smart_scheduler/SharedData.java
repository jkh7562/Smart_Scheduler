package com.example.smart_scheduler;

public class SharedData {
    private static String week;
    private static String start;
    private static String end;
    private static int time;

    public static String getWeek() {
        return week;
    }

    public static void setWeek(String week) {
        SharedData.week = week;
    }

    public static String getStart() {
        return start;
    }

    public static void setStart(String start) {
        SharedData.start = start;
    }

    public static String getEnd() {
        return end;
    }

    public static void setEnd(String end) {
        SharedData.end = end;
    }
    public static int getTime() {
        return time;
    }

    public static void setTime(int time) {
        SharedData.time = time;
    }
}
