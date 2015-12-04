package com.lym.twogoods.fragment.base;

import android.view.View;
import android.widget.AbsListView;
import me.maxwin.view.IAbsListViewListener;
import me.maxwin.view.XListView;

/**
 * <p>
 * 	带可下拉刷新,上拉加载Listiew的Fragment
 * </p>
 * <p>
 * 	该类实现了{@link XListView.IXListViewListener}接口和{@link XListView.OnXScrollListener}接口.
 * </p>
 * <p>
 * 	默认模式为{@link PullListFragment.Mode#BOTH}
 * </p>
 * 
 * @author 麦灿标
 * */
public abstract class PullListFragment extends BaseListFragment 
			implements IAbsListViewListener,XListView.OnXScrollListener{

	public static enum Mode {
		BOTH, PULLDOWN, PULLUP,NONE
	}
	
	/** ListView模式 */
	private Mode mMode;
	
	@Override
	protected void configListView() {
		super.configListView();
		//可上拉加载
		mListView.setPullLoadEnable(true);
		//可下拉刷新
		mListView.setPullRefreshEnable(true);
		
		mMode = Mode.BOTH;
		//设置上拉加载及下拉刷新监听器
		mListView.setXListViewListener(this);
		mListView.setOnScrollListener(this);
	}
	
	/**
	 * <p>设置ListView上拉及下拉模式</p>
	 * 
	 * @see {@link PullListFragment.Mode}
	 * */
	public void setMode(Mode mode) {
		boolean canPullDown = false;
		boolean canPullUp = false;
		switch(mode) {
		case BOTH:
			canPullDown = true;
			canPullUp = true;
			mMode = Mode.BOTH;
			break;
		case PULLDOWN:
			canPullDown = true;
			mMode = Mode.PULLDOWN;
			break;
		case PULLUP:
			canPullUp = true;
			mMode = Mode.PULLUP;
		case NONE:
			mMode = Mode.NONE;
			break;
		default:
		}
		mListView.setPullLoadEnable(canPullUp);
		mListView.setPullRefreshEnable(canPullDown);
	}
	
	/**
	 * <p>获取ListView下拉及上拉模式</p>
	 * 
	 * @return 当前ListView模式
	 * */
	public Mode getMode() {
		return mMode;
	}
	
	/**
	 * <p>
	 * 	当下拉刷新时回调该回调函数,注意只有ListView模式为{@link PullListFragment.Mode#BOTH}
	 *  或者{@link PullListFragment.Mode#PULLDOWN}才会调用该函数.你可以重写该方法以处理下拉刷
	 *  新事件
	 * </p>
	 * */
	@Override
	public void onRefresh() {		
	}

	/**
	 * <p>
	 * 	当下拉刷新时回调该回调函数,注意只有ListView模式为{@link PullListFragment.Mode#BOTH}
	 *  或者{@link PullListFragment.Mode#PULLUP}才会调用该函数.你可以重写该方法以处理上拉加
	 *  载事件
	 * </p>
	 * */
	@Override
	public void onLoadMore() {
	}
	
	/**
	 * 停止刷新
	 * */
	public void stopRefresh() {
		if(mListView != null) {
			mListView.stopRefresh();
		}
	}
	
	/**
	 * 停止加载更多
	 * */
	public void stopLoadMore() {
		if(mListView != null) {
			mListView.stopLoadMore();
		}
	}
	
	/**
	 * 设置上次刷新时间
	 * 
	 * @param time 要设置的上次刷新时间
	 * */
	public void setLastRefreshTime(String time) {
		if(mListView != null) {
			mListView.setRefreshTime(time);
		}
		
	}
	
	/**
	 * <p>
	 * 	当ListView滚动时该方法会被调用,你可以重载该方法以便监听ListView滚动事件.
	 * </p>
	 * 
	 * @param view 当前ListView对象
	 * */
	@Override
	public void onXScrolling(View view) {
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {	
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}
}
