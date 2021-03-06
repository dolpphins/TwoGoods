package com.lym.twogoods.adapter.base;

import java.util.List;

import com.lym.twogoods.bean.Goods;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 商品列表适配器基类
 * 
 * @author 麦灿标
 *
 */
public abstract class BaseGoodsListAdapter extends BaseAdapter {

	protected Context mContext;
	
	protected List<Goods> mGoodsList;
	
	public BaseGoodsListAdapter(Context context, List<Goods> goodsList) {
		mContext = context;
		mGoodsList = goodsList;
	}
	
	@Override
	public int getCount() {
		if(mGoodsList != null) {
			return mGoodsList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if(mGoodsList != null && position < mGoodsList.size()) {
			return mGoodsList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
