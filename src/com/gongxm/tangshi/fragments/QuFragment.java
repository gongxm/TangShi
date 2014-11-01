package com.gongxm.tangshi.fragments;

import java.util.List;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.gongxm.tangshi.adapter.MyListAdapter;
import com.gongxm.tangshi.bean.Bean;
import com.gongxm.tangshi.ui.LoadingPage.LoadStatus;
import com.gongxm.tangshi.utils.UrlUtils;

public class QuFragment extends BaseFragment {
	
	private static int curPosition=1;
	private ListView listView;
	private List<Bean> items;

	@Override
	protected LoadStatus load() {
		String path = UrlUtils.getQuList(1);
		items = UrlUtils.getItems(path);
		return check(items);
	}

	@Override
	protected View createSuccessView() {
		listView = new ListView(getActivity());
		TsAdapter adapter = new TsAdapter(listView, items);
		listView.setAdapter(adapter);
		return listView;
	}
	
	class TsAdapter extends MyListAdapter{

		public TsAdapter(AbsListView mListView, List<Bean> mDatas) {
			super(mListView, mDatas);
		}
		
		@Override
		public List<Bean> onLoadMore() {
			String path = UrlUtils.getQuList(curPosition++);
			List<Bean> more = UrlUtils.getItems(path);
			return more;
		}

		@Override
		protected boolean hasMore() {
			// TODO Auto-generated method stub
			return curPosition<100;
		}
		
	}

}
