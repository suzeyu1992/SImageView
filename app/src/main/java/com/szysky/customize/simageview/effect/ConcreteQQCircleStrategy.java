package com.szysky.customize.simageview.effect;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.provider.Settings;
import android.util.Log;

import com.szysky.customize.simageview.SImageView;
import com.szysky.customize.simageview.range.ILayoutManager;
import com.szysky.customize.simageview.util.GraphsTemplate;


/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午6:06
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   具体内部元素的显示实现.
 *                      实现效果: 圆形头像.  当控件需要展示多张图片为QQ群组元素样式
 */

public class ConcreteQQCircleStrategy implements IDrawingStrategy {

    /**
     *  默认两张图片间隔距离系数
     */
    private float mSpacing = 0.15f;

    /**
     *  控制是开启qq群组图片去除两张图片重叠的效果
     */
    private boolean mIsPicRotate = true;
    private static Paint  mPaint = new Paint();

    /**qq群组的不同数量时的对应旋转数组**/
    private static final float[][] rotations = { new float[] { 360.0f }, new float[] { 45.0f, 360.0f },
            new float[] { 120.0f, 0.0f, -120.0f }, new float[] { 90.0f, 180.0f, -90.0f, 0.0f },
            new float[] { 144.0f, 72.0f, 0.0f, -72.0f, -144.0f }, };


    private int mBorderWidth;

    /**
     *  描边画笔
     */
    private final Paint mBorderPaint;

    /**
     *  子元素内容画笔
     */
    private final Paint bodyPaint;


    public ConcreteQQCircleStrategy(){
        // 创建内容画笔和描边画笔 并设置属性
        bodyPaint = new Paint();
        bodyPaint.setAntiAlias(true);
        bodyPaint.setStyle(Paint.Style.FILL);

        mBorderPaint = new Paint();
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(1);
        mBorderPaint.setColor(Color.BLACK);
        mBorderPaint.setAntiAlias(true);
    }

    @Override
    public void algorithm(Canvas canvas, int childTotal, int curChild, Bitmap opeBitmap, SImageView.ConfigInfo info) {

        // 描边宽度
        mBorderWidth = info.borderWidth;
        mBorderPaint.setColor(info.borderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        Matrix matrix = new Matrix();

        ILayoutManager.LayoutInfoGroup layoutInfoGroup = info.coordinates.get(curChild-1);

        float maxHeight = layoutInfoGroup.innerHeight;

        int mBitmapWidth = opeBitmap.getWidth();   // 需要处理的bitmap宽度和高度
        int mBitmapHeight = opeBitmap.getHeight();
        canvas.save();


        if (!mIsPicRotate){
            matrix.postScale( maxHeight/(float)mBitmapWidth , maxHeight/(float)mBitmapHeight );
        }else{
            matrix.postScale(  maxHeight/(float)mBitmapWidth , maxHeight/(float)mBitmapHeight);

        }
//        canvas.translate(layoutInfoGroup.leftTopPoint.x , layoutInfoGroup.leftTopPoint.y);
        // 缩放
        Bitmap newBitmap = Bitmap.createBitmap(opeBitmap, 0, 0, mBitmapWidth,
                mBitmapHeight, matrix, true);




        adjustMaskBitmapDisplay(canvas,newBitmap, newBitmap.getWidth(),
                newBitmap.getHeight(), childTotal > 5 ? 360:rotations[childTotal-1][curChild-1], mSpacing , mIsPicRotate);



        canvas.restore();
        matrix.reset();

    }


    private  void adjustMaskBitmapDisplay(Canvas canvas, Bitmap bitmap ,int viewBoxW, int viewBoxH,
                                                float rotation, float gapSize ,boolean isRotate){
        mPaint.reset();
        mPaint.setAntiAlias(true);// 抗锯齿
        mPaint.setFilterBitmap(true);
        int center = Math.round(viewBoxW / 2f);

        int flag = 3;
        if (flag == 1){
            // qq群组效果
            // 先处理成圆形头像
            GraphsTemplate.drawCircle(canvas, bitmap, center, center,center,mPaint);

            if ( isRotate  &&   rotation != 360f  ) {
                Matrix matrix = new Matrix();
                // 根据原图的中心位置旋转
                matrix.setRotate(rotation, viewBoxW / 2, viewBoxH / 2);
                canvas.setMatrix(matrix);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawCircle(viewBoxW * (1.5f - gapSize), center, center, mPaint);
                mPaint.setXfermode(null);
            }
        }else if (flag == 2){       // 原图头像

            canvas.drawBitmap(bitmap, 0, 0, mPaint);
        }else if (flag == 3){

            // 椭圆头像
            GraphsTemplate.drawOval(canvas, bitmap, new RectF(viewBoxW*0.05f, viewBoxH*0.2f,viewBoxW*0.95f, viewBoxH*0.8f),0,0,mPaint , mBorderWidth ,mBorderPaint);

        }else if (flag == 4){

            // 五角星头像
            GraphsTemplate.drawFivePointedStar(canvas, bitmap, (int)(center * 0.9f), 0,0, mPaint ,mBorderWidth ,mBorderPaint);

        }else if (flag == 5){
            // 有圆角的头像
           GraphsTemplate.drawCornerRectBorder(canvas, bitmap, viewBoxW, viewBoxH, viewBoxW/8, viewBoxW/8,0,0,mPaint, mBorderWidth ,mBorderPaint);
        }
    }


    /**
     *  设置两张图片的间距
     *
     * @param spacingQuality 接收处理范围 0~2 ; 2的时候空隙为最大, 0的时候会重叠. 默认为1
     */
    public void setSpacing(float spacingQuality) {

        if (spacingQuality > 2){
            spacingQuality = 2;
        }else if (spacingQuality < 0){
            spacingQuality = 0;
        }


        mSpacing *= spacingQuality;
    }

    public boolean isIsPicRotate() {
        return mIsPicRotate;
    }


    /**
     * 设置qq群组图片去除重叠方法
     *
     * @param mIsPicRotate 为true时, 为QQ群组的样式, 默认属性
     *                      为false时: 可去除两个图片重叠确实的效果
     */
    public void setIsPicRotate(boolean mIsPicRotate) {
        this.mIsPicRotate = mIsPicRotate;
    }

    public float getSpacingQuality() {
        return Math.round((mSpacing / 0.15f)*100)/100;
    }

}
