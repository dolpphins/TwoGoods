package com.lym.twogoods.index.adapter;

import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;

import android.app.Activity;

/**
 * <p>
 * 	首页商品ListView适配器
 * </p>
 * 
 * @author 麦灿标
 * */
public class IndexGoodsListAdapter extends BaseGoodsListAdapter{

	public IndexGoodsListAdapter(Activity at, List<Goods> goodsList) {
		super(at, goodsList);
	}

}
