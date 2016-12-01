package com.szysky.customize.simageview.effect;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;

import com.szysky.customize.simageview.SImageView;
import com.szysky.customize.simageview.effect.IDrawingStrategy;
import com.szysky.customize.simageview.util.GraphsTemplate;

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
        int mBorderWidth = info.borderWidth;                   // 描边宽度
        int mBitmapWidth = opeBitmap.getWidth();   // 需要处理的bitmap宽度和高度
        int mBitmapHeight = opeBitmap.getHeight();
        int viewWidth = info.width;
        int viewHeight = info.height;


        // 容错控件非正方形场景处理
        int layoutOffsetX = 0;
        int layoutOffsetY = 0;
        int layoutSquareSide = 0;       // 正方形边长
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


        int bodySquareSide = layoutSquareSide - mBorderWidth*2;


        // 创建内容画笔和描边画笔 并设置属性
        Paint paint =  new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        Paint borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(info.borderWidth);
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
            scale = bodySquareSide / (float) mBitmapWidth;
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


        GraphsTemplate.drawFivePointedStar(canvas, layoutSquareSide/2, layoutOffsetX,layoutOffsetY,paint);





        // 画内容和边框
//                canvas.drawCircle(centerX, centerY, (layoutSquareSide>>1) - (mBorderWidth>>1), paint);
//                canvas.drawCircle(centerX, centerY, (layoutSquareSide-mBorderWidth)>>1, borderPaint);
    }


}