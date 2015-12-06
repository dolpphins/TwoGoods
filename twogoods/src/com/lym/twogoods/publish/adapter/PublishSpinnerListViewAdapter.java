package com.lym.twogoods.publish.adapter;

import java.util.ArrayList;

import com.lym.twogoods.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PublishSpinnerListViewAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> list;
	public PublishSpinnerListViewAdapter(Context context,ArrayList<String> list) {
		this.context=context;
		this.list=list;
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

	@SuppressLint("InflateParams") 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if (convertView==null) {
			viewHolder=new ViewHolder();
			LayoutInflater inflater=LayoutInflater.from(context);
			convertView=inflater.inflate(R.layout.publish_spinner_dropdown_item, null);
			viewHolder.textView=(TextView) convertView.findViewById(R.id.tv_spinner_dropdown_item);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		viewHolder.textView.setText(list.get(position));
		return convertView;
	}

	class ViewHolder{
		TextView textView;
	}
}
