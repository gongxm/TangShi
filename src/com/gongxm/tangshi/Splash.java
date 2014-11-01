package com.gongxm.tangshi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class Splash extends Activity {
	
	private Handler handler;
	private Task task;
	private long time=2000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Animation aa=new AlphaAnimation(0f, 1.0f);
		aa.setDuration(time);
		View view=View.inflate(getApplicationContext(), R.layout.activity_splash, null);
		view.startAnimation(aa);
		setContentView(view);
		handler = new Handler();
		task=new Task();
		handler.postDelayed(task, time);
	}
	
	class Task implements Runnable{

		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					startActivity(new Intent(Splash.this,MainActivity.class));
					finish();
				}
			});
		}
	}
	
	@Override
	public void onBackPressed() {
		handler.removeCallbacks(task);
		super.onBackPressed();
	}

}
