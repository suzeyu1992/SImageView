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

    public static void print_i(String tag, String body){
        Log.i(tag, "\r\nStart-----------------------------------------------------------------\r\n"+body+"\r\nEnd-----------------------------------------------------------------\r\n");
    }
}
