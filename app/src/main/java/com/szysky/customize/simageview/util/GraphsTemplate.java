package com.szysky.customize.simageview.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * Author :  suzeyu
 * Time   :  2016-12-01  上午2:24
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 图形样式工具
 */

public class GraphsTemplate {

    /**
     *  画五角星
     * @param canvas 接收画布
     * @param radius 五角星的外圆半径
     * @param offsetX 要偏移的位置,  默认为0.  从[0,0]点画五角星
     * @param offsetY 要偏移的位置,  默认为0.  从[0,0]点画五角星
     * @param paint 需要的画笔
     */
    public static void drawFivePointedStar(Canvas canvas, int radius, int offsetX, int offsetY, Paint paint){
        int half = radius;
        Path path = new Path();
        // E --> B --> D --> A --> C
        path.moveTo(0 + offsetX, half * 0.73f + offsetY);   //E
        path.lineTo(half * 2 + offsetX, half * 0.73f + offsetY);//B
        path.lineTo(half * 0.38f + offsetX, half * 1.9f + offsetY);//D
        path.lineTo(half + offsetX, 0 + offsetY);//A
        path.lineTo(half * 1.62f + offsetX, half * 1.9f + offsetY);//C
        path.close();
        canvas.drawPath(path, paint);
    }
}
