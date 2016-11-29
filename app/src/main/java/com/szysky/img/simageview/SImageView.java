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
import android.widget.ImageView;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午3:15
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class SImageView extends ImageView {

    private Paint mPaint = new Paint();
    private Bitmap mBmp;

    public SImageView(Context context) {
        super(context);
        init();
    }

    public SImageView(Context context, AttributeSet attrs) {
        super(context, attrs);


        //init();
    }

    public SImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2, mPaint);
    }

    private void init(){
        setScaleType(ScaleType.FIT_XY);

        mBmp = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_test);

        BitmapShader mBitmapShader = new BitmapShader(mBmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setShader(mBitmapShader);

        Matrix mShaderMatrix = new Matrix();
//        mShaderMatrix.setScale(2, 2);
//        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
        mShaderMatrix.setTranslate(100,100);
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }
}
