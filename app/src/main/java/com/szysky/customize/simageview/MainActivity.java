package com.szysky.customize.simageview;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.szysky.customize.simageview.effect.ConcreteQQCircleStrategy;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sususu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        SImageView iv= (SImageView) findViewById(R.id.iv_main);
        ConcreteQQCircleStrategy concreteQQCircleStrategy = new ConcreteQQCircleStrategy();
        concreteQQCircleStrategy.setSpacing(1f);
        iv.setDrawStrategy(concreteQQCircleStrategy);



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
        bitmaps.add(bitmap);
        iv.setImages(bitmaps);

    }
}
