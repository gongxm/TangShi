package com.gongxm.tangshi;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongxm.tangshi.ui.LoadingPage;
import com.gongxm.tangshi.ui.LoadingPage.LoadStatus;
import com.gongxm.tangshi.utils.ApkInstaller;
import com.gongxm.tangshi.utils.Constants;
import com.gongxm.tangshi.utils.StreamUtils;
import com.iflytek.speech.ISpeechModule;
import com.iflytek.speech.InitListener;
import com.iflytek.speech.SpeechConstant;
import com.iflytek.speech.SpeechSynthesizer;
import com.iflytek.speech.SpeechUtility;
import com.iflytek.speech.SynthesizerListener;

public class DetailActivity extends Activity implements OnTouchListener,
		OnClickListener {
	private String url;
	private String context;
	private TextView detail_title;
	private TextView detail_content;
	private String title;
	private String author;
	private TextView detail_author;

	private static final long time = 5000;

	private int[] bgs = new int[] { R.drawable.bg_1, R.drawable.bg_2,
			R.drawable.bg_3, R.drawable.bg_4 };
	private ImageView iv_play;

	private Animation aa;
	private Task task;

	private SpeechSynthesizer mTts;
	private boolean isPlay = false;
	private Myhandler mHandler = new Myhandler();// 创建消息处理器

	private ProgressDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		initView();
	}

	private void initView() {
		LoadingPage loadingPage = new LoadingPage(this) {

			@Override
			public LoadStatus load() {
				return DetailActivity.this.load();
			}

			@Override
			public View createSuccessView() {
				return DetailActivity.this.createSuccessView();
			}
		};

		setContentView(loadingPage);

		loadingPage.show();
	}

	protected View createSuccessView() {
		View view = View.inflate(this, R.layout.detail, null);
		int index = new Random().nextInt(4);
		view.setBackgroundResource(bgs[index]);

		iv_play = (ImageView) view.findViewById(R.id.iv_play);
		iv_play.setOnClickListener(this);

		detail_title = (TextView) view.findViewById(R.id.detail_title);
		detail_author = (TextView) view.findViewById(R.id.detail_author);
		detail_content = (TextView) view.findViewById(R.id.detail_content);
		detail_content.setText(context);
		detail_content.setOnTouchListener(this);
		detail_title.setText(title);
		detail_author.setText(author);
		return view;
	}

	protected LoadStatus load() {
		try {
			URL contentUrl = new URL(Constants.BASE_URL + url);
			HttpURLConnection contentCon = (HttpURLConnection) contentUrl
					.openConnection();
			InputStream stream = contentCon.getInputStream();
			String string = StreamUtils.readStream(stream);
			context = string.substring(
					string.indexOf("<span>原文：</span>") + 20,
					string.indexOf(" </div>",
							string.indexOf("<span>原文：</span>")));
			int start = 0;
			int end = 0;
			List<String> list = new ArrayList<String>();
			while (start > -1) {
				start = context.indexOf("(", start + 1);
				end = context.indexOf(")", start + 1) + 1;
				if (start > -1 && end > -1) {
					String str = context.substring(start, end);
					list.add(str);
				}
			}

			start = 0;
			while (start > -1) {
				start = context.indexOf("（", start + 1);
				end = context.indexOf("）", start + 1) + 1;
				if (start > -1 && end > -1) {
					String str = context.substring(start, end);
					list.add(str);
				}
			}

			for (String str : list) {
				System.out.println(str);
				context = context.replace(str, "");
			}
			context = context.replace("<br />", "\n");
			context = context.replace("<p>", "\n");
			context = context.replace("。", "。\n");
			context = context.replace("!", "!\n");
			context = context.replace("！", "！\n");
			context = context.replace("，", ",\n");
			context = context.replace("？", "？\n");
			context = context.replace("?", "?\n");
			context = context.replace("；", ";\n");
			context = context.replace("</p>", "\n");
			context = context.replace("&nbsp;", "");
			context = context.replace("</strong>", "");
			context = context.replace("<strong>", "");
			context = context.replace(
					"<span style=\"font-family:KaiTi_GB2312;\">", "");
			context = context.replace("</span>", "");
			context = context.replace(" ", "");
			context = context.replace("　　", "");
			context = context.trim();
			context=context.replace("<divclass=\"xhe-paste\"style=\"top:0px;\"></div>", "");
			if (!TextUtils.isEmpty(context)) {
				System.out.println(context);
				return LoadStatus.SUCCESS;
			}
		} catch (Exception e) {
		}
		return LoadStatus.ERROR;
	}

	private void init() {
		Intent intent = getIntent();
		if (intent != null) {
			url = intent.getStringExtra("url");
			title = intent.getStringExtra("title");
			author = intent.getStringExtra("author");
		}
		aa = new AlphaAnimation(1.0f, 0f);
		aa.setDuration(time);
		task = new Task();
		reset();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (iv_play != null) {
				if (task != null) {
					mHandler.removeCallbacks(task);
				}
				iv_play.setVisibility(View.VISIBLE);
				iv_play.startAnimation(aa);
				mHandler.postDelayed(task, time);
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 合成回调监听。
	 */
	private SynthesizerListener mTtsListener = new SynthesizerListener.Stub() {
		@Override
		public void onBufferProgress(int progress) throws RemoteException {
			System.out.println("progress="+progress);
		}

		@Override
		public void onCompleted(int code) throws RemoteException {
			iv_play.setBackgroundResource(R.drawable.play);
			isPlay = false;
		}

		@Override
		public void onSpeakBegin() throws RemoteException {
		}

		@Override
		public void onSpeakPaused() throws RemoteException {
		}

		@Override
		public void onSpeakProgress(int progress) throws RemoteException {
		}

		@Override
		public void onSpeakResumed() throws RemoteException {
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mTts != null) {
			mTts.stopSpeaking(mTtsListener);
			// 退出时释放连接
			mTts.destory();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		onTouchEvent(event);
		return true;
	}

	class Task implements Runnable {

		@Override
		public void run() {
			runOnUiThread(new Runnable() {
				public void run() {
					iv_play.setVisibility(View.INVISIBLE);
				}
			});
		}

	}

	/**
	 * 点击图片时语音播放唐诗
	 */
	@Override
	public void onClick(View v) {
		isPlay = !isPlay;
		if (isPlay) {
			if (!checkSpeechServiceInstall()) {
				// 提示安装apk
				Builder builder = new Builder(this);
				builder.setTitle("提示：");
				builder.setMessage("如果需要使用朗读功能，需要安装语音播放插件，是否安装？");
				builder.setPositiveButton("安装",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								// 注意此处要放在show之后 否则会报异常
								Message message = new Message();
								message.what = 0;
								mHandler.sendMessage(message);
							}
						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								isPlay = false;
							}

						});
				builder.show();
				return;
			} else {
				iv_play.setBackgroundResource(R.drawable.pause);
				mTts.startSpeaking(title + "," + author + "," + context,
						mTtsListener);
			}
		} else {
			iv_play.setBackgroundResource(R.drawable.play);
			if (mTts != null)
				mTts.stopSpeaking(mTtsListener);
		}
	}

	// 判断手机中是否安装了讯飞语音+
	private boolean checkSpeechServiceInstall() {
		String packageName = "com.iflytek.speechcloud";
		List<PackageInfo> packages = getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++) {
			PackageInfo packageInfo = packages.get(i);
			if (packageInfo.packageName.equals(packageName)) {
				return true;
			} 
		}
		return false;
	}

	// Handler消息处理器
	class Myhandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog = new ProgressDialog(DetailActivity.this);
				dialog.setMessage("处理中……");
				dialog.show();
				installApk();
				break;
			case 1:
				dialog.dismiss();
				finish();
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	}

	private void installApk() {
		new Thread() {
			public void run() {
				String url = SpeechUtility.getUtility(DetailActivity.this)
						.getComponentUrl();
				String assetsApk = "SpeechService.apk";
				if (processInstall(DetailActivity.this, url, assetsApk)) {
					Message message = new Message();
					message.what = 1;
					mHandler.sendMessage(message);
				}
			};
		}.start();
	}

	/**
	 * 重置语音播放
	 */
	private void reset() {
		if (mTts != null)
			mTts.destory();
		mTts = new SpeechSynthesizer(this, mTtsInitListener);
		setParam();
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	private void setParam() {
		mTts.setParameter(SpeechConstant.ENGINE_TYPE, "local");

		mTts.setParameter(SpeechSynthesizer.VOICE_NAME, "xiaoyan");
		mTts.setParameter(SpeechSynthesizer.SPEED, "30");

		mTts.setParameter(SpeechSynthesizer.PITCH, "50");

		mTts.setParameter(SpeechSynthesizer.VOLUME, "50");
	}

	/**
	 * 初始化监听。
	 */
	private InitListener mTtsInitListener = new InitListener() {

		@Override
		public void onInit(ISpeechModule arg0, int code) {
		}
	};

	private boolean processInstall(Context context, String url, String assetsApk) {
		// 本地安装方式
		if (!ApkInstaller.installFromAssets(context, assetsApk)) {
			Toast.makeText(context, "安装失败", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
}
