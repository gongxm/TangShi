package com.gongxm.tangshi;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.gongxm.tangshi.fragments.BaseFragment;
import com.gongxm.tangshi.fragments.FragmentFactory;
import com.gongxm.tangshi.ui.PagerTab;
import com.iflytek.speech.SpeechUtility;

public class MainActivity extends FragmentActivity {
	protected static final int SUCCESS = 0;
	protected static final int ERROR = 1;
	private ViewPager view_pager;
	private String[] titles;
	private MyAdapter adapter;
	
	public static MainActivity instance;
	private PagerTab indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance=this;
		setContentView(R.layout.activity_main);
		initData();
		initView();
		// 设置你申请的应用appid
		SpeechUtility.getUtility(MainActivity.this).setAppid("54465ef9");
	}

	private void initView() {
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new MyAdapter(getSupportFragmentManager());
		view_pager.setAdapter(adapter);
		
		indicator = (PagerTab) findViewById(R.id.tabs);
		indicator.setViewPager(view_pager);
		indicator.setOnPageChangeListener(new MyPageListener());
	}

	private void initData() {
		titles = new String[] { "唐诗", "宋词", "元曲" };
	}

	class MyAdapter extends FragmentPagerAdapter {
		
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return titles.length;
		}


		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public Fragment getItem(int position) {
			return FragmentFactory.createFragment(position);
		}
	}
	
	class MyPageListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int position) {
			BaseFragment fragment=FragmentFactory.createFragment(position);
			fragment.show();
		}
	}
	
	
	@Override
	public void onBackPressed() {
		Builder builder=new Builder(this);
		builder.setTitle("提示：");
		builder.setMessage("是否要退出？");
		builder.setPositiveButton("退出", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		builder.setNegativeButton("取消",new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
}
