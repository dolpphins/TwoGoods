package com.lym.twogoods.network;

import java.util.List;

import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.BaseListFragment;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


/**
 * 默认OnLoaderListener实现类
 * 
 * @author 麦灿标
 * */
public class DefaultOnLoaderListener implements ListViewLoader.OnLoaderListener{
	
	private BaseListFragment mFragment;
	
	private ListViewLoader mListViewLoader;
	
	/**
	 * 构造函数
	 * 
	 * @param fragment 
	 * @param loader
	 * */
	public DefaultOnLoaderListener(BaseListFragment fragment, ListViewLoader loader) {
		mFragment = fragment;
		mListViewLoader = loader;
	}
	
	@Override
	public void onLoaderStart() {
		mFragment.showLoadingAnimation();
	}

	@Override
	public void onLoaderSuccess(List<Goods> goodsList) {
		mFragment.hideLoadingAnimation();
	}

	@Override
	public void onLoaderFail() {
		mFragment.hideLoadingAnimation();
		TextView tv = mFragment.showRetryText("请点击屏幕，重新加载！", Color.GRAY, -1);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mFragment.hideRetryText();
				mFragment.showLoadingAnimation();//重新显示加载动画
				mListViewLoader.requestRetryLoadData();
			}
		});
	}

	@Override
	public void onLoadMoreStart() {
		
	}

	
}
