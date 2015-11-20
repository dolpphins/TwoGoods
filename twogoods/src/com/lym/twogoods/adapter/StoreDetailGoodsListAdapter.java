package com.lym.twogoods.adapter;

import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;

import android.app.Activity;

/**
 * 某一店家详情页面列表适配器
 * 
 * @author 麦灿标
 * */
public class StoreDetailGoodsListAdapter extends BaseGoodsListAdapter {

	public StoreDetailGoodsListAdapter(Activity at, List<Goods> goodsList) {
		super(at, goodsList);
	}

	@Override
	protected void setCustomContent(ItemViewHolder viewHolder) {
		super.setCustomContent(viewHolder);
		//禁止跳转
		viewHolder.base_goods_listview_item_user_layout.setOnTouchListener(null);
		
	}
}
