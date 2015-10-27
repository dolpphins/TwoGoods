package com.lym.twogoods.adapter;

import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.config.EmotionUtils;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AbsListView.LayoutParams;

/**
 * <p>
 * 发布商品添加表情功能的gridview的适配器
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishEmotionGvAdapter extends BaseAdapter {

	private Context context;
	private List<String> emotionNames;
	private int itemWidth;

	public PublishEmotionGvAdapter(Context context, List<String> emotionNames,
			int itemWidth) {
		this.context = context;
		this.emotionNames = emotionNames;
		this.itemWidth = itemWidth;
	}

	@Override
	public int getCount() {
		return emotionNames.size() + 1;
	}

	@Override
	public String getItem(int position) {
		return emotionNames.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(context);
		LayoutParams params = new LayoutParams(itemWidth, itemWidth);
		iv.setPadding(itemWidth / 8, itemWidth / 8, itemWidth / 8,
				itemWidth / 8);
		iv.setLayoutParams(params);

		if (position == getCount() - 1) {
			iv.setImageResource(R.drawable.emotion_delete_icon);
		} else {
			String emotionName = emotionNames.get(position);
			iv.setImageResource(EmotionUtils.getImgByName(emotionName));
		}

		return iv;
	}

}
