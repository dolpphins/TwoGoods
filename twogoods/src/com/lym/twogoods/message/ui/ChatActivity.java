package com.lym.twogoods.message.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


/**
 * <p>
 * 	聊天Activity
 * </p>
 * 
 * 
 * */
public class ChatActivity extends BottomDockBackFragmentActivity{
	
	private View bottomView;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setCenterTitle("与yao聊天");
		init();
	}

	

	private void init() {
		// TODO 自动生成的方法存根
		
	}



	@Override
	public View onCreateBottomView() {
		bottomView = getLayoutInflater().inflate(R.layout.
				message_chat_bottom_bar, null);
		return bottomView;
	}
	
	

}
