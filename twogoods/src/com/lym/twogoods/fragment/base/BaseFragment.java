package com.lym.twogoods.fragment.base;

import android.app.Activity;
import android.app.Fragment;

/**
 * <p>
 * 	该项目所有Fragment都必须继承自该类,提供了该项目所有Fragment公共实现和一些接口方法等.
 * </p>
 * 
 * @author 麦灿标
 * */
public abstract class BaseFragment extends Fragment{

	private final static String TAG = "BaseFragment";
	
	/** 当前Fragment附着Activity */
	protected Activity mAttachActivity;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mAttachActivity = activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mAttachActivity = null;
	}
}
