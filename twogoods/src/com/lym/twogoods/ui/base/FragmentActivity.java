package com.lym.twogoods.ui.base;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * <p>界面都是通过Fragment显示的Activity.</p>
 * <p>所有添加且被显示的Fragment的tag都是相应的Fragment类的Class对象Name值,
 * 即{@link Class#getName()}</p>
 * 
 * @author 麦灿标
 * */
public abstract class FragmentActivity extends BaseActivity{

	private final static String TAG = "FragmentActivity";
	
	/** 存放所有BaseFragment */
	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	
	/** Fragment管理器 */
	private FragmentManager mFragmentManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.app_fragment_activity);
		Log.i(TAG, "onCreate");
		//Fragment显示装饰布局
		FrameLayout contentView = (FrameLayout) findViewById(android.R.id.content);
		LinearLayout decorView = (LinearLayout) getLayoutInflater().inflate(R.layout.app_main_decorview, null);
		contentView.addView(decorView);
		
		mFragmentManager = getFragmentManager();
	}
	
	/**
	 * <p>添加Fragment,该方法可用于预先添加待显示的Fragment.</p>
	 * 
	 * @param fragment 要添加的Fragment
	 * 
	 * @return 如果fragment为null或者已经添加过该fragment,那么返回false,否则返回true.
	 * */
	public boolean addFragment(Fragment fragment) {
		if(fragment == null) {
			return false; 
		}
		if(mFragmentList.contains(fragment)) {
			return false;
		}
		return mFragmentList.add(fragment);//add方法总是返回true
	}
	
	/**
	 * <p>
	 * 	显示指定的Fragment
	 * </p>
	 * */
	public void showFragment(Fragment fragment) {
		if(fragment == null) {
			return;
		}
		hideAllFragment();//先隐藏所有Fragment
		
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		//如果还没添加
		if(!fragment.isAdded()) {
			ft.add(R.id.app_decorview_content, fragment, fragment.getClass().getName());
		}
		ft.show(fragment);
		ft.commit();
	}
	
	/**
	 * <p>
	 * 	隐藏指定的Fragment
	 * </p>
	 * */
	public void hideFragment(Fragment fragment) {
		if(fragment == null) {
			return;
		}
		if(fragment.isAdded() && fragment.isVisible()) {
			FragmentTransaction ft = mFragmentManager.beginTransaction();
			ft.hide(fragment);
			ft.commit();
		}
	}
	
	//隐藏所有正在显示的Fragment
	private void hideAllFragment() {
		for(Fragment f : mFragmentList) {
			hideFragment(f);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mFragmentList = null;
		mFragmentManager = null;
	}
}
