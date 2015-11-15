package com.lym.twogoods.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 可显示表情的TextView
 * 
 * @author 麦灿标
 * */
public class FaceTextView extends TextView{

	public FaceTextView(Context context) {
		this(context, null);
	}
	
	public FaceTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setText(CharSequence text, BufferType type) {

		super.setText(text, type);
	}
}
