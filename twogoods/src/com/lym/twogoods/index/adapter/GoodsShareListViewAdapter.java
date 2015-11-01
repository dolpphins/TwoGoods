package com.lym.twogoods.index.adapter;

import java.util.List;
import java.util.Map;

import com.lym.twogoods.R;
import com.lym.twogoods.config.ShareConfiguration;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>
 * 	商品分享布局GridView适配器
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsShareListViewAdapter extends BaseAdapter{

	private Context mContext;
	
	private List<ShareConfiguration.ShareItem> mShareList;
	
	/**
	 * 构造函数
	 * 
	 * @param context 上下文
	 * @param shareMap 分享集合
	 * */
	public GoodsShareListViewAdapter(Context context, List<ShareConfiguration.ShareItem> shareList) {
		mContext = context;
		mShareList = shareList;
	}
	
	@Override
	public int getCount() {
		if(mShareList == null) {
			return 0;
		}
		return mShareList.size();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.index_goods_detail_share_listview_item, null);
			AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			convertView.setLayoutParams(params);
			viewHolder = new ItemViewHolder();
			viewHolder.index_goods_detail_share_listview_item_iv = (ImageView) convertView.findViewById(R.id.index_goods_detail_share_listview_item_iv);
			viewHolder.index_goods_detail_share_listview_item_tv = (TextView) convertView.findViewById(R.id.index_goods_detail_share_listview_item_tv);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ItemViewHolder) convertView.getTag();
		}
		
		ShareConfiguration.ShareItem item = mShareList.get(position);
		setItemContent(viewHolder, item);
		
		return convertView;
	}
	
	private void setItemContent(ItemViewHolder viewHolder, ShareConfiguration.ShareItem value) {
		if(viewHolder == null || value == null) {
			return;
		}
		Drawable d = mContext.getResources().getDrawable(value.shareDrawableID);
		viewHolder.index_goods_detail_share_listview_item_iv.setImageDrawable(d);
		viewHolder.index_goods_detail_share_listview_item_tv.setText(value.shareName);
	}
	
	
	
	private static class ItemViewHolder {
		
		private ImageView index_goods_detail_share_listview_item_iv;
		
		private TextView index_goods_detail_share_listview_item_tv;
	}

}
