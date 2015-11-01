package com.lym.twogoods.index.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.index.fragment.GoodsSearchFragment;
import com.lym.twogoods.ui.base.BackFragmentActivity;

import android.os.Bundle;

/**
 * <p>
 * 	商品搜索Activity
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsSearchActivity extends BackFragmentActivity{

	private GoodsSearchFragment goodsSearchFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setCenterTitle(getResources().getString(R.string.search_goods));
		
		goodsSearchFragment = new GoodsSearchFragment();
		showFragment(goodsSearchFragment);
	}
}
