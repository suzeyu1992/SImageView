package com.szysky.customize.simageview;

import android.graphics.Paint;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午4:47
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 图片处理工厂
 */

public class ImageFactory {

    private static  ImageFactory mInstance ;
    static Paint paint =  new Paint();

    private ImageFactory(){}

    public static ImageFactory getInstance(){
        if (null == mInstance){
            synchronized (ImageFactory.class){
                if (null == mInstance){
                    mInstance = new ImageFactory();
                }
            }
        }
        return mInstance;
    }



}
