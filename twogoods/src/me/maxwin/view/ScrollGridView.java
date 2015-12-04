package me.maxwin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 提供一系列与滚动有关的方法的GridView
 * 
 * @author 麦灿标
 *
 */
public class ScrollGridView extends GridView {

	public ScrollGridView(Context context) {
		this(context, null);
	}
	
	public ScrollGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public int computeVerticalScrollOffset() {
		// TODO Auto-generated method stub
		return super.computeVerticalScrollOffset();
	}
}
