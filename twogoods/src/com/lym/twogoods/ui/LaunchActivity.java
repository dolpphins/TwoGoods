package com.lym.twogoods.ui;

import com.lym.twogoods.AppManager;
import com.lym.twogoods.R;
import com.lym.twogoods.bean.Login;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.SharePreferencesConfiguration;
import com.lym.twogoods.screen.DisplayUtils;
import com.lym.twogoods.user.Loginer;
import com.lym.twogoods.user.listener.DefaultLoginListener;
import com.lym.twogoods.utils.SharePreferencesManager;
import com.lym.twogoods.utils.StringUtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <p>App启动Activity</p>
 * 
 * @author 龙宇文
 **/
public class LaunchActivity extends Activity {

	// 定义两个选择button 
	private Button btn_login_choice_direct;
	private Button btn_login_choice_login;
	private LinearLayout ll_app_login_choice_main;
	private TextView tv_login_choice_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.app_login_choice_activity);

		init();
		clickEvent();
		
		//自动登录
		autoLogin();
	}
	
	private void autoLogin() {
		SharePreferencesManager spm = SharePreferencesManager.getInstance();
		int versionCode = spm.getInt(getApplicationContext(), SharePreferencesConfiguration.APP_VERSION_CODE_KEY, -1);
		if(AppManager.isUpdate(getApplicationContext(), versionCode)) {
			spm.putInt(getApplicationContext(), SharePreferencesConfiguration.APP_VERSION_CODE_KEY, AppManager.getAppVersion(getApplicationContext()));
		} else {
			btn_login_choice_direct.setVisibility(View.INVISIBLE);
			btn_login_choice_login.setVisibility(View.INVISIBLE);
			
			String username = spm.getLoginMessageString(getApplicationContext(), SharePreferencesConfiguration.LOGIN_USERNAME_KEY, null);
			String password = spm.getLoginMessageString(getApplicationContext(), SharePreferencesConfiguration.LOGIN_PASSWORD_KEY, null);
			//自动登录
			Login item = new Login();
			item.setUsername(username);
			item.setPassword(password);
			//显示进度框
//			if(pd == null) {
//				pd = new ProgressDialog(this);
//				pd.setCancelable(false);
//			}
//			pd.show();
			Loginer.getInstance().login(getApplicationContext(), item, new DefaultLoginListener(getApplicationContext()){
				
				@Override
				public void onError(int errorCode) {
					super.onError(errorCode);
					handleLoginFinish();
				}
				
				@Override
				public void onSuccess(User user) {
					super.onSuccess(user);
					handleLoginFinish();
				}
			});
		}
	}
	
	private void handleLoginFinish() {
		//pd.dismiss();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void init() {
		btn_login_choice_direct = (Button) findViewById(R.id.btn_login_choice_direct);
		btn_login_choice_login = (Button) findViewById(R.id.btn_login_choice_login);
		ll_app_login_choice_main=(LinearLayout) findViewById(R.id.ll_app_login_choice_main);

		//tv_login_choice_title=(TextView) findViewById(R.id.tv_login_choice_title);
		//StringUtil.setTextFont(this, tv_login_choice_title, "fonts/huawenxingkai.ttf");
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
			}
		});
	}
}
