package com.szysky.customize.simageview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.szysky.customize.simageview.data.ListDisplayData;
import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.range.ILayoutManager;
import com.szysky.customize.siv.range.QQLayoutManager;
import com.szysky.customize.siv.range.WeChatLayoutManager;

import java.util.ArrayList;

public class PhotoWallActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageAdapter imageAdapter;
    private Button btn_border;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); // 隐藏android系统的状态栏
        setContentView(R.layout.activity_photo_wall);
        MANAGER_WECHAT = new WeChatLayoutManager(getApplicationContext());



        // 查找按钮并设置点击事件
        findViewById(R.id.btn_circle).setOnClickListener(this);
        findViewById(R.id.btn_rect).setOnClickListener(this);
        findViewById(R.id.btn_oval).setOnClickListener(this);
        findViewById(R.id.btn_round).setOnClickListener(this);
        findViewById(R.id.btn_mul_display).setOnClickListener(this);
        btn_border = (Button) findViewById(R.id.btn_border);
        btn_border.setOnClickListener(this);


        ArrayList<ListDisplayData> datas = ListDisplayData.structureData();


        ListView gv_main = (ListView) findViewById(R.id.gv_main);



        imageAdapter = new ImageAdapter(getApplicationContext(), datas);
        gv_main.setAdapter(imageAdapter);


    }

    private static final ILayoutManager MANAGER_QQ = new QQLayoutManager();                                    // qq布局
    private  ILayoutManager MANAGER_WECHAT ;    // 微信布局
    // 三个控制控件显示的变量
    @SImageView.ShapeDisplay private static int mDisplayType;
    private static ILayoutManager mDisplayLayoutManager = MANAGER_QQ;
    private static int mDisplayBorder ;
    private boolean isHaveBorder = false;



    private long lastClickTime;
    @Override
    public void onClick(View v) {
        // 防止过快操作
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > 1000){
            lastClickTime = currentTime;
        }else{
            Toast.makeText(getApplicationContext(), "你点的太快啦.",Toast.LENGTH_SHORT).show();
            return;
        }

        switch (v.getId()){
            case R.id.btn_circle:
                mDisplayType = SImageView.TYPE_CIRCLE;
                imageAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_rect:
                mDisplayType = SImageView.TYPE_RECT;
                imageAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_round:
                mDisplayType = SImageView.TYPE_ROUND_RECT;
                imageAdapter.notifyDataSetChanged();

                break;
            case R.id.btn_oval:
                mDisplayType = SImageView.TYPE_OVAL;
                imageAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_mul_display:
                if (mDisplayLayoutManager == MANAGER_QQ){
                    mDisplayLayoutManager = MANAGER_WECHAT;
                    Toast.makeText(getApplicationContext(), "切换为微信群组样式.",Toast.LENGTH_SHORT).show();

                }else{
                    mDisplayLayoutManager = MANAGER_QQ;
                    Toast.makeText(getApplicationContext(), "切换为QQ群组样式.",Toast.LENGTH_SHORT).show();
                }
                imageAdapter.notifyDataSetChanged();
                break;

            case R.id.btn_border:
                if (isHaveBorder){
                    btn_border.setText("添加描边");
                    mDisplayBorder = 0;
                    isHaveBorder = false;
                }else{
                    btn_border.setText("清除描边");
                    mDisplayBorder = 2;
                    isHaveBorder = true;
                }
                imageAdapter.notifyDataSetChanged();
                break;
        }

    }


    /**
     * 给GridView创建一个适配器
     */
    private static class ImageAdapter extends BaseAdapter {

        private final ArrayList<ListDisplayData> mUrls;
        private Context mContext;

        public ImageAdapter(Context context, ArrayList<ListDisplayData> mUrls){
            mContext = context;
            this.mUrls = mUrls;
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }

        @Override
        public String getItem(int position) {
            return mUrls.get(position).name;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null){
                convertView = View.inflate(mContext, R.layout.item_photo_wall, null);
                holder = new ViewHolder();
                holder.mImageView = (SImageView) convertView.findViewById(R.id.siv_main);
                holder.mTextView = (TextView) convertView.findViewById(R.id.tv_name);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置当前要显示的属性
            holder.mImageView.setDisplayShape(mDisplayType);
            holder.mImageView.setBorderWidth(mDisplayBorder);

            // 这里的布局管理器, 千万不要复用, 因为内部已经实现了缓存策略, 如果多个的控件用一个, 可能会因为并发问题, 导致崩溃
            if ((holder.mImageView.getLayoutManager() instanceof QQLayoutManager) && (mDisplayLayoutManager instanceof WeChatLayoutManager)){
                holder.mImageView.setLayoutManager(new WeChatLayoutManager(mContext));
            }else if((holder.mImageView.getLayoutManager() instanceof WeChatLayoutManager) && (mDisplayLayoutManager instanceof QQLayoutManager)){
                holder.mImageView.setLayoutManager(new QQLayoutManager());
            }

            // 为了让圆角矩形好看, 如果是圆角矩形那么就去掉背景
            if (mDisplayType == SImageView.TYPE_ROUND_RECT){
                holder.mImageView.setBackgroundColor(Color.WHITE);
            }else{
                holder.mImageView.setBackgroundColor(Color.GRAY);

            }



            ListDisplayData data = mUrls.get(position);
            holder.mImageView.setImageUrls(data.url);
            holder.mTextView.setText(data.name);




            return convertView;
        }

        class ViewHolder{
            private SImageView mImageView;
            private TextView mTextView;
        }
    }



}
