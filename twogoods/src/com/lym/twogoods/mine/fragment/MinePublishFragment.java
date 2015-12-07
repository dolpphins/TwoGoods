package com.lym.twogoods.mine.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsListViewAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.index.manager.GoodsSortManager;
import com.lym.twogoods.index.manager.GoodsSortManager.GoodsSort;
import com.lym.twogoods.mine.adapter.MinePublishGoodsListAdapter;
import com.lym.twogoods.network.AbsListViewLoader;
import com.lym.twogoods.network.DefaultOnLoaderListener;
import com.lym.twogoods.network.ListViewOnLoaderListener;
import com.lym.twogoods.ui.GoodsDetailActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;

/**
 * 我发布的商品Fragment
 * 
 * @author 麦灿标
 * */
public class MinePublishFragment extends PullListFragment {

	/** 当前用户名 */
	private String mUsername;
	
	private AbsListViewLoader mAbsListViewLoader;
	private BaseGoodsListViewAdapter mAdapter;
	private List<Goods> mGoodsList;
	private int perPageCount = 10;
	private AbsListViewLoader.OnLoaderListener mOnLoaderListener;
	
	/**
	 * 构造函数
	 * 
	 * @param username 用户名
	 */
	public MinePublishFragment(String username) {
		mUsername = username;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		initListView();
		
		loadDataInit();
		
		return v;
	}
	
	private void initListView() {
		setMode(Mode.PULLDOWN);
		
		mGoodsList = new ArrayList<Goods>();
		mAdapter = new MinePublishGoodsListAdapter(mAttachActivity, mGoodsList);
		mAbsListViewLoader = new AbsListViewLoader(mAttachActivity, mListView, mAdapter, mGoodsList);
		mOnLoaderListener = new ListViewOnLoaderListener(this, mAbsListViewLoader, mListView);
		mAbsListViewLoader.setOnLoaderListener(mOnLoaderListener);
		mListView.setAdapter(mAdapter);
		
		if(mListView != null) {
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(mAttachActivity, GoodsDetailActivity.class);
					intent.putExtra("goods", mGoodsList.get(position - 1));
					startActivity(intent);
				}
			});
		}
	}
	
	//加载初始化数据
	private void loadDataInit() {
		reloadData(AbsListViewLoader.Type.INIT, true);
	}
	
	@Override
	public void onRefresh() {
		reloadData(AbsListViewLoader.Type.REFRESH, false);
	}
	
	private void reloadData(AbsListViewLoader.Type type, boolean clear) {
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.setSkip(0);
		query.setLimit(perPageCount);
		query.addWhereEqualTo("username", mUsername);
		String order = "-" + GoodsSortManager.getColumnString(GoodsSort.NEWEST_PUBLISH);
		query.order(order);
		mAbsListViewLoader.requestLoadData(query, null, clear, type);
	}
	
	@Override
	protected boolean requestDelayShowAbsListView() {
		return true;
	}
}
