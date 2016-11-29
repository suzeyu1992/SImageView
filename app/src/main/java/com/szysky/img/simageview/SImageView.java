package com.szysky.img.simageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午3:15
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 一个功能完备的ImageView
 */

public class SImageView extends ImageView {

    private Paint mPaint = new Paint();
    private Bitmap mBmp;
    private Bitmap mResultBmp;
    private ConfigInfo mInfo = new ConfigInfo();

    public static class ConfigInfo{
        public int height;
        public int width;
        public ArrayList<Bitmap> readyBmp = new ArrayList<>();
        public int border = 10;
    }

    public void setDrawStrategy(IDrawingStrategy mDrawStrategy) {
        this.mDrawStrategy = mDrawStrategy;
    }

    private IDrawingStrategy mDrawStrategy = new DrawCircle();

    public SImageView(Context context) {
        super(context);
        init();
    }

    public SImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_test);

        //init();
    }

    public SImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mInfo.height = getWidth();
        mInfo.width = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (null != mDrawStrategy){
            long l = System.nanoTime();
            mDrawStrategy.algorithm(canvas, mInfo);

            Log.i("susu", "ondraw()执行时间:"+ (System.nanoTime() - l));
        }
    }

    @Override
    public void setImageResource(int resId) {
        mInfo.readyBmp.add(mBmp) ;
        invalidate();
    }

    private void init(){


    }
}
