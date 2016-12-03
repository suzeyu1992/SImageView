package com.szysky.customize.siv.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class UIUtils {
	private static String TAG = UIUtils.class.getName();

	/**
	 * 获取资源字符串数组
	 */
	public static String[] getStringArray(Context context, int id) {
		return context.getResources().getStringArray(id);
	}


	/** dip转换px */
	public static float dip2px(Context context, float dip) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return  Math.round(dip * scale *100) /100 ;
	}

	/** px转换dip */

	public static float px2dip(Context context, float px) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return  Math.round(px / scale * 100) /100;
	}


	/**
	 * 获取资源目录的颜色
	 */
	public static int getColor(Context context, int id) {
		return context.getResources().getColor(id);
	}

	public static View inflate(Context context,int resource) {
		return View.inflate(context, resource, null);
	}

	/**
	 * 时间戳字符串转换成 2016-11-11 格式返回
     */
	public static String setDataSimple(String replaceTime){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		long parseLong = Long.parseLong(replaceTime);
		return simpleDateFormat.format(parseLong);
	}


	/**
	 * 获取屏幕宽高
     */
	public static DisplayMetrics getCurrentDisplayMetrics(Context context){
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics();
		float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
		int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
		float xdpi = dm.xdpi;
		float ydpi = dm.ydpi;
		Log.e(TAG + " DisplayMetrics", "xdpi=" + xdpi + "; ydpi=" + ydpi);
		Log.e(TAG + " DisplayMetrics", "density=" + density + "; densityDPI=" + densityDPI);
		int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
		int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
		Log.e(TAG + " DisplayMetrics() ", "screenWidth=" + screenWidth + "; screenHeight=" + screenHeight);

		return dm;
	}

}
