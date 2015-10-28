package com.lym.twogoods.message.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.ChatDetailBean;

public class MessageChatAdapter extends ChatBaseAdapter<ChatDetailBean> {

	//8种Item的类型
		//文本
		private final int TYPE_RECEIVER_TXT = 0;
		private final int TYPE_SEND_TXT = 1;
		//图片
		private final int TYPE_SEND_IMAGE = 2;
		private final int TYPE_RECEIVER_IMAGE = 3;
		//位置
		private final int TYPE_SEND_LOCATION = 4;
		private final int TYPE_RECEIVER_LOCATION = 5;
		//语音
		private final int TYPE_SEND_VOICE =6;
		private final int TYPE_RECEIVER_VOICE = 7;
		
		
		
		
		//当前用户
		private String currentUserName;
		
		public MessageChatAdapter(Context context, List<ChatDetailBean> list) {
			super(context, list);
			// TODO 自动生成的构造函数存根
			currentUserName = getCurrentUserName();
		}
		
		
		//获取当前用户的信息
		private String getCurrentUserName() {
			// TODO 自动生成的方法存根
			return null;
		}



		@Override
		public int getItemViewType(int position) {
			ChatDetailBean msg = list.get(position);
			if(msg.getMessage_type()==BmobConfig.TYPE_IMAGE){
				return msg.getUsername().equals(currentUserName) ? TYPE_SEND_IMAGE: TYPE_RECEIVER_IMAGE;
			}else if(msg.getMessage_type()==BmobConfig.TYPE_LOCATION){
				return msg.getUsername().equals(currentUserName) ? TYPE_SEND_LOCATION: TYPE_RECEIVER_LOCATION;
			}else if(msg.getMessage_type()==BmobConfig.TYPE_VOICE){
				return msg.getUsername().equals(currentUserName) ? TYPE_SEND_VOICE: TYPE_RECEIVER_VOICE;
			}else{
			    return msg.getUsername().equals(currentUserName) ? TYPE_SEND_TXT: TYPE_RECEIVER_TXT;
			}
		}
		
		private View createViewByType(BmobMsg message, int position) {
			int type = message.getMsgType();
		  /* if(type==BmobConfig.TYPE_IMAGE){//图片类型
				return getItemViewType(position) == TYPE_RECEIVER_IMAGE ? 
						mInflater.inflate(R.layout.item_chat_received_image, null) 
						:
						mInflater.inflate(R.layout.item_chat_sent_image, null);
			}else if(type==BmobConfig.TYPE_LOCATION){//位置类型
				return getItemViewType(position) == TYPE_RECEIVER_LOCATION ? 
						mInflater.inflate(R.layout.item_chat_received_location, null) 
						:
						mInflater.inflate(R.layout.item_chat_sent_location, null);
			}else if(type==BmobConfig.TYPE_VOICE){//语音类型
				return getItemViewType(position) == TYPE_RECEIVER_VOICE ? 
						mInflater.inflate(R.layout.item_chat_received_voice, null) 
						:
						mInflater.inflate(R.layout.item_chat_sent_voice, null);
			}else{//剩下默认的都是文本
				return getItemViewType(position) == TYPE_RECEIVER_TXT ? 
						mInflater.inflate(R.layout.item_chat_received_message, null) 
						:
						mInflater.inflate(R.layout.item_chat_sent_message, null);
			}*/
			return null;
		}
		
		@Override
		public View bindView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			return null;
		}
		

}
