package com.example.xenhao.foregroundsqlite;

/**
 * Created by XenHao on 30/11/2014.
 */
public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.example.xenhao.foregroundserviceexample.action.main";
        public static String PREV_ACTION = "com.example.xenhao.foregroundserviceexample.action.prev";
        public static String PLAY_ACTION = "com.example.xenhao.foregroundserviceexample.action.play";
        public static String NEXT_ACTION = "com.example.xenhao.foregroundserviceexample.action.next";
        public static String STARTFOREGROUND_ACTION = "com.example.xenhao.foregroundserviceexample.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.example.xenhao.foregroundserviceexample.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}

