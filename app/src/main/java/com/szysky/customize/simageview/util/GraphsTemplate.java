package com.szysky.customize.simageview.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

/**
 * Author :  suzeyu
 * Time   :  2016-12-01  上午2:24
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 图形样式合成工具
 */

public class GraphsTemplate {

    /**
     *  合成五角星
     * @param canvas 接收画布
     * @param radius 五角星的外圆半径
     * @param offsetX 要偏移的位置,  默认为0.  从[0,0]点画五角星
     * @param offsetY 要偏移的位置,  默认为0.  从[0,0]点画五角星
     * @param paint 需要的画笔
     */
    public static void drawFivePointedStar(Canvas canvas,Bitmap bitmap, int radius, int offsetX, int offsetY, Paint paint){
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
        if (null == bitmap) return; // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
        paint.setXfermode(null);
    }



    /**
     *  合成一个圆角图片
     * @param canvas  画布
     * @param bitmap  要处理的成圆角的 Bitmap图片
     * @param sideWidth 圆角矩形的宽度
     * @param sideHeight 圆角矩形的高度
     * @param cornerX  决定圆角横向弧度     默认你可以设值 = 矩形宽度的 / 8;
     * @param cornerY 决定圆角纵向弧度      默认你可以设值 = 矩形宽度的 / 8;
     * @param offsetX 是否要偏移画布, 移动原点位置, 不需要传入0即可
     * @param offsetY 是否要偏移画布, 移动原点位置, 不需要传入0即可
     */
    public static void drawCornerRect(Canvas canvas, Bitmap bitmap, float sideWidth, float sideHeight, float cornerX, float cornerY, int offsetX, int offsetY, Paint paint ) {
        //画出一个圆角矩形
        RectF rectF = new RectF(0, 0, sideWidth, sideHeight);
        canvas.drawRoundRect(rectF,cornerX,cornerY,paint);

        if (null == bitmap) return; // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode

        //设置混合的模式
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
        paint.setXfermode(null);
    }

    /**
     * 合成一个圆
     * @param canvas  画布
     * @param bitmap  要处理的成圆角的 Bitmap图片
     * @param centerX 圆心点的x坐标
     * @param centerY 圆心点的y坐标
     * @param radius  圆的半径
     */
    public static void drawCircle(Canvas canvas, Bitmap bitmap, float centerX, float centerY, float radius, Paint paint){
        canvas.drawCircle(centerX, centerY, radius, paint);

        if (null == bitmap) return; // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        paint.setXfermode(null);
    }


    /**
     * 合成一个椭圆
     * @param canvas  画布
     * @param bitmap  要处理的成圆角的 Bitmap图片
     * @param rectF   椭圆对应的矩形
     * @param offsetX 是否要偏移画布, 移动原点位置, 不需要传入0即可
     * @param offsetY 是否要偏移画布, 移动原点位置, 不需要传入0即可
     */
    public static void drawOval(Canvas canvas, Bitmap bitmap, RectF rectF, float offsetX, float offsetY,Paint paint){
        // 位置校正
        rectF.right += offsetX;
        rectF.left += offsetX;
        rectF.top += offsetY;
        rectF.bottom += offsetY;
        canvas.drawOval(rectF, paint);

        if (null == bitmap) return; // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode


        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
        paint.setXfermode(null);
    }


}
