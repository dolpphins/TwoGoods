package com.lym.twogoods.mine.ui;

import com.lym.twogoods.mine.fragment.MineFocusFragment;
import com.lym.twogoods.mine.fragment.MinePublishFragment;
import com.lym.twogoods.ui.base.BackFragmentActivity;

import android.os.Bundle;


/**
 * <p>
 * 	我关注的商品Activity
 * </p>
 * 
 * @author 麦灿标
 * */
public class MineFocusActivity extends BackFragmentActivity{

	private MineFocusFragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterTitle("我关注的贰货");
		
		String username = getIntent().getStringExtra("username");
		mFragment = new MineFocusFragment(username);
		showFragment(mFragment);
	}
}
