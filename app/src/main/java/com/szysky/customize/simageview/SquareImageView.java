package com.szysky.customize.simageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Author :  suzeyu
 * Time   :  2016-08-08  下午2:24
 * Blog   :  http://szysky.com
 * GitHub :  https://github.com/suzeyu1992
 *
 * ClassDescription : 派生出一个具有自动适应手机, 让控件的宽高相等的ImageView
 */

public class SquareImageView extends ImageView {
    public SquareImageView(Context context) {
        super(context);
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 由于这里准备提供给GridView控件使用, 对于一个水平线有几个是未知的
        // 那么通过父控件的测量传到这里的宽度规格, 也当做高度即可
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
