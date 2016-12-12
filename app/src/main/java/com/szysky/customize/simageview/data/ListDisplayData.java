package com.szysky.customize.simageview.data;

import java.util.ArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-12-12  下午5:47
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 列表展示的数据
 */

public class ListDisplayData {

    public  String[] url;
    public  String name;

    public ListDisplayData( String name, String... url){
        this.url = url;
        this.name = name;
    }

    public static ArrayList<ListDisplayData> structureData(){
        ArrayList<ListDisplayData> datas = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            ListDisplayData data_0 = new ListDisplayData("sex","http://img02.tooopen.com/images/20160404/tooopen_sy_158262392146.jpg" );
            ListDisplayData data_1 = new ListDisplayData("沙皮狗","http://i1.dpfile.com/groups/grouppic/2009-06-10/sjw211_4063222_1383101_l.jpg" );
            ListDisplayData data_2 = new ListDisplayData("小萌狗", "http://www.vstou.com/upload/image/312/201603/1457831503284724.jpg" );
            ListDisplayData data_mul_1 = new ListDisplayData("狗狗群组 - 2张", "http://www.vstou.com/upload/image/312/201603/1457831503284724.jpg"
                                                                ,"http://i1.dpfile.com/groups/grouppic/2009-06-10/sjw211_4063222_1383101_l.jpg");

            ListDisplayData data_3 = new ListDisplayData("小丸子", "http://www.tshseo.com/uploads/allimg/141024/2234236415-0.jpg");
            ListDisplayData data_4 = new ListDisplayData("美女", "http://www.duoziwang.com/uploads/c160225/14563LE5630-22626.jpg" );
            ListDisplayData data_5 = new ListDisplayData("小黑子", "http://www.3dmgame.com/uploads/allimg/141224/270_141224171322_1.jpg");
            ListDisplayData data_6 = new ListDisplayData("小赤司", "https://images.plurk.com/f76339fbc1eb3a1381b811b2879ac8e3.jpg");
            ListDisplayData data_7 = new ListDisplayData("小青峰", "http://dbimg.orzyouxi.com/Webatt/201508/20150807/1438960292362734.jpg");
            ListDisplayData data_8 = new ListDisplayData( "小火神", "http://img170.poco.cn/mypoco/myphoto/20120430/12/64575493201204301209181258063444825_001.jpg");
            ListDisplayData data_9 = new ListDisplayData("小紫", "http://pic2.52pk.com/files/120613/1283574_171642_1_lit.jpg");
            ListDisplayData data_10 = new ListDisplayData("小黄濑", "http://pic.hanhande.com/files/130708/1283574_115242_1_lit.jpg");
            ListDisplayData data_mul_2 = new ListDisplayData("黑子群组 - 7张", "http://www.3dmgame.com/uploads/allimg/141224/270_141224171322_1.jpg",
                    "https://images.plurk.com/f76339fbc1eb3a1381b811b2879ac8e3.jpg",
                    "http://dbimg.orzyouxi.com/Webatt/201508/20150807/1438960292362734.jpg",
                    "http://img170.poco.cn/mypoco/myphoto/20120430/12/64575493201204301209181258063444825_001.jpg",
                    "http://pic2.52pk.com/files/120613/1283574_171642_1_lit.jpg",
                    "http://pic.hanhande.com/files/130708/1283574_115242_1_lit.jpg",
                    "http://pic.pimg.tw/mutsumi326/1341722635-1743631151.jpg");






            ListDisplayData data_11 = new ListDisplayData("小桃井", "http://pic.pimg.tw/mutsumi326/1341722635-1743631151.jpg");
            ListDisplayData data_12 = new ListDisplayData("王祖贤", "http://img3.cache.netease.com/ent/2009/4/17/20090417104402666a4.jpg");
            ListDisplayData data_13 = new ListDisplayData("张敏", "http://www.people.com.cn/mediafile/pic/20150710/76/5290773661176280932.jpg");
            ListDisplayData data_14 = new ListDisplayData("邱淑贞", "http://photocdn.sohu.com/20100201/Img269969338.jpg");
            ListDisplayData data_15 = new ListDisplayData("刘涛", "http://vignette2.wikia.nocookie.net/chunwan/images/0/00/47-150FG52033-50.jpg/revision/latest?cb=20160126210715&path-prefix=zh");
            ListDisplayData data_16 = new ListDisplayData("朱茵", "http://img1.gtimg.com/astro/pics/hv1/28/77/794/51649513.jpg");
            ListDisplayData data_mul_3 = new ListDisplayData("女神群组 - 5张",
                    "http://img3.cache.netease.com/ent/2009/4/17/20090417104402666a4.jpg",
                    "http://www.people.com.cn/mediafile/pic/20150710/76/5290773661176280932.jpg",
                    "http://img1.gtimg.com/astro/pics/hv1/28/77/794/51649513.jpg",
                    "http://photocdn.sohu.com/20100201/Img269969338.jpg",
                    "http://vignette2.wikia.nocookie.net/chunwan/images/0/00/47-150FG52033-50.jpg/revision/latest?cb=20160126210715&path-prefix=zh");

            ListDisplayData data_mul_5 = new ListDisplayData("女神群组 - 4张",
                    "http://img3.cache.netease.com/ent/2009/4/17/20090417104402666a4.jpg",
                    "http://www.people.com.cn/mediafile/pic/20150710/76/5290773661176280932.jpg",
                    "http://img1.gtimg.com/astro/pics/hv1/28/77/794/51649513.jpg",
                    "http://photocdn.sohu.com/20100201/Img269969338.jpg");
            ListDisplayData data_mul_6 = new ListDisplayData("女神群组 - 3张",
                    "http://img3.cache.netease.com/ent/2009/4/17/20090417104402666a4.jpg",
                    "http://www.people.com.cn/mediafile/pic/20150710/76/5290773661176280932.jpg",
                    "http://photocdn.sohu.com/20100201/Img269969338.jpg");



            ListDisplayData data_mul_4 = new ListDisplayData("美景群组 - 8张",
                    "http://img02.tooopen.com/images/20160408/tooopen_sy_158723161481.jpg",
                    "http://img02.tooopen.com/images/20160404/tooopen_sy_158262392146.jpg",
                    "http://img02.tooopen.com/images/20160318/tooopen_sy_156339294124.jpg",
                    "http://img06.tooopen.com/images/20160823/tooopen_sy_176393394325.jpg",
                    "http://img06.tooopen.com/images/20160821/tooopen_sy_176144979595.jpg",
                    "http://img06.tooopen.com/images/20160723/tooopen_sy_171462742667.jpg",
                    "http://img05.tooopen.com/images/20150417/tooopen_sy_119014046478.jpg",
                    "http://img02.tooopen.com/images/20150318/tooopen_sy_82853534894.jpg");




            datas.add(data_1);
            datas.add(data_2);
            datas.add(data_3);
            datas.add(data_mul_1);
            datas.add(data_4);
            datas.add(data_mul_4);
            datas.add(data_5);
            datas.add(data_6);
            datas.add(data_7);
            datas.add(data_8);
            datas.add(data_9);
            datas.add(data_10);
            datas.add(data_11);
            datas.add(data_mul_2);
            datas.add(data_mul_4);
            datas.add(data_mul_5);
            datas.add(data_mul_6);
            datas.add(data_mul_3);
            datas.add(data_mul_1);
            datas.add(data_12);
            datas.add(data_13);
            datas.add(data_14);
            datas.add(data_15);
            datas.add(data_16);


        }

        return datas;
    }
}
