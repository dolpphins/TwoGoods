package com.lym.twogoods.fragment.base;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.lym.twogoods.R;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.GoodsCategory;
import com.lym.twogoods.config.GoodsCategory.Category;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.index.adapter.IndexGoodsListAdapter;
import com.lym.twogoods.index.manager.GoodsSortManager;
import com.lym.twogoods.index.manager.GoodsSortManager.GoodsSort;
import com.lym.twogoods.local.bean.LocalGoods;
import com.lym.twogoods.ui.GoodsDetailActivity;
import com.lym.twogoods.utils.NetworkHelper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 
 * 
 * 
 * */
public abstract class HeaderPullListGoodsFragment extends HeaderPullListFragment{

	private final static String TAG = "HeaderPullListGoodsFragment";
	
	//商品ListView适配器
	private IndexGoodsListAdapter mListViewAdapter;
	//商品数据List
	private List<Goods> mGoodsList = new ArrayList<Goods>();
	//当前分类
	private Category mCurrentCategory = Category.ALL;
	//排序方式
	private GoodsSort mCurrentGoodsSort = GoodsSort.NEWEST_PUBLISH;
	//分页
	private int mPerPageCount = 10;
	//private int mCurrentPageIndex = 0;
	
	private enum Status {
		REFRSHING, LOADING, NONE
	}
	
	//ListView当前状态
	private Status mStatus = Status.NONE;
	//ListView滚动状态
	private int mScrollStatus = OnScrollListener.SCROLL_STATE_IDLE;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		initView();
		//显示加载动画
		showLoadingAnimation();
		//为ListView设置事件
		setOnclickForListView();
		
		//在初始化完View之后再去获取数据
		initData();
		
		return v;
	}
	
	private void initData() {
		//读取缓存数据
		if(!readDiskCacheData()) {
			//获取失败尝试自动通过网络获取数据
			tryRefreshFromNetwork(false, false);
		} else {
			initAndShowListView();//初始化并显示ListView
		}
	}
	
	private void initView() {
		mListView.setAdapter(mListViewAdapter);
		setMode(Mode.PULLDOWN);
		
		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mScrollStatus = scrollState;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItemPosition = mListView.getLastVisiblePosition();
				//加载更多要求:1.没有正在刷新或加载 2.ListView处于静止状态 3.ListView最后一个item可见
				if(mStatus == Status.NONE && mScrollStatus ==  OnScrollListener.SCROLL_STATE_IDLE
										  && lastItemPosition == totalItemCount - 1) {
					loadDataFromNetwork(false, true);
				}
			}
		});
	}
	
	private void setOnclickForListView() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Log.i(TAG, "onItemClick position:" + position);
				//注意position从1开始
				Intent intent = new Intent(mAttachActivity, GoodsDetailActivity.class);
				intent.putExtra("goods", mGoodsList.get(position - 1));
				startActivity(intent);
			}
		});
	}
	
	//获取成功且有数据返回true,否则返回false
	private boolean readDiskCacheData() {
		OrmDatabaseHelper ormHelper = new OrmDatabaseHelper(mAttachActivity);
		Dao<LocalGoods, Integer> dao = ormHelper.getGoodsDao();
		QueryBuilder<LocalGoods, Integer> builder = dao.queryBuilder();
		//分类
		String all = getResources().getString(R.string.category_all);
		String currentCategoryString = GoodsCategory.getString(mAttachActivity, mCurrentCategory);
		if(!all.equals(currentCategoryString)) {
			try {
				builder.where().eq(LocalGoods.getCategoryColoumnString(), currentCategoryString);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		//查询结果排序
		String sort = GoodsSortManager.getColumnString(mCurrentGoodsSort);
		if(!TextUtils.isEmpty(sort)) {
			builder.orderBy(sort, true);
		}
		//开始查询
		try {
			List<LocalGoods> goodsList = builder.query();
			if(goodsList != null) {
				Log.i(TAG, "database query size:" + goodsList.size());
				int size = goodsList.size();
				for(int i = 0; i < size; i++) {
					mGoodsList.add(LocalGoods.toGoods(goodsList.get(i)));
				}
				if(goodsList.size() > 0) {
					return true;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 设置当前分类
	 * 
	 * @param category 要设置的分类
	 * */
	public void setCurrentCategory(Category category) {
		mCurrentCategory = category;
	}
	
	/**
	 * 获取当前分类
	 * 
	 * @return 返回当前分类
	 * */
	public Category getCurrentCategory() {
		return mCurrentCategory;
	}
	
	/**
	 * 设置商品排序方式
	 * 
	 * @param gs 要设置的排序方式
	 * */
	public void setGoodsSort(GoodsSort gs) {
		mCurrentGoodsSort = gs;
	}
	
	/**
	 * 获取当前商品的排序方式
	 * 
	 * @return 返回当前商品的排序方式
	 * */
	public GoodsSort getGoodsSort() {
		return mCurrentGoodsSort;
	}
	
	@Override
	public void onRefresh() {
		super.onRefresh();
		//尝试从网络获取数据
		tryRefreshFromNetwork(true, false);
	}
	
	private void tryRefreshFromNetwork(boolean isRefresh, boolean isLoadMore) {
		if(!checkNetworkCondition()) {
			Toast.makeText(mAttachActivity, "网络不可用", Toast.LENGTH_SHORT).show();
			handlleLoadFormNetworkFinish(null);
		} else {
			loadDataFromNetwork(isRefresh, isLoadMore);
		}
	}
	
	//检查网络状态
	private boolean checkNetworkCondition() {
		if(NetworkHelper.isNetworkAvailable(mAttachActivity)) {
			return true;
		} else {
			return false;
		}
	}
	
	//请求网络数据唯一入口
	private void loadDataFromNetwork(final boolean isRefresh, final boolean isLoadMore) {
		
		if(isRefresh && !isLoadMore) {
			mStatus = Status.REFRSHING;
		} else if(!isRefresh && isLoadMore) {
			mStatus = Status.LOADING;
		} else if(isRefresh && isLoadMore) {
			//非法状态
			return;
		}
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		
		if(mStatus == Status.REFRSHING) {
			query.setSkip(0);
		} else {
			query.setSkip(mGoodsList.size());
		}
		query.setLimit(mPerPageCount);
		String all = getResources().getString(R.string.category_all);
		String currentCategoryString = GoodsCategory.getString(mAttachActivity, mCurrentCategory);
		if(all != null && !all.equals(currentCategoryString)) {
			query.addWhereEqualTo("category", currentCategoryString);
		}
		
		//请求网络唯一出口
		query.findObjects(mAttachActivity, new FindListener<Goods>() {
			
			@Override
			public void onSuccess(List<Goods> goodsList) {
				Log.i(TAG, "onSuccess");
				handlleLoadFormNetworkFinish(goodsList);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				handlleLoadFormNetworkFinish(null);
			}
		});
	}
	
	private void handlleLoadFormNetworkFinish(List<Goods> goodsList) {
		if(goodsList == null) {
			handleLoadFromNetworkFail();
		} else if(goodsList.size() <= 0) {
			Toast.makeText(mAttachActivity, "没有更多的数据了", Toast.LENGTH_SHORT).show();
		} else {
			System.out.println("goodsList size:" + goodsList.size());
			initAndShowListView();//初始化并显示ListView
			//如果是刷新则清除所有旧的数据
			if(mStatus == Status.REFRSHING) {
				mGoodsList.clear();
			}
			mGoodsList.addAll(goodsList);
		}
		stopRefreshAndLoadMore();
		hideLoadingAnimation();//尝试隐藏加载动画
		resetStatus();
	}
	
	private void handleLoadFromNetworkFail() {
		//如果第一次加载失败且没有缓存则显示加载失败提示文字
		if(mGoodsList.size() <= 0) {
			TextView tv = showRetryText("请点击屏幕，重新加载！", Color.GRAY, -1);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("onClick");
					hideRetryText();
					showLoadingAnimation();//重新显示加载动画
					tryRefreshFromNetwork(false, false);//再次尝试从网络加载
				}
			});
		} else {
			Toast.makeText(mAttachActivity, "获取数据失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void stopRefreshAndLoadMore() {
		stopRefresh();
		stopLoadMore();
	}
	
	//重新设置状态
	private void resetStatus() {
		mStatus = Status.NONE;
	}
	
	@Override
	protected boolean requestDelayShowListView() {
		return true;
	}
	
	private void initAndShowListView() {
		mListViewAdapter = new IndexGoodsListAdapter(mAttachActivity, mGoodsList);
		setAdapter(mListViewAdapter);
		showListView();
	}
}
