package com.lym.twogoods.fragment.base;

import com.lym.twogoods.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import me.maxwin.view.XListView;

/**
 * <p>
 * 	带ListView列表的Fragment.
 * </p>
 * 
 * @author 麦灿标
 * */
public abstract class BaseListFragment extends BaseFragment {

	private final static String TAG = "BaseListFragment";
	
	/** ListView列表 */
	protected XListView mListView;
	
	/** ListView适配器 */
	protected ListAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mListView = (XListView) inflater.inflate(R.layout.app_basefragment_listview, null);
		//在onCreateView方法调用前就已经给mAdapter赋值
		if(mAdapter != null) {
			mListView.setAdapter(mAdapter);
		}
		configListView();
		
		return mListView;
	}
	
	//子类可以重写该方法自定义ListView配置
	protected void configListView() {
		//不可上拉加载
		mListView.setPullLoadEnable(false);
		//不可下拉刷新
		mListView.setPullRefreshEnable(false);
	}
	
	/**
	 * 设置适配器,注意如果为adapter为null不进行任何操作
	 * 
	 * @param adapter 要设置的适配器
	 * */
	protected void setAdapter(ListAdapter adapter) {
		if(adapter != null) {
			mAdapter = adapter;
			mListView.setAdapter(mAdapter);
		}
	}
}
