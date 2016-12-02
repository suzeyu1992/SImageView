package com.szysky.customize.simageview.range;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-12-01  下午11:14
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :  微信布局接口
 */

public class WeCharLayoutManager implements ILayoutManager {


    private final ArrayList<LayoutInfoGroup> mCacheList;
    private int curCachePoint ;

    public WeCharLayoutManager(){
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

        if (viewNum > 5){
            viewNum = 5;
        }else if (viewNum < 2){
            throw new UnsupportedOperationException("不支持操作异常");
        }

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
        }else{
            layoutSquareSide =  viewWidth;
        }


        ArrayList<LayoutInfoGroup> infos = new ArrayList<>();
        int half = layoutSquareSide / 2;

        if (viewNum == 2){

            // 第一个元素
            infos.add(createChildrenForTop(viewWidth/2 - half , (viewHeight-half)/2, half ));
            // 第二个元素
            infos.add(createChildrenForTop(viewWidth/2  , (viewHeight-half)/2, half ));
        }else if (viewNum == 3){

            LayoutInfoGroup layoutInfoGroup1 = new LayoutInfoGroup();
            layoutInfoGroup1.innerHeight = layoutInfoGroup1.innerWidth = (int) (layoutSquareSide/2*0.9f);
            layoutInfoGroup1.leftTopPoint = new Point(viewWidth/2 - half/2 , viewHeight/2 - half);
            infos.add(layoutInfoGroup1);

            // 第一个元素
            LayoutInfoGroup layoutInfoGroup2 = new LayoutInfoGroup();
            layoutInfoGroup2.innerHeight = layoutInfoGroup2.innerWidth = (int) (layoutSquareSide/2*0.9f);
            layoutInfoGroup2.leftTopPoint = new Point(viewWidth/2 - half , (viewHeight-half)/2 + half/2);
            infos.add(layoutInfoGroup2);

            // 第二个元素
            LayoutInfoGroup layoutInfoGroup3 = new LayoutInfoGroup();
            layoutInfoGroup3.innerHeight = layoutInfoGroup3.innerWidth = (int) (layoutSquareSide/2*0.9f);
            layoutInfoGroup3.leftTopPoint = new Point(viewWidth/2 , (viewHeight-half)/2 + half/2);
            infos.add(layoutInfoGroup3);
        }else if (viewNum == 4){

            // 第一个元素
            LayoutInfoGroup layoutInfoGroup1 = new LayoutInfoGroup();
            layoutInfoGroup1.innerHeight = layoutInfoGroup1.innerWidth = layoutSquareSide/2;
            layoutInfoGroup1.leftTopPoint = new Point(viewWidth/2 - half , (viewHeight-half)/2 + half/2);

        }


        return infos;
    }


    private void cleanMaskCache() {
        for (LayoutInfoGroup layoutInfoGroup : mCacheList) {
            layoutInfoGroup.leftTopPoint.set(0,0);
            layoutInfoGroup.rightBottomPoint.set(0,0);
            layoutInfoGroup.innerHeight = layoutInfoGroup.innerWidth = 0;
        }
        curCachePoint = 0;
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

//    private void fastMaxTwoChild(int viewWidth, int positiveY, int side, ArrayList<LayoutInfoGroup> mLayouts){
//
//    }
}
