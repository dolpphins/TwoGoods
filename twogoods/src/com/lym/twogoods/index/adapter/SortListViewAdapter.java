package com.lym.twogoods.index.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.lym.twogoods.R;
import com.lym.twogoods.index.manager.GoodsSortManager;
import com.lym.twogoods.index.manager.GoodsSortManager.GoodsSort;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>
 * 	the ListView adapter of index fragment's header sort dropdown layout.
 * </p>
 * 
 * @author mao
 * */
public class SortListViewAdapter extends BaseAdapter {

	private final static String TAG = "SortListViewAdapter";
	
	private Context mContext;
	
	private List<GoodsSort> mGoodsSortData;
	
	private Map<Integer, GoodsSortManager.GoodsSort> goodsSortMap = new HashMap<Integer, GoodsSortManager.GoodsSort>();
	
	private int mCurrentSelectedPosition;
	
	public SortListViewAdapter(Context context, List<GoodsSort> goodsSort) {
		mContext = context;
		mGoodsSortData = goodsSort;
	}
	
	@Override
	public int getCount() {
		if(mGoodsSortData == null) {
			return 0;
		}
		return mGoodsSortData.size();
 	}

	@Override
	public Object getItem(int position) {
		return mGoodsSortData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Log.i(TAG, "getView");
		ItemViewHolder viewHolder = null;
		if(convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.index_fragment_head_sort_dropdown_item, null);
			viewHolder = new ItemViewHolder();
			viewHolder.index_fragment_head_sort_name = (TextView) convertView.findViewById(R.id.index_fragment_head_sort_name);
			viewHolder.index_fragment_head_sort_icon = (ImageView) convertView.findViewById(R.id.index_fragment_head_sort_icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}
		GoodsSort item = mGoodsSortData.get(position);
		
		if(goodsSortMap.get(Integer.valueOf(position)) == null) {
			goodsSortMap.put(Integer.valueOf(position), item);
		}
		
		setItemContent(viewHolder, item, position);
		
		return convertView;
	}
	
	private void setItemContent(ItemViewHolder viewHolder,  GoodsSort item, int position) {
		if(viewHolder == null || item == null) {
			return;
		}
		String s = GoodsSortManager.getString(mContext, item);
		viewHolder.index_fragment_head_sort_name.setText(s);
		if(position == mCurrentSelectedPosition) {
			viewHolder.index_fragment_head_sort_icon.setVisibility(View.VISIBLE);
		} else {
			viewHolder.index_fragment_head_sort_icon.setVisibility(View.GONE);
		}
	}
	
	public GoodsSort getCurrentGoodsSort(int position) {
		return (GoodsSort)goodsSortMap.get(Integer.valueOf(position));
	}
	
	public void setCurrentSelectedPosition(int position) {
		mCurrentSelectedPosition = position;
	}
	
	private class ItemViewHolder {
		
		private TextView index_fragment_head_sort_name;
		
		private ImageView index_fragment_head_sort_icon;
	}

}
