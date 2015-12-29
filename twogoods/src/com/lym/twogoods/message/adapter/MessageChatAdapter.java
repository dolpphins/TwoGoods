package com.lym.twogoods.message.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;





import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ChatConfiguration;
import com.lym.twogoods.manager.UniversalImageLoaderConfigurationManager;
import com.lym.twogoods.manager.UniversalImageLoaderManager;
import com.lym.twogoods.message.ImageLoadOptions;
import com.lym.twogoods.message.config.MessageConfig;
import com.lym.twogoods.message.listener.RecordPlayClickListener;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.ui.PersonalityInfoActivity;
import com.lym.twogoods.utils.TimeUtil;
import com.lym.twogoods.widget.EmojiTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 聊天的listview的适配器
 * @author 尧俊锋
 *
 */
public class MessageChatAdapter extends ChatBaseAdapter<ChatDetailBean> {
	//8种Item的类型
	//文本
	private final int TYPE_SEND_TXT = 0;	//发送的文本消息
	private final int TYPE_RECEIVER_TXT = 1; //接收的文本消息
	//图片
	private final int TYPE_SEND_IMAGE = 2;	//发送的图片消息
	private final int TYPE_RECEIVER_IMAGE = 3;	//接收的图片消息
	//位置
	private final int TYPE_SEND_LOCATION = 4;	//发送的位置消息
	private final int TYPE_RECEIVER_LOCATION = 5;	//接收的位置消息
	//语音	
	private final int TYPE_SEND_VOICE =6;	//发送的语音消息
	private final int TYPE_RECEIVER_VOICE = 7;	//接收的语音消息
	
	private DisplayImageOptions options;
	
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	
	private final String TAG = "MessageChatAdapter";
	//当前用户
	private String currentUserName;
	/**保存最近一条消息的时间*/
	private long last_message_time;
	/**对方头像url*/
	private String otherUserHeadUrl;
	
	static int acount = 0;
	/**
	 * 
	 * @param context
	 * @param list
	 * @param haedUrl对方的头像url
	 */
	public MessageChatAdapter(Context context, List<ChatDetailBean> list,String headUrl) {
		super(context, list);
		currentUserName = getCurrentUserName();
		otherUserHeadUrl = headUrl;
		last_message_time = TimeUtil.getCurrentMilliSecond();
		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher).resetViewBeforeLoading(true).cacheOnDisc(true)
		.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
		.considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();
	}
	
	//获取当前用户的信息
	private String getCurrentUserName() {
		return UserInfoManager.getInstance().getmCurrent().getUsername();
	}
	@Override
	public int getCount() {
		if(list==null){
			return 0;
		}
		return list.size();
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
	/**
	 * 通过判断消息类型来显示不一样的view
	 * @param message 
	 * @param position 
	 * @author 尧俊锋
	 */
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
					mInflater.inflate(R.layout.message_chat_item_received_text, null) 
					:
					mInflater.inflate(R.layout.message_chat_item_sent_text, null);
		}
	}
	
	@Override
	public View bindView(final int position, View convertView, ViewGroup parent) {
		final ChatDetailBean item = list.get(position);
		Log.i(TAG,"acount="+acount);
		acount++;
		convertView = createViewByType(item, position);
		//文本类型
		ImageView iv_avatar = ViewHolder.get(convertView, R.id.iv_avatar);
		final ImageView iv_fail_resend = ViewHolder.get(convertView, R.id.iv_fail_resend);//失败重发
		TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
		EmojiTextView tv_message = ViewHolder.get(convertView, R.id.tv_message);
		//图片
		ImageView iv_picture = ViewHolder.get(convertView, R.id.iv_picture);
		final ProgressBar progress_load = ViewHolder.get(convertView, R.id.progress_load);//进度条
		//位置
		TextView tv_location = ViewHolder.get(convertView, R.id.tv_location);
		//语音
		final ImageView iv_voice = ViewHolder.get(convertView, R.id.iv_voice);
		//语音长度
		final TextView tv_voice_length = ViewHolder.get(convertView, R.id.tv_voice_length);
		//点击头像进入个人资料,拿到缓存的
		String avatar = null;
		if(item.getUsername().equals(currentUserName)){
			avatar = UserInfoManager.getInstance().getmCurrent().getHead_url();
		}else{
			avatar = "";
		}
		//需要获得对方的头像url
		if(avatar!=null && !avatar.equals("")){//加载头像-为了不每次都加载头像
			ImageLoader.getInstance().displayImage(avatar, iv_avatar, ImageLoadOptions.getOptions(),animateFirstListener);
		}else{
			iv_avatar.setImageResource(R.drawable.user_default_head);
		}
		iv_avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent(mContext,PersonalityInfoActivity.class);
				User user = new User();
				user.setUsername(item.getUsername());
				if(item.getUsername().equals(currentUserName)){
					user.setHead_url(UserInfoManager.getInstance().getmCurrent().getHead_url());
				}else{
					user.setHead_url(otherUserHeadUrl);
				}
				user.setHead_url(otherUserHeadUrl);
				intent.putExtra("user", user);
				mContext.startActivity(intent);
			}
		});
		Log.i(TAG,"last_message_time:"+last_message_time);
		if((last_message_time-item.getPublish_time())<0)
		{
			
		}
		tv_time.setText(TimeUtil.getDescriptionTimeFromTimestamp(item.getPublish_time()));
		
		//根据消息的发送状态来显示相关的表示发送状态的控件
		if(getItemViewType(position)==TYPE_SEND_TXT||getItemViewType(position)==TYPE_SEND_LOCATION
				||getItemViewType(position)==TYPE_SEND_VOICE){//只有自己发送的消息才有重发机制
			//发送状态描述
			if(item.getLast_Message_Status()==MessageConfig.SEND_MESSAGE_SUCCEED){//发送成功
				Log.i(TAG,"发送状态：成功");
				progress_load.setVisibility(View.INVISIBLE);
				iv_fail_resend.setVisibility(View.INVISIBLE);
				if(item.getMessage_type()==ChatConfiguration.TYPE_MESSAGE_VOICE){
					if(tv_voice_length!=null)
						tv_voice_length.setVisibility(View.VISIBLE);
				}
			}else {
				if(item.getLast_Message_Status()==MessageConfig.SEND_MESSAGE_FAILED){//服务器无响应或者查询失败等原因造成的发送失败，均需要重发
				Log.i(TAG,"发送状态：失败");
				progress_load.setVisibility(View.INVISIBLE);
				iv_fail_resend.setVisibility(View.VISIBLE);
				if(item.getMessage_type()==ChatConfiguration.TYPE_MESSAGE_VOICE){
					tv_voice_length.setVisibility(View.GONE);
					}
				}else if(item.getLast_Message_Status()==MessageConfig.SEND_MESSAGE_ING){//开始上传
					Log.i(TAG,"发送状态：开始上传");
					progress_load.setVisibility(View.VISIBLE);
					iv_fail_resend.setVisibility(View.INVISIBLE);
					if(item.getMessage_type()==ChatConfiguration.TYPE_MESSAGE_VOICE){
						tv_voice_length.setVisibility(View.GONE);
					}
				}
			}
		}
		//根据消息内容的类别来显示
		final String text = item.getMessage();
		switch (item.getMessage_type()) {
		case ChatConfiguration.TYPE_MESSAGE_TEXT:
			if(text==null)
				tv_message.setText(null);
			else
				tv_message.setText(text);
			break;

		case  ChatConfiguration.TYPE_MESSAGE_PICTURE://图片类
			try {
				if (text != null && !text.equals("")) {//发送成功之后存储的图片类型的content和接收到的是不一样的
					dealWithImage(position, progress_load, iv_fail_resend, iv_picture, item);
				}else
				{
					iv_picture.setImageResource(R.drawable.goods_empty_picture);
				}
				iv_picture.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Intent intent =new Intent(mContext,DisplayPicturesActivity.class);
						ArrayList<String> photos = new ArrayList<String>();
						photos.add(getImageUrl(item));
						intent.putStringArrayListExtra("picturesUrlList", photos);
						intent.putExtra("position", 0);
						mContext.startActivity(intent);
					}
				});
				
			} catch (Exception e) {
			}
			break;
		
		case  ChatConfiguration.TYPE_MESSAGE_VOICE://语音消息
			if (text != null && !text.equals("")) {
				tv_voice_length.setVisibility(View.VISIBLE);
				tv_voice_length.setText("3''");
				String content = item.getMessage();
				RecordPlayClickListener clickListener = new RecordPlayClickListener(mContext);
				clickListener.setFilePath(content);
				clickListener.setImageView(iv_voice);
				//发送的消息
				if (item.getUsername().equals(currentUserName)) {
					//当发送成功或者发送已阅读的时候，则显示语音长度
					if(item.getLast_Message_Status()==MessageConfig.SEND_MESSAGE_SUCCEED){
						tv_voice_length.setVisibility(View.VISIBLE);
					}else{
						tv_voice_length.setVisibility(View.INVISIBLE);
						iv_fail_resend.setVisibility(View.VISIBLE);
					}
				} else {//接收的语音消息
					clickListener.setIsNetUrl(true);
				}
				iv_voice.setOnClickListener(clickListener);
			}
			break;
		default:
			break;
		}
		last_message_time = item.getPublish_time();
		return convertView;
	}
	
	private String getImageUrl(ChatDetailBean item){
		String showUrl = "";
		String content = item.getMessage();
		if(item.getUsername().equals(currentUserName)){
			String path = "";//path是用户手机发送的本地图片的路径
			//showUrl = "file://"+path;
			showUrl = content;
		}else{//如果是收到的消息，则需要从网络下载
			showUrl = content;
		}
		return showUrl;
	}
	
	private void dealWithImage(int position,final ProgressBar progress_load,
			ImageView iv_fail_resend,ImageView iv_picture,ChatDetailBean item){
		String content = item.getMessage();
		//发送的消息
		if(getItemViewType(position)==TYPE_SEND_IMAGE){
			if(item.getLast_Message_Status()==MessageConfig.SEND_MESSAGE_START){
				progress_load.setVisibility(View.VISIBLE);
				iv_fail_resend.setVisibility(View.INVISIBLE);
			}else if(item.getLast_Message_Status()==MessageConfig.SEND_MESSAGE_SUCCEED){
				progress_load.setVisibility(View.INVISIBLE);
				iv_fail_resend.setVisibility(View.INVISIBLE);
			
			}else if(item.getLast_Message_Status()==MessageConfig.SEND_MESSAGE_FAILED){
				progress_load.setVisibility(View.INVISIBLE);
				iv_fail_resend.setVisibility(View.VISIBLE);
			}else if(item.getLast_Message_Status()==MessageConfig.SEND_MESSAGE_RECEIVERED){
				progress_load.setVisibility(View.INVISIBLE);
				iv_fail_resend.setVisibility(View.INVISIBLE);
			}
			//发送的图片，直接用本地地址展示，
			String showUrl = item.getMessage();
			//为了方便每次都是取本地图片显示
			ImageLoaderConfiguration configuration = UniversalImageLoaderConfigurationManager
					.getDefaultImageLoaderConfiguration(mContext);
			ImageLoader imageLoader = UniversalImageLoaderManager.getImageLoader(configuration);
			imageLoader.displayImage("file://"+showUrl, iv_picture);
		}else{
			//接收的图片
			ImageLoader.getInstance().displayImage(content, iv_picture,options,new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					progress_load.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					progress_load.setVisibility(View.INVISIBLE);
				}
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					progress_load.setVisibility(View.INVISIBLE);
				}
				@Override
				public void onLoadingFailed(String arg0, View arg1,FailReason arg2) {
					progress_load.setVisibility(View.INVISIBLE);
				}
			});
		}
	}
	
	
	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}
	
	/**
	 * 用于在getView中获得相应的控件
	 * @author 尧俊锋
	 */
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
}
