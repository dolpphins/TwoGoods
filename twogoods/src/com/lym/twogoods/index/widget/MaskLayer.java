package com.lym.twogoods.index.widget;

import com.lym.twogoods.index.interf.DropDownAble;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * <p>
 * 	首页遮罩层
 * </p>
 * 
 * @author 麦灿标
 * */
public class MaskLayer extends View {

	private final static String TAG = "MaskLayer";
	
	/** 显示时颜色 */
	private final int SHOW_COLOR = Color.parseColor("#aa000000");
	
	/** 隐藏时颜色 */
	private int HIDE_COLOR = Color.parseColor("#00000000");
	
	/** 标记该View是被正在被显示 */
	private boolean isShowing = false;
	
	/**  */
	private DropDownAble d;
	
	public MaskLayer(Context context) {
		this(context, null);
	}
	
	public MaskLayer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * <p>
	 * 	显示该遮罩层
	 * </p>
	 * */
	public void show() {
		setBackgroundColor(SHOW_COLOR);
		isShowing = true;
	}
	
	/**
	 * <p>
	 * 	隐藏该遮罩层
	 * </p>
	 * */
	public void hide() {
		setBackgroundColor(HIDE_COLOR);
		isShowing = false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isShowing && d != null) {
			d.hideAllDropdownAnimation();
			hide();
			return true;
		}
		return super.onTouchEvent(event);
	}
	
	/**
	 * <p>
	 * 	设置触摸监听器,注意该监听器独立于View{@link #setOnTouchListener(OnTouchListener)}
	 * </p>
	 * 
	 * @param d 要设置的监听器
	 * */
	public void setOnTouchDropDownAbleListener(DropDownAble d) {
		this.d = d;
	}
	
}
