package com.lym.twogoods.message.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;




import com.lym.twogoods.R;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.config.ChatConfiguration;
import com.lym.twogoods.ui.DisplayPicturesActivity;


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
			if(msg.getMessage_type()==ChatConfiguration.TYPE_MESSAGE_PICTURE){
				return msg.getUsername().equals(currentUserName) ? TYPE_SEND_IMAGE: TYPE_RECEIVER_IMAGE;
			}else if(msg.getMessage_type()==ChatConfiguration.TYPE_MESSAGE_LOCATION){
				return msg.getUsername().equals(currentUserName) ? TYPE_SEND_LOCATION: TYPE_RECEIVER_LOCATION;
			}else if(msg.getMessage_type()==ChatConfiguration.TYPE_MESSAGE_VOICE){
				return msg.getUsername().equals(currentUserName) ? TYPE_SEND_VOICE: TYPE_RECEIVER_VOICE;
			}else{
			    return msg.getUsername().equals(currentUserName) ? TYPE_SEND_TXT: TYPE_RECEIVER_TXT;
			}
		}
		
		public View createViewByType(ChatDetailBean message, int position) {
			int type = message.getMessage_type();
		   if(type==ChatConfiguration.TYPE_MESSAGE_PICTURE){//图片类型
				return getItemViewType(position) == TYPE_RECEIVER_IMAGE ? 
						mInflater.inflate(R.layout.message_chat_item_received_image, null) 
						:
						mInflater.inflate(R.layout.message_chat_item_sent_image, null);
			}else if(type==ChatConfiguration.TYPE_MESSAGE_LOCATION){//位置类型
				return getItemViewType(position) == TYPE_RECEIVER_LOCATION ? 
						mInflater.inflate(R.layout.message_chat_item_received_location, null) 
						:
						mInflater.inflate(R.layout.message_chat_item_sent_location, null);
			}else if(type==ChatConfiguration.TYPE_MESSAGE_VOICE){//语音类型
				return getItemViewType(position) == TYPE_RECEIVER_VOICE ? 
						mInflater.inflate(R.layout.message_chat_item_received_voice, null) 
						:
						mInflater.inflate(R.layout.message_chat_item_sent_voice, null);
			}else{//剩下默认的都是文本
				return getItemViewType(position) == TYPE_RECEIVER_TXT ? 
						mInflater.inflate(R.layout.message_chat_item_received_message, null) 
						:
						mInflater.inflate(R.layout.message_chat_item_sent_message, null);
			}
		}
		
		@Override
		public View bindView(int position, View convertView, ViewGroup parent) {
			// TODO 自动生成的方法存根
			
			final ChatDetailBean item = list.get(position);
			if (convertView == null) {
				convertView = createViewByType(item, position);
			}
			//文本类型
			ImageView iv_avatar = ViewHolder.get(convertView, R.id.iv_avatar);
			final ImageView iv_fail_resend = ViewHolder.get(convertView, R.id.iv_fail_resend);//失败重发
			final TextView tv_send_status = ViewHolder.get(convertView, R.id.tv_send_status);//发送状态
			TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
			TextView tv_message = ViewHolder.get(convertView, R.id.tv_message);
			//图片
			ImageView iv_picture = ViewHolder.get(convertView, R.id.iv_picture);
			final ProgressBar progress_load = ViewHolder.get(convertView, R.id.progress_load);//进度条
			//位置
			TextView tv_location = ViewHolder.get(convertView, R.id.tv_location);
			//语音
			final ImageView iv_voice = ViewHolder.get(convertView, R.id.iv_voice);
			//语音长度
			final TextView tv_voice_length = ViewHolder.get(convertView, R.id.tv_voice_length);
			
			//点击头像进入个人资料
			/*String avatar = item.get;
			if(avatar!=null && !avatar.equals("")){//加载头像-为了不每次都加载头像
				ImageLoader.getInstance().displayImage(avatar, iv_avatar, ImageLoadOptions.getOptions(),animateFirstListener);
			}else{
				iv_avatar.setImageResource(R.drawable.head);
			}
			
			iv_avatar.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent intent =new Intent(mContext,SetMyInfoActivity.class);
					if(getItemViewType(position) == TYPE_RECEIVER_TXT 
							||getItemViewType(position) == TYPE_RECEIVER_IMAGE
					        ||getItemViewType(position)==TYPE_RECEIVER_LOCATION
					        ||getItemViewType(position)==TYPE_RECEIVER_VOICE){
						intent.putExtra("from", "other");
						intent.putExtra("username", item.getBelongUsername());
					}else{
						intent.putExtra("from", "me");
					}
					mContext.startActivity(intent);
				}
			});*/
			
			tv_time.setText(com.lym.twogoods.utils.TimeUtil.longToString(item.getPublish_time(),null));
			
			/*if(getItemViewType(position)==TYPE_SEND_TXT
//					||getItemViewType(position)==TYPE_SEND_IMAGE//图片单独处理
					||getItemViewType(position)==TYPE_SEND_LOCATION
					||getItemViewType(position)==TYPE_SEND_VOICE){//只有自己发送的消息才有重发机制
				//状态描述
				if(item.getStatus()==BmobConfig.STATUS_SEND_SUCCESS){//发送成功
					progress_load.setVisibility(View.INVISIBLE);
					iv_fail_resend.setVisibility(View.INVISIBLE);
					if(item.getMessage_type()==ChatConfiguration.TYPE_MESSAGE_VOICE){
						tv_send_status.setVisibility(View.GONE);
						tv_voice_length.setVisibility(View.VISIBLE);
					}else{
						tv_send_status.setVisibility(View.VISIBLE);
						tv_send_status.setText("已发送");
					}
				}else if(item.getStatus()==BmobConfig.STATUS_SEND_FAIL){//服务器无响应或者查询失败等原因造成的发送失败，均需要重发
					progress_load.setVisibility(View.INVISIBLE);
					iv_fail_resend.setVisibility(View.VISIBLE);
					tv_send_status.setVisibility(View.INVISIBLE);
					if(item.getMsgType()==BmobConfig.TYPE_VOICE){
						tv_voice_length.setVisibility(View.GONE);
					}
				}else if(item.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED){//对方已接收到
					progress_load.setVisibility(View.INVISIBLE);
					iv_fail_resend.setVisibility(View.INVISIBLE);
					if(item.getMsgType()==BmobConfig.TYPE_VOICE){
						tv_send_status.setVisibility(View.GONE);
						tv_voice_length.setVisibility(View.VISIBLE);
					}else{
						tv_send_status.setVisibility(View.VISIBLE);
						tv_send_status.setText("已阅读");
					}
				}else if(item.getStatus()==BmobConfig.STATUS_SEND_START){//开始上传
					progress_load.setVisibility(View.VISIBLE);
					iv_fail_resend.setVisibility(View.INVISIBLE);
					tv_send_status.setVisibility(View.INVISIBLE);
					if(item.getMsgType()==BmobConfig.TYPE_VOICE){
						tv_voice_length.setVisibility(View.GONE);
					}
				}
			}*/
			//根据类型显示内容
			final String text = item.getMessage();
			switch (item.getMessage_type()) {
			case ChatConfiguration.TYPE_MESSAGE_TEXT:
				/*try {
					SpannableString spannableString = FaceTextUtils
							.toSpannableString(mContext, text);
					tv_message.setText(spannableString);
				} catch (Exception e) {
				}*/
				tv_message.setText(text);
				
				break;

			case  ChatConfiguration.TYPE_MESSAGE_PICTURE://图片类
				try {
					if (text != null && !text.equals("")) {//发送成功之后存储的图片类型的content和接收到的是不一样的
						dealWithImage(position, progress_load, iv_fail_resend, tv_send_status, iv_picture, item);
					}
					iv_picture.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							Intent intent =new Intent(mContext,DisplayPicturesActivity.class);
							ArrayList<String> photos = new ArrayList<String>();
							photos.add(getImageUrl(item));
							intent.putStringArrayListExtra("photos", photos);
							intent.putExtra("position", 0);
							mContext.startActivity(intent);
						}
					});
					
				} catch (Exception e) {
				}
				break;
				
			
			/*case  ChatConfiguration.TYPE_MESSAGE_VOICE://语音消息
				try {
					if (text != null && !text.equals("")) {
						tv_voice_length.setVisibility(View.VISIBLE);
						String content = item.getMessage();
						if (item.getUsername().equals(currentUserName)) {//发送的消息
							if(item.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED
									||item.getStatus()==BmobConfig.STATUS_SEND_SUCCESS){//当发送成功或者发送已阅读的时候，则显示语音长度
								tv_voice_length.setVisibility(View.VISIBLE);
								String length = content.split("&")[2];
								tv_voice_length.setText(length+"\''");
							}else{
								tv_voice_length.setVisibility(View.INVISIBLE);
							}
						} else {//收到的消息
							boolean isExists = BmobDownloadManager.checkTargetPathExist(currentObjectId,item);
							if(!isExists){//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
								String netUrl = content.split("&")[0];
								final String length = content.split("&")[1];
								
								downloadTask.execute(netUrl);
							}else{
								String length = content.split("&")[2];
								tv_voice_length.setText(length+"\''");
							}
						}
					}
					//播放语音文件
					iv_voice.setOnClickListener(new NewRecordPlayClickListener(mContext,item,iv_voice));
				} catch (Exception e) {
					
				}
				
				break;*/
			default:
				break;
			}
			return null;
		}
		
		public static class ViewHolder {
			public static <T extends View> T get(View view, int id) {
				SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
				if (viewHolder == null) {
					viewHolder = new SparseArray<View>();
					view.setTag(viewHolder);
				}
				View childView = viewHolder.get(id);
				if (childView == null) {
					childView = view.findViewById(id);
					viewHolder.put(id, childView);
				}
				return (T) childView;
			}
		}
		
		
		/** 获取图片的地址--
		  * @Description: TODO
		  * @param @param item
		  * @param @return 
		  * @return String
		  * @throws
		  */
		private String getImageUrl(ChatDetailBean item){
			String showUrl = "";
			String text = item.getMessage();
			if(item.getUsername().equals(currentUserName)){//
				if(text.contains("&")){
					showUrl = text.split("&")[0];
				}else{
					showUrl = text;
				}
			}else{//如果是收到的消息，则需要从网络下载
				showUrl = text;
			}
			return showUrl;
		}
		
		
		/** 处理图片
		  * @Description: TODO
		  * @param @param position
		  * @param @param progress_load
		  * @param @param iv_fail_resend
		  * @param @param tv_send_status
		  * @param @param iv_picture
		  * @param @param item 
		  * @return void
		  * @throws
		  */
		private void dealWithImage(int position,final ProgressBar progress_load,
				ImageView iv_fail_resend,TextView tv_send_status,
				ImageView iv_picture,ChatDetailBean item){
			String text = item.getMessage();
			/*if(getItemViewType(position)==TYPE_SEND_IMAGE){//发送的消息
				if(item.getStatus()==BmobConfig.STATUS_SEND_START){
					progress_load.setVisibility(View.VISIBLE);
					iv_fail_resend.setVisibility(View.INVISIBLE);
					tv_send_status.setVisibility(View.INVISIBLE);
				}else if(item.getStatus()==BmobConfig.STATUS_SEND_SUCCESS){
					progress_load.setVisibility(View.INVISIBLE);
					iv_fail_resend.setVisibility(View.INVISIBLE);
					tv_send_status.setVisibility(View.VISIBLE);
					tv_send_status.setText("已发送");
				}else if(item.getStatus()==BmobConfig.STATUS_SEND_FAIL){
					progress_load.setVisibility(View.INVISIBLE);
					iv_fail_resend.setVisibility(View.VISIBLE);
					tv_send_status.setVisibility(View.INVISIBLE);
				}else if(item.getStatus()==BmobConfig.STATUS_SEND_RECEIVERED){
					progress_load.setVisibility(View.INVISIBLE);
					iv_fail_resend.setVisibility(View.INVISIBLE);
					tv_send_status.setVisibility(View.VISIBLE);
					tv_send_status.setText("已阅读");
				}
//				如果是发送的图片的话，因为开始发送存储的地址是本地地址，发送成功之后存储的是本地地址+"&"+网络地址，因此需要判断下
				String showUrl = "";
				if(text.contains("&")){
					showUrl = text.split("&")[0];
				}else{
					showUrl = text;
				}
				//为了方便每次都是取本地图片显示
				ImageLoader.getInstance().displayImage(showUrl, iv_picture);
			}else{
				ImageLoader.getInstance().displayImage(text, iv_picture,options,new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						progress_load.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
						progress_load.setVisibility(View.INVISIBLE);
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						// TODO Auto-generated method stub
						progress_load.setVisibility(View.INVISIBLE);
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						progress_load.setVisibility(View.INVISIBLE);
					}
				});
			}*/
		}

}
