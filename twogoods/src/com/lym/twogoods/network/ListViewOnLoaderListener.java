package com.lym.twogoods.network;

import java.util.List;

import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.BaseListFragment;

import me.maxwin.view.XListView;

/**
 * ListView加载监听器
 * 
 * @author 麦灿标
 *
 */
public class ListViewOnLoaderListener extends DefaultOnLoaderListener {

	private final static String TAG = "ListViewOnLoaderListener";
	
	private XListView mListView;
	
	public ListViewOnLoaderListener(BaseListFragment fragment, AbsListViewLoader loader, XListView listView) {
		super(fragment, loader);
		mListView = listView;
	}
	
	@Override
	public void onRefreshFinish(boolean success) {
		super.onRefreshFinish(success);
		if(mListView != null) {
			mListView.stopRefresh();
		}
	}
	
	@Override
	public void onLoadMoreFinish(boolean success) {
		super.onLoadMoreFinish(success);
		if(mListView != null) {
			mListView.stopLoadMore();
		}
	}
}
