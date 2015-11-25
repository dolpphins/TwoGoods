package com.lym.twogoods.index.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.HeaderListFragment;
import com.lym.twogoods.index.adapter.IndexGoodsListAdapter;
import com.lym.twogoods.ui.GoodsDetailActivity;
import com.lym.twogoods.utils.NetworkHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


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
	
	private int perPageCount = 10;
	private List<Goods> mSearchGoodsList = new ArrayList<Goods>();
	private IndexGoodsListAdapter mGoodsListAdapter;
	
	/** 标记是否正在从网络加载数据 */
	private boolean isLoadingFromNetwork = false;
	/** ListView滚动状态 */
	private int mListViewScrollStatus = OnScrollListener.SCROLL_STATE_IDLE;
	
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
		
		mGoodsListAdapter = new IndexGoodsListAdapter(mAttachActivity, mSearchGoodsList);
		mListView.setAdapter(mGoodsListAdapter);
		
		initListView();
		
		prepareLoadDataFromNetwork();
		
		return v;
	}

	private void initHeaderView() {
		index_goods_search_edittext = (EditText) mHeaderView.findViewById(R.id.index_goods_search_edittext);
		index_goods_search_button = (TextView) mHeaderView.findViewById(R.id.index_goods_search_button);
		index_goods_search_button.setVisibility(View.GONE);
		
		index_goods_search_edittext.setText(keyword);
		index_goods_search_edittext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAttachActivity.finish();
			}
		});
	}
	
	private void initListView() {
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mListViewScrollStatus = scrollState;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				if(mListViewScrollStatus == OnScrollListener.SCROLL_STATE_IDLE 
						&& mListView.getLastVisiblePosition() == totalItemCount - 1) {
					loadDataFromNetwork();
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//注意position从1开始
				Intent intent = new Intent(mAttachActivity, GoodsDetailActivity.class);
				intent.putExtra("goodsList", mSearchGoodsList.get(position - 1));
				startActivity(intent);
			}
		});
	}
	
	private void prepareLoadDataFromNetwork() {
		if(!NetworkHelper.isNetworkAvailable(mAttachActivity)) {
			
		} else if(isLoadingFromNetwork) {
			return;
		} else {
			showLoadingAnimation();
			loadDataFromNetwork();
		}
	}
	
	private void loadDataFromNetwork() {
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.addWhereMatches("description", keyword);
		query.setSkip(mSearchGoodsList.size());
		query.setLimit(perPageCount);
		
		query.findObjects(mAttachActivity, new FindListener<Goods>(){

			@Override
			public void onSuccess(List<Goods> goodsList) {
				handleLoadDataFromNetowrkFinish(goodsList);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				handleLoadDataFromNetowrkFinish(null);
			}
		});
	}
	
	private void handleLoadDataFromNetowrkFinish(List<Goods> goodsList) {
		System.out.println("goodsList:" + goodsList);
		hideLoadingAnimation();
		if(goodsList == null) {
					
		} else if(goodsList.size() <= 0) {
			//Toast.makeText(mAttachActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
		} else {
			mSearchGoodsList.addAll(goodsList);
			mGoodsListAdapter.notifyDataSetChanged();
		}
	}
}
