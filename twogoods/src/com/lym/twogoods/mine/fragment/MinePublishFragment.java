package com.lym.twogoods.mine.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lym.twogoods.adapter.StoreDetailGoodsListAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.local.bean.LocalGoods;
import com.lym.twogoods.mine.adapter.MinePublishGoodsListAdapter;
import com.lym.twogoods.network.DefaultOnLoaderListener;
import com.lym.twogoods.network.ListViewLoader;
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
	
	private ListViewLoader mListViewLoader;
	private BaseGoodsListAdapter mAdapter;
	private List<Goods> mGoodsList;
	private int perPageCount = 10;
	private ListViewLoader.OnLoaderListener mOnLoaderListener;
	
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
		mListViewLoader = new ListViewLoader(mAttachActivity, mListView, mAdapter, mGoodsList);
		mOnLoaderListener = new DefaultOnLoaderListener(this, mListViewLoader);
		mListViewLoader.setOnLoaderListener(mOnLoaderListener);
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
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.setSkip(0);
		query.setLimit(perPageCount);
		query.addWhereEqualTo("username", mUsername);
		mListViewLoader.requestLoadData(query, null, true, true);
	}
	
	@Override
	public void onRefresh() {
		super.onRefresh();
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.setSkip(0);
		query.setLimit(perPageCount);
		query.addWhereEqualTo("username", mUsername);
		mListViewLoader.requestLoadData(query, null, true, false);
	}
}
