package com.gongxm.tangshi.holder;

import android.view.View;

public abstract class BaseHolder<T> {
	private View view;
	private T mDatas;
	private int position;


	public BaseHolder() {
		view = initView();
		view.setTag(this);
	}
	
	

	public View getView() {
		return view;
	}



	public T getDatas() {
		return mDatas;
	}



	public void setDatas(T mDatas) {
		this.mDatas = mDatas;
		refresView();
	}



	public int getPosition() {
		return position;
	}



	public void setPosition(int position) {
		this.position = position;
	}



	public abstract View initView() ;
	public abstract void refresView() ;
	
	
}
