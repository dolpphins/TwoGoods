package com.lym.twogoods.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import com.lym.twogoods.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

/**
 * <p>
 * 发布商品照片的gridview的适配器
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishGridViewAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<HashMap<String, Object>> list;

	public PublishGridViewAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHoder viewHoder = null;
		if (convertView == null) {
			viewHoder = new ViewHoder();
			convertView = mInflater.inflate(R.layout.publish_gridview_item,
					parent, false);
			viewHoder.item_image = (ImageView) convertView
					.findViewById(R.id.iv_publish_gridview_image);
			viewHoder.item_delect = (ImageView) convertView
					.findViewById(R.id.iv_publish_gridview_item_delect);
			convertView.setTag(viewHoder);
		} else {
			viewHoder=(ViewHoder) convertView.getTag();
		}
		viewHoder.item_image.setImageResource((Integer) list.get(position)
				.get("image"));
		return convertView;
	}

	class ViewHoder {
		public ImageView item_image;
		public ImageView item_delect;
	}
}
