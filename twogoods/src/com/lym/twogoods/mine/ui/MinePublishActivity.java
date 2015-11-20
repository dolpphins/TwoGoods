package com.lym.twogoods.mine.ui;

import com.lym.twogoods.mine.fragment.MinePublishFragment;
import com.lym.twogoods.ui.base.BackFragmentActivity;

import android.os.Bundle;


/**
 * <p>
 * 	我发布的商品Activity
 * </p>
 * 
 * @author 麦灿标
 * */
public class MinePublishActivity extends BackFragmentActivity{

	private MinePublishFragment mFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterTitle("我发布的贰货");
		
		String username = getIntent().getStringExtra("username");
		mFragment = new MinePublishFragment(username);
		showFragment(mFragment);
	}
}
