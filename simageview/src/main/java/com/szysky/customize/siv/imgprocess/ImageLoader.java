package com.szysky.customize.siv.imgprocess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.szysky.customize.siv.SImageView;
import com.szysky.customize.siv.imgprocess.db.RequestBean;
import com.szysky.customize.siv.util.CloseUtil;
import com.szysky.customize.siv.util.SecurityUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author :  suzeyu
 * Time   :  2016-12-07  下午1:26
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 图片的加载类
 */

public class ImageLoader {

    /**
     *  多张图片从磁盘获取缓存, 没有完全成功
     */
    public static final int MESSAGE_MULTI_DISK_GET_ERR = 0x97;
    /**
     * 多张图片从磁盘获取缓存成功
     */
    public static final int MESSAGE_MULTI_DISK_GET_OK = 0x96;
    private static volatile ImageLoader mInstance;

    private Context mContext;

    /**
     *  图片下载时的规则, 提供外部可自定义, 自定之后所有的图片地址匹配都会生效
     */
    private String mPicUrlRegex = "";

    /**
     * 默认加载错误的图片
     */
    private Bitmap mLoadErrBmp;

    /**
     * 默认加载中的图片
     */
    private Bitmap mLoadingBmp;

    private ImageLoader (Context context){
        mContext = context.getApplicationContext();
        mImageCache = new DefaultImageCache(mContext, this);

        mLoadErrBmp = BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.ic_menu_close_clear_cancel);
        mLoadingBmp = BitmapFactory.decodeResource(mContext.getResources(), android.R.drawable.stat_notify_sync);
    }

    public static ImageLoader getInstance(Context context){
        if (null == mInstance){
            synchronized (ImageLoader.class){
                if (null == mInstance){
                    mInstance = new ImageLoader(context);
                }
            }
        }
        return mInstance;
    }


    private static final String TAG = ImageLoader.class.getName();
    IImageCache mImageCache  ;

    /**
     * 设置字节流一次缓冲的数据流大小
     */
    private static final int IO_BUFFER_SIZE = 8 * 1024;


    /**
     * 获得系统的cpu核数
     */
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    /**
     * 分别对线程池中的核心线程数, 最大线程数, 最大最大存活定义常量
     */
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 10L;
    /**
     * 创建线程工厂, 提供给ThreadPoolExecutor使用
     */
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "ImageLoader#" + mCount.getAndIncrement());
        }
    };
    /**
     * 创建线程池, 用在异步加载图片时
     */
    public static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            sThreadFactory

    );

    // 暴露注入的缓存策略
    public void setImageCache(IImageCache imageCache){
        mImageCache = imageCache;
    }


    /**
     *  对外提供的下载图片方法.
     *
     * @param imaUrl 需要加载的图片
     * @param sImageView  图片设置的控件
     * @param reqWidth  需要大小, 可以为0
     * @param reqHeight 需要大小, 可以为0
     */
    public void setPicture(final String imaUrl, final ImageView sImageView, final int reqWidth, final int reqHeight){

        // 判断图片链接是否符合格式--> http(s)://..... .(jpg|png|bmp|jpeg|gif)


        Bitmap bitmap = mImageCache.get(imaUrl, reqWidth, reqHeight,null, false, null);
        if (null != bitmap){
            sImageView.setImageBitmap(bitmap);
            return;
        }



        Log.e(TAG, imaUrl+" "+System.currentTimeMillis() );
        mImageCache.get(imaUrl, reqWidth, reqHeight, sImageView, true, null);


        sImageView.setTag(imaUrl);

    }

    /**
     * 下载多张图片的方法
     * 只针对SImageView控件场景使用
     */
    public void setMulPicture(List<String> urls, SImageView sImageView, int reqWidth, int reqHeight){


        RequestBean requestBean = RequestBean.obtain(urls, sImageView, reqWidth, reqHeight);

        // 进行图片地址有效性匹配
        matchUrlLink(requestBean);

        // 首先从内存中获取
        for (int i = 0; i < urls.size(); i++) {
            Bitmap bitmap = mImageCache.get(requestBean.urls.get(i), reqWidth, reqHeight, null, false, null);
            if (null != bitmap){
                requestBean.addBitmap(requestBean.urls.get(i), bitmap);
            }
        }


        // 判断从内存获取是否全部加载完毕, 如果没有, 尝试从磁盘中获取
        if (requestBean.isLoadSuccessful()){
            Log.i(TAG, "多张图片的获取时间  >> 内存: "+(System.currentTimeMillis() - requestBean.startTime) + " ms");
            sImageView.setImages(requestBean.asListBitmap());
            return ;
        }

        // 设置加载中图片
        sImageView.setBitmap(mLoadingBmp);

        // 开始从磁盘缓存获取
        mImageCache.get(null, 0,0, null,true, requestBean);


    }


    /**
     * 从一个地址下载图片并转换成bitmap,  并先对bitmap进行磁盘的写入. 然后再返回
     */
    private Bitmap downloadBitmapFromUrl(String uriStr,final int reqWidth, final int reqHeight ) {
        Bitmap bitmap = null;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;

        try {
            URL url = new URL(uriStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);
            bitmap = BitmapFactory.decodeStream(in);
            // bitmap的缓存
            mImageCache.put(uriStr , bitmap, 0, 0);

        } catch (IOException e) {
            Log.e(TAG, ">>>>>>downloadBitmapFromUrl()   再次进行网络下载也失败");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CloseUtil.close(in);
        }

        return bitmap;
    }

    /**
     * 从网络下载图片的流直接存入磁盘, 保证图片的原大小. 并在内部进行内存缓存添加.
     *
     */
    private boolean downloadFirstDiskToCache(String uriStr) {

        boolean result = false;
        HttpURLConnection urlConnection = null;
        BufferedInputStream in = null;
        try {
            URL url = new URL(uriStr);
            urlConnection = (HttpURLConnection) url.openConnection();

            in = new BufferedInputStream(urlConnection.getInputStream(), IO_BUFFER_SIZE);


            result =  mImageCache.putRawStream(uriStr, in);


        } catch (IOException e) {
            Log.e(TAG, ">>>>>>网络图片流直接存入磁盘失败");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            CloseUtil.close(in);
            return result;
        }

    }

    /**
     * 发送Handler的标识
     */
    public static final int MESSAGE_POST_RESULT = 0x99;
    public static final int MESSAGE_DISK_GET_ERR = 0x98;

    /**
     * 利用主线程个Loop来创建一个Handler用来给图片设置bitmap前景
     */
    public Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                // 网络下载图片成功
                case MESSAGE_POST_RESULT:
                    LoaderResult result = (LoaderResult) msg.obj;

                    ImageView imageView = result.imageView;
                    String url = (String) imageView.getTag();
                    if (url.equals(result.url)) {
                        imageView.setImageBitmap(result.bitmap);
                    } else {
                        Log.w(TAG, "要设置的控件的url发生改变, so 取消设置图片");
                    }
                    break;

                // 从磁盘加载失败
                case MESSAGE_DISK_GET_ERR:

                    final LoaderResult errResult = (LoaderResult) msg.obj;

                    // 2. 创建一个Runable调用同步加载的方法去获取Bitmap
                    Runnable loadBitmapTask = new Runnable() {
                        @Override
                        public void run() {
                            Bitmap bitmap = null;
                            // 根据默认缓存添加的分支, 网络下载的输入流直接存入磁盘, 先进行bitmap转换可能会影响到原图片的大小
                            boolean result = downloadFirstDiskToCache(errResult.url);
                            if (result){
                                if (mImageCache instanceof DefaultImageCache){
                                    bitmap = ((DefaultImageCache) mImageCache).loadBitmapFromDiskCache(errResult.url, errResult.reqWidth, errResult.reqHeight);
                                }
                            }else{
                                // 通用逻辑, 从网络下载之后, 先把bitmap存入硬盘然后返回bitmap
                                // 一般情况下不会走此逻辑, 为了保险起见, 和后续扩展其他实现类可以保证bitmap会被添加到IImageView的put()回调中
                                bitmap = downloadBitmapFromUrl(errResult.url, errResult.reqWidth, errResult.reqHeight);
                            }

                            if (bitmap != null) {
                                LoaderResult loaderResult = new LoaderResult(errResult.imageView, errResult.url,bitmap, errResult.reqWidth, errResult.reqHeight);
                                mMainHandler.obtainMessage(MESSAGE_POST_RESULT, loaderResult).sendToTarget();
                                return;
                            }

                            Log.e(TAG, "在磁盘获取缓存失败发送handler后, 无法从网络获取到bitmap对象!!! " );
                        }
                    };
                    // 添加任务到线程池
                    THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
                    break;


                // 多张图片在磁盘未获取到全部的bitmap
                case MESSAGE_MULTI_DISK_GET_ERR:

                    final RequestBean diskGetErrRequest = (RequestBean) msg.obj;

                    for (final String noLoadUrl: diskGetErrRequest.checkNoLoadUrl()) {
                        // 2. 创建一个Runable调用同步加载的方法去获取Bitmap
                        Runnable loadMultiTask = new Runnable() {
                            @Override
                            public void run() {
                                Bitmap bitmap = null;
                                // 根据默认缓存添加的分支, 网络下载的输入流直接存入磁盘, 先进行bitmap转换可能会影响到原图片的大小
                                boolean result = downloadFirstDiskToCache(noLoadUrl);
                                if (result){
                                    if (mImageCache instanceof DefaultImageCache){
                                        bitmap = ((DefaultImageCache) mImageCache).loadBitmapFromDiskCache(noLoadUrl, diskGetErrRequest.reqWidth, diskGetErrRequest.reqHeight);
                                    }
                                }else{
                                    // 通用逻辑, 从网络下载之后, 先把bitmap存入硬盘然后返回bitmap
                                    // 一般情况下不会走此逻辑, 为了保险起见, 和后续扩展其他实现类可以保证bitmap会被添加到IImageView的put()回调中
                                    //bitmap = downloadBitmapFromUrl(noLoadUrl, diskGetErrRequest.reqWidth, diskGetErrRequest.reqHeight);
                                }

                                // 判断网络加载是否成功
                                if (bitmap != null) {
                                    diskGetErrRequest.addBitmap(noLoadUrl, bitmap);
                                }else{
                                    diskGetErrRequest.addBitmap(noLoadUrl, mLoadErrBmp);
                                    Log.e(TAG, "多张图片下载失败, >>>> 图片地址:"+noLoadUrl);
                                }

                                // 判断是否全部加载完成, 如果全部加载完成, 那么发送通知到Handler
                                if (diskGetErrRequest.isLoadSuccessful()){
                                    mMainHandler.obtainMessage(ImageLoader.MESSAGE_MULTI_DISK_GET_OK, diskGetErrRequest).sendToTarget();
                                }

                            }
                        };
                        // 添加任务到线程池
                        THREAD_POOL_EXECUTOR.execute(loadMultiTask);
                    }



                    break;

                // 多张图片从磁盘获取成功
                case MESSAGE_MULTI_DISK_GET_OK:

                    RequestBean requestOk = (RequestBean) msg.obj;
                    // 打印多张图片的处理时间
                    Log.i(TAG, "多张图片的获取时间  >> 磁盘或者网络: "+(System.currentTimeMillis() - requestOk.startTime) + " ms");

                    // 进行控件是否需要有效的判断
                    if (requestOk.sImageView.getTag().equals(requestOk.getTag())){
                        requestOk.sImageView.setImages(requestOk.asListBitmap());
                    }else{
                        Log.w(TAG, "多张图片>>>>控件的url发生改变, so取消设置图片");
                    }

                    // recycle global pool
                    requestOk.recycle();

                    break;

                default:
                    super.handleMessage(msg);

            }

        }
    };

    static class LoaderResult {
        public ImageView imageView;
        public String url;
        public Bitmap bitmap;
        public int reqWidth;
        public int reqHeight;

        public LoaderResult(ImageView imageview, String urlStr, Bitmap bitmap, int reqWidth, int reqHeight) {
            imageView = imageview;
            url = urlStr;
            this.bitmap = bitmap;
            this.reqWidth = reqWidth;
            this.reqHeight = reqHeight;

        }
    }

    private void matchUrlLink( RequestBean req){
        int errNum = 0;

        if (req == null)
            return;

        // 进行过滤
        for (String url: req.urls) {
            if (!SecurityUtil.matchUrlPicture(url, mPicUrlRegex)){
                // 添加默认错误图片
                req.addBitmap(url, mLoadErrBmp);
                errNum++;
            }
        }

        // 判断是否需要清除无效的url
        if (errNum > 0){
            Log.w(TAG, "发现了 "+ errNum +" 个无效的地址");
        }

    }

    /**
     * 获取用户定义的图片链接匹配正则,
     * 如果为定义, 默认为空传
     */
    public String getPicUrlRegex() {
        return mPicUrlRegex;
    }

    /**
     * 可自定义匹配规则图片链接合法的正则, 设置之后将使用用户自定义的匹配规则
     * 默认匹配正则为: https?://.*?.(jpg|png|bmp|jpeg|gif)
     * @param mPicUrlRegex 正则匹配
     */
    public void setPicUrlRegex(String mPicUrlRegex) {
        this.mPicUrlRegex = mPicUrlRegex;
    }

    /**
     * 获得默认图片加载失败,显示的图片
     */
    public Bitmap getLoadErrBmp() {
        return mLoadErrBmp;
    }

    /**
     * 设置默认图片加载失败显示的图片
     */
    public void setLoadErrBmp(Bitmap mLoadErrBmp) {
        this.mLoadErrBmp = mLoadErrBmp;
    }

    /**
     * 获得默认图片加载中,显示的图片
     */
    public Bitmap getLoadingBmp() {
        return mLoadingBmp;
    }

    /**
     * 设置默认图片加载中,显示的图片
     */
    public void setLoadingBmp(Bitmap mLoadingBmp) {
        this.mLoadingBmp = mLoadingBmp;
    }
}
