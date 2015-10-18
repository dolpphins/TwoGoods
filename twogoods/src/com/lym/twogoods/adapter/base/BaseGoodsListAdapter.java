package com.lym.twogoods.adapter.base;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * <p>
 * 	基本的商品列表适配器
 * </p>
 * <p>
 * 	所有商品展示列表ListView的Adapter都必须继承自该类
 * </p>
 * 
 * @author 麦灿标
 * */
public class BaseGoodsListAdapter extends BaseAdapter{

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
