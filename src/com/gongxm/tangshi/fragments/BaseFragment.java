package com.gongxm.tangshi.fragments;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gongxm.tangshi.MainActivity;
import com.gongxm.tangshi.ui.LoadingPage;
import com.gongxm.tangshi.ui.LoadingPage.LoadStatus;

public abstract class BaseFragment extends Fragment {
	private Activity mActivity=MainActivity.instance;
	private LoadingPage mLoadingPage;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mLoadingPage == null) {
			mLoadingPage = new LoadingPage(mActivity) {
				@Override
				public View createSuccessView() {
					return BaseFragment.this.createSuccessView();
				}

				@Override
				public LoadStatus load() {
					return BaseFragment.this.load();
				}
			};
		} else {
			ViewGroup vg=(ViewGroup) (mLoadingPage.getParent());
			vg.removeView(mLoadingPage);
		}
		return mLoadingPage;
	}
	
	public void show(){
		if(mLoadingPage!=null){
			mLoadingPage.show();
		}
	}

	public LoadStatus check(Object obj) {
		if (obj == null) {
			return LoadStatus.ERROR;
		}
		if (obj instanceof List) {
			List list = (List) obj;
			if (list.size() == 0) {
				return LoadStatus.EMPTY;
			}
		}
		return LoadStatus.SUCCESS;
	}

	protected abstract LoadStatus load();

	protected abstract View createSuccessView();
}
