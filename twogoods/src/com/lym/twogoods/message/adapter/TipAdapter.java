package com.lym.twogoods.message.adapter;


import com.lym.twogoods.R;
import com.lym.twogoods.message.viewHolder.MessageItemViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 如果用户没有登录，用来提示用户登录
 * @author 尧俊锋
 *
 */
public class TipAdapter extends BaseAdapter {
	
	public Context mContext;
	public LayoutInflater mInflater;
	public int photos[];
	
	public TipAdapter(Context context,int Images[]){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		photos = Images;
	}

	@Override
	public int getCount() {
		return photos.length;
	}

	@Override
	public Object getItem(int position) {
		return photos[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		view = convertView;
		view = mInflater.inflate(R.layout.message_list_no_login_tip, parent, false);
		view.setClickable(false);
		
		return view;
	}

}
