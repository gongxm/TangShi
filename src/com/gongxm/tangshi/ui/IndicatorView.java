package com.gongxm.tangshi.ui;

import com.gongxm.tangshi.MainActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

public class IndicatorView extends View {
	private static final int POSITION_NONE = -1;

	private Drawable mDrbIndicator;
	private int mCount;
	private int mSelection;
	private int mInterval;

	public IndicatorView(Context context) {
		super(context);
		init();
	}

	/** 初始�?*/
	private void init() {
		mSelection = POSITION_NONE;
	}

	/** 设置数目 */
	public void setCount(int count) {
		count = count > 0 ? count : 0;
		if (count != mCount) {
			mCount = count;
			requestLayoutInner();
			requestInvalidate();
		}
	}

	/** 设置选中�?*/
	public void setSelection(int selection) {
		if (selection != mSelection) {
			mSelection = selection;
			requestInvalidate();
		}
	}

	/** 设置选中项的图片 */
	public void setIndicatorDrawable(Drawable drawable) {
		mDrbIndicator = drawable;
		requestLayoutInner();
		requestInvalidate();
	}

	/** 设置item之间间隔 */
	public void setInterval(int interval) {
		if (interval != mInterval) {
			mInterval = interval;
			requestLayoutInner();
			requestInvalidate();
		}
	}

	private void requestInvalidate() {
			postInvalidate();
	}

	private void requestLayoutInner() {
		MainActivity.instance.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				requestLayout();
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width;
		int height;

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		// 计算宽度
		if (widthMode == MeasureSpec.EXACTLY) {//如果是精确的，就采用精确�?
			width = widthSize;
		} else {//否则就采用图片的宽度
			int indicatorW = mDrbIndicator == null ? 0 : mDrbIndicator.getIntrinsicWidth();
			int expectedW = indicatorW * mCount + mInterval * (mCount - 1) + getPaddingLeft() + getPaddingRight();
			if (widthMode == MeasureSpec.AT_MOST) {
				width = Math.min(expectedW, widthSize);
			} else {
				width = expectedW;
			}
		}
		// 计算高度
		if (heightMode == MeasureSpec.EXACTLY) {//如果是精确的，就采用精确�?
			height = heightSize;
		} else {//否则就采用图片的高度
			int indicatorH = mDrbIndicator == null ? 0 : mDrbIndicator.getIntrinsicHeight();
			int expectedH = indicatorH + getPaddingTop() + getPaddingBottom();
			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(expectedH, heightSize);
			} else {
				height = expectedH;
			}
		}
		setMeasuredDimension(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (mDrbIndicator == null || mCount == 0) {
			return;
		}
		int w = mDrbIndicator.getIntrinsicWidth();
		int h = mDrbIndicator.getIntrinsicHeight();
		int horizontalSideSpacing = (getWidth() - getPaddingLeft() - getPaddingRight() - w * mCount - mInterval * (mCount - 1)) / 2;
		int verticalSideSpacing = (getHeight() - getPaddingTop() - getPaddingBottom() - h) / 2;
		int l = getPaddingLeft() + horizontalSideSpacing;
		int t = getPaddingTop() + verticalSideSpacing;
		int rEdge = getRight() - getPaddingRight();
		int bEdge = getBottom() - getPaddingBottom();
		//计算出间隙和范围，然后画图片，实际画的是同一张图片，只是改变图片的bounds
		for (int i = 0; i < mCount; i++) {
			mDrbIndicator.setBounds(l, t, Math.min(l + w, rEdge), Math.min(t + h, bEdge));
			if (i == mSelection) {
				mDrbIndicator.setState(new int[]{android.R.attr.state_selected});
			} else {
				mDrbIndicator.setState(null);
			}
			mDrbIndicator.draw(canvas);
			l += w + mInterval;
		}
	}
}
