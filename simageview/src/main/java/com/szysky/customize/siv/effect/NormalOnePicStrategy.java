package com.szysky.customize.siv.effect;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

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

    private static final String TAG = NormalOnePicStrategy.class.getName();
    private float mBorderWidth;
    private final Paint paint;
    private final Paint borderPaint;

    /**
     * 圆角矩形的圆角半径系数
     */
    private float mRectRoundRadius = 8;


    /**
     * 椭圆的宽高比值
     */
    private float mOvalWidthRatio = 1f;
    private float mOvalHeightRatio = 0.75f;

    public NormalOnePicStrategy(){
        // 创建内容画笔和描边画笔 并设置属性
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.BLACK);
        borderPaint.setAntiAlias(true);
    }

    /**
     * 获得圆角矩形的圆角弧度系数, 范围0~2; 默认为1
     */
    public float getRectRoundRadius() {
        if (mRectRoundRadius == 8){
            return 1;
        }else if (mRectRoundRadius < 8){  // 弧度大
            return Math.round(((8 - mRectRoundRadius ) /4 + 1) *100)/100f;

        }else {     // 弧度小
            return Math.round((1 - (mRectRoundRadius - 8) / 4) * 100 )/100f ;

        }
    }

    /**
     *  设置圆角矩形的圆角弧度系数, 取值为0~2, 默认为1
     *  此设置属性不会立即生效, 需下次圆角矩形加载时才会有效.
     */
    public void setRectRoundRadius(float mRectRoundRadius) {

        mRectRoundRadius = mRectRoundRadius <= 0 ? 0.1f :  mRectRoundRadius;
        mRectRoundRadius = mRectRoundRadius > 2 ? 2f :  mRectRoundRadius;

        if (mRectRoundRadius < 1){
            this.mRectRoundRadius += ((1 - mRectRoundRadius) * 4);
        }else if (mRectRoundRadius > 1){
            this.mRectRoundRadius -= ((mRectRoundRadius-1) * 4);
        }else{
            this.mRectRoundRadius = 8;
        }
    }

    @Override
    public void algorithm(Canvas canvas, int childTotal, int curChild, Bitmap opeBitmap, SImageView.ConfigInfo info) {
        // 描边宽度
        mBorderWidth = info.borderWidth;
        int mBitmapWidth = opeBitmap.getWidth();   // 需要处理的bitmap宽度和高度
        int mBitmapHeight = opeBitmap.getHeight();

        int viewWidth = info.width;                // 控件的宽高
        int viewHeight = info.height;
        int display = info.displayType;            // 显示的类型
        int scaleType = info.scaleType;            // 矩形的缩放类型


        // 容错控件非正方形场景处理
        int layoutOffsetX = 0;
        int layoutOffsetY = 0;
        int layoutSquareSide = 0;                  // 正方形边长
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


        // 首先处理是否是矩形, 如果是, 提高效率直接处理
        if ((display == SImageView.TYPE_RECT) && (mBorderWidth<=0)){
            switch (scaleType){
                case SImageView.SCALE_TYPE_CENTER_INSIDE:
                    GraphsTemplate.drawBitmap(canvas, opeBitmap, bodySquareSide,bodySquareSide, layoutOffsetX, layoutOffsetY,null, scaleType);
                    return;
                case SImageView.SCALE_TYPE_CENTER_CROP:
                    GraphsTemplate.drawBitmap(canvas, opeBitmap, viewWidth,viewHeight, 0, 0,null , scaleType);
                    return;

                case SImageView.SCALE_TYPE_FIX_XY:
                    GraphsTemplate.drawBitmap(canvas, opeBitmap, viewWidth,viewHeight, 0, 0,null, scaleType);
                    return;
            }
        }


        // 描边画笔 并设置属性
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
            // qq群组效果 包括圆形头像
            GraphsTemplate.drawCircle(canvas, null, centerX, centerY,(layoutSquareSide>>1) - (mBorderWidth/2),paint , mBorderWidth ,borderPaint);

        }else if (SImageView.TYPE_RECT == display){

            // 矩形图像
            GraphsTemplate.drawRect(canvas, null, layoutSquareSide, layoutSquareSide, layoutOffsetX, layoutOffsetY, paint, mBorderWidth, borderPaint);

        }else if (SImageView.TYPE_OVAL == display){

            // 椭圆头像
//            GraphsTemplate.drawOval(canvas, null, new RectF(layoutSquareSide*0.05f, layoutSquareSide*0.2f,layoutSquareSide*0.95f, layoutSquareSide*0.8f),layoutOffsetX,layoutOffsetY,paint , mBorderWidth ,borderPaint);
            GraphsTemplate.drawOval(canvas, null, new RectF(layoutSquareSide*(1-mOvalWidthRatio), layoutSquareSide*(1-mOvalHeightRatio),layoutSquareSide*mOvalWidthRatio, layoutSquareSide*mOvalHeightRatio),layoutOffsetX,layoutOffsetY,paint , mBorderWidth ,borderPaint);

        }else if (SImageView.TYPE_FIVE_POINTED_STAR == display){

            // 五角星头像
            GraphsTemplate.drawFivePointedStar(canvas, opeBitmap, (int)(layoutSquareSide / 2f), layoutOffsetX,layoutOffsetY, null ,mBorderWidth ,borderPaint);

        }else if (SImageView.TYPE_ROUND_RECT == display){

            // 有圆角的头像
            GraphsTemplate.drawCornerRectBorder(canvas, null, layoutSquareSide, layoutSquareSide, layoutSquareSide/mRectRoundRadius, layoutSquareSide/mRectRoundRadius,layoutOffsetX,layoutOffsetY,paint,mBorderWidth,borderPaint);
        }


    }



    /**
     * 设置单张图片 oval椭圆的宽高比.
     * @param widthHeightRadio  宽高比.
     */
    public void setOvalWidthOrHeight(float widthHeightRadio){

        if (widthHeightRadio < 0){
            throw new IllegalArgumentException("传入的宽高比值不能是负值");
        }
        if (widthHeightRadio == 0){
            throw new IllegalArgumentException("不能传入比值为0, 请重新确认");
        }

        if (widthHeightRadio > 1){
            mOvalWidthRatio = 1;
            mOvalHeightRatio = 0.5f * (1 + 1/widthHeightRadio);
        }else if (widthHeightRadio < 1){
            mOvalHeightRatio = 1;
            mOvalWidthRatio = 0.5f * (1 + widthHeightRadio);

        }else{
            mOvalHeightRatio = mOvalWidthRatio = 1;
        }
    }

    /**
     * 获取当前椭圆的宽高比
     */
    public float getOvalWidthOrHeight(){
        return Math.round(mOvalWidthRatio / mOvalHeightRatio * 100)/100f;
    }
}