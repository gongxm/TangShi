package com.gongxm.tangshi.ui;

import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.gongxm.tangshi.R;

public abstract class LoadingPage extends FrameLayout{
	private Activity activity;
	
	public LoadingPage(Context context) {
		super(context);
		this.activity=(Activity) context;
		init();
	}
	

	public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LoadingPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}


	private static final int SUCCESS=0;
	private static final int ERROR=1;
	private static final int EMPTY=2;
	private static final int LOADING=3;
	private static final int NONE=3;
	
	private int currentState;
	private View loadingView;
	private View errorView;
	private View emptyView;
	private View successView;
	private LayoutParams params;
	

	/**
	 * 初始化方法
	 */
	private void init() {
		currentState=NONE;
		params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		loadingView = createLoadingView();
		if(loadingView!=null){
			addView(loadingView,params);
		}
		errorView = createErrorView();
		if(errorView!=null){
			addView(errorView, params);
		}
		emptyView = createEmptyView();
		if(emptyView!=null){
			addView(emptyView, params);
		}
		
		showPage();
	}

	private void showPage() {
		if(loadingView!=null){
			loadingView.setVisibility(currentState==LOADING?View.VISIBLE:View.INVISIBLE);
		}
		if(errorView!=null){
			errorView.setVisibility(currentState==ERROR?View.VISIBLE:View.INVISIBLE);
		}
		if(emptyView!=null){
			emptyView.setVisibility(currentState==EMPTY?View.VISIBLE:View.INVISIBLE);
		}
		if(currentState==SUCCESS&&successView==null){
			successView=createSuccessView();
			addView(successView, params);
		}
		if(successView!=null){
			successView.setVisibility(currentState==SUCCESS?View.VISIBLE:View.INVISIBLE);
		}
	}
	
	
	public void show(){
		if(currentState==EMPTY||currentState==ERROR){
			currentState=NONE;
		}
		if(currentState==NONE){
			currentState=LOADING;
			showPage();
			LoadTask task=new LoadTask();
			Executors.newSingleThreadExecutor().execute(task);
		}
	}


	/**
	 * 由子类实现的方法
	 * @return
	 */
	public abstract View createSuccessView();
	public abstract LoadStatus load();


	private View createEmptyView() {
		return View.inflate(activity, R.layout.loading_page_empty, null);
	}

	private View createErrorView() {
		View view=View.inflate(activity, R.layout.loading_page_error, null);
				view.findViewById(R.id.page_bt).setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						show();
					}
				});
		return view;
	}

	private View createLoadingView() {
		return View.inflate(activity, R.layout.loading_page_loading, null);
	}
	
	private class LoadTask implements Runnable{
		@Override
		public void run() {
			final LoadStatus statu=load();
			activity.runOnUiThread(new Runnable() {
				public void run() {
					currentState=statu.getValue();
					showPage();
				}
			});
		}
	}
	
	public enum LoadStatus{
			ERROR(1),EMPTY(2),SUCCESS(0);
			int value;
			LoadStatus(int v){
				value=v;
			}
			public int getValue(){
				return value;
			}
	}
	
	
}
