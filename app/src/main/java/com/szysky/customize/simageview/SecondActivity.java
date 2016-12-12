package com.szysky.customize.simageview;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
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
    private List<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mSImageView = (SImageView) findViewById(R.id.siv_main);

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
