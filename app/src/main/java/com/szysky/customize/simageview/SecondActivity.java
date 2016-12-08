package com.szysky.customize.simageview;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.imgprocess.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author :  suzeyu
 * Time   :  2016-12-01  下午7:53
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 展示scaleType缩放类型的界面
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private SImageView mSImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //mSImageView = (SImageView) findViewById(R.id.siv_main);

//        findViewById(R.id.btn_crop).setOnClickListener(this);
//        findViewById(R.id.btn_fix_xy).setOnClickListener(this);
//        findViewById(R.id.btn_inside).setOnClickListener(this);

        // 测试网络
        SImageView sImageViewForHttp = (SImageView) findViewById(R.id.siv_url);

        List<String> urls = new ArrayList<>();

        for (int i = 0; i < 1; i++) {
//            urls.add("http://szysky.com/2016/12/05/%E5%B9%B4%E7%BB%88%E7%A6%8F%E5%88%A9-SImageView%E5%AE%9E%E7%94%A8%E7%9A%84%E5%9B%BE%E7%89%87%E6%8E%A7%E4%BB%B6/sample_2.gif");
            urls.add("http://img9.dzdwl.com/img/11543935W-1.jpg");
            urls.add("http://img02.tooopen.com/images/20160408/tooopen_sy_158723161481.jpg");
//            urls.add("http://img02.tooopen.com/images/20160404/tooopen_sy_158262392146.jpg");
            urls.add("http://img02.tooopen.com/images/20160318/tooopen_sy_156339294124.jpg");
//            urls.add("http://img06.tooopen.com/images/20160823/tooopen_sy_176393394325.jpg");
//            urls.add("http://img06.tooopen.com/images/20160821/tooopen_sy_176144979595.jpg");
//            urls.add("http://img06.tooopen.com/images/20160723/tooopen_sy_171462742667.jpg");
//            urls.add("http://img05.tooopen.com/images/20150417/tooopen_sy_119014046478.jpg");
//            urls.add("http://img02.tooopen.com/images/20150318/tooopen_sy_82853534894.jpg");
//            urls.add("http://img05.tooopen.com/images/20150204/tooopen_sy_80359399983.jpg");
//            urls.add("http://img01.tooopen.com/Downs/images/2010/4/9/sy_20100409135808693051.jpg");
//            urls.add("http://pics.sc.chinaz.com/files/pic/pic9/201410/apic7065.jpg");
        }

        ImageLoader.getInstance(getApplicationContext()).setMulPicture(urls, sImageViewForHttp, 0,0);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_inside:
                mSImageView.setScaleType(SImageView.SCALE_TYPE_CENTER_INSIDE);
                mSImageView.invalidate();
                break;

            case R.id.btn_crop:
                mSImageView.setScaleType(SImageView.SCALE_TYPE_CENTER_CROP);
                mSImageView.invalidate();
                break;

            case R.id.btn_fix_xy:
                mSImageView.setScaleType(SImageView.SCALE_TYPE_FIX_XY);
                mSImageView.invalidate();

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
