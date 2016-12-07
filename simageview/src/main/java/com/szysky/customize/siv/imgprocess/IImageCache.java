package com.szysky.customize.siv.imgprocess;

import android.graphics.Bitmap;

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
     * @return  对应的bitmap
     */
    Bitmap get(String url,int reqWidth, int reqHeight);

    /**
     * 对图片的bitmap进行缓存
     *
     * @param url 图片地址
     * @param bmp 地址对应的bmp对象
     */
    void put(String url, Bitmap bmp);


    /**
     * 从网络下载图片后会调用, 按需选择是否进行原图片流的文件输出步骤
     *   如果不需要可空实现
     * @param url 图片的地址
     */
    void putRawStream(String url, BufferedInputStream in) throws IOException;

}
