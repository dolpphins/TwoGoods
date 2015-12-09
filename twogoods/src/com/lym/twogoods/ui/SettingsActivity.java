package com.lym.twogoods.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.fragment.SettingsFragment;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.ui.base.BackFragmentActivity;

import android.os.Bundle;


/**
 * <p>
 * 	设置Activity
 * </p>
 * 
 * 
 * */
public class SettingsActivity extends BackFragmentActivity{

	private BaseFragment mFragment; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setCenterTitle(getResources().getString(R.string.settings));
		
		mFragment = new SettingsFragment();
		showFragment(mFragment);
	}
}
