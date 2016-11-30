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

import com.szysky.customize.simageview.SImageView;
import com.szysky.customize.simageview.range.ILayoutManager;


/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午6:06
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :   具体内部元素的显示实现.
 *                      实现效果: 圆形头像.  但控件需要展示多张图片为QQ群组元素样式
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

        for (int i = 0; i < info.readyBmp.size(); i++) {
            Bitmap bitmap = info.readyBmp.get(i);
            ILayoutManager.LayoutInfoGroup layoutInfoGroup = info.coordinates.get(i);

            float maxHeight = layoutInfoGroup.innerHeight;

            int mBitmapWidth = bitmap.getWidth();   // 需要处理的bitmap宽度和高度
            int mBitmapHeight = bitmap.getHeight();
            canvas.save();
            Matrix matrix = new Matrix();
            matrix.postScale( maxHeight/mBitmapWidth , maxHeight/mBitmapHeight);
            canvas.translate(layoutInfoGroup.leftTopPoint.x , layoutInfoGroup.leftTopPoint.y);

            // 缩放
            Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            // 裁剪
            Bitmap bitmapOk = createMaskBitmap(newBitmap, newBitmap.getWidth(),
                    newBitmap.getHeight(), (int) v[i], 0.15f);

            canvas.drawBitmap(bitmapOk, 0, 0, paint);

            canvas.restore();

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
        canvas.drawCircle(center, center, center, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);

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
        borderPaint.setStrokeWidth(info.borderWidth);
        borderPaint.setColor(info.borderColor);


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
}
