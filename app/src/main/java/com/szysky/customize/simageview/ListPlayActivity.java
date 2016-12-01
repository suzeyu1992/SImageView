package com.szysky.customize.simageview;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Author :  suzeyu
 * Time   :  2016-12-01  下午7:53
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription :
 */

public class ListPlayActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Random random = new Random();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_test);



        ArrayList<ArrayList<Bitmap>> arrayLists = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            int i1 = random.nextInt(5);
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                bitmaps.add(bitmap);
            }
            arrayLists.add(bitmaps);
        }


        MyAdapter myAdapter = new MyAdapter(getApplicationContext(), arrayLists);
        setListAdapter(myAdapter);
    }


    static class MyAdapter extends BaseAdapter{

        private final Context context;
        private final ArrayList<ArrayList<Bitmap>> mData;

        public MyAdapter(Context context, ArrayList<ArrayList<Bitmap>> mData) {

            this.context = context;
            this.mData = mData;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView  = View.inflate(context, R.layout.item_list, null);
                viewHolder.siv = (SImageView) convertView.findViewById(R.id.siv);

                convertView.setTag(viewHolder);

            }else{
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.siv.setImages(mData.get(position));

            return convertView;
        }

        static class ViewHolder{
            public SImageView siv;
            public ImageView iv;
        }
    }

}
