package com.lym.twogoods.fragment.base;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.adapter.NearbyAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsGridViewAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.network.AbsListViewLoader;
import com.lym.twogoods.network.GridViewOnLoaderListener;
import com.lym.twogoods.ui.GoodsDetailActivity;
import com.lym.twogoods.utils.Debugger;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.BmobQuery;
import me.maxwin.view.IAbsListViewListener;
import me.maxwin.view.XGridView;

/**
 * 带GridView可下拉刷新的Fragment
 * 
 * @author 麦灿标
 *
 */
public abstract class PullGridViewFragment extends BaseListFragment implements IAbsListViewListener{

	private final static String TAG = "PullGridViewFragment";
	
	protected XGridView mGridView;
	
	/** 水平方向上占用的额外宽度 */
	private int mHorizontalExtraDistance;
		
	private static int mGridViewColumnNum = 2;
	
	/**
	 * 商品列表加载器相关
	 * */
	private AbsListViewLoader mAbsListViewLoader;
	private BaseGoodsListAdapter mAdapter;
	private List<Goods> mGoodsList;
	private int perPageCount = 10;
	private AbsListViewLoader.OnLoaderListener mOnLoaderListener;
	
	private BmobQuery<Goods> mBmobquery;
	
	@Override
	protected View onCreateContentView() {
		
		//将默认的XListView换成XGridView
		mGridView = new XGridView(mAttachActivity.getApplicationContext());
		initGridView();
		
		return mGridView;
	}
	
	@Override
	protected void onCreateViewAfter() {
		super.onCreateViewAfter();
		
		setClickEvent();
		
		mGoodsList = new ArrayList<Goods>();
		mAdapter = new BaseGoodsGridViewAdapter(mAttachActivity, mGoodsList, this);
		mAbsListViewLoader = new AbsListViewLoader(mAttachActivity, mGridView.getAbsListView(), mAdapter, mGoodsList);
		mOnLoaderListener = new GridViewOnLoaderListener(this, mAbsListViewLoader, mGridView);
		mAbsListViewLoader.setOnLoaderListener(mOnLoaderListener);
		mGridView.setNumColumns(mGridViewColumnNum);
		mGridView.setAdapter(mAdapter);
	}
	
	private void initGridView() {
		int horizontalSpacing = (int) mAttachActivity.getResources().getDimension(R.dimen.app_base_goods_gridview_horizontalSpacing);
		//int verticalSpacing = (int) mAttachActivity.getResources().getDimension(R.dimen.app_base_goods_gridview_verticalSpacing);
		int padding = (int) mAttachActivity.getResources().getDimension(R.dimen.app_base_goods_gridview_padding);
		mGridView.setHorizontalSpacing(horizontalSpacing);
		//mGridView.setVerticalSpacing(verticalSpacing);
		mGridView.setPadding(padding, 0, padding, 0);
		mHorizontalExtraDistance = horizontalSpacing + 2 * padding;
		mGridView.setVerticalScrollBarEnabled(false);//隐藏滚动条
		Drawable sel = mAttachActivity.getResources().getDrawable(R.drawable.nearby_gridview_item_selector);
		if(sel != null) {
			mGridView.setSelector(sel);
		}
		
		mGridView.setXGridViewListener(this);
	}
	
	private void setClickEvent() {
		if(mGridView != null) {
			mGridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(mAttachActivity, GoodsDetailActivity.class);
					intent.putExtra("goods", mGoodsList.get(position));
					startActivity(intent);
				}
			});
		}
	}
	
	/**
	 * 获取水平方向上额外宽度,即总宽度 - Item占用的宽度
	 * 
	 * @return 返回水平方向上额外宽度
	 */
	public int getHorizontalExtraDistance() {
		return mHorizontalExtraDistance;
	}
	
	@Override
	protected boolean requestDelayShowAbsListView() {
		return true;
	}
	
	/**
	 * 创建合适的BmobQuery<Goods>对象
	 * 
	 * @return
	 */
	protected abstract BmobQuery<Goods> onCreateBmobQuery();
	
	public void loadDataInit() {
		reloadData(AbsListViewLoader.Type.INIT, true);
	}
	
	private void reloadData(AbsListViewLoader.Type type, boolean clear) {
		if(mBmobquery == null) {
			mBmobquery = onCreateBmobQuery();
		}
		if(mBmobquery == null) {
			return;
		}
		
		mBmobquery.setSkip(0);
		mBmobquery.setLimit(perPageCount);
		mAbsListViewLoader.requestLoadData(mBmobquery, null, clear, type);
	}
	
	@Override
	public void onRefresh() {
		reloadData(AbsListViewLoader.Type.REFRESH, false);
	}
	
	@Override
	public void onLoadMore() {	
	}
}
