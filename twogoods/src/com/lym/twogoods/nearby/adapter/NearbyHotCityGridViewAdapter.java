package com.lym.twogoods.nearby.adapter;

import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.screen.NearbyScreen;

import android.app.Activity;
import android.content.Context;
import android.text.NoCopySpan.Concrete;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

public class NearbyHotCityGridViewAdapter extends BaseAdapter {

	private Context context;
	private List<String> hot_city_list;
	private LayoutInflater mInflater;

	public NearbyHotCityGridViewAdapter(Context context, List<String> list) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		hot_city_list = list;
	}

	@Override
	public int getCount() {
		return hot_city_list.size();
	}

	@Override
	public Object getItem(int position) {
		return hot_city_list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(
					R.layout.nearby_select_city_hot_city_gridview_item, null);
			viewHolder.btn_nearby_select_city_hot_city_griview_item = (Button) convertView
					.findViewById(R.id.btn_nearby_select_city_hot_city_griview_item);
			convertView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) convertView.getTag();
		}
		buttonSetLayoutParams(viewHolder.btn_nearby_select_city_hot_city_griview_item);
		viewHolder.btn_nearby_select_city_hot_city_griview_item.setText(hot_city_list.get(position));
		return convertView;
	}

	class ViewHolder {
		public Button btn_nearby_select_city_hot_city_griview_item;
	}
	
	private void buttonSetLayoutParams(Button button) {
		PictureThumbnailSpecification specification=new PictureThumbnailSpecification();
		specification=NearbyScreen.getHotCityItemThumbnailSpecification((Activity) context);
		LayoutParams params=new LayoutParams(specification.getWidth(),specification.getHeight());
		button.setLayoutParams(params);
	}
}
