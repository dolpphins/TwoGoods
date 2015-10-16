package com.lym.twogoods.ui;

import cn.bmob.v3.Bmob;

import com.lym.twogoods.AccessTokenKeeper;
import com.lym.twogoods.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * <p>App启动Activity</p>
 * 
 * @author 龙宇文
 **/
public class LaunchActivity extends Activity {

	// 定义两个选择button 
	private Button btn_login_choice_direct;
	private Button btn_login_choice_login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		// 初始化Bmob SDK
		Bmob.initialize(this, AccessTokenKeeper.Bmob_ApplicationID);
		setContentView(R.layout.app_login_choice_activity);

		init();
		clickEvent();

	}

	private void init() {
		btn_login_choice_direct = (Button) findViewById(R.id.btn_login_choice_direct);
		btn_login_choice_login = (Button) findViewById(R.id.btn_login_choice_login);
	}

	private void clickEvent() {
		btn_login_choice_direct.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LaunchActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

		btn_login_choice_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LaunchActivity.this,
						LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}
}
