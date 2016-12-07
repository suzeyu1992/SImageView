package com.szysky.customize.simageview.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午3:46
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 检测网络wifi是否开启
 */

public class NetWorkUtil {
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
}
