package com.szysky.customize.simageview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


import com.szysky.customize.simageview.data.SimulationData;
import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.effect.ConcreteQQCircleStrategy;
import com.szysky.customize.siv.range.QQLayoutManager;
import com.szysky.customize.siv.range.WeChatLayoutManager;

import java.util.ArrayList;

import static com.szysky.customize.simageview.R.styleable.SImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "sususu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setSubtitle("can do things");


        RecyclerView rv_main = (RecyclerView) findViewById(R.id.rv_main);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        DividerGridItemDecoration itemDivider = new DividerGridItemDecoration(getApplicationContext());
        rv_main.addItemDecoration(itemDivider);
        rv_main.setLayoutManager(gridLayoutManager);

        ArrayList<SimulationData> datas = new ArrayList<>();
        new SimulationData(getApplicationContext(), datas);





        rv_main.setAdapter(new MyAdapter(getApplicationContext(), datas));



        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_test);




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_pager, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_open:
                // 跳转第二个展示页面
                startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder>{

        private final Context context;
        private final ArrayList<SimulationData> datas;

        MyAdapter (Context context, ArrayList<SimulationData> datas){
            this.context = context;
            this.datas = datas;

        }
        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = View.inflate(context, R.layout.item_list, null);

            return new MyHolder(inflate);
        }

        @Override
        public void onBindViewHolder(MyHolder holder, int position) {

            // 获取控件要展示的信息
            SimulationData data = datas.get(position);
            SImageView sImageView = holder.siv_display;
//            sImageView.setScaleType(com.szysky.customize.siv.SImageView.SCALE_TYPE_FIX_XY);
//            sImageView.setDisplayShape(com.szysky.customize.siv.SImageView.TYPE_RECT);
//            sImageView.setBorderWidth(0);



            // 设置是否关闭单张图片处理逻辑
            sImageView.setCloseNormalOnePicLoad(data.mCloseNormalOnePicLoad);

            // 设置描边颜色
            if (data.borderColor <= 0){
                sImageView.setBorderColor(data.borderColor);
            }else{
                sImageView.setBorderColor(Color.BLACK);
            }

            // 设置描边宽度
            sImageView.setBorderWidth(data.borderWidth);

            // 设置显示类型
            sImageView.setDisplayShape(data.displayType);

            // 设置测量布局
            if (null != data.mMeasureManager){
                sImageView.setLayoutManager(data.mMeasureManager);
            }else{
                sImageView.setLayoutManager(new QQLayoutManager());
            }

            // 最后设置图片展示
//            sImageView.setIdRes(R.mipmap.ic_1);
            sImageView.setImages(data.readyBmp);
        }



        @Override
        public int getItemCount() {
            return datas.size();
        }

        static class MyHolder extends RecyclerView.ViewHolder{
            public SImageView siv_display;

            public MyHolder(View itemView) {
                super(itemView);
                siv_display = (SImageView) itemView.findViewById(R.id.siv);
            }
        }


    }


}
