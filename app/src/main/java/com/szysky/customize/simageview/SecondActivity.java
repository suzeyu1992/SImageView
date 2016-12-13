package com.szysky.customize.simageview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.szysky.customize.siv.ImageLoader;
import com.szysky.customize.siv.SImageView;

import java.util.List;

/**
 * Author :  suzeyu
 * Time   :  2016-12-01  下午7:53
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 展示scaleType缩放类型的界面
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private SImageView mSImageView;
    private List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSImageView = (SImageView) findViewById(R.id.siv_main);

//        ImageLoader.getInstance(getApplicationContext()).setLoadingResId(R.mipmap.ic_launcher);
//        ImageLoader.getInstance(getApplicationContext()).setLoadErrResId(R.mipmap.icon_test);

        mSImageView.setLoadingResID(R.mipmap.ic_4);
        //mSImageView.setErrPicResID(R.mipmap.ic_1);


        mSImageView.setImageUrls("http://img02.tooopen.com/images/20160318/tooopen_sy_1563392941241.jpg");

        findViewById(R.id.btn_crop).setOnClickListener(this);
        findViewById(R.id.btn_fix_xy).setOnClickListener(this);
        findViewById(R.id.btn_inside).setOnClickListener(this);


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
