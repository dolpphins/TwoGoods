package com.lym.twogoods.message.adapter;


import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.config.ChatConfiguration;
import com.lym.twogoods.message.ImageLoadOptions;
import com.lym.twogoods.message.config.MessageConfig;
import com.lym.twogoods.message.view.BadgeView;
import com.lym.twogoods.message.viewHolder.MessageItemViewHolder;
import com.lym.twogoods.utils.ImageUtil;
import com.lym.twogoods.utils.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 消息列表的适配器
 * @author yao
 *
 */
public class MessageListAdapter extends ArrayAdapter<ChatSnapshot> implements Filterable{

	String TAG = "MessageListAdapter";
	private LayoutInflater inflater;
	private List<ChatSnapshot> mData;
	private Context mContext;
	
	public MessageListAdapter(Context context, int resource, List<ChatSnapshot> objects) {
		super(context, resource, objects);
		// TODO 自动生成的构造函数存根
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		mData = objects;
	}

	

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public ChatSnapshot getItem(int position) {
		return mData.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public void remove(ChatSnapshot object) {
		super.remove(object);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ChatSnapshot item = mData.get(position);
		View view;
		view = convertView;
		MessageItemViewHolder holder = null;
		if (convertView == null) {
			view = inflater.inflate(R.layout.message_list_item_conversation, parent, false);
			holder = new MessageItemViewHolder();
			holder.avatar = (ImageView) view.findViewById(R.id.
					message_list_iv_recent_avatar_head);
			holder.name = (TextView) view.findViewById(R.id.
					message_list_tv_recent_name);
			
			holder.recent_msg = (TextView) view.findViewById(R.id.
					message_list_tv_recent_msg);
			holder.time = (TextView) view.findViewById(R.id.
					message_list_tv_recent_time);
			holder.status = (ImageView) view.findViewById(R.id.
					message_list_iv_recent_msg_status);
			view.setTag(holder);
		}else{
			holder = (MessageItemViewHolder) view.getTag();
		}
		
		ImageView iv_recent_avatar = holder.avatar;
		TextView tv_recent_name = holder.name;
		TextView tv_recent_msg = holder.recent_msg;
		TextView tv_recent_time = holder.time;
		ImageView iv_recent_msg_status = holder.status;
		//填充数据
		String avatar = item.getHead_url();
		if(avatar!=null&& !avatar.equals("")){
			iv_recent_avatar.setImageBitmap(ImageUtil.decodeBitmapFromNet(avatar,
					iv_recent_avatar.getWidth() ,iv_recent_avatar.getHeight()));
			ImageLoader.getInstance().displayImage(avatar, iv_recent_avatar, 
			ImageLoadOptions.getOptions());
			Uri uri = Uri.parse(avatar);
			iv_recent_avatar.setImageURI(uri);
		}else{
			iv_recent_avatar.setImageResource(R.drawable.user_default_head);
		}
		
		String name = item.getOther_username();
		if(name.equals(UserInfoManager.getInstance().getmCurrent().getUsername()))
			name = item.getUsername();
		Log.i(TAG,"item"+item.getOther_username());
		Log.i(TAG,"item"+item.getUsername());
		tv_recent_name.setText(name);
		if(item.getLast_message_type()==ChatConfiguration.TYPE_MESSAGE_TEXT){
			tv_recent_msg.setText(item.getLast_message());
		}else if(item.getLast_message_type()==ChatConfiguration.TYPE_MESSAGE_PICTURE){
			tv_recent_msg.setText("[图片]");
		}else if(item.getLast_message_type()==ChatConfiguration.TYPE_MESSAGE_LOCATION){
			String all =item.getLast_message();
			if(all!=null &&!all.equals("")){//位置类型的信息组装格式：地理位置&维度&经度
				String address = all.split("&")[0];
				tv_recent_msg.setText("[位置]"+address);
			}
		}else if(item.getLast_message_type()==ChatConfiguration.TYPE_MESSAGE_VOICE){
			tv_recent_msg.setText("[语音]");
		}
		if(item.getUnread_num()>0){
			BadgeView badge = new BadgeView(mContext, iv_recent_avatar);
			badge.setText(""+item.getUnread_num());
			badge.show();
		}
		if(item.getlast_message_status()==MessageConfig.SEND_MESSAGE_ING){
			iv_recent_msg_status.setVisibility(View.VISIBLE);
			iv_recent_msg_status.setImageResource(R.drawable.message_chat_status_sending);
			tv_recent_time.setTextSize(12);
			tv_recent_time.setText("发送中...");
		}else if(item.getlast_message_status()==MessageConfig.SEND_MESSAGE_FAILED){
			iv_recent_msg_status.setVisibility(View.VISIBLE);
			iv_recent_msg_status.setImageResource(R.drawable.message_chat_fail_resend_normal);
			tv_recent_time.setText(TimeUtil.getDescriptionTimeFromTimestamp(item.getLast_time()));
		}else{
			tv_recent_time.setText(TimeUtil.getDescriptionTimeFromTimestamp(item.getLast_time()));
		}
		
		
		return view;
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount();
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty();
	}
}
