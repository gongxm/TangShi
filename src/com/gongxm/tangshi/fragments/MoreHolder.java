package com.gongxm.tangshi.fragments;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.gongxm.tangshi.MainActivity;
import com.gongxm.tangshi.R;
import com.gongxm.tangshi.adapter.MyBaseAdapter;
import com.gongxm.tangshi.holder.BaseHolder;

public class MoreHolder extends BaseHolder<Integer> implements OnClickListener {
	public static final int HAS_MORE = 0;
	public static final int NO_MORE = 1;
	public static final int ERROR = 2;
	private MyBaseAdapter adapter;
	private RelativeLayout mLoading;
	private RelativeLayout mError;

	public MoreHolder(MyBaseAdapter adapter, boolean hasMore) {
		setDatas(hasMore ? HAS_MORE : NO_MORE);
		this.adapter = adapter;
	}

	@Override
	public View initView() {
		View view=View.inflate(MainActivity.instance, R.layout.list_more_loading, null);
		mLoading = (RelativeLayout) view.findViewById(R.id.rl_more_loading);
		mError = (RelativeLayout) view.findViewById(R.id.rl_more_error);
		mError.setOnClickListener(this);
		return view;
	}

	@Override
	public void refresView() {
		Integer data = getDatas();
		System.out.println(data);
		mLoading.setVisibility(data == HAS_MORE ? View.VISIBLE : View.GONE);
		mError.setVisibility(data == ERROR ? View.VISIBLE : View.GONE);
	}
	
	@Override
	public View getView() {
		if (getDatas() == HAS_MORE) {
			loadMore();
		}
		return super.getView();
	}

	public void loadMore() {
		adapter.loadMore();
	}

	@Override
	public void onClick(View v) {
		loadMore();
	}
	
	

}
