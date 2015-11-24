package com.lym.twogoods.message.adapter;


import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.config.ChatConfiguration;
import com.lym.twogoods.message.ImageLoadOptions;
import com.lym.twogoods.message.viewHolder.MessageItemViewHolder;
import com.lym.twogoods.utils.ImageUtil;
import com.lym.twogoods.utils.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;


import android.content.Context;
import android.net.Uri;
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
		// TODO 自动生成的方法存根
		System.out.println("position="+position);
		return mData.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		// TODO 自动生成的方法存根
		return position;
	}

	@Override
	public boolean hasStableIds() {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public void remove(ChatSnapshot object) {
		// TODO 自动生成的方法存根
		super.remove(object);
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO 自动生成的方法存根
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
			holder.newMessageTip = (TextView) view.findViewById(R.id.
					message_list_tv_recent_unread);
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
		TextView tv_recent_unread = holder.newMessageTip;
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
		if(name==UserInfoManager.getInstance().getmCurrent().getUsername())
			name = item.getUsername();
		tv_recent_name.setText(item.getOther_username());
		tv_recent_time.setText(TimeUtil.getDescriptionTimeFromTimestamp(item.getLast_time()));
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
			tv_recent_unread.setVisibility(View.VISIBLE);
			tv_recent_unread.setText(item.getUnread_num());
		}else
			tv_recent_unread.setVisibility(View.GONE);
		
		return view;
	}

	@Override
	public int getItemViewType(int position) {
		// TODO 自动生成的方法存根
		return super.getItemViewType(position);
	}

	@Override
	public int getViewTypeCount() {
		// TODO 自动生成的方法存根
		return super.getViewTypeCount();
	}

	@Override
	public boolean isEmpty() {
		// TODO 自动生成的方法存根
		return super.isEmpty();
	}
}
