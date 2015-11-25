package com.lym.twogoods.network;
 
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.local.bean.LocalGoods;
import com.lym.twogoods.utils.NetworkHelper;

import android.content.Context;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import me.maxwin.view.XListView;

/**
 * 所有商品列表的通用加载工具类
 * 
 * @author 麦灿标
 * */
public class ListViewLoader {

	private final static String TAG = "ListViewLoader";
	
	/** 上下文 */
	private Context mContext;
	
	/** 相关联的ListView */
	private XListView mListView;
	
	/** 数据集 */
	private List<Goods> mGoodsList;
	
	/** 适配器 */
	private BaseGoodsListAdapter mAdapter;
	
	/** 是否从磁盘中读取缓存 */
	private boolean mLoadCacheFromDisk;
	
	/** 是否将获取到的信息保存到磁盘缓存中 */
	private boolean mSaveCacheToDisk;
	
	private enum Status {
		LOADING, NONE
	}
	
	//ListView当前状态
	private Status mStatus = Status.NONE;
	//ListView滚动状态
	private int mScrollStatus = OnScrollListener.SCROLL_STATE_IDLE;
	
	/** 数据加载监听器 */
	private OnLoaderListener mOnLoaderListener;
	
	/** 查询对象 */
	BmobQuery<Goods> mBmobQuery;
	
	/**
	 * 重试相关
	 * */
	private BmobQuery<Goods> mLastBmobQuery;
	private boolean mLastClear;
	private boolean mLastInit;
	private Map<String, Object> mLastDbMap;
	
	/**
	 * 构造函数
	 * 
	 * @param context 
	 * @param listView
	 * @param adapter
	 * @param goodsList
	 * */
	public ListViewLoader(Context context, XListView listView, BaseGoodsListAdapter adapter, List<Goods> goodsList) {
		mContext = context;
		mListView = listView;
		mAdapter = adapter;
		mGoodsList = goodsList;
		init();
	}
	
	private void init() {
		//默认不从磁盘读取缓存
		mLoadCacheFromDisk = false;
		//默认不保存数据到磁盘缓存中
		mSaveCacheToDisk = false;

		mListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mScrollStatus = scrollState;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				int lastItemPosition = mListView.getLastVisiblePosition();
				if(lastItemPosition == totalItemCount - 1 && 
						mScrollStatus == OnScrollListener.SCROLL_STATE_IDLE
						&& mStatus == Status.NONE
						&& visibleItemCount < totalItemCount) {
					if(mBmobQuery != null) {
						mBmobQuery.setSkip(mGoodsList.size());
						prepareLoadDataFromNetwork(mBmobQuery, false);
					}
					if(mOnLoaderListener != null) {
						mOnLoaderListener.onLoadMoreStart();
					}
				}
			}
		});
	}

	/**
	 * 判断是否允许从磁盘读取缓存
	 * 
	 * @return 如果允许从缓存读取缓存返回true,否则返回false.
	 * */
	public boolean isLoadCacheFromDisk() {
		return mLoadCacheFromDisk;
	}

	/**
	 * 设置是否从磁盘读取缓存
	 * 
	 * @param mLoadCacheFromDisk true表示从磁盘读取缓存,false表示
	 * 		  不从磁盘读取缓存
	 * */
	public void setLoadCacheFromDisk(boolean loadCacheFromDisk) {
		this.mLoadCacheFromDisk = loadCacheFromDisk;
	}
	
	/**
	 * 判断是否允许将获取到的数据写入到磁盘缓存
	 * 
	 * @return 如果允许保存数据到磁盘缓存中返回true,否则返回false.
	 * */
	public boolean isSaveCacheToDisk() {
		return mSaveCacheToDisk;
	}
	
	/**
	 * 设置是否运行将获取到的数据保存到磁盘缓存中
	 * 
	 * @param saveCacheToDisk true表示允许将获取到的数据保存到磁盘缓存中,
	 *        false表示不允许将获取到的数据保存到磁盘缓存中
	 */
	public void setSaveCacheToDisk(boolean saveCacheToDisk) {
		this.mSaveCacheToDisk = saveCacheToDisk;
	}
	
	/**
	 * 请求加载数据
	 * 
	 * @param query
	 * @param dbMap
	 * @param clear
	 * @param isInit
	 */
	public void requestLoadData(BmobQuery<Goods> query, Map<String, Object> dbMap, boolean clear, boolean isInit) {
		if(query == null) {
			return;
		}
		mBmobQuery = query;
		
		//用于重试
		mLastBmobQuery = query;
		mLastClear = clear;
		mLastInit = isInit;
		mLastDbMap = dbMap;
		
		prepareLoadData(query, dbMap, clear, isInit);
	}
	
	private void prepareLoadData(BmobQuery<Goods> query, Map<String, Object> dbMap, boolean clear, boolean isInit) {
		//如果是初始加载
		if(isInit) {
			//允许读取磁盘缓存
			if(mLoadCacheFromDisk) {
				List<LocalGoods> goodsList = readCacheFromDisk(dbMap);
				//有缓存
				if(goodsList != null && goodsList.size() > 0) {
					for(LocalGoods g : goodsList) {
						mGoodsList.add(LocalGoods.toGoods(g));
					}
					return;
				}
			}
		}
		prepareLoadDataFromNetwork(query, clear);
	}
	
	private void prepareLoadDataFromNetwork(BmobQuery<Goods> query, boolean clear) {
		if(!NetworkHelper.isNetworkAvailable(mContext)) {
			handleLoadDataForPrepareFail();
			return;
		}
		loadDataFromNetwork(query, clear);
	}
	
	private void loadDataFromNetwork(BmobQuery<Goods> query, boolean clear) {
		Log.i(TAG, "loadDataFromNetwork");
		if(mOnLoaderListener != null) {
			mOnLoaderListener.onLoaderStart();
		}
		mStatus = Status.LOADING;
		final boolean c = clear;
		query.findObjects(mContext, new FindListener<Goods>() {
			
			@Override
			public void onSuccess(List<Goods> goodsList) {
				Log.i(TAG, "onSuccess");
				handleLoadDataFromNetworkFinish(goodsList, c);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.i(TAG, "onError " + arg0 + " " + arg1);
				handleLoadDataFromNetworkFinish(null, c);
			}
		});
	}
	
	//通过网络获取数据唯一出口
	private void handleLoadDataFromNetworkFinish(List<Goods> goodsList, boolean clear) {
		if(goodsList == null) {
			//获取失败
			if(mOnLoaderListener != null) {
				mOnLoaderListener.onLoaderFail();
			}
		} else {
			if(clear) {
				mGoodsList.clear();
			}
			if(goodsList.size() <= 0) {
				Toast.makeText(mContext, "没有更多的数据了", Toast.LENGTH_SHORT).show();
			} else {
				mGoodsList.addAll(goodsList);
				mAdapter.notifyDataSetChanged();
			}
			if(mOnLoaderListener != null) {
				mOnLoaderListener.onLoaderSuccess(goodsList);
			}
		}
		//保存缓存
		if(mSaveCacheToDisk) {
			OrmDatabaseHelper helper = new OrmDatabaseHelper(mContext);
			//先清空表
			helper.clearTable(LocalGoods.class);
			Dao<LocalGoods, Integer> dao = helper.getGoodsDao();
			for(Goods g : mGoodsList) {
				try {
					dao.create(LocalGoods.valueOf(g));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mStatus = Status.NONE;
		
	} 
	
	//获取数据准备工作失败调用
	private void handleLoadDataForPrepareFail() {
		mListView.stopRefresh();
		mListView.stopLoadMore();
		mStatus = Status.NONE;
		if(mOnLoaderListener != null) {
			mOnLoaderListener.onLoaderFail();
		}
		
	}
	
	/**
	 * 设置数据加载监听器
	 * 
	 * @param l 指定要设置的数据加载监听器
	 * */
	public void setOnLoaderListener(OnLoaderListener l) {
		mOnLoaderListener = l;
	}
	
	/**
	 * 获取数据加载监听器
	 * 
	 * @return 返回数据加载监听器
	 * */
	public OnLoaderListener getOnLoaderListener() {
		return mOnLoaderListener;
	}
	
	/**
	 * 请求重试加载数据,该方法会自动执行上一次的请求任务
	 */
	public void requestRetryLoadData() {
		if(mLastBmobQuery != null) {
			requestLoadData(mLastBmobQuery, mLastDbMap, mLastClear, mLastInit);
		}
	}
	
	private List<LocalGoods> readCacheFromDisk(Map<String, Object> dbMap) {
		OrmDatabaseHelper helper = new OrmDatabaseHelper(mContext);
		Dao<LocalGoods, Integer> dao = helper.getGoodsDao();
		try {
			if(dbMap != null && dbMap.size() > 0) {
				return dao.queryForFieldValuesArgs(dbMap);
			} else {
				return dao.queryForAll();
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 数据加载监听器接口
	 * 
	 * @author 麦灿标
	 */
	public interface OnLoaderListener {
		
		void onLoaderStart();
		
		void onLoaderSuccess(List<Goods> goodsList);
		
		void onLoaderFail();
		
		void onLoadMoreStart();
	}
	
}
