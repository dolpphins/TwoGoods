package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.adapter.NearbyAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.GoodsCategory;
import com.lym.twogoods.fragment.base.PullGridViewFragment;
import com.lym.twogoods.index.manager.GoodsSortManager;
import com.lym.twogoods.network.AbsListViewLoader;
import com.lym.twogoods.network.ListViewOnLoaderListener;

import cn.bmob.v3.BmobQuery;


/**
 * <p>
 * 	附近Fragment
 * </p>
 * 
 * @author 麦灿标
 * */
public class NearbyFragment extends PullGridViewFragment{

	private static int mGridViewColumnNum = 2;
	
	/**
	 * 商品列表加载器相关
	 * */
	private AbsListViewLoader mAbsListViewLoader;
	private BaseGoodsListAdapter mAdapter;
	private List<Goods> mGoodsList;
	private int perPageCount = 10;
	private AbsListViewLoader.OnLoaderListener mOnLoaderListener;
	
	@Override
	protected void onCreateViewAfter() {
		super.onCreateViewAfter();
		
		mGoodsList = new ArrayList<Goods>();
		mAdapter = new NearbyAdapter(mAttachActivity, mGoodsList, this);
		mAbsListViewLoader = new AbsListViewLoader(mAttachActivity, mGridView.getAbsListView(), mAdapter, mGoodsList);
		mOnLoaderListener = new ListViewOnLoaderListener(this, mAbsListViewLoader, mListView);
		mAbsListViewLoader.setOnLoaderListener(mOnLoaderListener);
		mGridView.setNumColumns(mGridViewColumnNum);
		mGridView.setAdapter(mAdapter);
		
		loadDataInit();
	}
	
	private void loadDataInit() {
		reloadData(true);
	}
	
	private void reloadData(boolean isInit) {
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.setSkip(0);
		query.setLimit(perPageCount);
//		String order = GoodsSortManager.getBmobQueryOrderString(mCurrentGoodsSort);
//		query.order(order);
		mAbsListViewLoader.requestLoadData(query, null, true, isInit);
	}
}
