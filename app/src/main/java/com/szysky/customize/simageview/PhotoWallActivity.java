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
import android.widget.TextView;
import android.widget.Toast;


import com.szysky.customize.simageview.data.ListDisplayData;
import com.szysky.customize.simageview.util.NetWorkUtil;
import com.szysky.customize.siv.SImageView;
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

//        ArrayList<String> urls = new ArrayList<>();
//        for (int i = 0; i < 100; i++) {
//            urls.add("http://szysky.com/2016/12/05/%E5%B9%B4%E7%BB%88%E7%A6%8F%E5%88%A9-SImageView%E5%AE%9E%E7%94%A8%E7%9A%84%E5%9B%BE%E7%89%87%E6%8E%A7%E4%BB%B6/sample_2.gif");
//            urls.add("http://img9.dzdwl.com/img/11543935W-1.jpg");
//            urls.add("http://img02.tooopen.com/images/20160408/tooopen_sy_158723161481.jpg");
//            urls.add("http://img02.tooopen.com/images/20160404/tooopen_sy_158262392146.jpg");
//            urls.add("http://img02.tooopen.com/images/20160318/tooopen_sy_156339294124.jpg");
//            urls.add("http://img06.tooopen.com/images/20160823/tooopen_sy_176393394325.jpg");
//            urls.add("http://img06.tooopen.com/images/20160821/tooopen_sy_176144979595.jpg");
//            urls.add("http://img06.tooopen.com/images/20160723/tooopen_sy_171462742667.jpg");
//            urls.add("http://img05.tooopen.com/images/20150417/tooopen_sy_119014046478.jpg");
//            urls.add("http://img02.tooopen.com/images/20150318/tooopen_sy_82853534894.jpg");
//            urls.add("http://img05.tooopen.com/images/20150204/tooopen_sy_80359399983.jpg");
//            urls.add("http://pics.sc.chinaz.com/files/pic/pic9/201410/apic7065.jpg");
//        }


        ArrayList<ListDisplayData> datas = ListDisplayData.structureData();


        ListView gv_main = (ListView) findViewById(R.id.gv_main);



        imageAdapter = new ImageAdapter(getApplicationContext(), datas);
        gv_main.setAdapter(imageAdapter);


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
            return 1;
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
