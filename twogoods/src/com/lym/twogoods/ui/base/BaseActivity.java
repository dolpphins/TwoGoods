package com.lym.twogoods.ui.base;

import com.lym.twogoods.R;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>该项目所有Activity的直接或者间接父类.</p>
 * <p>继承自该类的Activity都会有一个ActionBar,你可以通过调用{@link Window#requestFeature(int)}
 * 设置自定义窗口样式</p>
 * 
 * <p>注意该类为抽象类,不可实例化</p>
 * 
 * @author 麦灿标
 * */
public abstract class BaseActivity extends Activity{

	private final static String TAG = "BaseActivity";
	
	/** ActionBar对应的View */
	private View mActionBarView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		//初始化ActionBar
		ActionBar actionBar = getActionBar();
		if(actionBar != null) {
			//所有Option都设置为false
			actionBar.setDisplayOptions(0);
			//显示自定义View
			actionBar.setDisplayShowCustomEnabled(true);
			mActionBarView = getLayoutInflater().inflate(R.layout.app_common_actionbar, null);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			actionBar.setCustomView(mActionBarView, lp);
		}
		//设置只允许竖屏
		if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	
	/**
	 * <p>
	 * 	设置ActionBar左边图片
	 * </p>
	 * 
	 * @param drawable 要设置的图片
	 * */
	public void setLeftDrawable(Drawable drawable) {
		if(mActionBarView != null) {
			ImageView actionBarLeftImage = (ImageView) mActionBarView.findViewById(R.id.app_common_actionbar_left_iv);
			if(actionBarLeftImage != null) {
				actionBarLeftImage.setVisibility(View.VISIBLE);//可能为不可见
				actionBarLeftImage.setImageDrawable(drawable);
			}
		}
	}
	
	/**
	 * <p>
	 * 	设置ActionBar左边的标题文字
	 * </p>
	 * 
	 * @param title 要设置的内容,如果为null相当于""
	 * */
	public void setLeftTitle(String title) {
		if(mActionBarView != null) {
			TextView actionBarLeftTitle = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_left_tv);
			if(actionBarLeftTitle != null) {
				actionBarLeftTitle.setVisibility(View.VISIBLE);//可能为不可见
				actionBarLeftTitle.setText(title);
			}
		}
	}
	
	/**
	 * <p>
	 * 	设置ActionBar中央图片
	 * </p>
	 * 
	 * @param drawable 要设置的图片
	 * */
	public void setCenterDrawable(Drawable drawable) {
		if(mActionBarView != null) {
			ImageView actionBarCenterImage = (ImageView) mActionBarView.findViewById(R.id.app_common_actionbar_center_iv);
			if(actionBarCenterImage != null) {
				actionBarCenterImage.setVisibility(View.VISIBLE);//可能为不可见
				actionBarCenterImage.setImageDrawable(drawable);
			}
		}
	}
	
	/**
	 * <p>
	 * 	设置ActionBar中央的标题文字
	 * </p>
	 * 
	 * @param title 要设置的内容,如果为null相当于""
	 * */
	public void setCenterTitle(String title) {
		if(mActionBarView != null) {
			TextView actionBarCenterTitle = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_center_tv);
			if(actionBarCenterTitle != null) {
				actionBarCenterTitle.setVisibility(View.VISIBLE);//可能为不可见
				actionBarCenterTitle.setText(title);
			}
		}
	}
	
	/**
	 * <p>
	 * 	设置ActionBar右边图片
	 * </p>
	 * 
	 * @param drawable 要设置的图片
	 * */
	public void setRightDrawable(Drawable drawable) {
		if(mActionBarView != null) {
			ImageView actionBarRightImage = (ImageView) mActionBarView.findViewById(R.id.app_common_actionbar_right_iv);
			if(actionBarRightImage != null) {
				actionBarRightImage.setVisibility(View.VISIBLE);//可能为不可见
				actionBarRightImage.setImageDrawable(drawable);
			}
		}
	}
	
	/**
	 * <p>
	 * 	设置ActionBar右边的标题文字
	 * </p>
	 * 
	 * @param title 要设置的内容,如果为null相当于""
	 * */
	public void setRightTitle(String title) {
		if(mActionBarView != null) {
			TextView actionBarRightTitle = (TextView) mActionBarView.findViewById(R.id.app_common_actionbar_right_tv);
			if(actionBarRightTitle != null) {
				actionBarRightTitle.setVisibility(View.VISIBLE);//可能为不可见
				actionBarRightTitle.setText(title);
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
