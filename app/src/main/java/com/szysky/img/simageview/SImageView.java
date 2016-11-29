package com.szysky.img.simageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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


    private ILayoutManager mLayoutManager = new QQLayoutManager();


    public static class ConfigInfo{

        public int height;
        public int width;
        public ArrayList<Bitmap> readyBmp = new ArrayList<>();
        public int borderWidth = 10;
        public int borderColor = Color.BLACK;
        public ArrayList<ILayoutManager.LayoutInfoGroup> coordinates ;

    }

    public void setDrawStrategy(IDrawingStrategy mDrawStrategy) {
        this.mDrawStrategy = mDrawStrategy;
    }

    private IDrawingStrategy mDrawStrategy ;

    public void setLayoutManager(ILayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;
    }

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
        mInfo.height = getHeight();
        mInfo.width = getWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (null != mLayoutManager){
            long l = System.currentTimeMillis();
            mInfo.coordinates = mLayoutManager.calculate(getWidth(), getHeight(), mInfo.readyBmp.size());

            mDrawStrategy.algorithm(canvas, mInfo);
            Log.i("susu", "2张图片ondraw()执行时间:"+ (System.currentTimeMillis() - l));


        }

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

    public void setImages(ArrayList<Bitmap> bitmaps){
        mInfo.readyBmp = bitmaps ;
        invalidate();
    }

    private void init(){


    }
}
