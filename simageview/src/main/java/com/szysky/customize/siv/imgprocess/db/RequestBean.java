package com.szysky.customize.siv.imgprocess.db;

import android.graphics.Bitmap;

import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.util.SecurityUtil;

import java.util.ArrayList;
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
    public List<String> urls = new ArrayList<>();
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
    private String mTag = "" ;                 // 设置图片对应的控件的tag



    // sometimes we store linked lists of these things
    /*package*/ RequestBean next;

    private static final Object sPoolSync = new Object();
    private static RequestBean sPool;
    private static int sPoolSize = 0;
    private static final int MAX_POOL_SIZE = 20;




    public RequestBean(List<String> urls, SImageView sImageView, int reqWidth, int reqHeight) {
        this.sImageView = sImageView;
        this.urls = urls;
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
        loadTotal = urls.size();

        // 初始化tag, 并设置
        getTag();
        sImageView.setTag(mTag);

        noLoadUrls = new CopyOnWriteArrayList<>();
        bitmaps = new ConcurrentHashMap<>();

        // 创建图片地址, 对应map
        for (String url:urls) {
            noLoadUrls.add(url);
        }

    }

    /**
     * 检测对应的url, 是否已经存在bitmap
     */
    public String[] checkNoLoadUrl(){

        String [] strs = new String[noLoadUrls.size()];
        for (int i = 0; i < strs.length;i++) {
            strs[i] = noLoadUrls.get(i);
        }

        return strs ;
    }

    /**
     * 添加网址对应的bitmap
     */
    public  void addBitmap(String url, Bitmap bitmap){
        if (bitmap != null){
            bitmaps.put(url, bitmap);
        }
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

    /**
     * 缓存策略, 从全局缓存池中返回一个新的对象, 通常需要在调用了
     */
    public static RequestBean obtain(List<String> urls, SImageView sImageView, int reqWidth, int reqHeight){
        synchronized (sPoolSync){
            if (sPool != null){
                // 缓存链表的修正
                RequestBean req = sPool;
                sPool = req.next;
                req.next = null;
                sPoolSize--;

                // 进行赋值初始化
                req.startTime = System.currentTimeMillis();
                req.urls.addAll(urls);
                req.sImageView = sImageView;
                req.loadTotal = req.urls.size();
                req.reqHeight = reqHeight;
                req.reqWidth = reqWidth;
                req.getTag();
                sImageView.setTag(req.mTag);
                for (String url:urls) {
                    req.noLoadUrls.add(url);
                }
                return req;
            }
        }

        return new RequestBean( urls,  sImageView,  reqWidth,  reqHeight);
    }

    /**
     * 返回一个干净的实例到全区缓冲池中
     */
    public void recycle() {
        // 清除所有数据
        reqWidth = 0;
        reqHeight = 0;
        startTime = 0;
        loadedNum = 0;
        loadTotal = 0;
        mTag = "";
        bitmaps.clear();
        urls.clear();
        noLoadUrls.clear();
        sImageView = null;

        // 填入缓冲池 并修正
        synchronized (sPoolSync){
            if (sPoolSize < MAX_POOL_SIZE){
                next = sPool;
                sPool = this;
                sPoolSize--;
            }
        }
    }


}
