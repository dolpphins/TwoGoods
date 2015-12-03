package com.lym.twogoods.network;

import com.lym.twogoods.fragment.base.BaseListFragment;

import me.maxwin.view.XGridView;

/**
 * GridView形式显示的数据加载监听器
 * 
 * @author 麦灿标
 *
 */
public class GridViewOnLoaderListener extends DefaultOnLoaderListener {

	private XGridView mGridView;
	
	public GridViewOnLoaderListener(BaseListFragment fragment, AbsListViewLoader loader, XGridView gridView) {
		super(fragment, loader);
		mGridView = gridView;
	}

}
