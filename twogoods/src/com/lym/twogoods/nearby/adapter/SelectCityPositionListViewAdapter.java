package com.lym.twogoods.nearby.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.nearby.NearbyPositionModelBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SelectCityPositionListViewAdapter extends BaseAdapter {

	private List<NearbyPositionModelBean> list;
	private Context context;

	public SelectCityPositionListViewAdapter(Context context,
			List<NearbyPositionModelBean> list) {
		this.context = context;
		this.list = list;
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
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.nearby_select_city_position_item, null);
			viewHolder.city = (TextView) convertView
					.findViewById(R.id.tv_select_city_position_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NearbyPositionModelBean bean = list.get(position);
		viewHolder.city.setText(bean.getName());
		return convertView;
	}

	class ViewHolder {
		TextView city;
	}

	/*
	 * 
	 * 当listview数据发生变化的时候，调用此方法更新listview
	 */
	public void updateListView(List<NearbyPositionModelBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}
}
