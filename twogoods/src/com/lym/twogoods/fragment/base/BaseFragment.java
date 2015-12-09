package com.lym.twogoods.fragment.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

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
	
	private OnFragmentActivityResultListener mOnFragmentActivityResultListener;
	
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
	
	/**
	 * 设置Fragment的onActivityResult回调函数监听器
	 * 
	 * @param listener 要设置的监听器
	 */
	public void setOnFragmentActivityResultListener(OnFragmentActivityResultListener listener) {
		mOnFragmentActivityResultListener = listener;
	}
	
	/**
	 * 获取Fragment的onActivityResult回调函数监听器
	 * 
	 * @return 返回Fragment的onActivityResult回调函数监听器
	 */
	public OnFragmentActivityResultListener getOnFragmentActivityResultListener() {
		return mOnFragmentActivityResultListener;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(mOnFragmentActivityResultListener != null) {
			mOnFragmentActivityResultListener.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public interface OnFragmentActivityResultListener {
		
		void onActivityResult(int requestCode, int resultCode, Intent data);
	}
}
