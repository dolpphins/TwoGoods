package com.lym.twogoods.message.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.message.chatfragment.ChatFragment;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
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
	
	private View bottomView;
	
	private LinearLayout moreLinearLayout,emoLinearLayout,voiceLinearLayout,addLinearLayout;
	
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
	}



	@Override
	public View onCreateBottomView() {
		bottomView = getLayoutInflater().inflate(R.layout.
				message_chat_bottom_bar, null);
		return bottomView;
	}
	
	
	public void toAction(View view)
	{
		Toast.makeText(this, "哈哈哈哈哈", Toast.LENGTH_LONG).show();
		moreLinearLayout.setVisibility(View.VISIBLE);
		switch(view.getId()){
		case R.id.message_chat_btn_add:
			if(emoLinearLayout.getVisibility()==View.VISIBLE)
				emoLinearLayout.setVisibility(View.GONE);
			addLinearLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.message_chat_btn_emo:
			if(addLinearLayout.getVisibility()==View.VISIBLE)
				addLinearLayout.setVisibility(View.GONE);
			emoLinearLayout.setVisibility(View.VISIBLE);
			break;
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

}