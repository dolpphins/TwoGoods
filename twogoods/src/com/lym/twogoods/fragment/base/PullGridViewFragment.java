package com.lym.twogoods.fragment.base;

import com.lym.twogoods.R;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import me.maxwin.view.XGridView;

/**
 * 带GridView可下拉刷新的Fragment
 * 
 * @author 麦灿标
 *
 */
public class PullGridViewFragment extends BaseListFragment {

	protected XGridView mGridView;
	
	/** 水平方向上占用的额外宽度 */
	private int mHorizontalExtraDistance;
		
	
	@Override
	protected View onCreateContentView() {
		
		//将默认的XListView换成XGridView
		mGridView = new XGridView(mAttachActivity.getApplicationContext());
		initGridView();
		
		return mGridView;
	}
	
	private void initGridView() {
		int horizontalSpacing = (int) mAttachActivity.getResources().getDimension(R.dimen.app_base_goods_gridview_horizontalSpacing);
		int verticalSpacing = (int) mAttachActivity.getResources().getDimension(R.dimen.app_base_goods_gridview_verticalSpacing);
		int padding = (int) mAttachActivity.getResources().getDimension(R.dimen.app_base_goods_gridview_padding);
		mGridView.setHorizontalSpacing(horizontalSpacing);
		mGridView.setVerticalSpacing(verticalSpacing);
		mGridView.setPadding(padding, 0, padding, 0);
		mHorizontalExtraDistance = horizontalSpacing + 2 * padding;
		mGridView.setVerticalScrollBarEnabled(false);//隐藏滚动条
	}
	
	/**
	 * 获取水平方向上额外宽度,即总宽度 - Item占用的宽度
	 * 
	 * @return 返回水平方向上额外宽度
	 */
	public int getHorizontalExtraDistance() {
		return mHorizontalExtraDistance;
	}
	
	@Override
	protected boolean requestDelayShowAbsListView() {
		return true;
	}
}
