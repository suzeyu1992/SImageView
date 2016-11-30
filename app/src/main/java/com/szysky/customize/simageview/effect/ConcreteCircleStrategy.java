package com.szysky.customize.simageview.effect;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.provider.Settings;
import android.util.Log;

import com.szysky.customize.simageview.SImageView;
import com.szysky.customize.simageview.range.ILayoutManager;


/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午6:06
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   具体内部元素的显示实现.
 *                      实现效果: 圆形头像.  当控件需要展示多张图片为QQ群组元素样式
 */

public class ConcreteCircleStrategy implements IDrawingStrategy {

    private final Matrix mShaderMatrix = new Matrix();
    /**
     * 要设置的图片大小
     */
    private Rect mDrawableRect = new Rect();

    @Override
    public void algorithm(Canvas canvas , SImageView.ConfigInfo info) {

        if (info.readyBmp.size() == 1){
            onePicture(canvas, info);

        }else{
            mulPicture(canvas, info);
        }

    }
    private static final float[][] rotations = { new float[] { 360.0f }, new float[] { 45.0f, 360.0f },
            new float[] { 120.0f, 0.0f, -120.0f }, new float[] { 90.0f, 180.0f, -90.0f, 0.0f },
            new float[] { 144.0f, 72.0f, 0.0f, -72.0f, -144.0f }, };
    /**
     * 多个图片处理
     * @param canvas
     * @param info
     */
    private void mulPicture(Canvas canvas , SImageView.ConfigInfo info){

        int mBorderWidth = info.borderWidth;                   // 描边宽度

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

            matrix.postScale( maxHeight/mBitmapWidth , maxHeight/mBitmapHeight);
            canvas.translate(layoutInfoGroup.leftTopPoint.x , layoutInfoGroup.leftTopPoint.y);

            // 缩放
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mBitmapWidth,
                    mBitmapHeight, matrix, true);
            // 裁剪
            Bitmap bitmapOk = createMaskBitmap(newBitmap, newBitmap.getWidth(),
                    newBitmap.getHeight(), (int) v[i], 0.15f);

            canvas.drawBitmap(bitmapOk, 0, 0, paint);

            canvas.restore();
            matrix.reset();
        }
    }


    public static final Bitmap createMaskBitmap(Bitmap bitmap, int viewBoxW, int viewBoxH,
                                                int rotation, float gapSize) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);// 抗锯齿
        paint.setFilterBitmap(true);
        int center = Math.round(viewBoxW / 2f);
        long start = System.nanoTime();
        canvas.drawCircle(center, center, center, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        Log.e("susu", "setXfermode模式事件>>> "+(System.nanoTime()-start) );
        if (rotation != 360) {
            Matrix matrix = new Matrix();
            // 根据原图的中心位置旋转
            matrix.setRotate(rotation, viewBoxW / 2, viewBoxH / 2);
            canvas.setMatrix(matrix);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
            canvas.drawCircle(viewBoxW * (1.5f - gapSize), center, center, paint);
        }
        return output;
    }

    /**
     * 单个图片处理
     */
    private void onePicture(Canvas canvas , SImageView.ConfigInfo info) {



        int mBorderWidth = info.borderWidth;                   // 描边宽度
        int mBitmapWidth = info.readyBmp.get(0).getWidth();   // 需要处理的bitmap宽度和高度
        int mBitmapHeight = info.readyBmp.get(0).getHeight();
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
        }

        // 中间内容画笔
        Paint paint =  new Paint();
        paint.setAntiAlias(true);

        // 控件可绘制内容的矩形
        Rect mBorderRect = new Rect();
        mBorderRect.set(0, 0, layoutSquareSide, layoutSquareSide);

        // 创建着色器 shader
        BitmapShader mBitmapShader = new BitmapShader(info.readyBmp.get(0), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(mBitmapShader);

        // 创建描边画笔 并设置属性
        Paint borderPaint = new Paint();
        paint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(info.borderWidth);
        borderPaint.setColor(info.borderColor);


        // bitmap的对应矩形
        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
        int mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);


        // 获取 的位置调整的信息 和bitmap需要缩放的比值
        float scale = 0;
        float dx = 0;
        float dy = 0;

        if (mBitmapWidth  >  mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }

        // 进行调整
        mShaderMatrix.set(null);
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth + layoutOffsetX, (int) (dy + 0.5f) + mBorderWidth + layoutOffsetY);
        mBitmapShader.setLocalMatrix(mShaderMatrix);

        // 计算需要显示的圆的圆心点和半径
        int centerX = viewWidth  >> 1 ;
        int centerY = viewHeight >> 1 ;

        // 画内容和边框
        canvas.drawCircle(centerX, centerY, mBorderRadius-(mBorderWidth>>1), paint);
        canvas.drawCircle(centerX, centerY, mBorderRadius, borderPaint);


    }
}
