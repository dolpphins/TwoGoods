package com.lym.twogoods.message.ui;

import java.util.ArrayList;

import com.lym.twogoods.R;
import com.lym.twogoods.message.MessageConstant;
import com.lym.twogoods.message.fragment.ChatFragment;
import com.lym.twogoods.message.view.EmoticonsEditText;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
	
	private EmoticonsEditText edit_user_comment;
	
	private int SEND_PICTURE = 0;
	private int SEND_VOICE = 1;
	private int SEND_LOCATION = 2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setCenterTitle("与yao聊天");
		init();
	}

	

	private void init() {
		// TODO 自动生成的方法存根
		ChatFragment chatFragment = new ChatFragment();
		addFragment(chatFragment);
		showFragment(chatFragment);
		moreLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_more);
		emoLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_emo_content);
		voiceLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_voice_content);
		addLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_add_content);
		
		edit_user_comment = (EmoticonsEditText) findViewById(R.id.message_chat_edit_user_comment);
	}

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
			sendVoiceToFriend();
			break;
		default:
			break;
		}
	}
	
	
	private void sendVoiceToFriend() {
		// TODO 自动生成的方法存根
		
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

}