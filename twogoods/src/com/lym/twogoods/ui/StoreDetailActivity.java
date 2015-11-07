package com.lym.twogoods.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.fragment.StoreDetailFragment;
import com.lym.twogoods.ui.base.BackFragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;


/**
 * <p>
 * 	店铺详情Activity
 * </p>
 * <p>
 * 	必须传递用户数据:<pre>intent.putExtra("user", user);</pre>
 * </p>
 * 
 * @author 麦灿标
 * */
public class StoreDetailActivity extends BackFragmentActivity{
	
	private final static String TAG = "StoreDetailActivity";
	
	private StoreDetailFragment mStoreDetailFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		User user = (User) getIntent().getSerializableExtra("user");
		mStoreDetailFragment = new StoreDetailFragment(user);
		showFragment(mStoreDetailFragment);
	}
}
