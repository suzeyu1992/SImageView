package com.szysky.img.simageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;


/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午6:06
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class DrawCircle implements IDrawingStrategy {

    private final Matrix mShaderMatrix = new Matrix();
    /**
     * 要设置的图片大小
     */
    private Rect mDrawableRect = new Rect();

    @Override
    public void algorithm(Canvas canvas , SImageView.ConfigInfo info) {


        int mBorderWidth = info.border;                   // 描边宽度
        int mBitmapWidth = info.readyBmp.get(0).getWidth();   // 需要处理的bitmap宽度和高度
        int mBitmapHeight = info.readyBmp.get(0).getHeight();

        // 中间内容画笔
        Paint paint =  new Paint();

        // 控件内容的宽高矩形
        Rect mBorderRect = new Rect();
        mBorderRect.set(0, 0, info.width, info.height);

        // 创建着色器 shader
        BitmapShader mBitmapShader = new BitmapShader(info.readyBmp.get(0), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(mBitmapShader);

        // 创建描边画笔 并设置属性
        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(info.border);
        borderPaint.setColor(Color.BLACK);


        // 传入的bitmap最终要缩放的比值
        float scale;
        float dx = 0;
        float dy = 0;

        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
        int mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);



        mShaderMatrix.set(null);

        // 获取 的位置调整的信息
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        // 进行调整
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
        mBitmapShader.setLocalMatrix(mShaderMatrix);

        // 显示内容, 和描边
        canvas.drawCircle(300, 300, 300, paint);
        canvas.drawCircle(300, 300, mBorderRadius, borderPaint);
    }

    private void updateShaderMatrix() {






    }
}
