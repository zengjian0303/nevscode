package com.nevs.car.tools.view.safecode;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;

import com.nevs.car.R;


/**
 * 提示Dialog
 * 
 * @author LanYan
 *
 */
public class KeyBoardDialog extends Dialog {
	Activity activity;
	private View view;
	private boolean isOutSideTouch = false;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public boolean isOutSideTouch() {
		return isOutSideTouch;
	}

	public void setOutSideTouch(boolean isOutSideTouch) {
		this.isOutSideTouch = isOutSideTouch;
	}

	public KeyBoardDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public KeyBoardDialog(Context context) {
		this(context, 0);
		// TODO Auto-generated constructor stub
	}

	public KeyBoardDialog(Activity activity, View view) {
		super(activity, R.style.MyDialog);
		this.activity = activity;
		this.view = view;
	}

	public KeyBoardDialog(Activity activity, View view, int theme) {
		super(activity, theme);
		this.activity = activity;
		this.view = view;
	}

	public KeyBoardDialog(Activity activity, View view, int theme, boolean isOutSide) {
		super(activity, theme);
		this.activity = activity;
		this.view = view;
		this.isOutSideTouch = isOutSide;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(view);
		setCanceledOnTouchOutside(isOutSideTouch);//点屏幕
		setCancelable(false);//back键
		DisplayMetrics dm = new DisplayMetrics();
//		// 取得窗口属性
//		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//		// 窗口的宽度
//		int screenWidth = dm.widthPixels;
//		int screenHeight = dm.heightPixels;
//		WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
////		layoutParams.width = screenWidth;
////		layoutParams.height = screenHeight - 60;
//		layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//		layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//		this.getWindow().setAttributes(layoutParams);

		getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

	}

}
