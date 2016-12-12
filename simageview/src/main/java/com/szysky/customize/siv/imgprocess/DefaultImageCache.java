package com.szysky.customize.siv.imgprocess;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.LruCache;
import android.widget.ImageView;

import com.szysky.customize.siv.imgprocess.db.RequestBean;
import com.szysky.customize.siv.util.CloseUtil;
import com.szysky.customize.siv.util.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Author :  suzeyu
 * Time   :  2016-12-07  下午1:34
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 内置的默认实现缓存类, 内存缓存(LruCache)和磁盘缓存(DiskCache)
 */

public class DefaultImageCache implements IImageCache {

    private static final String TAG = DefaultImageCache.class.getName();
    private final Context mContext;
    private final LruCache<String, Bitmap> mMemoryCache;

    /**
     * 默认磁盘缓存的大小值
     */
    private static final long DISK_CACHE_SIZE = 1024 * 1024 * 50;
    private static final int DISK_CACHE_IDEX = 0;

    /**
     * 设置字节流一次缓冲的数据流大小
     */
    private static final int IO_BUFFER_SIZE = 8 * 1024;
    private final ImageLoader mImageLoader;


    private boolean mIsDiskLruCacheCreated;
    private DiskLruCache mDiskLruCache;


    public DefaultImageCache(Context context, ImageLoader imageLoader){
        mContext = context.getApplicationContext();
        mImageLoader = imageLoader;

        // 获得当前进程最大可用内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;

        // 创建内存缓存的LruCache对象
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 返回缓存的bitmap大小
                return value.getRowBytes() * value.getHeight() / 1024;
            }
        };
        LogUtil._i(TAG, "设置内存缓存成功--> 大小为:"+cacheSize/1024+"MB");

        // 获得磁盘缓存的路径
        File diskCacheDir = getDiskCacheDir(mContext, "bitmap");
        String path = Environment.getExternalStorageDirectory().getPath();

        diskCacheDir = new File(path, "testing");


        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdir();
        }

        // 判断磁盘路径下可用的空间 是否达到预期大小, 如果达到, 那就创建磁盘缓存对象
        if (getUsableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            // 利用open函数来构建磁盘缓存对象
            try {
                mDiskLruCache = DiskLruCache.open(diskCacheDir, 1, 1, DISK_CACHE_SIZE);
                mIsDiskLruCacheCreated = true;
                LogUtil._i(TAG, "设置磁盘缓存成功--> 路径为:"+diskCacheDir.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public Bitmap get(final String url, final int reqWidth, final int reqHeight, final ImageView imageView, boolean isDiskCacheGet, final RequestBean bean) {
        final long entry = System.currentTimeMillis();
        // 从内存缓存获取
        if (!isDiskCacheGet) {
            // 1.从内存中读取
            String key = keyFormUrlAndWH(url , reqWidth, reqHeight);
            Bitmap bitmap = getBitmapFromMemoryCache(key);
            if (bitmap != null) {
                LogUtil._d(TAG, "loadBitmap --> 图片从内存中加载成功 uri=" + url + "\r\n消耗时间=" + (System.currentTimeMillis() - entry) + "ms");
                return bitmap;
            }
        }else{
            // 2.从磁盘缓存加载
            // 首先判断是否是多张图片加载
            if ((bean != null) &&(bean.loadTotal > 0)){

                // 读取硬盘属于耗时操作, 使用子线程
                Runnable loadBitmapTask = new Runnable(){
                    @Override
                    public void run() {
                        int processNum = 0;

                        // 对url对应value值为null的元素进行磁盘获取
                        for (String url : bean.checkNoLoadUrl()) {
                            Bitmap checkBitmap  = loadBitmapFromDiskCache(url, bean.reqWidth, bean.reqHeight);
                            // 如果不等于空进行有效添加
                            if (null != checkBitmap){
                               bean.addBitmap(url, checkBitmap);
                                processNum++;
                            }
                        }

                        // 判断磁盘获取结束后是否全部完毕
                        if (!bean.isLoadSuccessful()){
                            // 通知Handler多张图片从磁盘获取为完成, 但是还未全部完成
                            mImageLoader.mMainHandler.obtainMessage(ImageLoader.MESSAGE_MULTI_DISK_GET_ERR, bean).sendToTarget();
                            LogUtil._i(TAG, "info>>>>磁盘缓存获取的图片数量: "+processNum +" 张, 还剩 "+bean.checkNoLoadUrl().length+" 张图片需要网络下载");
                        }else {
                            // 通知成功并处理
                            mImageLoader.mMainHandler.obtainMessage(ImageLoader.MESSAGE_MULTI_DISK_GET_OK, bean).sendToTarget();
                            LogUtil._i(TAG, "info>>>>磁盘缓存获取的图片数量: "+processNum +"    图片全部处理完毕 ");
                        }

                    }
                };

                // 添加任务到线程池
                ImageLoader.THREAD_POOL_EXECUTOR.execute(loadBitmapTask);
                return null;
            }
        }

        return null;
    }



    @Override
    public void put(String url, Bitmap bmp , int reqWidth, int reqHeight) {


            // 存储原始图片
            long l = System.currentTimeMillis();
            // 存储到磁盘
            putBitmap(url, bmp);
            LogUtil._i(TAG, "磁盘存储的时间 "+(System.currentTimeMillis()-l));
            addBitmapToMemoryCache(url,reqWidth, reqHeight, bmp);


    }

    @Override
    public boolean putRawStream(String url, InputStream in) {

        boolean result = false;
        // 因为本实例 是先下载先保存在磁盘, 然后从磁盘获取 所以如果磁盘无效那么就停止.
        if (mDiskLruCache == null) {
            return false;
        }

        // 根据url算出md5值
        String key = keyFormUrl(url);
        BufferedOutputStream out = null;
        DiskLruCache.Editor editor = null ;

        try {
            // 开始对磁盘缓存的一个存储对象进行操作
            editor = mDiskLruCache.edit(key);
            if (editor != null){
                // 如果==null说明这个editor对象正在被使用
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_IDEX);


                // 创建Buffer并指定要写入的磁盘缓存输出流
                out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

                BufferedInputStream ins = new BufferedInputStream(in, IO_BUFFER_SIZE);

                byte[] b = new byte[1024 * 20];
                int point;
                while((point = ins.read(b)) != -1){
                    out.write(b,0,point);
                }

                //加载成功进行 提交操作
                editor.commit();


                result = true;
            }

            LogUtil._i(TAG, "putRawStream: ==> "+"原始图片流写入磁盘缓存成功");
        } catch (IOException e) {
            LogUtil._w(TAG, "putRawStream: ==> "+"原始图片流写入磁盘缓存失败 ", e);
            // 进行数据回滚
            if (null != editor){
                try {
                    editor.abort();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            result = false;
        }finally {
            if (out != null){
                CloseUtil.close(out);
            }
            try {
                mDiskLruCache.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public void putBitmap(String url, Bitmap bitmap) {
        // 因为本实例 是先下载先保存在磁盘, 然后从磁盘获取 所以如果磁盘无效那么就停止.
        if (mDiskLruCache == null) {
            return ;
        }

        // 根据url算出md5值
        String key = keyFormUrl(url);
        BufferedOutputStream out = null;
        DiskLruCache.Editor editor = null;

        try {
            // 开始对磁盘缓存的一个存储对象进行操作
            editor = mDiskLruCache.edit(key);
            if (editor != null){
                // 如果==null说明这个editor对象正在被使用
                OutputStream outputStream = editor.newOutputStream(DISK_CACHE_IDEX);


                // 创建Buffer并指定要写入的磁盘缓存输出流
                out = new BufferedOutputStream(outputStream, IO_BUFFER_SIZE);

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);


                //加载成功进行 提交操作
                editor.commit();

                mDiskLruCache.flush();

            }

            LogUtil._i(TAG, "putRawStream: ==> "+"原始图片bitmap写入磁盘缓存成功, 地址:"+url);
        } catch (IOException e) {
            LogUtil._w(TAG, "putRawStream: ==> "+"原始图片bitmap写入磁盘缓存失败, 地址:"+url, e);

        }finally {
            if (out != null){
                CloseUtil.close(out);
            }

        }
    }



    public  Bitmap loadBitmapFromDiskCache(String url, int reqWidth, int reqHeight)  {
        if (mDiskLruCache == null) {
            return null;
        }

        Bitmap bitmap ;
        String key = keyFormUrl(url);

        DiskLruCache.Snapshot snapshot ;
        try {
            snapshot = mDiskLruCache.get(key);
            if (null != snapshot) {
                FileInputStream fileInputStream = (FileInputStream) snapshot.getInputStream(DISK_CACHE_IDEX);
                // 由于文件流属于一种有序的文件流, 所以无法进行两次decode. 这里通过获得文件描述符的方法解决
                FileDescriptor fd = fileInputStream.getFD();
                bitmap = ImageCompression.decodeFixedSizeForFileDescription(fd, reqWidth, reqHeight);

                if (bitmap != null) {

                    LogUtil.print_i(TAG, "loadBitmapFromDiskCache(): ==> "+"从磁盘加载图片成功, \r\n   地址:"+url
                            +"\r\n     加载到内存的图片大小  --> 宽:"+bitmap.getWidth() +"   高:"+bitmap.getHeight()
                            +"\r\n     目标需要的大小图大小  --> 宽:"+reqWidth +"   高:"+reqHeight);
                    addBitmapToMemoryCache(url, reqWidth, reqHeight, bitmap);

                }
                return bitmap;
            }
        } catch (IOException e) {
            LogUtil._e(TAG, "从磁盘获取IO失败", e);
        }


        return null;
    }

    /**********************给内存缓存添加操作方法**********************/
    /**
     * 添加bitmap对象到内存缓存中
     *
     * @param url    图片的url
     * @param bitmap 需要缓存的bitmap对象
     */
    private void addBitmapToMemoryCache(String url, int reqWidth, int reqHeight, Bitmap bitmap) {
        String key = keyFormUrlAndWH(url, reqWidth, reqHeight);
        // 如果内存缓存中不存在, 那么才进行添加的动作
        if (null == getBitmapFromMemoryCache(key)) {
            mMemoryCache.put(key, bitmap);

            LogUtil.print_i(TAG, "addBitmapToMemoryCache(): ==> "+"图片添加内存缓存成功, \r\n   地址:"+url
                    +"\r\n     添加内存缓存的图片大小  --> 宽:"+bitmap.getWidth() +"   高:"+bitmap.getHeight()
                    +"\r\n     目标需要的大小图大小    --> 宽:"+reqWidth +"   高:"+reqHeight
                    +"\r\n     占用内存大小:"+(bitmap.getRowBytes() * bitmap.getHeight() / 1024f /1024f) + "MB");
        }
    }


    /**
     * 根据key值获取在内存缓存中保存的bitmap
     *
     * @param key 根据图片的url生成的32md5值
     * @return 如果内存缓存中有对应的值, 那么就返回bitmap, 没有返回值就为null
     */
    private Bitmap getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 获得一个指定的文件夹路径的File对象
     *
     * @param context 应用上下文
     * @param dirName 想要在SD卡的缓存路径下的哪一个子文件夹的对应名称
     * @return 返回一个路径对应的File对象
     */
    public File getDiskCacheDir(Context context, String dirName) {
        // 判断SD卡是否被挂起
        boolean externalIsAlive = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);

        final String cachePath;

        if (externalIsAlive) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir == null){
                cachePath = Environment.getExternalStorageDirectory().getPath();
            }else {cachePath =  externalCacheDir.getPath();
            }
        } else {
            cachePath = context.getCacheDir().getPath();
        }

        // 对想要的相对路径下的具体文件名进行创建并返回
        return new File(cachePath + File.separator + dirName);
    }


    // 指定文件对象可用的空间
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private long getUsableSpace(File path) {
        // 大于等于android版本9
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            return path.getUsableSpace();
        }

        StatFs statFs = new StatFs(path.getPath());
        return (long) statFs.getBlockSize() * (long) statFs.getAvailableBlocks();
    }


    /**
     * 接收一个url地址, 对其转换成md5值并返回
     * 转成一个32md5值
     */
    public String keyFormUrl(String url) {
        String cacheKey;
        try {
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }


    /**
     * 接收一个url地址,和宽高属性,  对其组合转换成md5值并返回, 使用与内存缓存中
     * 转成一个32md5值
     */
    public String keyFormUrlAndWH(String url, int reqWidth, int reqHeight) {
        String cacheKey;
        try {
            if (!(reqHeight == 0 || reqWidth ==0)){
                int marker = reqWidth * 17 + reqHeight * 31;
                url += marker;
            }
            MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(url.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(url.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                stringBuilder.append('0');
            }
            stringBuilder.append(hex);
        }
        return stringBuilder.toString();
    }


}
