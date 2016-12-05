package com.szysky.customize.siv.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.Log;

import com.szysky.customize.siv.SImageView;

/**
 * Author :  suzeyu
 * Time   :  2016-12-01  上午2:24
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 图形样式合成工具
 */

public class GraphsTemplate {


    private static final String TAG = GraphsTemplate.class.getName();

    public static void drawRect(Canvas canvas, Bitmap bitmap, float sideWidth, float sideHeight,
                                int offsetX, int offsetY, Paint paint, float borderWidth, Paint borderPaint ){
        // 画矩形
        RectF rectF = new RectF(0 + offsetX, 0 + offsetY, sideWidth + offsetX, sideHeight + offsetY);
        canvas.drawRect( rectF,paint);

        // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode
        if (null != bitmap) {
            //设置混合的模式
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
            paint.setXfermode(null);
        }

        // 设置描边
        if (borderWidth > 0 && borderPaint != null){
            // 位置修正
            rectF.bottom -= borderWidth/2;
            rectF.top += borderWidth/2;
            rectF.right -= borderWidth/2;
            rectF.left += borderWidth/2;
            canvas.drawRect(rectF, borderPaint);
        }
    }

    public static void drawBitmap(Canvas canvas, Bitmap bitmap, float sideWidth, float sideHeight,
                                  int offsetX, int offsetY, Paint paint , int flag){

        Matrix matrix = new Matrix();
        float scale = 0;
        float dx = 0;
        float dy = 0;


        switch (flag){

            case SImageView.SCALE_TYPE_CENTER_INSIDE:
                // 保持图片的完整 , 尽量缩小.  比例不变
                if (bitmap.getWidth() > bitmap.getHeight()){
                    scale = sideHeight / bitmap.getWidth();
                    dy = (sideHeight - bitmap.getWidth() * scale) * 0.5f;
                }else{
                    scale = sideHeight / bitmap.getHeight();
                    dx = (sideHeight - bitmap.getHeight() * scale) * 0.5f;
                }
                matrix.postScale(scale, scale);
                matrix.postTranslate(dx, dy);
                break;

            case SImageView.SCALE_TYPE_CENTER_CROP:
                // 尽量放大, 填充控件, 比例不变
                float scaleY = sideHeight / bitmap.getHeight();
                float scaleX = sideWidth / bitmap.getWidth();

                scale = scaleY > scaleX ? scaleY : scaleX;
                matrix.postScale(scale, scale);

                break;

            case SImageView.SCALE_TYPE_FIX_XY:
                // 填充控件, 保证图片完整,  比例可能会变
                float tempX = sideWidth / bitmap.getWidth();
                float tempY = sideHeight / bitmap.getHeight();
                matrix.postScale(tempX , tempY);
                if ((tempX <= 1) && (tempX >= 0.9f) && (tempY >= 0.9f) && (tempY <=1)){
                    scale = 1;
                }
                break;

            default:
                return;
        }
        if ((scale <= 1) && (scale >= 0.9f)){
            canvas.drawBitmap(bitmap, offsetX, offsetY, null);
        }else{
            canvas.drawBitmap(Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true),offsetX, offsetY, null);
        }
    }

    /**
     *  合成一个圆角矩形图片
     * @param canvas  画布
     * @param bitmap  要处理的成圆角的 Bitmap图片
     * @param sideWidth 圆角矩形的宽度
     * @param sideHeight 圆角矩形的高度
     * @param cornerX  决定圆角横向弧度     默认你可以设值 = 矩形宽度的 / 8;
     * @param cornerY 决定圆角纵向弧度      默认你可以设值 = 矩形宽度的 / 8;
     * @param offsetX 是否要偏移画布, 移动原点位置, 不需要传入0即可
     * @param offsetY 是否要偏移画布, 移动原点位置, 不需要传入0即可
     * @param borderWidth 描边宽度
     * @param borderPaint 描边的画笔
     */
    public static void drawCornerRectBorder(Canvas canvas, Bitmap bitmap, float sideWidth, float sideHeight, float cornerX, float cornerY,
                                            int offsetX, int offsetY, Paint paint, float borderWidth, Paint borderPaint ) {
        //画出一个圆角矩形
        RectF rectF = new RectF(offsetX, offsetY, sideWidth+offsetX, sideHeight+offsetY);
        canvas.drawRoundRect(rectF,cornerX,cornerY,paint);

        // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode
        if (null != bitmap) {
            //设置混合的模式
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
            paint.setXfermode(null);
        }
        // 判断是否需要描边
        if (borderWidth >0 && borderPaint != null){
            // 如果有bitmap, 描边应该以bitmap的宽高设置描边配置, 否则可能会出现描边缺失
            if (null != bitmap){
                float temp = borderWidth / 2.5f;
                rectF.left += temp;
                rectF.top += temp;
                rectF.right = bitmap.getWidth() - temp;
                rectF.bottom = bitmap.getHeight() - temp;
                canvas.drawRoundRect(rectF,cornerX*0.8f,cornerY*0.8f , borderPaint);

            }else{
                rectF.bottom -= borderWidth/2;
                rectF.top += borderWidth/2;
                rectF.right -= borderWidth/2;
                rectF.left += borderWidth/2;
                canvas.drawRoundRect(rectF,cornerX*0.8f,cornerY*0.8f , borderPaint);
            }

        }
    }

    /**
     * 合成一个圆
     * @param canvas  画布
     * @param bitmap  要处理的成圆角的 Bitmap图片
     * @param centerX 圆心点的x坐标
     * @param centerY 圆心点的y坐标
     * @param radius  圆的半径
     * @param borderWidth  描边宽度 , 不需要可以设置0
     * @param borderPaint  描边画笔  不需要可以设置null
     */
    public static void drawCircle(Canvas canvas, Bitmap bitmap, float centerX, float centerY, float radius,
                                  Paint paint, float borderWidth, Paint borderPaint){

        canvas.drawCircle(centerX, centerY, radius, paint);

        // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode
        if (null != bitmap) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
            paint.setXfermode(null);
        }

        // 描边处理
        if (borderWidth > 0 && borderPaint != null){
            canvas.drawCircle(centerX, centerY, radius - borderWidth/2, borderPaint);

        }


    }


    /**
     * 合成一个椭圆
     * @param canvas  画布
     * @param bitmap  要处理的成圆角的 Bitmap图片
     * @param rectF   椭圆对应的矩形
     * @param offsetX 是否要偏移画布, 移动原点位置, 不需要传入0即可
     * @param offsetY 是否要偏移画布, 移动原点位置, 不需要传入0
     * @param borderWidth  描边宽度 , 不需要可以设置0
     * @param borderPaint  描边画笔  不需要可以设置null
     */
    public static void drawOval(Canvas canvas, Bitmap bitmap, RectF rectF, float offsetX, float offsetY,Paint paint, float borderWidth, Paint borderPaint){
        // 位置校正
        rectF.right += offsetX;
        rectF.left += offsetX;
        rectF.top += offsetY;
        rectF.bottom += offsetY;
        canvas.drawOval(rectF, paint);

        // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode
        if (null != bitmap) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
            paint.setXfermode(null);
        }

        // 开始描边
        if (borderWidth > 0 && borderPaint != null){
            // 位置校正
            float off = borderWidth / 2f;
            rectF.right -= off;
            rectF.left += off;
            rectF.top += off;
            rectF.bottom -= off;
            canvas.drawOval(rectF, borderPaint);
        }

    }


    /**
     *  合成五角星
     * @param canvas 接收画布
     * @param radius 五角星的外圆半径
     * @param offsetX 要偏移的位置,  默认为0.  从[0,0]点画五角星
     * @param offsetY 要偏移的位置,  默认为0.  从[0,0]点画五角星
     * @param paint 需要的画笔
     * @param borderWidth  描边宽度 , 不需要可以设置0
     * @param borderPaint  描边画笔  不需要可以设置null
     */
    public static void drawFivePointedStar(Canvas canvas,Bitmap bitmap, int radius, int offsetX, int offsetY, Paint paint, float borderWidth, Paint borderPaint) {
        int half = radius;
        Path path = new Path();

        boolean isSupportBorder = true;          // 判断是否支持描边
        int layoutId = -1;                       // 图层id
        boolean isOneStrategy = false;                   // 是否是单图片处理调用


        if (Build.VERSION.SDK_INT < 21){
            isSupportBorder = false;
            Log.w(TAG, "此操作版本不支持五角星的描边绘制");
        }

        // 开始画描边
        if (isSupportBorder && borderWidth > 0 && borderPaint != null){

            // 对描边进行边界的最大长度进行判断 不得超过半径的1/6
            if (borderWidth*3 > half){
                borderWidth = half/3;
                borderPaint.setStrokeWidth(borderWidth);
            }

            canvas.translate(borderWidth*2f,borderWidth*2f);
            half -= borderWidth*2f;
            // E --> B --> D --> A --> C
            path.moveTo(offsetX, half * 0.73f + offsetY);   //E
            path.lineTo(half * 2 + offsetX, half * 0.73f + offsetY);//B
            path.lineTo(half * 0.38f + offsetX, half * 1.9f + offsetY);//D
            path.lineTo(half + offsetX, offsetY);//A
            path.lineTo(half * 1.62f + offsetX, half * 1.9f + offsetY);//C
            path.close();
            canvas.drawPath(path, borderPaint);
            canvas.translate(-borderWidth*2,-borderWidth*2);

            half -= borderWidth * 1.5f ;
        }

        // 提取图层, 此方法需要sdk21, 已经加了判断
        if (bitmap != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutId = canvas.saveLayer(new RectF(0, 0, (radius+offsetX)*2  , (radius+offsetY)*2), null);
        }

        // 提供给 单张图片处理策略使用
        if (paint == null && bitmap != null){
            isOneStrategy = true;
            paint = new Paint();
            paint.setAntiAlias(true);


            Matrix matrix = new Matrix();
            float scale = 0;
            if (bitmap.getHeight() > bitmap.getWidth()){
                scale = (radius * 2f -borderWidth)/bitmap.getWidth();
            }else{
                scale = (radius * 2f -borderWidth)/bitmap.getHeight();
            }
            matrix.postScale(scale , scale);
            // 缩放
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);

        }


        // 开始画出五角星
        // E --> B --> D --> A --> C
        path.reset();
        if (isSupportBorder && borderWidth > 0 && borderPaint != null){
            canvas.translate(borderWidth*3.5f,borderWidth*3.6f);
        }
        path.moveTo(offsetX, half * 0.73f + offsetY);   //E
        path.lineTo(half * 2 + offsetX, half * 0.73f + offsetY);//B
        path.lineTo(half * 0.38f + offsetX, half * 1.9f + offsetY);//D
        path.lineTo(half + offsetX, offsetY);//A
        path.lineTo(half * 1.62f + offsetX, half * 1.9f + offsetY);//C

        canvas.drawPath(path, paint);

        if (bitmap != null && isSupportBorder &&borderWidth > 0 && borderPaint != null ){
            canvas.translate(-borderWidth*3.5f,-borderWidth*3.6f);
        }



        // 表明只需要要画出想要的图形即可, 可能实现合成方式是Shader着色器,而不是setXformode
        if (null != bitmap) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            if (isOneStrategy){
                canvas.drawBitmap(bitmap, radius + offsetX -bitmap.getWidth()/2, radius + offsetY -bitmap.getHeight()/2, paint);

            }else{
                canvas.drawBitmap(bitmap, 0, 0, paint);
            }
            paint.setXfermode(null);
        }


        if (bitmap != null && isSupportBorder &&borderWidth > 0 && borderPaint != null  && layoutId != -1){
            canvas.restore();
        }


    }

    static float cos(int num){
        return (float) Math.cos(num*Math.PI/180);
    }

    static float sin(int num){
        return (float) Math.sin(num*Math.PI/180);
    }
}
