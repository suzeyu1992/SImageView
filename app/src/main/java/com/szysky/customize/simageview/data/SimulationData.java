package com.szysky.customize.simageview.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.szysky.customize.simageview.R;
import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.effect.IDrawingStrategy;
import com.szysky.customize.siv.range.ILayoutManager;
import com.szysky.customize.siv.range.WeChatLayoutManager;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-12-04  下午2:57
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 模拟图片空间的数据
 */

public class SimulationData {

    public ArrayList<Bitmap> readyBmp = new ArrayList<>();  // 需要显示的图片集合
    public float borderWidth ;                              // 描边宽度
    public int borderColor ;                                // 描边颜色
    @SImageView.ShapeDisplay public int displayType ;                                // 显示类型
    public boolean mCloseNormalOnePicLoad = false;                  // 默认单个图片的处理策略开关
    public ILayoutManager mMeasureManager;                  // 测量布局管理器
    public IDrawingStrategy mDrawingStrategy;               // 绘图策略

    public SimulationData(Context context){
        readyBmp.add(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_2));
    }

    static {

    }

    private static  Bitmap ic_1 ;
    private static  Bitmap ic_2 ;
    private static  Bitmap ic_3 ;
    private static  Bitmap ic_4 ;
    private static  Bitmap ic_5 ;
    private static  Bitmap ic_6 ;
    private static  Bitmap ic_7 ;
    private static  Bitmap ic_8 ;
    private static  Bitmap ic_9 ;

    public SimulationData(Context context, int loadNumPic){

        if (null == ic_1){
            ic_1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_1);
            ic_2 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_2);
            ic_3 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_3);
            ic_4 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_4);
            ic_5 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_5);
            ic_6 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_6);
            ic_7 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_7);
            ic_8 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_8);
            ic_9 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_9);
        }


        switch (loadNumPic){
            case 1:
                readyBmp.add(ic_1);
                break;
            case 2:
                readyBmp.add(ic_1);
                readyBmp.add(ic_2);

                break;
            case 3:
                readyBmp.add(ic_1);
                readyBmp.add(ic_2);
                readyBmp.add(ic_3);

                break;
            case 4:
                readyBmp.add(ic_1);
                readyBmp.add(ic_2);
                readyBmp.add(ic_3);
                readyBmp.add(ic_4);

                break;
            case 5:
                readyBmp.add(ic_1);
                readyBmp.add(ic_2);
                readyBmp.add(ic_3);
                readyBmp.add(ic_4);
                readyBmp.add(ic_5);
                break;
            case 6:
                readyBmp.add(ic_1);
                readyBmp.add(ic_2);
                readyBmp.add(ic_3);
                readyBmp.add(ic_4);
                readyBmp.add(ic_5);
                readyBmp.add(ic_6);

                break;
            case 7:
                readyBmp.add(ic_1);
                readyBmp.add(ic_2);
                readyBmp.add(ic_3);
                readyBmp.add(ic_4);
                readyBmp.add(ic_5);
                readyBmp.add(ic_6);
                readyBmp.add(ic_7);

                break;
            case 8:
                readyBmp.add(ic_1);
                readyBmp.add(ic_2);
                readyBmp.add(ic_3);
                readyBmp.add(ic_4);
                readyBmp.add(ic_5);
                readyBmp.add(ic_6);
                readyBmp.add(ic_7);
                readyBmp.add(ic_8);

                break;
            case 9:
                readyBmp.add(ic_1);
                readyBmp.add(ic_2);
                readyBmp.add(ic_3);
                readyBmp.add(ic_4);
                readyBmp.add(ic_5);
                readyBmp.add(ic_6);
                readyBmp.add(ic_7);
                readyBmp.add(ic_8);
                readyBmp.add(ic_9);

                break;


        }
    }

    /**
     *     提供给展示页, RecyclerView使用的数据集合初始化
     */
    public SimulationData( Context context, ArrayList<SimulationData> datas){

        for (int i = 0; i < 10; i++) {
            // 先初始化10个单张的数据对象
            datas.add(new SimulationData(context,1));
        }


        // 处理默认状态下, 单张图片的 display 显示
        datas.get(0).displayType = SImageView.TYPE_CIRCLE;
        datas.get(1).displayType = SImageView.TYPE_OVAL;
        datas.get(2).displayType = SImageView.TYPE_RECT;
        datas.get(3).displayType = SImageView.TYPE_ROUND_RECT;
        datas.get(4).displayType = SImageView.TYPE_FIVE_POINTED_STAR;

        // 对上面五种显示多设置描边属性
        SimulationData siv_5 = datas.get(5);
        SimulationData siv_6 = datas.get(6);
        SimulationData siv_7 = datas.get(7);
        SimulationData siv_8 = datas.get(8);
        SimulationData siv_9 = datas.get(9);

        siv_5.displayType = SImageView.TYPE_CIRCLE;
        siv_5.borderColor = Color.BLACK;
        siv_5.borderWidth = 1;              // 1dp

        siv_6.displayType = SImageView.TYPE_OVAL;
        siv_6.borderColor = Color.BLACK;
        siv_6.borderWidth = 1;              // 1dp

        siv_7.displayType = SImageView.TYPE_RECT;
        siv_7.borderColor = Color.BLACK;
        siv_7.borderWidth = 1;              // 1dp

        siv_8.displayType = SImageView.TYPE_ROUND_RECT;
        siv_8.borderColor = Color.BLACK;
        siv_8.borderWidth = 1;              // 1dp

        siv_9.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_9.borderColor = Color.BLACK;
        siv_9.borderWidth = 1;              // 1dp

        /***********************************************************************************************/

        for (int i = 0; i < 5; i++) {
            // 先初始化5个qq群组的数据对象
            datas.add(new SimulationData(context,i+1));
        }
        // 添加qq群组的5种效果
        SimulationData siv_qq_1 = datas.get(10);
        SimulationData siv_qq_2 = datas.get(11);
        SimulationData siv_qq_3 = datas.get(12);
        SimulationData siv_qq_4 = datas.get(13);
        SimulationData siv_qq_5 = datas.get(14);
        siv_qq_1.mCloseNormalOnePicLoad = true;  // 关闭单张图片处理策略, 强制一张图片是用IDrawingStrategy的绘图策略

        /***********************************************************************************************/



        for (int i = 0; i < 5; i++) {
            // 先初始化5个qq群组2张图片的数据对象
            datas.add(new SimulationData(context,2));
        }
        // qq群组添加描边
        SimulationData siv_qq_6 = datas.get(15);
        SimulationData siv_qq_7 = datas.get(16);
        SimulationData siv_qq_8 = datas.get(17);
        SimulationData siv_qq_9 = datas.get(18);
        SimulationData siv_qq_10 = datas.get(19);

        siv_qq_6.displayType = SImageView.TYPE_CIRCLE;
        siv_qq_6.borderColor = Color.BLACK;
        siv_qq_6.borderWidth = 1;              // 1dp

        siv_qq_7.displayType = SImageView.TYPE_OVAL;
        siv_qq_7.borderColor = Color.BLACK;
        siv_qq_7.borderWidth = 1;              // 1dp

        siv_qq_8.displayType = SImageView.TYPE_RECT;
        siv_qq_8.borderColor = Color.BLACK;
        siv_qq_8.borderWidth = 1;              // 1dp

        siv_qq_9.displayType = SImageView.TYPE_ROUND_RECT;
        siv_qq_9.borderColor = Color.BLACK;
        siv_qq_9.borderWidth = 1;              // 1dp

        siv_qq_10.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_qq_10.borderColor = Color.BLACK;
        siv_qq_10.borderWidth = 1;              // 1dp


        /***********************************************************************************************/

        for (int i = 0; i < 5; i++) {
            // 先初始化5个qq群组图片的数据对象  设置椭圆显示
            datas.add(new SimulationData(context,i+1));
        }
        // qq群组添加描边
        SimulationData siv_qq_11 = datas.get(20);
        SimulationData siv_qq_12 = datas.get(21);
        SimulationData siv_qq_13 = datas.get(22);
        SimulationData siv_qq_14 = datas.get(23);
        SimulationData siv_qq_15 = datas.get(24);

        siv_qq_11.displayType = SImageView.TYPE_ROUND_RECT;
        siv_qq_11.borderColor = Color.DKGRAY;
        siv_qq_11.borderWidth = 3;              // 1dp

        siv_qq_12.displayType = SImageView.TYPE_ROUND_RECT;
        siv_qq_12.borderColor = Color.DKGRAY;
        siv_qq_12.borderWidth = 3;              // 1dp

        siv_qq_13.displayType = SImageView.TYPE_ROUND_RECT;
        siv_qq_13.borderColor = Color.DKGRAY;
        siv_qq_13.borderWidth = 3;              // 1dp

        siv_qq_14.displayType = SImageView.TYPE_ROUND_RECT;
        siv_qq_14.borderColor = Color.DKGRAY;
        siv_qq_14.borderWidth = 3;              // 1dp

        siv_qq_15.displayType = SImageView.TYPE_ROUND_RECT;
        siv_qq_15.borderColor = Color.DKGRAY;
        siv_qq_15.borderWidth = 3;              // 1dp



        /***********************************************************************************************/

        for (int i = 0; i < 9; i++) {
            // 先初始化9个微信群组数据对象
            datas.add(new SimulationData(context,i+1));
        }
        SimulationData siv_wechat_1 = datas.get(25);
        SimulationData siv_wechat_2 = datas.get(26);
        SimulationData siv_wechat_3 = datas.get(27);
        SimulationData siv_wechat_4 = datas.get(28);
        SimulationData siv_wechat_5 = datas.get(29);
        SimulationData siv_wechat_6 = datas.get(30);
        SimulationData siv_wechat_7 = datas.get(31);
        SimulationData siv_wechat_8 = datas.get(32);
        SimulationData siv_wechat_9 = datas.get(33);

        siv_wechat_1.displayType = SImageView.TYPE_RECT;
        siv_wechat_1.mMeasureManager = new WeChatLayoutManager(context);

        siv_wechat_2.displayType = SImageView.TYPE_RECT;
        siv_wechat_2.mMeasureManager = new WeChatLayoutManager(context);

        siv_wechat_3.displayType = SImageView.TYPE_RECT;
        siv_wechat_3.mMeasureManager = new WeChatLayoutManager(context);

        siv_wechat_4.displayType = SImageView.TYPE_RECT;
        siv_wechat_4.mMeasureManager = new WeChatLayoutManager(context);

        siv_wechat_5.displayType = SImageView.TYPE_RECT;
        siv_wechat_5.mMeasureManager = new WeChatLayoutManager(context);

        siv_wechat_6.displayType = SImageView.TYPE_RECT;
        siv_wechat_6.mMeasureManager = new WeChatLayoutManager(context);

        siv_wechat_7.displayType = SImageView.TYPE_RECT;
        siv_wechat_7.mMeasureManager = new WeChatLayoutManager(context);

        siv_wechat_8.displayType = SImageView.TYPE_RECT;
        siv_wechat_8.mMeasureManager = new WeChatLayoutManager(context);

        siv_wechat_9.displayType = SImageView.TYPE_RECT;
        siv_wechat_9.mMeasureManager = new WeChatLayoutManager(context);


        // 随便放一个凑数的
        SimulationData siv_temp_1 = new SimulationData(context, 9);
        siv_temp_1.borderColor = 2;
        siv_temp_1.displayType = SImageView.TYPE_ROUND_RECT;
        siv_temp_1.mMeasureManager = new WeChatLayoutManager(context);
        datas.add(siv_temp_1);


        /***********************************************************************************************/

        for (int i = 0; i < 9; i++) {
            // 先初始化9个微信群组数据对象  样式为五角星
            datas.add(new SimulationData(context,i+1));
        }
        SimulationData siv_wechat_11 = datas.get(35);
        SimulationData siv_wechat_12 = datas.get(36);
        SimulationData siv_wechat_13 = datas.get(37);
        SimulationData siv_wechat_14 = datas.get(38);
        SimulationData siv_wechat_15 = datas.get(39);
        SimulationData siv_wechat_16 = datas.get(40);
        SimulationData siv_wechat_17 = datas.get(41);
        SimulationData siv_wechat_18 = datas.get(42);
        SimulationData siv_wechat_19 = datas.get(43);

//        siv_wechat_11.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_11.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_11.borderColor = Color.BLACK;
        siv_wechat_11.borderWidth = 1;

//        siv_wechat_12.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_12.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_12.borderColor = Color.BLACK;
        siv_wechat_12.borderWidth = 1;

//        siv_wechat_13.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_13.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_13.borderColor = Color.BLACK;
        siv_wechat_13.borderWidth = 1;

//        siv_wechat_14.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_14.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_14.borderColor = Color.BLACK;
        siv_wechat_14.borderWidth = 1;

//        siv_wechat_15.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_15.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_15.borderColor = Color.BLACK;
        siv_wechat_15.borderWidth = 1;

//        siv_wechat_16.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_16.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_16.borderColor = Color.BLACK;
        siv_wechat_16.borderWidth = 1;

//        siv_wechat_17.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_17.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_17.borderColor = Color.BLACK;
        siv_wechat_17.borderWidth = 1;

//        siv_wechat_18.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_18.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_18.borderColor = Color.BLACK;
        siv_wechat_18.borderWidth = 1;

//        siv_wechat_19.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_wechat_19.mMeasureManager = new WeChatLayoutManager(context);
        siv_wechat_19.borderColor = Color.BLACK;
        siv_wechat_19.borderWidth = 1;

        // 再来一个凑数的
        SimulationData siv_temp_2 = new SimulationData(context, 1);
        siv_temp_2.borderColor = Color.BLACK;
        siv_temp_2.displayType = SImageView.TYPE_FIVE_POINTED_STAR;
        siv_temp_2.borderWidth = 5000;
        siv_temp_2.mMeasureManager = new WeChatLayoutManager(context);
    }
}
