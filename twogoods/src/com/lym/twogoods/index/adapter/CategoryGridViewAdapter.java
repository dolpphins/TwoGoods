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
 * 
 * 
 * 
 * */
public class CategoryGridViewAdapter extends BaseAdapter{

	/** 上下文 */
	private Context mContext;
	
	/** 数据集 */
	private List<String> mCategoryData;
	
	public CategoryGridViewAdapter(Context context, List<String> categoryData) {
		mContext = context;
		mCategoryData = categoryData;
	}
	
	@Override
	public int getCount() {
		if(mCategoryData == null) {
			return 0;
		}
		return mCategoryData.size();
	}

	@Override
	public Object getItem(int position) {
		return mCategoryData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder viewHolder = null;
		if(convertView == null) {
			viewHolder = new ItemViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.index_fragment_head_category_dropdown_item, null);
			viewHolder.index_fragment_head_category_name = (TextView) convertView.findViewById(R.id.index_fragment_head_category_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (CategoryGridViewAdapter.ItemViewHolder) convertView.getTag();
		}
		
		return convertView;
	}
	
	
	private class ItemViewHolder {
		
		private TextView index_fragment_head_category_name;
	}

}
