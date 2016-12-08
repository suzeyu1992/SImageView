package com.szysky.customize.simageview;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


import com.szysky.customize.simageview.util.NetWorkUtil;
import com.szysky.customize.siv.imgprocess.ImageLoader;

import java.util.ArrayList;

public class PhotoWallActivity extends AppCompatActivity {

    private static boolean mCanLoadForPhoneNet;
    private ImageAdapter imageAdapter;
    private static boolean mIsGridViewIdle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_wall);

        ArrayList<String> urls = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
//            urls.add("http://szysky.com/2016/12/05/%E5%B9%B4%E7%BB%88%E7%A6%8F%E5%88%A9-SImageView%E5%AE%9E%E7%94%A8%E7%9A%84%E5%9B%BE%E7%89%87%E6%8E%A7%E4%BB%B6/sample_2.gif");
            urls.add("http://img9.dzdwl.com/img/11543935W-1.jpg");
            urls.add("http://img02.tooopen.com/images/20160408/tooopen_sy_158723161481.jpg");
            urls.add("http://img02.tooopen.com/images/20160404/tooopen_sy_158262392146.jpg");
            urls.add("http://img02.tooopen.com/images/20160318/tooopen_sy_156339294124.jpg");
            urls.add("http://img06.tooopen.com/images/20160823/tooopen_sy_176393394325.jpg");
            urls.add("http://img06.tooopen.com/images/20160821/tooopen_sy_176144979595.jpg");
            urls.add("http://img06.tooopen.com/images/20160723/tooopen_sy_171462742667.jpg");
            urls.add("http://img05.tooopen.com/images/20150417/tooopen_sy_119014046478.jpg");
            urls.add("http://img02.tooopen.com/images/20150318/tooopen_sy_82853534894.jpg");
            urls.add("http://img05.tooopen.com/images/20150204/tooopen_sy_80359399983.jpg");
            urls.add("http://img01.tooopen.com/Downs/images/2010/4/9/sy_20100409135808693051.jpg");
            urls.add("http://pics.sc.chinaz.com/files/pic/pic9/201410/apic7065.jpg");
        }

        // 根据连接网络的情况判断是否加载图片
        if (!NetWorkUtil.isWifi(getApplicationContext())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("首次使用会从手机网络下载图片, 是否确认下载?")
                    .setTitle("友情提示")
                    .setPositiveButton("好的.", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCanLoadForPhoneNet = true;
                            imageAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("不行!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "瞅你扣那样!!!", Toast.LENGTH_LONG).show();
                        }
                    }).show();
        }else{
            mCanLoadForPhoneNet = true;
        }


        ListView gv_main = (ListView) findViewById(R.id.gv_main);
        // 监听GridView的滑动状态
            gv_main.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                        mIsGridViewIdle = true;
                        // 并触发更新adapter
//                        imageAdapter.notifyDataSetChanged();
                    }else{
                        mIsGridViewIdle = false;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });


        imageAdapter = new ImageAdapter(getApplicationContext(), urls);
        gv_main.setAdapter(imageAdapter);


    }


    /**
     * 给GridView创建一个适配器
     */
    private static class ImageAdapter extends BaseAdapter {

        private final ArrayList<String> mUrls;
        private Context mContext;
        private final ImageLoader mImageLoader;

        public ImageAdapter(Context context, ArrayList<String> mUrls){
            mContext = context;
            this.mUrls = mUrls;
            mImageLoader =  ImageLoader.getInstance(context);
            Log.e("lalala", "@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }

        @Override
        public String getItem(int position) {
            return mUrls.get(position);
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
                holder.mImageView = (ImageView) convertView.findViewById(R.id.iv_square);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            // 设置默认图片
            ImageView mImageView = holder.mImageView;
            mImageView.setImageResource(android.R.drawable.screen_background_dark_transparent);


            // 检测是否wifi 和 是否是滑动状态
            if (mCanLoadForPhoneNet){
//            if (mCanLoadForPhoneNet && mIsGridViewIdle){
                // 加载图片
               // mImageLoader.setImageView(mImageView).url(mUrls.get(position));
                mImageLoader.setPicture(mUrls.get(position), mImageView,0,0);
            }


            return convertView;
        }

        class ViewHolder{
            private ImageView mImageView;
        }
    }



}
