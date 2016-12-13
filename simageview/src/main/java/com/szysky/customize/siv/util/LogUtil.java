package com.szysky.customize.siv.util;

import android.util.Log;

/**
 * Author :  suzeyu
 * Time   :  2016-12-11  下午8:45
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 方便查看log输出的类
 */

public class LogUtil {

    /**
     * SImageView 相关的log信息开关
     * true 为开启, false为关闭(默认)
     */
    public static boolean GlobalLogPrint = true;

    public static void print_i(String tag, String body){
        if (GlobalLogPrint){
            Log.i(tag, "\r\nStart-----------------------------------------------------------------\r\n"+body+"\r\nEnd-----------------------------------------------------------------\r\n");
        }
    }


    public static void _i(String tag, String body){
        if (GlobalLogPrint) {
            Log.i(tag, body);
        }
    }

    public static void _w(String tag, String body){
        if (GlobalLogPrint) {
            Log.w(tag, body);
        }
    }

    public static void _w(String tag, String body, Exception e){
        if (GlobalLogPrint) {
            Log.w(tag, body ,e);
        }
    }

    public static void _e(String tag, String body, Exception e){
        if (GlobalLogPrint) {
            Log.e(tag, body ,e);
        }
    }

    public static void _e(String tag, String body){
        if (GlobalLogPrint) {
            Log.e(tag, body);
        }

    }

    public static void _d(String tag, String body){
        if (GlobalLogPrint) {
            Log.d(tag, body);
        }
    }
}
