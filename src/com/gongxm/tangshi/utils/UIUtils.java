package com.gongxm.tangshi.utils;

import com.gongxm.tangshi.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class UIUtils {
	
	

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = MainActivity.instance.getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */
	public static int px2dip(int px) {
		final float scale = MainActivity.instance.getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}


	public static View inflate(int resId){
		return LayoutInflater.from(MainActivity.instance).inflate(resId,null);
	}

	/** 获取资源 */
	public static Resources getResources() {
		return MainActivity.instance.getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择�?*/
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}
    
	public static void runInMainThread(Runnable runnable) {
		MainActivity.instance.runOnUiThread(runnable);
	}



}


