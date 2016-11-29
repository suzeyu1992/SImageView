package com.szysky.img.simageview;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午6:02
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 画图策略
 */

public interface IDrawingStrategy {
    void algorithm(Canvas canvas , SImageView.ConfigInfo info);
}
