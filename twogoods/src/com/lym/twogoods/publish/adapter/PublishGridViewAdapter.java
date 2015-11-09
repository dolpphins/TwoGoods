package com.lym.twogoods.publish.adapter;

import java.io.IOException;
import java.util.ArrayList;

import com.lym.twogoods.R;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.publish.util.PublishBimp;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PublishGridViewAdapter extends BaseAdapter {

	private LayoutInflater minInflater;
	private ArrayList<String> list;
	
	public PublishGridViewAdapter(Context context,ArrayList<String> list) {
		this.list=list;
		minInflater=LayoutInflater.from(context);
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
		final int index=position;
		ViewHoder viewHoder = null;
		if (convertView == null) {
			viewHoder = new ViewHoder();
			convertView = minInflater.inflate(R.layout.publish_gridview_item,
					parent, false);
			viewHoder.item_image = (ImageView) convertView
					.findViewById(R.id.iv_publish_gridview_image);
			viewHoder.item_delect = (ImageView) convertView
					.findViewById(R.id.iv_publish_gridview_item_delect);
			convertView.setTag(viewHoder);
		} else {
			viewHoder=(ViewHoder) convertView.getTag();
		}
		try {
			viewHoder.item_image.setImageBitmap(PublishBimp.revitionImageSize(list.get(position)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewHoder.item_delect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PublishConfigManger.picsPath.remove(index);
			}
		});
		
		return convertView;
	}

	class ViewHoder{
		public ImageView item_image;
		public ImageView item_delect;
	}
}
