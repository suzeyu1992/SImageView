package com.szysky.customize.siv.range;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;


import com.szysky.customize.siv.SImageView;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午9:03
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 控件多个图片时 布局排列的位置计算接口
 */

public interface ILayoutManager {

    /**
     * 布局measure排列计算方法, 具体规则由子类实现
     *
     * @param viewWidth 控件的宽
     * @param viewHeight 控件的高
     * @param viewNum   控件图片的数量
     * @return  返回一个信息集合, 提供 {@link com.szysky.customize.siv.effect.IDrawingStrategy#algorithm(Canvas, int, int, Bitmap, SImageView.ConfigInfo)}使用
     */
    ArrayList<LayoutInfoGroup> calculate(int viewWidth, int viewHeight, int viewNum);


    /**
     * 封装控件内部单个元素显示的布局信息
     */
    class LayoutInfoGroup implements Cloneable{
        /**
         * 组合头像时, 每个单独元素可分配的最大宽高
         */
        public int innerWidth;
        public int innerHeight;

        /**
         * 每个单独元素,左上点和右下点.   可规划区域
         */
        public Point leftTopPoint = new Point();
        public Point rightBottomPoint = new Point();

        @Override
        protected Object clone() throws CloneNotSupportedException {
            LayoutInfoGroup clone = (LayoutInfoGroup) super.clone();
            clone.leftTopPoint.set(leftTopPoint.x, leftTopPoint.y);
            clone.rightBottomPoint.set(rightBottomPoint.x, rightBottomPoint.y);

            return clone;
        }
    }
}
