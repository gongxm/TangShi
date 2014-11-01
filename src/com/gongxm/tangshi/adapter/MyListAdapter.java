package com.gongxm.tangshi.adapter;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.gongxm.tangshi.DetailActivity;
import com.gongxm.tangshi.MainActivity;
import com.gongxm.tangshi.bean.Bean;
import com.gongxm.tangshi.holder.BaseHolder;
import com.gongxm.tangshi.holder.ListHolder;

public abstract class MyListAdapter extends MyBaseAdapter<Bean> {
	List<Bean> list;

	public MyListAdapter(AbsListView mListView, List<Bean> mDatas) {
		super(mListView, mDatas);
		list = mDatas;
	}

	@Override
	protected BaseHolder<Bean> getHolder() {

		return new ListHolder();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// 打开新activity，显示内容
		List<Bean> data = getData();
		Intent intent = new Intent();
		intent.setClass(MainActivity.instance, DetailActivity.class);
		intent.putExtra("title", data.get(position).getTitle());
		intent.putExtra("url", data.get(position).getTextUrl());
		intent.putExtra("author", data.get(position).getAuthor());
		MainActivity.instance.startActivity(intent);
	}

	@Override
	protected abstract boolean hasMore();

}
