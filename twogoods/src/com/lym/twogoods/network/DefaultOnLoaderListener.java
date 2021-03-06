package com.lym.twogoods.network;

import java.util.List;

import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.BaseListFragment;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


/**
 * 默认OnLoaderListener实现类
 * 
 * @author 麦灿标
 * */
public class DefaultOnLoaderListener implements AbsListViewLoader.OnLoaderListener{
	
	private final static String TAG = "DefaultOnLoaderListener";
	
	private BaseListFragment mFragment;
	
	private AbsListViewLoader mAbsListViewLoader;
	
	/**
	 * 构造函数
	 * 
	 * @param fragment 
	 * @param loader
	 * */
	public DefaultOnLoaderListener(BaseListFragment fragment, AbsListViewLoader loader) {
		mFragment = fragment;
		mAbsListViewLoader = loader;
	}

	@Override
	public void onInitLoaderStart() {
		mFragment.hideRetryText();
		mFragment.hideContentView();
		mFragment.showLoadingAnimation();
	}

	@Override
	public void onInitLoaderFinish(boolean success) {
		if(success) {
			mFragment.hideLoadingAnimation();
			mFragment.showContentView();
		} else {
			mFragment.hideLoadingAnimation();
			TextView tv = mFragment.showRetryText("请点击屏幕，重新加载！", Color.GRAY, -1);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mFragment.hideRetryText();
					mFragment.showLoadingAnimation();//重新显示加载动画
					mAbsListViewLoader.requestRetryLoadData();
				}
			});
		}
	}

	@Override
	public void onRefreshStart() {
		
	}

	@Override
	public void onRefreshFinish(boolean success) {
		
	}

	@Override
	public void onLoadMoreStart() {
		
	}

	@Override
	public void onLoadMoreFinish(boolean success) {
		
	}
	
//	@Override
//	public void onLoaderStart() {
//		mFragment.hideRetryText();
//		mFragment.hideContentView();
//		mFragment.showLoadingAnimation();
//	}
//
//	@Override
//	public void onLoaderSuccess(List<Goods> goodsList) {
//		mFragment.hideLoadingAnimation();
//		mFragment.showContentView();
//	}
//
//	@Override
//	public void onLoaderFail() {
//		mFragment.hideLoadingAnimation();
//		TextView tv = mFragment.showRetryText("请点击屏幕，重新加载！", Color.GRAY, -1);
//		tv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				mFragment.hideRetryText();
//				mFragment.showLoadingAnimation();//重新显示加载动画
//				mListViewLoader.requestRetryLoadData();
//			}
//		});
//	}
//
//	@Override
//	public void onLoadMoreStart() {
//		
//	}

	
}
