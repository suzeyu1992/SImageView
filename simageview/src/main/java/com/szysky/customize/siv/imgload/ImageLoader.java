package com.szysky.customize.siv.imgload;

/**
 * Author :  suzeyu
 * Time   :  2016-12-07  下午1:26
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 * ClassDescription : 图片的加载类
 */

public class ImageLoader {

    IImageCache mImageCache ;

    // 暴露注入的缓存策略
    public void setImageCache(IImageCache imageCache){
        mImageCache = imageCache;
    }
}
