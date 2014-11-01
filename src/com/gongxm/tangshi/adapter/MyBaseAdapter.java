package com.gongxm.tangshi.adapter;

import java.util.List;
import java.util.concurrent.Executors;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.gongxm.tangshi.MainActivity;
import com.gongxm.tangshi.fragments.MoreHolder;
import com.gongxm.tangshi.holder.BaseHolder;

public abstract class MyBaseAdapter<T> extends BaseAdapter implements OnItemClickListener {
	public static final int MORE_VIEW_TYPE = 0;
	public static final int ITEM_VIEW_TYPE = 1;
	protected AbsListView mListView;// 和该adapter关联的listView
	private List<T> mDatas;// adapter的数据集
	private volatile boolean mIsLoading;

	private MoreHolder mHolder;

	public MyBaseAdapter(AbsListView mListView, List<T> mDatas) {
		super();
		this.mListView = mListView;
		if (mListView != null) {
			mListView.setOnItemClickListener(this);
		}
		setData(mDatas);
	}

	public void setData(List<T> datas) {
		mDatas = datas;
	}

	public List<T> getData() {
		return mDatas;
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size() + 1;
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (mDatas != null && position < mDatas.size()) {
			return mDatas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		if (position == getCount() - 1) {
			return MORE_VIEW_TYPE;
		}
		return ITEM_VIEW_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder<T> holder;
		if (convertView != null && convertView.getTag() instanceof BaseHolder) {
			holder = (BaseHolder<T>) convertView.getTag();
		} else {
			if (getItemViewType(position) == MORE_VIEW_TYPE) {
				holder = getMoreHolder();
			} else {
				holder = getHolder();
			}
		}
		
		if(getItemViewType(position)==ITEM_VIEW_TYPE){
			holder.setDatas(mDatas.get(position));
		}
		return holder.getView();
	}

	protected abstract BaseHolder<T> getHolder();

	private BaseHolder getMoreHolder() {
		if (mHolder == null) {
			mHolder = new MoreHolder(this,hasMore());
		}
		return mHolder;
	}

	/**
	 * 是否还可以加载更多
	 * 
	 * @return
	 */
	protected abstract boolean hasMore();
	
	
	
	
	public void loadMore() {
		//防止重复加载
		if (!mIsLoading) {
			mIsLoading = true;
			Executors.newSingleThreadExecutor().execute(new Runnable() {
				@Override
				public void run() {
					final List<T> datas = onLoadMore();
					MainActivity.instance.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (datas == null) {
								getMoreHolder().setDatas(MoreHolder.ERROR);
							} else if (datas.size() < 10) {
								getMoreHolder().setDatas(MoreHolder.NO_MORE);
							} else {
								getMoreHolder().setDatas(MoreHolder.HAS_MORE);
							}
							if (datas != null) {
								if (getData() != null) {
									getData().addAll(datas);
								} else {
									setData(datas);
								}
							}
							notifyDataSetChanged();
							mIsLoading = false;
						}
					});
				}
			});
		}
	}
	
	
	public List<T> onLoadMore() {
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
	}

}
