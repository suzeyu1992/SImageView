package com.szysky.img.simageview;

import android.graphics.Matrix;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午9:11
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : QQ 群组布局是实现
 */

public class QQLayoutManager implements ILayoutManager {

    @Override
    public ArrayList<LayoutInfoGroup> calculate(int viewWidth, int viewHeight, int viewNum) {

        if (viewNum > 5){
            viewNum = 5;
        }else if (viewNum < 2){
            throw new UnsupportedOperationException("不支持操作异常");
        }

        // 控件正方形的边长
        int min = Math.min(viewHeight, viewWidth);

        // 获取角度集合
        float[] rotation = rotations[viewNum];

        // 获得对应缩放集合
        float[] size = sizes[viewNum-1];

        Matrix matrixJoin = new Matrix();
        // scale as join size
        matrixJoin.postScale(size[0], size[0]);

        ArrayList<LayoutInfoGroup> infos = new ArrayList<>();

        for (int i = 0; i < viewNum; i++) {
            LayoutInfoGroup layoutInfoGroup = new LayoutInfoGroup();





            layoutInfoGroup.centerPoint = offset(2,i,min, sizes[1]);

            infos.add(layoutInfoGroup);
        }


        return infos;
    }
    public static final float[][] sizes = { new float[] { 0.9f, 0.9f },
            new float[] { 0.5f, 0.65f }, new float[] { 0.45f, 0.8f },
            new float[] { 0.45f, 0.91f }, new float[] { 0.38f, 0.80f } };


    private static final float[][] rotations = { new float[] { 360.0f }, new float[] { 45.0f, 360.0f },
            new float[] { 120.0f, 0.0f, -120.0f }, new float[] { 90.0f, 180.0f, -90.0f, 0.0f },
            new float[] { 144.0f, 72.0f, 0.0f, -72.0f, -144.0f }, };


    /**
     *  根据个数选择具体实现的布局排放
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    public static float[] offset(int count, int index, float dimension, float[] size) {
        switch (count) {

            case 2:
                return offset2(index, dimension, size);
            case 3:
//                return offset3(index, dimension, size);
            case 4:
//                return offset4(index, dimension, size);
            case 5:
//                return offset5(index, dimension, size);
            default:
                break;
        }
        return new float[] { 0f, 0f };
    }

    /**
     * 2个头像
     *
     */
    private static float[] offset2(int index, float dimension, float[] size) {
        // 圆的直径
        float cd = (float) dimension * size[0];
        // 边距
        float s1 = cd * size[1];

        float x1 = 0;
        float y1 = 0;

        float x2 = s1;
        float y2 = s1;

        // Log.d(TAG, "x1:" + x1 + "/y1:" + y1);
        // Log.d(TAG, "x2:" + x2 + "/y2:" + y2);

        // 居中 X轴偏移量
        float xx1 = (dimension - cd - s1) / 2;
        switch (index) {
            case 0:
                return new float[] { x1 + xx1, y1 + xx1 };
            case 1:
                return new float[] { x2 + xx1, y2 + xx1 };
            default:
                break;
        }
        return new float[] { 0f, 0f };
    }
}
