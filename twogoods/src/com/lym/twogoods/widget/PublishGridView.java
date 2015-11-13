package com.lym.twogoods.widget;

/**
 * <p>
 * 	发布商品图片展示GridView
 * </p>
 * @author 龙宇文
 * */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class PublishGridView extends GridView {

	public PublishGridView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PublishGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public PublishGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	/*
	 * 
	 * 使其不能滑动
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
