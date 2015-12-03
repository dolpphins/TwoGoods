package com.lym.twogoods.adapter;

import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsGridViewAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.fragment.base.PullGridViewFragment;

import android.app.Activity;

public class NearbyAdapter extends BaseGoodsGridViewAdapter{

	public NearbyAdapter(Activity at, List<Goods> goodsList, PullGridViewFragment fragment) {
		super(at, goodsList, fragment);
	}
	
}
