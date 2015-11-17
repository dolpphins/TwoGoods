package com.lym.twogoods.index.adapter;

import java.util.List;

import com.lym.twogoods.bean.GoodsComment;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


/**
 * <p>
 * 	商品评论ListView适配器
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsCommentListViewAdapter extends BaseAdapter {

	private Context mContext;
	
	private List<GoodsComment> mGoodsCommentList;
	
	public GoodsCommentListViewAdapter(Context context, List<GoodsComment> goodsCommentList) {
		mContext = context;
		mGoodsCommentList = goodsCommentList;
	}
	
	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
	}

}
