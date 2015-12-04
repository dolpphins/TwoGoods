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
	public void onLoadMoreStart() {
		super.onLoadMoreStart();
	}
	
	@Override
	public void onLoaderFail() {
		super.onLoaderFail();
		stopRefreshAndLoadMore();
	}
	
	@Override
	public void onLoaderSuccess(List<Goods> goodsList) {
		super.onLoaderSuccess(goodsList);
		stopRefreshAndLoadMore();
	}

	private void stopRefreshAndLoadMore() {
		if(mListView != null) {
			mListView.stopRefresh();
			mListView.stopLoadMore();
		}
	}
}
