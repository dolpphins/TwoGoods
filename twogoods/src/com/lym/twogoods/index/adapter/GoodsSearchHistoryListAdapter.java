package com.lym.twogoods.index.adapter;

import java.util.List;

import com.lym.twogoods.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


/**
 * <p>
 * 	搜索商品历史记录ListView适配器
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsSearchHistoryListAdapter extends BaseAdapter{

	private Context mContext;
	
	private List<String> mHistoryList;

	public GoodsSearchHistoryListAdapter(Context context, List<String> historyList) {
		mContext = context;
		mHistoryList = historyList;
	}
	
	@Override
	public int getCount() {
		if(mHistoryList == null) {
			return 0;
		}
		return mHistoryList.size();
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
		ItemViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ItemViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.index_goods_search_history_item, null);
			viewHolder.index_goods_search_history_item_tv = (TextView) convertView.findViewById(R.id.index_goods_search_history_item_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}
		
		viewHolder.index_goods_search_history_item_tv.setText(mHistoryList.get(position));
		
		return convertView;
	}
	
	private static class ItemViewHolder {
		
		private TextView index_goods_search_history_item_tv;
	}
}
