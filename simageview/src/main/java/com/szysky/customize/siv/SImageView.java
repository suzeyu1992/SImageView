package com.szysky.customize.siv;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.szysky.customize.siv.effect.ConcreteDrawingStrategy;
import com.szysky.customize.siv.effect.IDrawingStrategy;
import com.szysky.customize.siv.effect.NormalOnePicStrategy;
import com.szysky.customize.siv.range.ILayoutManager;
import com.szysky.customize.siv.range.QQLayoutManager;
import com.szysky.customize.siv.util.UIUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午3:15
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 一个功能完备的ImageView
 */

public class SImageView extends View {

    private static final String TAG = SImageView.class.getName();
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private ConfigInfo mInfo = new ConfigInfo();

    private Context mContext;

    private int mDrawableWidth;
    private int mDrawableHeight;

    /**
     *  对具体绘图抽象过程提供的一个可利用的画布.
     */
    private Canvas mExternalUseCanvas= new Canvas();


    /**
     *  默认单图片处理策略的开关标记  true: 关闭   false: 开启
     */
    private boolean mCloseNormalOnePicLoad = false;

    /**
     *  具体子元素的 measure布局 策略,
     *  默认下,对于一张图片会使用 mNormalOnePicStrategy 变量, 如果实现了自定义策略,
     *  并且策略内部包含了一张图片的布局逻辑, 可以通过变量强制关闭单图片的默认处理.
     */
    private ILayoutManager mLayoutManager = new QQLayoutManager();

    /**
     *  单个图片默认加载策略, 优先级高于多张图,
     *  可以通过{@link #setCloseNormalOnePicLoad(boolean)}设置为true强制关闭此策略
     */
    private NormalOnePicStrategy mNormalOnePicStrategy = new NormalOnePicStrategy();


    /**
     *  具体的子图片绘制的策略对象
     */
    private IDrawingStrategy mDrawStrategy = new ConcreteDrawingStrategy();

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;


    /**
     *  控件属性类
     */
     public static class ConfigInfo implements Cloneable{

        public int height;                                      // 控件的高度
        public int width;                                       // 控件的宽度
        public ArrayList<Bitmap> readyBmp = new ArrayList<>();  // 需要显示的图片集合
        public float borderWidth = 0;                             // 描边宽度
        public int borderColor = Color.BLACK;                   // 描边颜色
        public ArrayList<ILayoutManager.LayoutInfoGroup> coordinates ;  // 测量过程返回的每个元素的对应位置信息
        public int displayType ;                                 // 子元素的显示类型
        public int scaleType ;                                   // 矩形的缩放类型

        @Override
        protected Object clone() {
            ConfigInfo clone ;
            try {
                clone = (ConfigInfo) super.clone();
                if (coordinates != null){
                    clone.coordinates = (ArrayList<ILayoutManager.LayoutInfoGroup>) coordinates.clone();
                }
                clone.readyBmp = (ArrayList<Bitmap>) readyBmp.clone();
            } catch (CloneNotSupportedException e) {
                Log.w(TAG, "图片信息 clone is error" );
                e.printStackTrace();
                return this;
            }
            return clone;
        }
    }



    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_RECT = 1;
    public static final int TYPE_ROUND_RECT = 2;
    public static final int TYPE_FIVE_POINTED_STAR = 3;
    public static final int TYPE_OVAL = 4;

    @IntDef({TYPE_CIRCLE, TYPE_RECT, TYPE_ROUND_RECT ,TYPE_FIVE_POINTED_STAR, TYPE_OVAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ShapeDisplay{}

    /**显示类型,默认为上面五种定义类型**/
    @ShapeDisplay private int mCurrentDisplayShape = TYPE_CIRCLE;


    public static final int SCALE_TYPE_CENTER_INSIDE = 0;  // 图片比例不变, 以最大边的为标准缩放, 可能会有留白,显示全部图片
    public static final int SCALE_TYPE_FIX_XY = 1;         // 图片比例改变, 已填充控件为主, 显示全部图片
    public static final int SCALE_TYPE_CENTER_CROP = 2;    // 图片比例不变, 已填充控件为主, 图片可能显示不全

    @IntDef({SCALE_TYPE_CENTER_INSIDE,SCALE_TYPE_FIX_XY ,SCALE_TYPE_CENTER_CROP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScaleType{}

    /**
     * 当显示类型为矩形的时候, 缩放类型才会生效. 并且当有描边时, 缩放类型失效,
     * 并且使用{@link #mCloseNormalOnePicLoad}的初始值通过使用单张图片的绘制逻辑才有处理效果
     * 默认为{@link #SCALE_TYPE_CENTER_INSIDE}
     **/
    @SImageView.ScaleType
    private int mScaleType = SCALE_TYPE_CENTER_INSIDE;


    public SImageView(Context context) {
        super(context);

    }

    public SImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 获得xml属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SImageView, defStyleAttr, 0);

        mInfo.borderWidth = typedArray.getDimensionPixelSize(R.styleable.SImageView_border_width, 0);
        mInfo.borderColor = typedArray.getColor(R.styleable.SImageView_border_color, Color.BLACK);
        mInfo.displayType = typedArray.getInt(R.styleable.SImageView_displayType, 0);
        mInfo.scaleType   = typedArray.getInt(R.styleable.SImageView_scaleType, 0);

        Drawable drawable = typedArray.getDrawable(R.styleable.SImageView_img);
        if ( null != drawable ){
            mDrawableHeight = drawable.getIntrinsicHeight();
            mDrawableWidth = drawable.getIntrinsicWidth();
            mInfo.readyBmp.clear();
            mInfo.readyBmp.add(getBitmapFromDrawable(drawable));
        }

        // padding setting
        mPaddingBottom = getPaddingBottom();
        mPaddingTop = getPaddingTop();
        mPaddingLeft = getPaddingLeft();
        mPaddingRight = getPaddingRight();

        typedArray.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = 0;
        int h = 0;

        final int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        final int pleft = mPaddingLeft;
        final int pright = mPaddingRight;
        final int ptop = mPaddingTop;
        final int pbottom = mPaddingBottom;


        // 宽度处理
        if (widthSpecMode == MeasureSpec.UNSPECIFIED){
            // 如果是UNSPECIFIED模式需要自定义大小 match_parent
            if (mDrawableWidth > 0){
                w = mDrawableWidth;
            }else if (heightSize > 0){
                w = heightSize;
            }else{
                w = (int) UIUtils.dip2px(mContext, 48f);
            }

            w += pleft + pright;    // 加上padding

            w = Math.max(w, getSuggestedMinimumWidth());
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            // 如果是AT_MOST模式特殊处理  wrap_content
            if (mDrawableWidth > 0){
                w = mDrawableWidth + pleft + pright;
                w = ( w > widthSize ? widthSize : w);
            }

            if (getSuggestedMinimumWidth() > 0){
                if (getSuggestedMinimumWidth() >= widthSize){
                    w = widthSize;
                }else {
                    w = (w > getSuggestedMinimumWidth() ? w : getSuggestedMinimumWidth());
                }
            }

            // 如果既没有设置前景, 也没有背景, 设置一个像素占位
            if ((mDrawableWidth <=0) && (getSuggestedMinimumWidth() <=0)){
                w = 1 + pleft + pright;
            }
        }else if (widthSpecMode == MeasureSpec.EXACTLY){
            w = widthSize;
        }


        // 高度测量
        if (heightSpecMode == MeasureSpec.UNSPECIFIED){
            // 如果是UNSPECIFIED模式需要自定义大小 match_parent
            if (mDrawableHeight > 0){
                h = mDrawableHeight;
            }else if (widthSize > 0){
                h = widthSize;
            }else{
                h = (int) UIUtils.dip2px(mContext, 48f);
            }
            h += ptop + pbottom; // 加上padding

            h = Math.max(h, getSuggestedMinimumHeight());
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            // 如果是AT_MOST模式特殊处理  wrap_content
            // 判断前景
            if (mDrawableHeight > 0){
                h = mDrawableHeight + ptop + pbottom;
                h = ( h > heightSize ? heightSize : h);
            }
            // 判断背景
            if (getSuggestedMinimumHeight() > 0){
                if (getSuggestedMinimumHeight() >= heightSize){
                    h = heightSize;
                }else {
                    h = (h > getSuggestedMinimumHeight() ? h : getSuggestedMinimumHeight());
                }
            }

            // 如果既没有设置前景, 也没有背景, 设置一个像素占位
            if ((mDrawableHeight <=0) && (getSuggestedMinimumHeight() <=0)){
                h = 1 + ptop + pbottom;
            }
        }else if (heightSpecMode == MeasureSpec.EXACTLY){
            h = heightSize;
        }




        widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
        heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);


        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        adjustPadding(left, top, right, bottom);

        mInfo.height = getHeight() - mPaddingBottom - mPaddingTop ;
        mInfo.width = getWidth() - mPaddingLeft - mPaddingRight;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // translate padding
        canvas.translate(mPaddingLeft, mPaddingTop);

        long startCur = System.nanoTime();


        if ( mInfo.readyBmp.size() == 1 && !mCloseNormalOnePicLoad){
            long l = System.nanoTime();
            mNormalOnePicStrategy.algorithm(canvas,1,1,mInfo.readyBmp.get(0), (ConfigInfo) mInfo.clone());
            Log.i(TAG, "一张图片执行时间: "+ (System.nanoTime() - l)/1000000f+"毫秒");

        }else if (mInfo.readyBmp.size() > 0 ){
            // measure布局参数
            mInfo.coordinates = mLayoutManager.calculate(mInfo.width, mInfo.height, mInfo.readyBmp.size());

            if (mInfo.coordinates == null) return;
            // layout 子元素布局
            Iterator<ILayoutManager.LayoutInfoGroup> iterator = mInfo.coordinates.iterator();
            int index = 0;
            while (iterator.hasNext()){
                index++;
                ILayoutManager.LayoutInfoGroup childInfo = iterator.next();

                int offsetX = childInfo.leftTopPoint.x;
                int offsetY = childInfo.leftTopPoint.y;


                Bitmap tempBmp = Bitmap.createBitmap(childInfo.innerWidth, childInfo.innerHeight, Bitmap.Config.ARGB_8888);


                // 首先关联一个bitmap, 并把关联的canvas对外提供出去
                mExternalUseCanvas.setBitmap(tempBmp);

                // **重点**. 具体实现由使用者通过mExternalUseCanvas定义.
                mDrawStrategy.algorithm(mExternalUseCanvas,mInfo.coordinates.size(),index ,mInfo.readyBmp.get(index-1), (ConfigInfo) mInfo.clone());


                canvas.drawBitmap(tempBmp,offsetX,offsetY,null);


                // 取消关联的bitmap  并 清空对外提供的canvas
                mExternalUseCanvas.setBitmap(null);
                mExternalUseCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            }
            // 清除布局消息
            mInfo.coordinates = null;

            Log.i(TAG, "多张图执行时间: "+ (System.nanoTime() - startCur)/1000000f +"毫秒");
        }

        // translate padding
        canvas.translate(-mPaddingLeft, -mPaddingTop);

    }






    /**
     *  对padding边界值处理
     */
    private void adjustPadding(int left, int top, int right, int bottom) {
        // 对padding进行极限值处理
        while ((mPaddingBottom + mPaddingTop) >= ( bottom - top )){
            mPaddingBottom >>= 1;
            mPaddingTop >>= 1;
        }

        while((mPaddingRight + mPaddingLeft) >= (right - left)){
            mPaddingRight >>= 1;
            mPaddingLeft >>= 1;
        }
    }

    /**
     *  返回 是否使用了默认的单张绘图显示策略.
     */
    public boolean isCloseNormalOnePicLoad() {
        return mCloseNormalOnePicLoad;
    }

    /**
     * 设置 是否关闭单张图片时, 使用特定的单图绘制策略. true为关闭, false为开启, 默认为false
     * 注明: 如果是默认值, 那么只有多个图片显示的时候才会使用{@link #mDrawStrategy}策略, 一张图片的时候会使用
     *      内置的单张图片处理策略{@link #mNormalOnePicStrategy}.
     *      如果通过{@link #setDrawStrategy(IDrawingStrategy)}实现了自定义策略, 那么单张图片开关标记将会
     *      自动设置为关闭.
     */
    public void setCloseNormalOnePicLoad(boolean isClose) {
        this.mCloseNormalOnePicLoad = isClose;
    }

    /**
     * 设置子元素 绘制图片 的具体显示策略
     */
    public void setDrawStrategy(IDrawingStrategy mDrawStrategy) {
        this.mDrawStrategy = mDrawStrategy;
        if (mDrawStrategy instanceof ConcreteDrawingStrategy){
            mCloseNormalOnePicLoad = false;
        }else{
            mCloseNormalOnePicLoad = true;
        }
    }


    /**
     * 设置 测量布局 规则
     */
    public void setLayoutManager(ILayoutManager mLayoutManager) {
        this.mLayoutManager = mLayoutManager;

        // 兼容qq群组绘制的重叠场景问题
        if (mLayoutManager instanceof QQLayoutManager){
            if (mDrawStrategy instanceof ConcreteDrawingStrategy){
                ((ConcreteDrawingStrategy)mDrawStrategy).setIsPicRotate(true);
            }
        }else{
            if (mDrawStrategy instanceof ConcreteDrawingStrategy){
                ((ConcreteDrawingStrategy)mDrawStrategy).setIsPicRotate(false);
            }
        }

    }


    /**
     * 返回当前控件的显示类型 如圆形, 矩形, 五角星等等...
     */
    @ShapeDisplay
    public int getDisplayShape() {
        return mCurrentDisplayShape;
    }

    /**
     * 设置当前控件的显示类型, 如圆形, 矩形, 五角星等等...
     * @param mCurrentDisplayShape  只能@{@link ShapeDisplay}类型
     */
    public void setDisplayShape(@ShapeDisplay int mCurrentDisplayShape) {
        this.mCurrentDisplayShape = mCurrentDisplayShape;
        mInfo.displayType = mCurrentDisplayShape;
    }

    /**
     * 设置图片描边宽度
     */
    public void setBorderWidth(float dp){
        mInfo.borderWidth = (int) UIUtils.dip2px(getContext(), dp);
    }

    /**
     * 获得描边的宽度 单位dp
     */
    public float getBorderWidth(){
        return UIUtils.px2dip(getContext(), mInfo.borderWidth);
    }

    /**
     * 设置描边颜色
     */
    public void setBorderColor(@ColorInt int color){
        mInfo.borderColor = color;
    }

    public @ColorInt int getBorderColor(){
        return mInfo.borderColor;
    }


    /**
     * 设置展示图片的集合
     * @param bitmaps 接收一个图片集合
     */
    public void setImages(List<Bitmap> bitmaps){
        updateForList(bitmaps);
    }

    /**
     * 传入drawable资源id
     */
    public void setIdRes(@DrawableRes int id){
        if (id != 0) {
            Drawable drawable = getResources().getDrawable(id);
            if ( null != drawable){
                updateForOne(getBitmapFromDrawable(drawable));
            }
        }
    }


    public void setDrawable(Drawable drawable){
        updateForOne(getBitmapFromDrawable(drawable));
    }

    private void updateForOne(Bitmap bitmap){
        if (null != bitmap){
            mInfo.readyBmp.clear();
            mInfo.readyBmp.add(bitmap);
            invalidate();
        }
    }

    private void updateForList(List<Bitmap> bitmaps) {

        if ((null != bitmaps) && (bitmaps.size() >0)){
            mInfo.readyBmp.clear();
            for (Bitmap bitmap : bitmaps) {
                mInfo.readyBmp.add(bitmap);
            }
            invalidate();
        }
    }

    /**
     * 设置展示的图像的bitmap
     */
    public void setBitmap(Bitmap bitmap){
        updateForOne(bitmap);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(1, 1, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    @ScaleType
    public int getScaleType() {
        return mScaleType;
    }

    /**
     * 设置单张图片时, 图片的缩放类型, 只有在矩形图片和无描边的场景下有效,
     * 并且使用{@link #mCloseNormalOnePicLoad}的初始值通过使用单张图片的绘制逻辑才有处理效果
     * {@link #mScaleType}
     */
    public void setScaleType(@ScaleType int mScaleType) {
        this.mScaleType = mScaleType;
        mInfo.scaleType = mScaleType;

    }

    /**
     * 获得单张图片时 圆角矩形 的圆角弧度系数, 范围0~2; 默认为1
     */
    public float getRectRoundRadius() {
        return mNormalOnePicStrategy.getRectRoundRadius();
    }

    /**
     *  设置单张图片时 圆角矩形 的圆角弧度系数, 取值为0~2, 默认为1
     *  此设置属性不会立即生效, 需下次圆角矩形加载时才会有效.可手动invalidate刷新
     */
    public void setRectRoundRadius(float mRectRoundRadius) {
        mNormalOnePicStrategy.setRectRoundRadius(mRectRoundRadius);
    }

    /**
     * 设置单张图片 oval椭圆的宽高比.
     * 此设置属性同样不会立即生效, 需下次椭圆显示加载时才会生效, 可手动invalidate刷新
     * @param widthHeightRadio  宽高比. 只能传入大于0, 默认值为宽高比为2/1  也就是2f
     */
    public  void setOvalRatio(float widthHeightRadio){
        mNormalOnePicStrategy.setOvalWidthOrHeight(widthHeightRadio);
    }

    /**
     * 获取当前椭圆的宽高比
     */
    public float getOvalRatio(){
        return mNormalOnePicStrategy.getOvalWidthOrHeight();
    }
}
