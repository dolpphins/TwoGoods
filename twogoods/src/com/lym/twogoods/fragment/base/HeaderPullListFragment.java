package com.lym.twogoods.fragment.base;

import com.lym.twogoods.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <p>带头部及ListView列表且可下拉刷新(上拉加载)Fragment</p>
 * 
 * @author 麦灿标
 * */
public abstract class HeaderPullListFragment extends PullListFragment{

	/** 头部布局 */
	protected View mHeaderView;
	
	private LinearLayout wrapper;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		mHeaderView = onCreateHeaderView();
		wrapper = (LinearLayout) inflater.inflate(R.layout.app_header_listview_decorview, null);
		if(mHeaderView != null) {
			wrapper.addView(mHeaderView);
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		wrapper.addView(v, params);
		
		return wrapper;
	}
	
	/**
	 * <p>
	 * 	创建头部View,子类必须实现该方法
	 * </p>
	 * 
	 * @return 返回头部布局View对象,如果为null则表示不设置头部布局
	 * */
	protected abstract View onCreateHeaderView();
	
	
}
