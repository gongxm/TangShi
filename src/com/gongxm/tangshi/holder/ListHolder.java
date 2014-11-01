package com.gongxm.tangshi.holder;

import android.view.View;
import android.widget.TextView;

import com.gongxm.tangshi.MainActivity;
import com.gongxm.tangshi.R;
import com.gongxm.tangshi.bean.Bean;

public class ListHolder extends BaseHolder<Bean> {
	private TextView tv_title;
	private TextView tv_author;
	private TextView tv_desc;
	@Override
	public View initView() {
		View view=View.inflate(MainActivity.instance, R.layout.list_item, null);
		tv_title = (TextView)view.findViewById(R.id.title);
		tv_author = (TextView)view.findViewById(R.id.author);
		tv_desc = (TextView)view.findViewById(R.id.desc);
		return view;
	}
	

	@Override
	public void refresView() {
		Bean bean=getDatas();
		tv_title.setText(bean.getTitle());
		tv_author.setText(bean.getAuthor());
		tv_desc.setText(bean.getDesc());
	}

}
