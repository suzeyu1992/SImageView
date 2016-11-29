package com.szysky.img.simageview;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sususu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SImageView iv= (SImageView) findViewById(R.id.iv_main);
        long l = System.nanoTime();
        iv.setImageResource(R.mipmap.icon_test);
        Log.i(TAG, "执行时间:"+ (System.nanoTime() - l));

    }
}
