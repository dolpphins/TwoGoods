package com.lym.twogoods.message.adapter;

import com.lym.twogoods.R;
import com.lym.twogoods.message.config.ChatBottomConfig;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 点击添加按钮显示的viewpager里gridveiw 的adapter
 * @author yao
 *
 */
class ChatButtonGridViewAdapter extends BaseAdapter
{
	Integer imgs[];
	String texts[];
	Context mContext;
	
	public ChatButtonGridViewAdapter(Context context) {
		mContext = context;
		init();
	}
	public void init()
	{
		imgs = ChatBottomConfig.getImages();
		texts = ChatBottomConfig.getTitles();
	}
	@Override
	public int getCount() {
		return imgs.length;
	}

	@Override
	public Object getItem(int position) {
		return imgs[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view; 
        if (convertView == null) { 
            view = LayoutInflater.from(mContext).inflate(R.layout.message_chat_bottom_add_item, null);
        }  
        else { 
            view = (View) convertView; 
        } 
        ImageView iv = (ImageView) view.findViewById(R.id.image);
        TextView tv = (TextView)view.findViewById(R.id.title);
        iv.setImageResource(imgs[position]);
        tv.setText(texts[position]);
        return view; 
	}
}