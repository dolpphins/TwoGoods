package com.lym.twogoods.network;

import java.util.List;

import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.BaseListFragment;

import android.util.Log;
import me.maxwin.view.XGridView;

/**
 * GridView形式显示的数据加载监听器
 * 
 * @author 麦灿标
 *
 */
public class GridViewOnLoaderListener extends DefaultOnLoaderListener {

	private final static String TAG = "GridViewOnLoaderListener";
	
	private XGridView mGridView;
	
	public GridViewOnLoaderListener(BaseListFragment fragment, AbsListViewLoader loader, XGridView gridView) {
		super(fragment, loader);
		mGridView = gridView;
	}

	@Override
	public void onLoaderSuccess(List<Goods> goodsList) {
		super.onLoaderSuccess(goodsList);
		mGridView.stopRefresh();
	}
	
	@Override
	public void onLoaderFail() {
		super.onLoaderFail();
		mGridView.stopRefresh();
	}
}
