package com.lym.twogoods.index.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.HeaderListFragment;
import com.lym.twogoods.index.adapter.IndexGoodsListAdapter;
import com.lym.twogoods.network.AbsListViewLoader;
import com.lym.twogoods.network.ListViewOnLoaderListener;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;


/**
 * 显示商品搜索结果Fragment
 * 
 * @author 麦灿标
 * */
public class GoodsSearchResultFragment extends HeaderListFragment {

	private String keyword;
	
	/**
	 * 头部相关
	 * */
	private View mHeaderView;
	private EditText index_goods_search_edittext;
	private TextView index_goods_search_button;
	
	private List<Goods> mSearchGoodsList;
	private IndexGoodsListAdapter mGoodsListAdapter;
	private int perPageCount = 10;
	private AbsListViewLoader mAbsListViewLoader; 
	private ListViewOnLoaderListener mOnLoaderListener;
	
	public GoodsSearchResultFragment(String keyword) {
		this.keyword = keyword;
	}
	
	@Override
	protected View onCreateHeaderView() {
		mHeaderView = LayoutInflater.from(mAttachActivity).inflate(R.layout.index_goods_search_header, null);
		
		initHeaderView();
		
		return mHeaderView;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		initListView();
		
		loadDataFromNetwork();
		
		return v;
	}

	private void initHeaderView() {
		index_goods_search_edittext = (EditText) mHeaderView.findViewById(R.id.index_goods_search_edittext);
		index_goods_search_button = (TextView) mHeaderView.findViewById(R.id.index_goods_search_button);
		index_goods_search_button.setVisibility(View.GONE);
		
		index_goods_search_edittext.setText(keyword);
		index_goods_search_edittext.setSelection(index_goods_search_edittext.length());
		index_goods_search_edittext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAttachActivity.finish();
			}
		});
	}
	
	private void initListView() {
		mSearchGoodsList = new ArrayList<Goods>();
		mGoodsListAdapter = new IndexGoodsListAdapter(mAttachActivity, mSearchGoodsList);
		mAbsListViewLoader = new AbsListViewLoader(this, mListView, mGoodsListAdapter, mSearchGoodsList);
		mOnLoaderListener = new ListViewOnLoaderListener(this, mAbsListViewLoader, mListView);
		mAbsListViewLoader.setOnLoaderListener(mOnLoaderListener);
		mListView.setAdapter(mGoodsListAdapter);
	}
	
	private void loadDataFromNetwork() {
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.addWhereMatches("description", keyword);
		query.setSkip(0);
		query.setLimit(perPageCount);
		mAbsListViewLoader.requestLoadData(query, null, true, AbsListViewLoader.Type.INIT);
	}
}
