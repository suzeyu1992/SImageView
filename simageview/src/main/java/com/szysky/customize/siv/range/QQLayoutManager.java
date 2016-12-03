package com.szysky.customize.siv.range;


import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-11-29  下午9:11
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : QQ 群组布局排列的具体实现
 */

public class QQLayoutManager implements ILayoutManager {

    /**
     *  针对图片的数量, 对应的图片处理的配置系数
     */
    public static final float[][] sizes = { new float[] { 0.9f, 0.9f },
            new float[] { 0.5f, 0.65f }, new float[] { 0.45f, 0.8f },
            new float[] { 0.45f, 0.91f }, new float[] { 0.38f, 0.80f } };


    @Override
    public ArrayList<LayoutInfoGroup> calculate(int viewWidth, int viewHeight, int viewNum) {

        if (viewNum > 5){
            viewNum = 5;
        }else if (viewNum < 1){
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


        // 获得对应缩放系数集合
        float[] size = sizes[viewNum-1];


        ArrayList<LayoutInfoGroup> infos = new ArrayList<>();

        // 计算各个子元素的位置
        for (int i = 0; i < viewNum; i++) {
            LayoutInfoGroup layoutInfoGroup = new LayoutInfoGroup();

            // 获得左上角坐标顶点
            int offsetX = (int) offset(viewNum, i, layoutSquareSide, size)[0] + layoutOffsetX;
            int offsetY = (int) offset(viewNum, i, layoutSquareSide, size)[1] + layoutOffsetY;
            layoutInfoGroup.leftTopPoint.set(offsetX, offsetY);

            layoutInfoGroup.innerWidth = layoutInfoGroup.innerHeight = (int) (layoutSquareSide * size[0]);
            infos.add(layoutInfoGroup);
        }
        return infos;
    }





    /**
     *  根据个数选择具体实现的布局排放
     *
     * @param index 下标
     * @param dimension 画布边长（正方形）
     * @param size size[0]缩放 size[1]边距
     *
     * @return 下标index的左上角X，Y轴坐标
     */
    public static float[] offset(int count, int index, float dimension, float[] size) {
        switch (count) {
            case 1:
                return offset1(index, dimension);
            case 2:
                return offset2(index, dimension, size);
            case 3:
                return offset3(index, dimension, size);
            case 4:
                return offset4(index, dimension, size);
            case 5:
                return offset5(index, dimension, size);
            default:
                break;
        }
        return new float[] { 0f, 0f };
    }

    private static float[] offset1(int index, float dimension) {
        // 圆的直径
        float cd = (float) dimension * 0.9f;
        float offset = (dimension - cd) / 2;
        return new float[] { offset, offset };
    }

    /**
     * 5个头像
     *
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    private static float[] offset5(int index, float dimension, float[] size) {
        // 圆的直径
        float cd =  dimension * size[0];
        // 边距
        float s1 = -cd * size[1];

        float x1 = 0;
        float y1 = s1;

        float x2 = (float) (s1 * Math.cos(19 * Math.PI / 180));
        float y2 = (float) (s1 * Math.sin(18 * Math.PI / 180));

        float x3 = (float) (s1 * Math.cos(54 * Math.PI / 180));
        float y3 = (float) (-s1 * Math.sin(54 * Math.PI / 180));

        float x4 = (float) (-s1 * Math.cos(54 * Math.PI / 180));
        float y4 = (float) (-s1 * Math.sin(54 * Math.PI / 180));

        float x5 = (float) (-s1 * Math.cos(19 * Math.PI / 180));
        float y5 = (float) (s1 * Math.sin(18 * Math.PI / 180));



        // 居中 Y轴偏移量
        float xx1 = (dimension - cd - y3 - s1) / 2;
        // 居中 X轴偏移量
        float xxc1 = (dimension - cd) / 2;

        switch (index) {
            case 0:
                // return new float[] { s1 + xxc1, xx1 };
                return new float[] { x1 + xxc1, y1 + xx1 };
            case 1:
                return new float[] { x2 + xxc1, y2 + xx1 };
            case 2:
                return new float[] { x3 + xxc1, y3 + xx1 };
            case 3:
                return new float[] { x4 + xxc1, y4 + xx1 };
            case 4:
                return new float[] { x5 + xxc1, y5 + xx1 };
            default:
                return new float[] { 0f, 0f };
        }
    }

    /**
     * 4个头像
     *
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    private static float[] offset4(int index, float dimension, float[] size) {
        // 圆的直径
        float cd = (float) dimension * size[0];
        // 边距
        float s1 = cd * size[1];

        float x1 = 0;
        float y1 = 0;

        float x2 = s1;
        float y2 = y1;

        float x3 = s1;
        float y3 = s1;

        float x4 = x1;
        float y4 = y3;



        // 居中 X轴偏移量
        float xx1 = (dimension - cd - s1) / 2;
        switch (index) {
            case 0:
                return new float[] { x1 + xx1, y1 + xx1 };
            case 1:
                return new float[] { x2 + xx1, y2 + xx1 };
            case 2:
                return new float[] { x3 + xx1, y3 + xx1 };
            case 3:
                return new float[] { x4 + xx1, y4 + xx1 };
            default:
                return new float[] { 0f, 0f };
        }
    }

    /**
     * 3个头像
     *
     * @param index
     *            下标
     * @param dimension
     *            画布边长（正方形）
     * @param size
     *            size[0]缩放 size[1]边距
     * @return 下标index X，Y轴坐标
     */
    private static float[] offset3(int index, float dimension, float[] size) {
        // 圆的直径
        float cd = (float) dimension * size[0];
        // 边距
        float s1 = cd * size[1];
        // 第二个圆的 Y坐标
        float y2 = s1 * (3 / 2);
        // 第二个圆的 X坐标
        float x2 = s1 - y2 / 1.73205f;
        // 第三个圆的 X坐标
        float x3 = s1 * 2 - x2;
        // 居中 Y轴偏移量
        float xx1 = (dimension - cd - y2) / 2;
        // 居中 X轴偏移量
        float xxc1 = (dimension - cd) / 2 - s1;
        // xx1 = xxc1 = 0;
        switch (index) {
            case 0:
                return new float[] { s1 + xxc1, xx1 };
            case 1:
                return new float[] { x2 + xxc1, y2 + xx1 };
            case 2:
                return new float[] { x3 + xxc1, y2 + xx1 };
            default:
                return new float[] { 0f, 0f };
        }

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


        // 从控件左边开始计算 X轴偏移量
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
