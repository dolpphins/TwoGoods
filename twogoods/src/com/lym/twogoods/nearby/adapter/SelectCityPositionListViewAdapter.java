package com.lym.twogoods.nearby.adapter;

import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.nearby.NearbyPositionModelBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SelectCityPositionListViewAdapter extends BaseAdapter implements
		SectionIndexer {

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
			viewHolder.letter = (TextView) convertView
					.findViewById(R.id.tv_select_city_position_letter);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.tv_select_city_position_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		NearbyPositionModelBean bean = list.get(position);
		// 根据position获取分类的首字母的AscII值
		int section = getSectionForPosition(position);
		// 如果当前位置等于分类首字母的char值，则认为第一次出现
		if (position == getPositionForSection(section)) {
			viewHolder.letter.setVisibility(View.VISIBLE);
			viewHolder.letter.setText(bean.getPositionLetters());
		} else {
			viewHolder.letter.setVisibility(View.GONE);
		}
		viewHolder.name.setText(bean.getName());
		return convertView;
	}

	class ViewHolder {
		TextView letter;
		TextView name;
	}

	/*
	 * 
	 * 当listview数据发生变化的时候，调用此方法更新listview
	 */
	public void updateListView(List<NearbyPositionModelBean> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	/*
	 * 
	 * 根据当前位置获取分类的首字母的ascII值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getPositionLetters().charAt(0);
	}

	/*
	 * 
	 * 根据分类的首字母的ascII值获取其第一次出现该首字母的位置
	 */
	@SuppressLint("DefaultLocale") 
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String letter = list.get(i).getPositionLetters();
			char letterAscII = letter.toUpperCase().charAt(0);
			if (letterAscII == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
