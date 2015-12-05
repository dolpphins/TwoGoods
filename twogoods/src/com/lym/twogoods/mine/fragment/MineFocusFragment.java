package com.lym.twogoods.mine.fragment;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsListViewAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.GoodsFocus;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.index.manager.GoodsSortManager;
import com.lym.twogoods.index.manager.GoodsSortManager.GoodsSort;
import com.lym.twogoods.mine.adapter.MineFocusGoodsListAdapter;
import com.lym.twogoods.network.AbsListViewLoader;
import com.lym.twogoods.network.ListViewOnLoaderListener;
import com.lym.twogoods.ui.GoodsDetailActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 我关注的商品Fragment
 * 
 * @author 麦灿标
 *
 */
public class MineFocusFragment extends PullListFragment {

	private final static String TAG = "MineFocusFragment";
	
	private String mUsername;
	
	private AbsListViewLoader mAbsListViewLoader;
	private BaseGoodsListViewAdapter mAdapter;
	private List<Goods> mGoodsList;
	private int perPageCount = 10;
	private AbsListViewLoader.OnLoaderListener mOnLoaderListener;
	
	private List<GoodsFocus> mFocusGoodsInfoList = new ArrayList<GoodsFocus>();
	private List<String> mFocusGoodsObjectIdList = new ArrayList<String>();
	
	/**
	 * 构造函数
	 * 
	 * @param username
	 */
	public MineFocusFragment(String username) {
		mUsername = username;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		initListView();
		
		tryLoadMineFocusGoodsInfoList();
		
		return v;
	}
	
	private void initListView() {
		setMode(Mode.PULLDOWN);
		LayoutParams params = mListView.getLayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;
		
		mGoodsList = new ArrayList<Goods>();
		mAdapter = new MineFocusGoodsListAdapter(mAttachActivity, mGoodsList);
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
	
	//尝试获取我关注商品的所有ObjectId
	private void tryLoadMineFocusGoodsInfoList() {
		BmobQuery<GoodsFocus> query = new BmobQuery<GoodsFocus>();
		query.addWhereEqualTo("username", mUsername);
		query.findObjects(mAttachActivity, new FindListener<GoodsFocus>() {
			
			@Override
			public void onSuccess(List<GoodsFocus> goodsFocusList) {
				Log.i(TAG, "onSuccess");
				if(goodsFocusList != null) {
					mFocusGoodsInfoList.addAll(goodsFocusList);
					for(GoodsFocus gf : goodsFocusList) {
						mFocusGoodsObjectIdList.add(gf.getGoods_objectId());
					}
					//开始加载数据
					loadDataInit();
				}
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.i(TAG, "onError");
				System.out.println(arg0);
				System.out.println(arg1);
			}
		});
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
		query.addWhereContainedIn("objectId", mFocusGoodsObjectIdList);
		String order = "-" + GoodsSortManager.getColumnString(GoodsSort.NEWEST_PUBLISH);
		query.order(order);
		mAbsListViewLoader.requestLoadData(query, null, clear, type);
	}
	
	@Override
	protected boolean requestDelayShowAbsListView() {
		return true;
	}
}
