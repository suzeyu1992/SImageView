package com.szysky.customize.simageview.effect;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
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
    private float mSpacing = 0.3f;

    /**
     *  为true时, 为QQ群组的样式, 默认属性
     *  为false时: 可去除两个图片重叠确实的效果
     */
    private boolean mIsPicRotate = false;

    public float getSpacingQuality() {
        return Math.round((mSpacing / 0.15f)*100)/100;
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

    @Override
    public void algorithm(Canvas canvas , SImageView.ConfigInfo info) {

        float[] v = rotations[info.readyBmp.size()-1];
        Paint paint = new Paint();

        paint.setAntiAlias(true);

        Matrix matrix = new Matrix();
        for (int i = 0; i < info.readyBmp.size(); i++) {
            Bitmap bitmap = info.readyBmp.get(i);
            ILayoutManager.LayoutInfoGroup layoutInfoGroup = info.coordinates.get(i);

            float maxHeight = layoutInfoGroup.innerHeight;

            int mBitmapWidth = bitmap.getWidth();   // 需要处理的bitmap宽度和高度
            int mBitmapHeight = bitmap.getHeight();
            canvas.save();


            if (!mIsPicRotate){
                matrix.postScale( maxHeight/mBitmapWidth * 0.9f , maxHeight/mBitmapHeight * 0.9f );
            }else{
                matrix.postScale(  maxHeight/(float)mBitmapWidth , maxHeight/(float)mBitmapHeight);

            }
            canvas.translate(layoutInfoGroup.leftTopPoint.x , layoutInfoGroup.leftTopPoint.y);


            bitmap.recycle();
            // 缩放
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mBitmapWidth,
                    mBitmapHeight, matrix, true);
            // 裁剪
            Bitmap bitmapOk = createMaskBitmap(newBitmap, newBitmap.getWidth(),
                    newBitmap.getHeight(), (int) v[i], mSpacing , mIsPicRotate);

            canvas.drawBitmap(bitmapOk, 0, 0, paint);

            canvas.restore();
            matrix.reset();
        }
    }

    /**qq群组的不同数量时的对应旋转数组**/
    private static final float[][] rotations = { new float[] { 360.0f }, new float[] { 45.0f, 360.0f },
            new float[] { 120.0f, 0.0f, -120.0f }, new float[] { 90.0f, 180.0f, -90.0f, 0.0f },
            new float[] { 144.0f, 72.0f, 0.0f, -72.0f, -144.0f }, };


    private static  Bitmap createMaskBitmap(Bitmap bitmap, int viewBoxW, int viewBoxH,
                                                int rotation, float gapSize ,boolean isRotate) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);// 抗锯齿
        paint.setFilterBitmap(true);
        int center = Math.round(viewBoxW / 2f);
        long start = System.nanoTime();
//
        int flag = 1;
        if (flag == 1){
            canvas.drawCircle(center, center, center, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);

            if (rotation != 360 && isRotate ) {
                Matrix matrix = new Matrix();
                // 根据原图的中心位置旋转
                matrix.setRotate(rotation, viewBoxW / 2, viewBoxH / 2);
                canvas.setMatrix(matrix);
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                canvas.drawCircle(viewBoxW * (1.5f - gapSize), center, center, paint);
            }
        }else if (flag == 2){
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }else if (flag == 3){
            canvas.drawOval(new RectF(0, 0,viewBoxW, viewBoxH),  paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }else{
            GraphsTemplate.drawFivePointedStar(canvas, center, 0,0,paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, 0, 0, paint);
        }



        Log.e("susu", "setXfermode模式事件>>> "+(System.nanoTime()-start) );


        return output;
    }


    public boolean isIsPicRotate() {
        return mIsPicRotate;
    }

    public void setIsPicRotate(boolean mIsPicRotate) {
        this.mIsPicRotate = mIsPicRotate;
    }


}
