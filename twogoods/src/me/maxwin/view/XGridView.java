package me.maxwin.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class XGridView extends LinearLayout{

	private final static String TAG = "XGridView";
	
	private XListViewHeader mHeaderView;
	private GridView mGridView;
	
	public XGridView(Context context) {
		this(context, null);
	}
	
	public XGridView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public XGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		//设置为垂直方向,默认为水平方向
		setOrientation(LinearLayout.VERTICAL);
		
		mHeaderView = new XListViewHeader(context);
		mGridView = new GridView(context);
		
		removeAllViews();
		addView(mHeaderView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
		addView(mGridView, android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
	}
	
	/**
	 * 设置适配器
	 * 
	 * @param adapter 要设置的适配器
	 */
	public void setAdapter(ListAdapter adapter) {
		mGridView.setAdapter(adapter);
	}
	
	/**
	 * 设置列数
	 * 
	 * @param numColumns 指定的列数
	 */
	public void setNumColumns(int numColumns) {
		mGridView.setNumColumns(numColumns);
	}
	
	/**
	 * 设置伸展模式
	 * 
	 * @param stretchMode 指定的设置模式
	 */
	public void setStretchMode(int stretchMode) {
		mGridView.setStretchMode(stretchMode);
	}
	
	/**
	 * 获取与该GridView相关联的AbsListView对象
	 * 
	 * @return 返回与该GridView相关联的AbsListView对象
	 */
	public AbsListView getAbsListView() {
		return mGridView;
	}
	
	/**
	 * 设置水平方向上Item之间的间距
	 * 
	 * @param horizontalSpacing 指定的水平方向上Item之间的间距
	 */
	public void setHorizontalSpacing(int horizontalSpacing) {
		mGridView.setHorizontalSpacing(horizontalSpacing);
	}
	
	/**
	 * 设置两行之间的间距
	 * 
	 * @param verticalSpacing 指定的两行之间的间距
	 */
	public void setVerticalSpacing(int verticalSpacing) {
		mGridView.setVerticalSpacing(verticalSpacing);
	}
	
	/**
	 * 设置滚动条可不可用
	 * 
	 * @param verticalScrollBarEnabled true表示可用,false表示不可用
	 */
	public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
		mGridView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
	}
}
