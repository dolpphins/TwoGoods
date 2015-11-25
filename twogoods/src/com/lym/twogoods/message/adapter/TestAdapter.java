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


public class TestAdapter extends BaseAdapter {
	
	public Context mContext;
	public LayoutInflater mInflater;
	public int photos[];
	
	public TestAdapter(Context context,int Images[])
	{
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		photos = Images;
	}

	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return photos.length;
	}
	
	public void remove(int position)
	{
		System.out.println("remove");
		for(int i = position-1;i<photos.length-1;i++)
		{
			photos[i] = photos[i+1];
			System.out.println("testadapter"+i);
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO 自动生成的方法存根
		return photos[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
		//final BmobRecent item = mData.get(position);
		View view;
		view = convertView;
		
		MessageItemViewHolder holder = null;
		 
		if (convertView == null) {
			view = mInflater.inflate(R.layout.message_list_item_conversation, parent, false);
			holder = new MessageItemViewHolder();
			holder.avatar = (ImageView) view.findViewById(R.id.
					message_list_iv_recent_avatar_head);
			holder.name = (TextView) view.findViewById(R.id.
					message_list_tv_recent_name);
			
			holder.recent_msg = (TextView) view.findViewById(R.id.
					message_list_tv_recent_msg);
			holder.time = (TextView) view.findViewById(R.id.
					message_list_tv_recent_time);
			view.setTag(holder);
		}else{
			holder = (MessageItemViewHolder) view.getTag();
		}
		
		ImageView iv_recent_avatar = holder.avatar;
		TextView tv_recent_name = holder.name;
		TextView tv_recent_msg = holder.recent_msg;
		TextView tv_recent_time = holder.time;
		//填充数据
//		String avatar = item.getAvatar();
//		if(avatar!=null&& !avatar.equals("")){
//			ImageLoader.getInstance().displayImage(avatar, iv_recent_avatar, ImageLoadOptions.getOptions());
//		}else{
//			iv_recent_avatar.setImageResource(R.drawable.user_default_head);
//		}
		iv_recent_avatar.setImageResource(photos[position]);
		tv_recent_name.setText("yao");
		tv_recent_msg .setText("hello");
		tv_recent_time.setText("12:00");
				
		
		return view;
	}

	

}
