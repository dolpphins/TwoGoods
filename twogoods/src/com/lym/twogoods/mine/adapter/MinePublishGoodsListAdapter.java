package com.lym.twogoods.mine.adapter;

import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;

import android.app.Activity;

/**
 * 我发布的商品列表适配器
 * 
 * @author 麦灿标
 */
public class MinePublishGoodsListAdapter extends BaseGoodsListAdapter {

	public MinePublishGoodsListAdapter(Activity at, List<Goods> goodsList) {
		super(at, goodsList);
	}

	@Override
	protected void setCustomContent(ItemViewHolder viewHolder) {
		super.setCustomContent(viewHolder);
		viewHolder.base_goods_listview_item_user_layout.setOnTouchListener(null);
	}
}
