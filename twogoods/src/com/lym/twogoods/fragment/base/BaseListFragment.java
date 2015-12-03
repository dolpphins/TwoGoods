package com.lym.twogoods.fragment.base;

import com.lym.twogoods.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import me.maxwin.view.XListView;

/**
 * <p>
 * 	带ListView列表的Fragment.
 * </p>
 * 
 * @author 麦灿标
 * */
public abstract class BaseListFragment extends BaseFragment {

	private final static String TAG = "BaseListFragment";
	
	protected LinearLayout mLoadingLayout;
	private ProgressBar app_basefragment_listview_pb;
	private TextView app_basefragment_listview_tv;
	
	/** ListView列表 */
	protected XListView mListView;
	
	/** Content View */
	protected View mContentView;
	
	/** 标记ContentView是否显示 */
	private boolean mContentIsShowing;
	
	/** ListView适配器 */
	protected ListAdapter mAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		mLoadingLayout = (LinearLayout) inflater.inflate(R.layout.app_common_loading_layout, null);
		mListView = (XListView) mLoadingLayout.findViewById(R.id.app_basefragment_listview_lv);
		initLoadingLayout();
		
		mContentView = onCreateContentView();
		mContentIsShowing = !requestDelayShowAbsListView();
		if(mContentView != null) {
			replaceView(mListView, mContentView);
			mListView = null;
			if(!mContentIsShowing) {
				mContentView.setVisibility(View.GONE);
			}
		} else {
			//在onCreateView方法调用前就已经给mAdapter赋值
			if(mAdapter != null) {
				mListView.setAdapter(mAdapter);
			}
			mContentView = mListView;
			configListView();
			if(!mContentIsShowing) {
				mListView.setVisibility(View.GONE);
			}
		}
		
		mContentIsShowing = mContentView.getVisibility() == View.VISIBLE ? true : false;
		
		onCreateViewAfter();
		
		return mLoadingLayout;
	}
	
	//子类可以重写该方法自定义ListView配置
	protected void configListView() {
		//不可上拉加载
		mListView.setPullLoadEnable(false);
		//不可下拉刷新
		mListView.setPullRefreshEnable(false); 
	}
	
	/**
	 * 设置适配器,注意如果为adapter为null不进行任何操作
	 * 
	 * @param adapter 要设置的适配器
	 * */
	protected void setAdapter(ListAdapter adapter) {
		mAdapter = adapter;
		if(mListView != null) {
			mListView.setAdapter(mAdapter);
		}
	}
	
	private void initLoadingLayout() {
		if(mLoadingLayout != null) {
			app_basefragment_listview_pb = (ProgressBar) mLoadingLayout.findViewById(R.id.app_basefragment_listview_pb);
			app_basefragment_listview_tv = (TextView) mLoadingLayout.findViewById(R.id.app_basefragment_listview_tv);
			
			//暂时这样设置
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mLoadingLayout.setLayoutParams(params);
		}
	}
	
	/**
	 * 显示加载动画
	 * */
	public void showLoadingAnimation() {
		System.out.println("showLoadingAnimation");
		if(app_basefragment_listview_pb != null && app_basefragment_listview_pb.getVisibility() != View.VISIBLE) {
			System.out.println("set app_basefragment_listview_pb visible");
			app_basefragment_listview_pb.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 隐藏加载动画
	 * */
	public void hideLoadingAnimation() {
		if(app_basefragment_listview_pb != null && app_basefragment_listview_pb.getVisibility() == View.VISIBLE) {
			app_basefragment_listview_pb.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 显示加载失败提示
	 * 
	 * @param text 提示内容
	 * @param color 文字颜色
	 * @param textSize 文字大小,如果小于0则使用默认字体大小
	 * 
	 * @param 返回TextView控件
	 * */
	public TextView showRetryText(String text, int color, float textSize) {
		if(app_basefragment_listview_tv != null && app_basefragment_listview_tv.getVisibility() != View.VISIBLE) {
			app_basefragment_listview_tv.setText(text);
			app_basefragment_listview_tv.setTextColor(color);
			app_basefragment_listview_tv.setTextSize(textSize);
			app_basefragment_listview_tv.setClickable(true);
			app_basefragment_listview_tv.setVisibility(View.VISIBLE);
		}
		return app_basefragment_listview_tv;
	}
	
	/**
	 * 隐藏提示文字
	 * */
	public void hideRetryText() {
		if(app_basefragment_listview_tv != null && app_basefragment_listview_tv.getVisibility() == View.VISIBLE) {
			app_basefragment_listview_tv.setVisibility(View.GONE);
		}
	}
	
	/**
	 * 显示Content View
	 * */
	public void showContentView() {
		if(mContentView != null && !mContentIsShowing) {
			mContentView.setVisibility(View.VISIBLE);
			mContentIsShowing = true;
		} 
	}
	
	/**
	 * 隐藏Content View
	 * */
	public void hideContentView() {
		if(mContentView != null && mContentIsShowing) {
			mContentView.setVisibility(View.GONE);
			mContentIsShowing = false;
		}
	}
	
	/**
	 * 请求开始隐藏AbsListView,你可以重写该方法使AbsListView开始时隐藏,在你想要显示的时候
	 * 调用{@link BaseListFragment#showAbsListView()}进行显示
	 * 
	 * @return 返回true表示开始隐藏AbsListView,false表示开始显示ListView
	 * */
	protected boolean requestDelayShowAbsListView() {
		return false;
	}
	
	/**
	 * 当执行完onCreateView时会回调该方法
	 */
	protected void onCreateViewAfter(){
	}
	
	/**
	 * 重新创建View,默认为ListView,子类可以重写该方法以
	 * 替换成自己的View
	 * 
	 * @return 返回新的View,如果返回null则使用默认的View
	 */
	protected View onCreateContentView() {
		return null;
	}
	
	private boolean replaceView(View oldView, View newView) {
		if(oldView == null || newView == null) {
			return false;
		}
		int index = mLoadingLayout.indexOfChild(oldView);
		if(index >= 0) {
			mLoadingLayout.removeViewAt(index);
			LayoutParams params = oldView.getLayoutParams();
			mLoadingLayout.addView(newView, index, params);//使用之前的LayoutParams
			return true;
		}
		return false;
	}
}
