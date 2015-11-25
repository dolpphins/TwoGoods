package com.lym.twogoods.mine.adapter;

import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;

import android.app.Activity;

/**
 * 我关注的商品列表适配器
 * 
 * @author 麦灿标
 */
public class MineFocusGoodsListAdapter extends BaseGoodsListAdapter {

	public MineFocusGoodsListAdapter(Activity at, List<Goods> goodsList) {
		super(at, goodsList);
	}
}
