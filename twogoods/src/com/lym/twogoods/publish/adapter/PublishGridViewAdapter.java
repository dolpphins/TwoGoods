package com.lym.twogoods.publish.adapter;

import java.io.IOException;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.publish.util.PublishBimp;
import com.lym.twogoods.screen.PublishGoodsScreen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class PublishGridViewAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater minInflater;
	private List<String> list;
	
	public PublishGridViewAdapter(Context context,List<String> list) {
		this.context=context;
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
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = minInflater.inflate(R.layout.publish_gridview_item,
					parent, false);
			viewHolder.item_image = (ImageView) convertView
					.findViewById(R.id.iv_publish_gridview_image);
			viewHolder.item_delect = (ImageView) convertView
					.findViewById(R.id.iv_publish_gridview_item_delect);
			convertView.setTag(viewHolder);
		} else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.item_image.setScaleType(ScaleType.CENTER_CROP);
		imageViewSetLayoutParams(viewHolder.item_image);
		try {
			viewHolder.item_image.setImageBitmap(PublishBimp.revitionImageSize(list.get(position)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		viewHolder.item_delect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				list.remove(index);
				notifyDataSetChanged();
			}
		});
		
		return convertView;
	}

	class ViewHolder{
		public ImageView item_image;
		public ImageView item_delect;
	}
	
	private void imageViewSetLayoutParams(ImageView imageView) {
		PictureThumbnailSpecification specification=new PictureThumbnailSpecification();
		specification=PublishGoodsScreen.getPublishPictureThumbnailSpecification((Activity) context);
		LayoutParams params=new LayoutParams(specification.getWidth(),specification.getHeight());
		imageView.setLayoutParams(params);
	}
}
