package com.stk.view;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.stk.eschool.R;

public class MyProgressDialog extends ProgressDialog {
	private AnimationDrawable mAnimation;
	private ImageView mImageView;
	private TextView mTextView;
	private String loadingTip;
	private int resid;

	public MyProgressDialog(Context context) {
		super(context);
	}

	public MyProgressDialog(Context context, String content, int resid) {
		super(context);
		this.loadingTip = content;
		this.resid = resid;
		// 点击提示框外面是否取消提示框
		setCanceledOnTouchOutside(false);
		setIndeterminate(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialogview);
		mTextView = (TextView) findViewById(R.id.loadingTv);
		mImageView = (ImageView) findViewById(R.id.loadingIv);
		mImageView.setBackgroundResource(resid);
		// 通过ImageView对象拿到背景显示的AnimationDrawable
		mAnimation = (AnimationDrawable) mImageView.getBackground();
		mImageView.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});
		mTextView.setText(loadingTip);
	}

}
