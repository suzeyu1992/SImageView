package com.szysky.customize.siv.effect;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.util.GraphsTemplate;

/**
 * Author :  suzeyu
 * Time   :  2016-11-30  下午10:23
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :  默认但图片处理策略
 */

public class  NormalOnePicStrategy implements IDrawingStrategy {

    @Override
    public void algorithm(Canvas canvas, int childTotal, int curChild, Bitmap opeBitmap, SImageView.ConfigInfo info) {
        // 描边宽度
        float mBorderWidth = info.borderWidth;
        int mBitmapWidth = opeBitmap.getWidth();   // 需要处理的bitmap宽度和高度
        int mBitmapHeight = opeBitmap.getHeight();

        int viewWidth = info.width;                // 控件的宽高
        int viewHeight = info.height;
        int display = info.displayType;            // 显示的类型


        // 容错控件非正方形场景处理
        int layoutOffsetX = 0;
        int layoutOffsetY = 0;
        int layoutSquareSide ;                  // 正方形边长
        if (viewWidth != viewHeight){
            int temp = viewHeight - viewWidth;
            if (temp > 0){
                layoutOffsetY += temp;
                layoutOffsetY >>= 1;
                layoutSquareSide = viewWidth;
            }else{
                layoutOffsetX -= temp;
                layoutOffsetX >>= 1;
                layoutSquareSide = viewHeight;
            }
        }else{
            layoutSquareSide = viewHeight;
        }

        // 描边宽度不能超多绘制内容的 1/8
        if (mBorderWidth * 6 > layoutSquareSide ){
            mBorderWidth = layoutSquareSide/6;
        }


        int bodySquareSide = (int) (layoutSquareSide - mBorderWidth *2);


        // 创建内容画笔和描边画笔 并设置属性
        Paint paint =  new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(mBorderWidth);
        borderPaint.setColor(info.borderColor);
        borderPaint.setAntiAlias(true);



        // 创建着色器 shader
        BitmapShader mBitmapShader = new BitmapShader(opeBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(mBitmapShader);


        // 获取 的位置调整的信息 和bitmap需要缩放的比值
        float scale ;
        float dx = 0;
        float dy = 0;

        if (mBitmapWidth  >  mBitmapHeight) {
            scale = bodySquareSide / (float) mBitmapHeight;
            dx = (bodySquareSide - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = bodySquareSide / (float) mBitmapWidth ;
            dy = (bodySquareSide - mBitmapHeight * scale) * 0.5f;
        }



        // 进行调整
        Matrix mShaderMatrix = new Matrix();
        mShaderMatrix.set(null);
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth + layoutOffsetX, (int) (dy + 0.5f) + mBorderWidth + layoutOffsetY);
        mBitmapShader.setLocalMatrix(mShaderMatrix);

        // 计算需要显示的圆的圆心点和半径
        int centerX = viewWidth  >> 1 ;
        int centerY = viewHeight >> 1 ;




        if (SImageView.TYPE_CIRCLE == display){
            // qq群组效果
            GraphsTemplate.drawCircle(canvas, null, centerX, centerY,(layoutSquareSide>>1) - (mBorderWidth /2),paint , mBorderWidth,borderPaint);

        }else if (SImageView.TYPE_RECT == display){

            // 矩形图像
            GraphsTemplate.drawRect(canvas, null, layoutSquareSide, layoutSquareSide, layoutOffsetX, layoutOffsetY, paint, mBorderWidth, borderPaint);

        }else if (SImageView.TYPE_OVAL == display){

            // 椭圆头像
            GraphsTemplate.drawOval(canvas, null, new RectF(layoutSquareSide*0.05f, layoutSquareSide*0.2f,layoutSquareSide*0.95f, layoutSquareSide*0.8f),layoutOffsetX,layoutOffsetY,paint , mBorderWidth,borderPaint);

        }else if (SImageView.TYPE_FIVE_POINTED_STAR == display){

            // 五角星头像
            GraphsTemplate.drawFivePointedStar(canvas, opeBitmap, (int)(layoutSquareSide / 2f), layoutOffsetX,layoutOffsetY, null , mBorderWidth,borderPaint);

        }else if (SImageView.TYPE_ROUND_RECT == display){

            // 有圆角的头像
            GraphsTemplate.drawCornerRectBorder(canvas, null, layoutSquareSide, layoutSquareSide, layoutSquareSide/8, layoutSquareSide/8,layoutOffsetX,layoutOffsetY,paint, mBorderWidth,borderPaint);
        }

    }


}