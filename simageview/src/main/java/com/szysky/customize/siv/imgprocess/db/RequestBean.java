package com.szysky.customize.siv.imgprocess.db;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.util.SecurityUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Author :  suzeyu
 * Time   :  2016-12-08  下午4:07
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 对控件的网络请求的进行一个封装
 */

public class RequestBean {
    public SImageView sImageView;
    public List<String> urls;
    /**
     * 还未进行url处理的地址集合
     */
    private CopyOnWriteArrayList<String>  noLoadUrls;
    private ConcurrentHashMap<String,Bitmap> bitmaps;
    public int reqWidth;
    public int reqHeight;
    public long startTime = System.currentTimeMillis();
    public int loadTotal;       // 需要下载的总数
    public volatile int loadedNum;        // 已经完成的数量
    private String mTag ;                 // 设置图片对应的控件的tag


    public RequestBean(List<String> urls, SImageView sImageView, int reqWidth, int reqHeight) {
        this.sImageView = sImageView;
        this.urls = urls;
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
        loadTotal = urls.size();

        // 初始化tag, 并设置
        getTag();
        sImageView.setTag(mTag);


        // 创建图片地址, 对应map
        for (String url:urls) {
            bitmaps.put(url, null);
            noLoadUrls.add(url);
        }

    }

    /**
     * 检测对应的url, 是否已经存在bitmap
     */
    public String[] checkNoLoadUrl(){
        return (String[]) noLoadUrls.toArray();
    }

    /**
     * 添加网址对应的bitmap
     */
    public  void addBitmap(String url, Bitmap bitmap){
        bitmaps.put(url, bitmap);
        noLoadUrls.remove(url);
        synchronized (RequestBean.class){
            loadedNum++;
        }
    }

    /**
     * 判断所有网址对应的bitmap是否全部获取成功
     */
    public boolean isLoadSuccessful(){
        return loadTotal == loadedNum ;
    }


    /**
     * 获得一个{@link SImageView}图片请求的tag
     */
    public String getTag(){
        if (mTag.isEmpty()){
            StringBuilder sb = new StringBuilder();
            for (String temp : urls) {
                sb.append(temp);
            }
           mTag =  SecurityUtil.md5keyFormUrl(sb.toString());
        }
        return mTag;
    }

    /**
     * 获取已经下载的图片集合
     */
    public List<Bitmap> asListBitmap(){

        ArrayList<Bitmap> tempBitmaps = new ArrayList<>();
        for (String url:urls) {
            tempBitmaps.add(bitmaps.get(url));
        }
        return tempBitmaps;
    }
}
