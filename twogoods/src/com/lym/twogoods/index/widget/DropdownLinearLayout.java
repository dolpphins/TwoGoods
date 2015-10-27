package com.lym.twogoods.index.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * <p>
 * 	首页下拉布局,该布局继承自LinearLayout,具有根据指定条件拦截事件的功能
 * </p>
 * 
 * @author 麦灿标
 * */
public class DropdownLinearLayout extends LinearLayout {

	private final static String TAG = "DropdownLinearLayout";
	
	private boolean mAllowDispatchTouchEvent = false;
	
	public DropdownLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mAllowDispatchTouchEvent;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return false;
	}
	
	/**
	 * <p>
	 * 	设置是否允许将触摸事件分发给子View,注意该ViewGroup并不消耗该事件
	 * </p>
	 * 
	 * @param allow true表示允许分发触摸事件,false表示不允许分发触摸事件
	 * */
	public void requestAllowDispatchTouchEvent(boolean allow) {
		mAllowDispatchTouchEvent = allow ? false : true;
	}
}
