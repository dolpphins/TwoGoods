package com.lym.twogoods.nearby.adapter;

import java.util.List;

import cn.bmob.push.a.thing;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.screen.NearbyScreen;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * <p>
 * 热门城市适配器
 * </p>
 * 
 * @author 龙宇文
 * */
public class NearbyHotCityGridViewAdapter extends BaseAdapter {

	private String TAG="NearbyHotCityGridViewAdapter";
	private Context context;
	private Activity activity;
	private List<String> hot_city_list;
	private LayoutInflater mInflater;

	public NearbyHotCityGridViewAdapter(Context context, List<String> list,Activity activity) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		hot_city_list = list;
		this.activity=activity;
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
					R.layout.nearby_select_city_hot_city_gridview_item, parent,
					false);
			viewHolder.btn_nearby_select_city_hot_city_griview_item = (TextView) convertView
					.findViewById(R.id.tv_nearby_select_city_hot_city_griview_item);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		buttonSetLayoutParams(viewHolder.btn_nearby_select_city_hot_city_griview_item);
		viewHolder.btn_nearby_select_city_hot_city_griview_item
				.setText(hot_city_list.get(position));
		viewHolder.btn_nearby_select_city_hot_city_griview_item.setTextColor(Color.BLACK);
		return convertView;
	}

	class ViewHolder {
		public TextView btn_nearby_select_city_hot_city_griview_item;
	}

	private void buttonSetLayoutParams(TextView textview) {
		PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
		specification = NearbyScreen
				.getHotCityItemThumbnailSpecification(activity);
		Log.v(TAG, "width="+specification.getWidth()+":heigth="+specification.getHeight());
		LayoutParams params = (LayoutParams) textview.getLayoutParams();
		params.height=specification.getHeight();
		params.width=specification.getWidth();
	}
}
