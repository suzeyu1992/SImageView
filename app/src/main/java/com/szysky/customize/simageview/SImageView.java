package com.szysky.customize.simageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.szysky.customize.simageview.effect.ConcreteQQCircleStrategy;
import com.szysky.customize.simageview.effect.IDrawingStrategy;
import com.szysky.customize.simageview.effect.NormalOnePicStrategy;
import com.szysky.customize.simageview.range.ILayoutManager;
import com.szysky.customize.simageview.range.QQLayoutManager;
import com.szysky.customize.simageview.range.WeCharLayoutManager;

import java.util.ArrayList;
import java.util.Iterator;

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

    /**
     *  对外提供的画布
     */
    private Canvas mExternalUseCanvas= new Canvas();


    /**
     *  默认单图片处理的开关标记
     */
    private boolean mCloseNormalOnePicLoad = false;

    /**
     *  具体子元素图片显示样式策略, 默认下,对于一张图片会使用 mNormalOnePicStrategy 变量, 如果实现了自定义策略,
     *  并且策略内部包含了一张图片的显示逻辑, 可以通过变量强制关闭单图片的默认处理.
     */
    private ILayoutManager mLayoutManager = new QQLayoutManager();

    /**
     *  默认单个图片加载策略
     */
    private IDrawingStrategy mNormalOnePicStrategy = new NormalOnePicStrategy();

    private IDrawingStrategy mDrawStrategy = new ConcreteQQCircleStrategy();

    public boolean isCloseNormalOnePicLoad() {
        return mCloseNormalOnePicLoad;
    }

    public void setCloseNormalOnePicLoad(boolean isClose) {
        this.mCloseNormalOnePicLoad = isClose;
    }

    /**
     *  控件属性类
     */
    public static class ConfigInfo{

        public int height;
        public int width;
        public ArrayList<Bitmap> readyBmp = new ArrayList<>();
        public int borderWidth = 10;
        public int borderColor = Color.BLACK;
        public ArrayList<ILayoutManager.LayoutInfoGroup> coordinates ;
        public boolean isNormalImpl = true;   // 此标记只在具体实现画图显示为qq策略才有用

    }

    public void setDrawStrategy(IDrawingStrategy mDrawStrategy) {
        this.mDrawStrategy = mDrawStrategy;
        if (mDrawStrategy instanceof ConcreteQQCircleStrategy){
            mInfo.isNormalImpl = mLayoutManager instanceof QQLayoutManager ;
        }
    }


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


        long startCur = System.nanoTime();


        if ( mInfo.readyBmp.size() == 1 && !mCloseNormalOnePicLoad){
            long l = System.nanoTime();
            mNormalOnePicStrategy.algorithm(canvas,1,1,mInfo.readyBmp.get(0), mInfo);
            Log.i("susu", "一张图片执行时间:"+ (System.nanoTime() - l)/1000000f+"毫秒");

        }else if (mInfo.readyBmp.size() > 0 ){
            // measure布局参数
            mInfo.coordinates = mLayoutManager.calculate(getWidth(), getHeight(), mInfo.readyBmp.size());

            // layout 子元素布局
            Iterator<ILayoutManager.LayoutInfoGroup> iterator = mInfo.coordinates.iterator();
            int index = 0;
            while (iterator.hasNext()){
                index++;
                ILayoutManager.LayoutInfoGroup childInfo = iterator.next();

                int offsetX = childInfo.leftTopPoint.x;
                int offsetY = childInfo.leftTopPoint.y;


                Bitmap tempBmp = Bitmap.createBitmap(childInfo.innerWidth, childInfo.innerWidth, Bitmap.Config.ARGB_8888);


                // 首先关联一个bitmap, 并把关联的canvas对外提供出去
                mExternalUseCanvas.setBitmap(tempBmp);

                // **重点**. 具体实现由使用者通过mExternalUseCanvas定义.
                mDrawStrategy.algorithm(mExternalUseCanvas,mInfo.coordinates.size(),index ,mInfo.readyBmp.get(index-1),mInfo );


                canvas.drawBitmap(tempBmp,offsetX,offsetY,null);


                // 取消关联的bitmap  并 清空对外提供的canvas
                mExternalUseCanvas.setBitmap(null);
                mExternalUseCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            }
        }

        Log.i("susu", "onDraw的执行时间:"+ (System.nanoTime() - startCur)/1000000f +"毫秒");


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
