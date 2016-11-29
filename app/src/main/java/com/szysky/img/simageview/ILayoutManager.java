package com.szysky.img.simageview;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午9:03
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 布局排列接口
 */

public interface ILayoutManager {

    ArrayList<LayoutInfoGroup> calculate(int viewWidth, int viewHeight, int viewNum);

    public static class LayoutInfoGroup{
        public int maxWidth;
        public int maxHeight;
        public float[] centerPoint;
        public Matrix matrix;
        public Point leftTopPoint;
        public Point rightBottomPoint;
    }
}
