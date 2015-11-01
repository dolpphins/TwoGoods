package com.lym.twogoods.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


/**
 * <p>
 * 	高度可自适应ViewPager,v4包自带的ViewPager并不能使用android:layout_height="wrap_content"
 * 	设置自适应.
 * </p>
 * 
 * @author 麦灿标
 * */
public class WrapContentViewPager extends ViewPager {

	private final static String TAG = "WrapContentViewPager";
	
	/** 强制设置的高度,-1表示没有设置 */
	private int mRequestHeight = -1;
	
	public WrapContentViewPager(Context context) {
		this(context, null);
	}
	
	public WrapContentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/*
	 * 说明:ViewPager内部总是使用MeasureSpec.EXACTLY(即match_parent),而heightMeasureSpec传进来的mode,
	 * 其实是xml布局文件指定的模式,比如wrap_content对应MeasureSpec.AT_MOST.
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int height = 0;
	    for(int i = 0; i < getChildCount(); i++) {
	        View child = getChildAt(i);
	        child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST));
	        int h = child.getMeasuredHeight();
	        if(h > height) height = h;//寻找最大高度的子View
	    }

	    if(mRequestHeight >= 0) {
	    	height = mRequestHeight;
	    }
	    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
	  
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * <p>
	 * 	强制设置ViewPager的高度
	 * </p>
	 * 
	 * @param height 要设置的高度,注意必须大于0
	 * */
	public void requestForceHeight(int height) {
		if(height < 0) {
			return;
		}
		mRequestHeight = height;
		requestLayout();
	}
	
}
