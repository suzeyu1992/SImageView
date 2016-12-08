package com.szysky.customize.siv.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author :  suzeyu
 * Time   :  2016-12-08  下午5:06
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 字符转化加密类
 */

public class SecurityUtil {

    /**
     * 接收一个url地址, 对其转换成md5值并返回
     * 转成一个32md5值
     */
    public  static String md5keyFormUrl(String url) {
        String cacheKey;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }


    /**
     * 判断字符串是否是图片链接
     *
     * @param picUrl 需要判断的字符串
     * @param replaceRegexStr 可以传入自定义的正则匹配规则, 如果不传使用默认
     *
     */
    public static boolean matchUrlPicture(String picUrl, String replaceRegexStr) {

        // 图片链接的默认匹配规则
        String regStr = "https?://.*?.(jpg|png|bmp|jpeg|gif)";

        if (!replaceRegexStr.isEmpty()){
            regStr = replaceRegexStr;
        }

        //Pattern.CASE_INSENSITIVE忽略 jpg 的大小写
        Matcher k=Pattern.compile(regStr,Pattern.CASE_INSENSITIVE).matcher(picUrl);
        return k.find();
    }

}
