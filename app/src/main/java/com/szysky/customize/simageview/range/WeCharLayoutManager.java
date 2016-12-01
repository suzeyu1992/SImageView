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
    @Override
    public ArrayList<LayoutInfoGroup> calculate(int viewWidth, int viewHeight, int viewNum) {

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

        if (viewNum == 2){
            int half = layoutSquareSide / 2;


            // 第一个元素
            LayoutInfoGroup layoutInfoGroup = new LayoutInfoGroup();
            layoutInfoGroup.innerHeight = layoutInfoGroup.innerWidth = layoutSquareSide/2;
            layoutInfoGroup.leftTopPoint = new Point(viewWidth/4 - half/2, (viewHeight-half)/2);
            infos.add(layoutInfoGroup);

            // 第二个元素
            LayoutInfoGroup layoutInfoGroup2 = new LayoutInfoGroup();
            layoutInfoGroup2.innerHeight = layoutInfoGroup2.innerWidth = layoutSquareSide/2;
            layoutInfoGroup2.leftTopPoint = new Point(viewWidth/4*3 - half/2, (viewHeight-half)/2);
            infos.add(layoutInfoGroup2);
        }


        return infos;
    }
}
