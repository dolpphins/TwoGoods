package com.lym.twogoods.message.adapter;


import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.dialog.FastLoginDialog;
import com.lym.twogoods.dialog.FastLoginDialog.OnFastLoginListener;
import com.lym.twogoods.message.config.MessageConfig;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 如果用户没有登录，用来提示用户登录
 * @author 尧俊锋
 *
 */
public class TipAdapter extends BaseAdapter {
	
	public Context mContext;
	public LayoutInflater mInflater;
	public int photos[];
	private Handler mHandler;
	private int tip;
	
	public TipAdapter(Context context,int Images[],Handler handler,int tip){
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		photos = Images;
		mHandler = handler;
		this.tip = tip;
	}

	@Override
	public int getCount() {
		return photos.length;
	}

	@Override
	public Object getItem(int position) {
		return photos[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		view = convertView;
		view = mInflater.inflate(R.layout.message_list_no_login_tip, parent, false);
		if(tip==MessageConfig.IS_LOGIN_AND_NO_MSG){
			TextView tv_tip = (TextView)view.findViewById(R.id.message_list_tv_tip);
			tv_tip.setText("暂时没有消息,赶紧联系卖家吧");
		}else{
			if(tip==MessageConfig.NOT_LOGIN){
				view.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showFastLoginDialog();
					}
				});
			}
		}
		
		return view;
	}
	
	private void showFastLoginDialog() {
		FastLoginDialog dialog = new FastLoginDialog(mContext);
		dialog.setOnFastLoginListener(new MessageOnFastLoginListener());
		dialog.show();
	}
	
	class MessageOnFastLoginListener implements OnFastLoginListener{

		@Override
		public void onError(int errorCode) {
		}
		@Override
		public void onSuccess(User user) {
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
		}
	}

}
