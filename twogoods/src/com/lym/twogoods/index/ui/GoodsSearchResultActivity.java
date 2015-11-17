package com.lym.twogoods.index.ui;

import com.lym.twogoods.index.fragment.GoodsSearchResultFragment;
import com.lym.twogoods.ui.base.FragmentActivity;

import android.os.Bundle;
import android.view.Window;


/**
 * 显示商品搜索结果Activity,必须使用以下代码传递搜索关键字
 * <pre>
 * 		intent.putExtra("keyword", keyword);
 * </pre>
 * 
 * @author 麦灿标
 * */
public class GoodsSearchResultActivity extends FragmentActivity {

	/** 搜索关键字 */
	private String keyword;
	
	private GoodsSearchResultFragment mGoodsSearchResultFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		keyword = getIntent().getStringExtra("keyword");
		
		mGoodsSearchResultFragment = new GoodsSearchResultFragment(keyword);
		showFragment(mGoodsSearchResultFragment);
	}
}
