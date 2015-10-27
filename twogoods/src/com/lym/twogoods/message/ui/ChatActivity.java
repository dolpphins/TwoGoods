package com.lym.twogoods.message.ui;

import java.util.ArrayList;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobRecordManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.db.BmobDB;
import cn.bmob.im.inteface.OnRecordChangeListener;
import cn.bmob.im.util.BmobLog;

import com.lym.twogoods.R;
import com.lym.twogoods.message.MessageConstant;
import com.lym.twogoods.message.fragment.ChatFragment;
import com.lym.twogoods.message.view.EmoticonsEditText;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * <p>
 * 	聊天Activity
 * </p>
 * 
 * 
 * */
public class ChatActivity extends BottomDockBackFragmentActivity{
	
	/*
	 * 底部布局
	 */
	private View bottomView;
	
	private LinearLayout moreLinearLayout,emoLinearLayout,voiceLinearLayout,addLinearLayout;
	//聊天编辑框
	private EmoticonsEditText edit_user_comment;
	//按住录音
	ImageView iv_recondVoice;
	//正在录音的动画
	ImageView iv_showIsReconding;
	//按下开始录音提示和正在录音的提示
	TextView tv_recondPressTip,tv_finish_recond_tip;
	// 话筒动画
	Drawable[] drawable_Anims;
	
	private int SEND_PICTURE = 0;
	//聊天对象的id
	String targetId = "";
	//聊天对象
	BmobChatUser targetUser;
	//Bmob的用户管理器
	BmobUserManager mUserManager;
	//Bmob的聊天manager
	BmobChatManager mManager;
	//Bmob录音管理器
	BmobRecordManager mRecordManager;
	//消息的总页数
	int MsgPagerNum;	
	// 收到消息
	public static final int NEW_MESSAGE = 0x001;
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		initBmob();
		init();
	}
	//初始化与Bmob相关的成员
	private void initBmob()
	{
		mManager = BmobChatManager.getInstance(this);
		mUserManager = BmobUserManager.getInstance(this);
		mRecordManager = BmobRecordManager.getInstance(this);
		MsgPagerNum = 0;
		// 组装聊天对象
		targetUser = (BmobChatUser) getIntent().getSerializableExtra("user");
		//targetId = targetUser.getObjectId();
	} 
	
	//进行初始化
	private void init() {
		// TODO 自动生成的方法存根
		//setCenterTitle("与"+targetUser.getUsername()+"聊天");
		setCenterTitle("与yao聊天");
		initFragment();
		initBottom();
		initVoice();
		
	}
	
	//初始化聊天的fragment
	private void initFragment() {
		// TODO 自动生成的方法存根
		ChatFragment chatFragment = new ChatFragment();
		addFragment(chatFragment);
		showFragment(chatFragment);
	}
	
	//初始化底部布局
	private void initBottom() {
		// TODO 自动生成的方法存根
		moreLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_more);
		emoLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_emo_content);
		voiceLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_voice_content);
		addLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_add_content);
		
		edit_user_comment = (EmoticonsEditText) findViewById(R.id.message_chat_edit_user_comment);
	}
	
	//初始化与录音相关的东西
	private void initVoice() {
		
		//初始化跟录音相关的控件
		iv_recondVoice = (ImageView) bottomView.findViewById(R.id.message_chat_record);
		iv_showIsReconding = (ImageView) bottomView.findViewById(R.id.message_chat_show_is_recording_iv);
		tv_recondPressTip = (TextView) bottomView.findViewById(R.id.message_chat_recond_press_tip);
		tv_finish_recond_tip = (TextView) bottomView.findViewById(R.id.message_chat_finish_recond_tip_tv);
		//给录音控件设置触摸事件
		iv_recondVoice.setOnTouchListener(new RecondTouchListener());
		
		//初始化在录音时音量改变的动画
		drawable_Anims = new Drawable[] {
				getResources().getDrawable(R.drawable.message_chat_reconding_audio1),
				getResources().getDrawable(R.drawable.message_chat_reconding_audio2),
				getResources().getDrawable(R.drawable.message_chat_reconding_audio3) };
		
		// 设置音量大小监听--在这里开发者可以自己实现：当剩余10秒情况下的给用户的提示，类似微信的语音那样
		mRecordManager.setOnRecordChangeListener(new OnRecordChangeListener() {
	
			@Override
			public void onVolumnChanged(int value) {
				// TODO Auto-generated method stub
				iv_showIsReconding.setImageDrawable(drawable_Anims[value/2]);
			}
	
			@Override
			public void onTimeChanged(int recordTime, String localPath) {
				// TODO Auto-generated method stub
				BmobLog.i("voice", "已录音长度:" + recordTime);
				if (recordTime >= BmobRecordManager.MAX_RECORD_TIME) {// 1分钟结束，发送消息
					// 需要重置按钮
					iv_recondVoice.setPressed(false);
					iv_recondVoice.setClickable(false);
					// 发送语音消息
					sendVoiceToFriend(localPath, recordTime);
					//是为了防止过了录音时间后，会多发一条语音出去的情况。
					handler.postDelayed(new Runnable() {
	
						@Override
						public void run() {
							// TODO Auto-generated method stub
							iv_recondVoice.setClickable(true);
						}
					}, 1000);
				}else{
					
				}
			}
		});
		
		
	}
	
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == NEW_MESSAGE) {
				BmobMsg message = (BmobMsg) msg.obj;
				String uid = message.getBelongId();
				BmobMsg m = BmobChatManager.getInstance(ChatActivity.this).getMessage(message.getConversationId(), message.getMsgTime());
				if (!uid.equals(targetId))// 如果不是当前正在聊天对象的消息，不处理
					return;
				//mAdapter.add(m);
				// 定位
				//mListView.setSelection(mAdapter.getCount() - 1);
				//取消当前聊天对象的未读标示
				BmobDB.create(ChatActivity.this).resetUnread(targetId);
			}
		}
	};

	@Override
	public View onCreateBottomView() {
		bottomView = getLayoutInflater().inflate(R.layout.
				message_chat_bottom_bar, null);
		return bottomView;
	}
	//实现控件点击事件
	public void toAction(View view)
	{
		
		switch(view.getId()){
		case R.id.message_chat_edit_user_comment://点击编辑框
			if(moreLinearLayout.getVisibility()==View.VISIBLE){
				moreLinearLayout.setVisibility(View.GONE);
				emoLinearLayout.setVisibility(View.GONE);
				addLinearLayout.setVisibility(View.GONE);
			}
			break;
		case R.id.message_chat_btn_add://点击添加按钮
			if (moreLinearLayout.getVisibility() == View.GONE) {
				moreLinearLayout.setVisibility(View.VISIBLE);
				addLinearLayout.setVisibility(View.VISIBLE);
				voiceLinearLayout.setVisibility(View.GONE);
				emoLinearLayout.setVisibility(View.GONE);
				hideSoftInputView();
			} else {
				if (emoLinearLayout.getVisibility() == View.VISIBLE) {
					emoLinearLayout.setVisibility(View.GONE);
					addLinearLayout.setVisibility(View.VISIBLE);
				} else {
					if(voiceLinearLayout.getVisibility()==View.VISIBLE){
						voiceLinearLayout.setVisibility(View.GONE);
						addLinearLayout.setVisibility(View.VISIBLE);
					}else{
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.showSoftInput(edit_user_comment, 0);
					moreLinearLayout.setVisibility(View.GONE);
					}
				}
			}
			break;
		case R.id.message_chat_btn_emo://点击表情按钮
			if (moreLinearLayout.getVisibility() == View.GONE) {
				showEditEmoState(true);
			} else {
				if (addLinearLayout.getVisibility() == View.VISIBLE) {
					addLinearLayout.setVisibility(View.GONE);
					emoLinearLayout.setVisibility(View.VISIBLE);
				} else {
					if(voiceLinearLayout.getVisibility()==View.VISIBLE){
						voiceLinearLayout.setVisibility(View.GONE);
						emoLinearLayout.setVisibility(View.VISIBLE);
					}else{
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.showSoftInput(edit_user_comment, 0);
					moreLinearLayout.setVisibility(View.GONE);
					}
				}
			}
			break;
		case R.id.message_chat_tv_location://点击发送位置
			Toast.makeText(this, "点击了发送位置", Toast.LENGTH_LONG).show();
			break;
		case R.id.message_chat_tv_picture://点击发送图片
			Intent intent = new Intent(this,SendPictureActivity.class);
			//设置一次可以发30张相片
			intent.putExtra("picCount", 30);
			startActivityForResult(intent, SEND_PICTURE);
			break;
		case R.id.message_chat_tv_voice://点击发送声音
			Toast.makeText(this, "点击了发送声音", Toast.LENGTH_LONG).show();
			addLinearLayout.setVisibility(View.GONE);
			voiceLinearLayout.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	
	private void sendVoiceToFriend(String localPath, int recordTime) {
		// TODO 自动生成的方法存根
		Toast.makeText(this, "发送语音给好友", Toast.LENGTH_LONG).show();
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case MessageConstant.SEND_CAMERA_PIC:
			String picPath = data.getExtras().getString("picture");
			Toast.makeText(this, picPath, Toast.LENGTH_LONG).show();
			break;
		case MessageConstant.SEND_LOCAL_PIC:
			ArrayList<String> pics = data.getExtras().getStringArrayList("pictures");
			Toast.makeText(this, "共发送本地图片"+pics.size()+"张", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
		
	} 
	
	private void showEditEmoState(boolean isEmo) {
		// TODO 自动生成的方法存根
		edit_user_comment.requestFocus();
		if (isEmo) {
			moreLinearLayout.setVisibility(View.VISIBLE);
			//layout_more.setVisibility(View.VISIBLE);
			emoLinearLayout.setVisibility(View.VISIBLE);
			addLinearLayout.setVisibility(View.GONE);
			hideSoftInputView();
		} else {
			moreLinearLayout.setVisibility(View.GONE);
			showSoftInputView();
		}
	}



	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
				&&moreLinearLayout.getVisibility()==View.VISIBLE) { 
			//按下的如果是返回键，同时没有重复，而且底部布局看得见的话则是先隐藏底部布局。
		    moreLinearLayout.setVisibility(View.GONE);
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	/*
	 * 隐藏软键盘
	 * 
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.
				LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	// 显示软键盘
	public void showSoftInputView() {
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.showSoftInput(edit_user_comment, 0);
		}
	}
	
	class RecondTouchListener implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN://按下手指时
				try {
					v.setPressed(true);
					tv_finish_recond_tip.setVisibility(View.VISIBLE);
					iv_showIsReconding.setVisibility(View.VISIBLE);
					tv_recondPressTip.setVisibility(View.INVISIBLE);
					// 开始录音
					mRecordManager.startRecording(targetId);
				} catch (Exception e) {
				}
				return true;
			case MotionEvent.ACTION_MOVE: //滑动手指时
					if (event.getY() < 0) {
						
					} else {
						//tv_voice_tips.setText(getString(R.string.voice_up_tips));
					//	tv_voice_tips.setTextColor(Color.WHITE);
					}
					return true;
			case MotionEvent.ACTION_UP://放开手指时
				v.setPressed(false);
				tv_finish_recond_tip.setVisibility(View.INVISIBLE);
				iv_showIsReconding.setVisibility(View.INVISIBLE);
				tv_recondPressTip.setVisibility(View.VISIBLE);
				try {
					if (event.getY() < 0) {// 放弃录音
						mRecordManager.cancelRecording();
						BmobLog.i("voice", "放弃发送语音");
					} else {
						int recordTime = mRecordManager.stopRecording();
						if (recordTime > 1) {
							// 发送语音文件
							BmobLog.i("voice", "发送语音");
							sendVoiceToFriend(
									mRecordManager.getRecordFilePath(targetId),
									recordTime);
						} else {// 录音时间过短，则提示录音过短的提示
							Toast.makeText(ChatActivity.this, "录音时间过短", Toast.LENGTH_LONG).show();
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				return true;
			default:
				return false;
			}
		}
	}

}