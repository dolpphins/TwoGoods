package com.lym.twogoods.mine.adapter;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.manager.ImageLoaderHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 我的更多信息列表Adapter
 * 
 * @author 麦灿标
 * */
public class PersonalityInfoAdapter extends BaseAdapter{

	private Context mContext;
	
	private User data;
	
	public PersonalityInfoAdapter(Context context, User user) {
		mContext = context;
		data = user;
	}
	
	@Override
	public int getCount() {
		if(data == null) {
			return 0;
		} else {
			return 3;
		}
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = null;
		//头像Item单独处理
		if(position == 0) {
			v = LayoutInflater.from(mContext).inflate(R.layout.user_detail_more_list_head_item, null);
			ImageView headIv = (ImageView) v.findViewById(R.id.user_detail_more_list_item_head_iv);
			TextView username = (TextView) v.findViewById(R.id.user_detail_more_list_item_username);
			ImageLoaderHelper.loadUserHeadPictureThumnail(mContext, headIv, data.getHead_url(), null);
			username.setText(data.getUsername());
			setClickEventForHead(headIv);
		} else {
			v = LayoutInflater.from(mContext).inflate(R.layout.mine_more_list_common_item, null);
			TextView leftTv = (TextView) v.findViewById(R.id.user_detail_more_list_common_item_left_tv);
			TextView rightTv = (TextView) v.findViewById(R.id.user_detail_more_list_common_item_right_tv);
			setItemContent(position, leftTv, rightTv);
			setItemClickEvent(position, v);
		}
		
		return v;
	}
	
	private void setItemContent(int position, TextView leftTv, TextView rightTv) {
		switch (position) {
		case 1:
			leftTv.setText(mContext.getResources().getString(R.string.mine_sex));
			rightTv.setText(data.getSex());
			break;
		case 2:
			leftTv.setText(mContext.getResources().getString(R.string.mine_phone));
			rightTv.setText(data.getPhone());
			break;
		default:
			break;
		}
	}
	
	private void setClickEventForHead(ImageView iv) {
		
	}
	
	private void setItemClickEvent(int position, View v) {
		if(position == 1) {
			
		} else if(position == 2) {
			
		}
	}

}
