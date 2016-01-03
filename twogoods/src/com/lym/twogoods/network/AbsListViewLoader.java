package com.lym.twogoods.network;
 
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lym.loader.HeadPictureLoader;
import com.lym.loader.HeadPictureLoader.OnLoadHeadPictureUrlFinishListener;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListViewAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListViewAdapter.ItemViewHolder;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.ActivityRequestResultCode;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.fragment.base.BaseFragment.OnFragmentActivityResultListener;
import com.lym.twogoods.ui.GoodsDetailActivity;
import com.lym.twogoods.utils.NetworkHelper;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 所有商品列表的通用加载工具类
 * 
 * @author 麦灿标
 * */
public class AbsListViewLoader {

	private final static String TAG = "AbsListViewLoader";
	
	/** 相关联 的Fragment */
	private BaseFragment mFragment;
	
	/** 相关联的AbsListView */
	private AbsListView mAbsListView;
	
	/** 数据集 */
	private List<Goods> mGoodsList;
	
	/** 适配器 */
	private BaseGoodsListAdapter mAdapter;
	
	private AbsListViewLoaderConfiguration mConfiguration;//暂时没用到
	
	private static enum Status {
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
	 * 请求的加载数据类型
	 */
	public static enum Type {
		INIT, REFRESH, LOADMORE
	}
	
	/**
	 * 重试相关
	 * */
	private BmobQuery<Goods> mLastBmobQuery;
	private boolean mLastClear;
	private Map<String, Object> mLastDbMap;
	private Type mType;
	
	/**
	 * 构造函数
	 * 
	 * @param context 
	 * @param listView
	 * @param adapter
	 * @param goodsList
	 * */
	public AbsListViewLoader(BaseFragment fragment, AbsListView absListView, BaseGoodsListAdapter adapter, List<Goods> goodsList) {
		mFragment = fragment;
		mAbsListView = absListView;
		mAdapter = adapter;
		mGoodsList = goodsList;
		//默认配置
		mConfiguration = new AbsListViewLoaderConfiguration.Builder()
							.setReadDiskCache(false)
							.setSaveDiskCache(false)
							.build();
		init();
	}
	
	/**
	 * 设置加载器配置
	 * 
	 * @param configuration 要设置的配置
	 */
	public void setConfiguration(AbsListViewLoaderConfiguration configuration) {
		mConfiguration = configuration;
	}
	
	/**
	 * 获取加载器配置
	 * 
	 * @return 返回加载器配置
	 */
	public AbsListViewLoaderConfiguration getConfiguration() {
		return mConfiguration;
	}
	
	private void init() {
		mAbsListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mScrollStatus = scrollState;
				
				int totalCount = view.getCount();
				//从Header算起
				int firstIndex = view.getFirstVisiblePosition();
				int lastIndex = view.getLastVisiblePosition();
				int visibleItemCount = lastIndex - firstIndex + 1;
				
				//如果停止滚动,那么开始异步加载数据,包括头像，图片等
				requestLoadHeadPicture(firstIndex, visibleItemCount);

				//停止滚动时加载更多
				if(lastIndex == totalCount - 1 
						&& scrollState == OnScrollListener.SCROLL_STATE_IDLE
						&& mStatus == Status.NONE
						&& visibleItemCount < totalCount) {
					if(mBmobQuery != null) {
						mBmobQuery.setSkip(mGoodsList.size());
						prepareLoadDataFromNetwork(mBmobQuery, Type.LOADMORE);
					}
					if(mOnLoaderListener != null) {
						mOnLoaderListener.onLoadMoreStart();
					}
				}
				
			}
			
			//注意是正在滚动才会调用该方法，因此OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
			//和OnScrollListener.SCROLL_STATE_FLING两种状态
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				
			}
		});
		mAbsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(mFragment.getActivity(), GoodsDetailActivity.class);
				//注意有Header的话position会发生移位
				intent.putExtra("goods", (Goods) parent.getAdapter().getItem(position));
				mFragment.startActivityForResult(intent, ActivityRequestResultCode.GOODS_ITEM_DETAIL_REQUESTCODE);
			}
		});
		mFragment.setOnFragmentActivityResultListener(new OnFragmentActivityResultListener() {
			
			@Override
			public void onActivityResult(int requestCode, int resultCode, Intent data) {
				if(ActivityRequestResultCode.GOODS_ITEM_DETAIL_REQUESTCODE == requestCode
						&& ActivityRequestResultCode.GOODS_ITEM_DETAIL_RESULTCODE == resultCode
						&& data != null) {
					Goods goods = (Goods) data.getSerializableExtra("goods");
					updateGoodsItem(goods);
				}
			}
		});
	}

	/**
	 * 请求加载头像,该方法只有在列表处于静止(没有滚动)状态才会加载头像
	 * 
	 * @param start 从start位置开始
	 * @param count 指定要加载的Item数
	 */
	public void requestLoadHeadPicture(int start, int count) {
		if(mScrollStatus == OnScrollListener.SCROLL_STATE_IDLE) {
			//头像
			List<HeadPictureLoader.HeadPictureTask> tasks = new ArrayList<HeadPictureLoader.HeadPictureTask>();
			//
			for(int i = 0; i < count; i++) {
				//getChildAt并没有包含Header
				View v = mAbsListView.getChildAt(i);
				//System.out.println(v == null ? "v null" : "v not null");
				if(v != null) {
					BaseGoodsListViewAdapter.ItemViewHolder holder = (ItemViewHolder) v.getTag();
					//holder可能为空
					//System.out.println(holder == null ? "holder null" : "holder not null");
					if(holder != null) {
						Goods goods = mGoodsList.get(start + i - 1);
						String username = holder.username;
						if(!TextUtils.isEmpty(username) && username.equals(goods.getUsername())) {
							continue;
						}
						holder.username = goods.getUsername();
						HeadPictureLoader.HeadPictureTask task = new HeadPictureLoader.HeadPictureTask();
						task.setGUID(goods.getGUID());
						task.setUsername(goods.getUsername());
						task.setIv(holder.base_goods_listview_item_headpic);
						tasks.add(task);
					}
				}
			}
			if(tasks.size() > 0) {
				HeadPictureLoader.getInstance().submit(mFragment.getActivity().getApplicationContext(), tasks, new OnLoadHeadPictureUrlFinishListener() {
					
					@Override
					public void onFinish(String username, String url) {
						if(!TextUtils.isEmpty(url)) {
							for(Goods goods : mGoodsList) {
								String goodsUsername = goods.getUsername();
								if(goodsUsername.equals(username)) {
									goods.setHead_url(url);
								}
							}	
						}
					}
				});
			}
		}
	}
	
	/**
	 * 请求加载数据
	 * 
	 * @param query
	 * @param dbMap
	 * @param clear
	 */
	public void requestLoadData(BmobQuery<Goods> query, Map<String, Object> dbMap, boolean clear, Type type) {
		if(query == null) {
			return;
		}
		mBmobQuery = query;
		
		//用于重试
		mLastBmobQuery = query;
		mLastClear = clear;
		mLastDbMap = dbMap;
		mType = type;
		
		prepareLoadData(query, dbMap, clear, type);
	}
	
	private void prepareLoadData(BmobQuery<Goods> query, Map<String, Object> dbMap, boolean clear, Type type) {
		if(clear) {
			mGoodsList.clear();
		}
		invokeListenerStart(type);
		prepareLoadDataFromNetwork(query, type);
	}
	
	private void prepareLoadDataFromNetwork(BmobQuery<Goods> query, Type type) {
		if(!NetworkHelper.isNetworkAvailable(mFragment.getActivity())) {
			handleLoadDataForPrepareFail(type);
			return;
		}
		loadDataFromNetwork(query, type);
	}
	
	private void loadDataFromNetwork(BmobQuery<Goods> query, Type type) {
		//Log.i(TAG, "loadDataFromNetwork");
		mStatus = Status.LOADING;
		final Type fType = type;
		query.findObjects(mFragment.getActivity(), new FindListener<Goods>() {
			
			@Override
			public void onSuccess(List<Goods> goodsList) {
				//Log.i(TAG, "onSuccess");
				handleLoadDataFromNetworkFinish(goodsList, fType);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				//Log.i(TAG, "onError " + arg0 + " " + arg1);
				handleLoadDataFromNetworkFinish(null, fType);
			}
		});
	}
	
	//通过网络获取数据唯一出口
	private void handleLoadDataFromNetworkFinish(List<Goods> goodsList, Type type) {
		if(goodsList == null) {
			//获取失败
			if(mOnLoaderListener != null) {
				invokeListenerFinish(type, false);
			}
		} else {
			//如果是下拉刷新则先清除掉之前的数据
			if(type == Type.REFRESH) {
				mGoodsList.clear();
			}
			if(goodsList.size() <= 0) {
				//Toast.makeText(mContext, "没有更多的数据了", Toast.LENGTH_SHORT).show();
			} else {
				mGoodsList.addAll(goodsList);
				mAdapter.notifyDataSetChanged();
			}
			
			if(mOnLoaderListener != null) {
				invokeListenerFinish(type, true);
			}
		}
		mStatus = Status.NONE;
	} 
	
	//获取数据准备工作失败调用
	private void handleLoadDataForPrepareFail(Type type) {
		mStatus = Status.NONE;
		if(mOnLoaderListener != null) {
			invokeListenerFinish(type, false);
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
			requestLoadData(mLastBmobQuery, mLastDbMap, mLastClear, mType);
		}
	}
	
	private void invokeListenerStart(Type type) {
		if(mOnLoaderListener == null) {
			return;
		}
		switch (type) {
		case INIT:
			mOnLoaderListener.onInitLoaderStart();
			break;
		case REFRESH:
			mOnLoaderListener.onRefreshStart();
			break;
		case LOADMORE:
			mOnLoaderListener.onLoadMoreStart();
			break;
		}
	}
	
	private void invokeListenerFinish(Type type, boolean success) {
		if(mOnLoaderListener == null) {
			return;
		}
		switch (type) {
		case INIT:
			mOnLoaderListener.onInitLoaderFinish(success);
			break;
		case REFRESH:
			mOnLoaderListener.onRefreshFinish(success);
			break;
		case LOADMORE:
			mOnLoaderListener.onLoadMoreFinish(success);
			break;
		}
	}
	
	private void updateGoodsItem(Goods goods) {
		if(goods != null) {
			for(Goods g : mGoodsList) {
				if(goods.getObjectId().equals(g.getObjectId())) {
					g.update(goods);
					return;
				}
			}
		}
	} 
	
	/**
	 * 数据加载监听器接口
	 * 
	 * @author 麦灿标
	 */
	public interface OnLoaderListener {
		
		void onInitLoaderStart();
		
		void onInitLoaderFinish(boolean success);
		
		void onRefreshStart();
		
		void onRefreshFinish(boolean success);
		
		void onLoadMoreStart();
		
		void onLoadMoreFinish(boolean success);
	}
	
}
