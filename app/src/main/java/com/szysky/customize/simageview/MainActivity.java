package com.szysky.customize.simageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.szysky.customize.siv.SImageView;

import java.util.ArrayList;

import static com.szysky.customize.simageview.R.styleable.SImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sususu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



         com.szysky.customize.siv.SImageView iv= (com.szysky.customize.siv.SImageView) findViewById(R.id.iv_main);




        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_test);
//        bitmaps.add(bitmap);
//        bitmaps.add(bitmap);
//        bitmaps.add(bitmap);
//        bitmaps.add(bitmap);
//        bitmaps.add(bitmap);
//        bitmaps.add(bitmap);
//        bitmaps.add(bitmap);
//        bitmaps.add(bitmap);
       // bitmaps.add(bitmap);
//        iv.setBorderWidth(10);
//        iv.setDisplayShape(SImageView.TYPE_CIRCLE);
      //  iv.setCloseNormalOnePicLoad(true);
       // iv.setBorderColor(getResources().getColor(R.color.testColor));

        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(5000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                               // iv.setIdRes(R.mipmap.icon_test);

                    }
                });
            }
        }).start();

    }
}
