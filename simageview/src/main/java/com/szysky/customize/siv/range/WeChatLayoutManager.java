package com.szysky.customize.siv.range;

import android.content.Context;
import android.graphics.Point;

import com.szysky.customize.siv.util.UIUtils;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-12-01  下午11:14
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :  微信 measure测量布局实现
 */

public class WeChatLayoutManager implements ILayoutManager {

    private final Context context;

    /**
     * 缓存返回子元素布局对象
     */
    private final ArrayList<LayoutInfoGroup> mCacheList;

    /**
     * 布局子元素的下标
     */
    private int curCachePoint ;

    /**
     *  子元素的空隙, 例如两个图片的距离. 单位dp
     */
    private float mSpacing = 1f;

    public WeChatLayoutManager(Context context) {
        this.context = context;
        // 创建子元素的布局对象集合, 用于后续使用
        mCacheList = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            LayoutInfoGroup layout = new LayoutInfoGroup();
            layout.leftTopPoint = new Point();
            layout.rightBottomPoint = new Point();
            mCacheList.add(layout);
        }
        curCachePoint = 8;
    }


    @Override
    public ArrayList<LayoutInfoGroup> calculate(int viewWidth, int viewHeight, int viewNum) {

        // 缓存集合清除无用信息
        cleanMaskCache();

        // 默认微信群组效果只支持9张最大图片
        if (viewNum > 9){
            viewNum = 9;
        }else if (viewNum < 1){
            throw new UnsupportedOperationException("不支持操作异常");
        }

        // 容错控件非正方形场景处理
        int layoutSquareSide ;       // 正方形边长
        if (viewWidth != viewHeight){
            if (viewHeight - viewWidth > 0){
                layoutSquareSide = viewWidth;
            }else{
                layoutSquareSide = viewHeight;
            }
        }else{
            layoutSquareSide =  viewWidth;
        }

        // 返回的所有子元素布局信息集合
        ArrayList<LayoutInfoGroup> infos = new ArrayList<>();

        int half =0;      // 子元素的边长

        // 开始测量布局
        if (viewNum == 1){

            half = layoutSquareSide ;
            infos.add(createChildrenForTop(viewWidth/2 - half/2 , viewHeight/2 - half/2,half));

        }else if(viewNum == 2){

            half = layoutSquareSide / 2;
            fastTwoChild(viewWidth,(viewHeight-half)/2,half, infos );

        }else if (viewNum == 3){
            half = layoutSquareSide / 2;
            infos.add(createChildrenForTop(viewWidth/2 - half/2 , viewHeight/2 - half,half));
            fastTwoChild(viewWidth,viewHeight/2,half, infos );

        }else if (viewNum == 4){
            half = layoutSquareSide / 2;
            fastTwoChild(viewWidth,viewHeight/2 - half,half, infos );
            fastTwoChild(viewWidth,viewHeight/2 ,half, infos );

        }else if (viewNum == 5){
            half = layoutSquareSide / 3;
            fastTwoChild(viewWidth,viewHeight/2 - half,half, infos );
            fastThreeChild(viewWidth, viewHeight/2, half, infos);
        }else if (viewNum == 6){
            half = layoutSquareSide / 3;
            fastThreeChild(viewWidth, viewHeight/2 - half, half, infos);
            fastThreeChild(viewWidth, viewHeight/2, half, infos);

        }else if (viewNum == 7){
            half = layoutSquareSide / 3;
            infos.add(createChildrenForTop(viewWidth/2 - half/2 , viewHeight/2 - half/2*3,half));
            fastThreeChild(viewWidth, viewHeight/2 - half/2, half, infos);
            fastThreeChild(viewWidth, viewHeight/2 + half/2, half, infos);

        }else if (viewNum == 8){
            half = layoutSquareSide / 3;
            fastTwoChild(viewWidth, viewHeight/2 - half/2*3 ,half,infos );
            fastThreeChild(viewWidth, viewHeight/2 - half/2, half, infos);
            fastThreeChild(viewWidth, viewHeight/2 + half/2, half, infos);
        }else if (viewNum == 9){
            half = layoutSquareSide / 3;
            fastThreeChild(viewWidth, viewHeight/2 - half/2*3, half, infos);
            fastThreeChild(viewWidth, viewHeight/2 - half/2, half, infos);
            fastThreeChild(viewWidth, viewHeight/2 + half/2, half, infos);
        }

        // 添加子元素之间的空隙
        if (mSpacing > 0 && half > 0){
            addSpacing(mSpacing, half, infos);
        }

        return infos;
    }

    /**存储子元素测量数据初始化**/
    private void cleanMaskCache() {
        for (LayoutInfoGroup layoutInfoGroup : mCacheList) {
            layoutInfoGroup.leftTopPoint.set(0,0);
            layoutInfoGroup.rightBottomPoint.set(0,0);
            layoutInfoGroup.innerHeight = layoutInfoGroup.innerWidth = 0;
        }
        curCachePoint = 0;
    }
    /**对布局元素中的每个子元素添加空隙**/
    private void addSpacing(float dp, int side , ArrayList<LayoutInfoGroup> datas){
        int addPixel = (int) UIUtils.dip2px(context, dp);

        // 每个子元素的空隙不得超出子元素边长的三分之一
        addPixel = addPixel > side/3 ? side/3 : addPixel ;

        // 开始添加空隙
        for (LayoutInfoGroup data : datas) {
            data.innerHeight = data.innerWidth = data.innerWidth-2*addPixel;
            data.rightBottomPoint.set(data.rightBottomPoint.x - addPixel, data.rightBottomPoint.y - addPixel);
            data.leftTopPoint.set(data.leftTopPoint.x + addPixel, data.leftTopPoint.y + addPixel);
        }
    }



    /**
     * 通过左上点  创建子元素布局信息
     * @param left 右上点的x
     * @param top  右上点的y
     * @param side 需要画出子元素的边长
     */
    private  LayoutInfoGroup createChildrenForTop(int left, int top, int side){

        LayoutInfoGroup childLayout = mCacheList.get(curCachePoint);
        childLayout.innerHeight = childLayout.innerWidth = side;
        childLayout.leftTopPoint.set(left, top);
        childLayout.rightBottomPoint.set(left + side, top+side);
        curCachePoint++;
        return childLayout;
    }

    /**
     * 通过右下点 创建子元素布局信息
     * @param right 右下点的x
     * @param bottom 右下点的y
     * @param side 需要画出子元素的边长
     */
    private  LayoutInfoGroup createChildrenForBottom(int right, int bottom, int side){
        LayoutInfoGroup childLayout = mCacheList.get(curCachePoint);
        childLayout.innerHeight = childLayout.innerWidth = side;
        childLayout.leftTopPoint.set(right-side, bottom-side);
        childLayout.rightBottomPoint.set(right, bottom);
        curCachePoint++;
        return childLayout;
    }

    /**
     * 快速创建一个水平线两个子元素的场景, 如微信群组的, 2人, 3人, 4人, 5人, 8人
     * @param viewWidth 控件的宽度
     * @param positiveY 这一个水平线两张图片的左上角的y点
     * @param side  子元素的边长
     * @param mLayouts 布局集合
     */
    private void fastTwoChild(int viewWidth, int positiveY, int side, ArrayList<LayoutInfoGroup> mLayouts){
        // 第一个元素
        mLayouts.add(createChildrenForTop(viewWidth/2 - side , positiveY, side ));
        // 第二个元素
        mLayouts.add(createChildrenForTop(viewWidth/2  , positiveY, side ));
    }

    /**
     * 快速创建一个水平线三个子元素的场景, 如微信群组的, 5人, 6人, 7人, 8人, 9人时
     * @param viewWidth 控件的宽度
     * @param positiveY 这一个水平线两张图片的左上角的y点
     * @param side  子元素的边长
     * @param mLayouts 布局集合
     */
    private void fastThreeChild(int viewWidth, int positiveY, int side, ArrayList<LayoutInfoGroup> mLayouts){

        mLayouts.add(createChildrenForTop(viewWidth/2 - side/2*3 , positiveY, side ));
        mLayouts.add(createChildrenForTop(viewWidth/2 - side/2 , positiveY, side ));
        mLayouts.add(createChildrenForTop(viewWidth/2 + side/2 , positiveY, side ));
    }

    /**
     * 获取子元素的空隙
     * @return 返回单位dp
     */
    public float getSpacing() {
        return mSpacing;
    }

    /**
     * 设置子元素间的空隙
     * @param mSpacing 单位dp
     */
    public void setSpacing(float mSpacing) {
        this.mSpacing = mSpacing;
    }
}
