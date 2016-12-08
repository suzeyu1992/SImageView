package com.szysky.customize.siv.imgprocess;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.imgprocess.db.RequestBean;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author :  suzeyu
 * Time   :  2016-12-07  下午1:22
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 缓存策略接口
 */

public interface IImageCache {

    /**
     * 通过图片地址获取bitmap对象
     *
     * @param url 图片的链接地址
     * @param isDiskCacheGet
     * @return  对应的bitmap
     */
    Bitmap get(String url, int reqWidth, int reqHeight, ImageView imageView, boolean isDiskCacheGet, RequestBean bean);

    /**
     * 对图片的bitmap进行缓存
     *
     * @param url 图片地址
     * @param bmp 地址对应的bmp对象
     */
    void put(String url, Bitmap bmp, int reqWidth, int reqHeight);


    boolean putRawStream(String url, InputStream in);


}
