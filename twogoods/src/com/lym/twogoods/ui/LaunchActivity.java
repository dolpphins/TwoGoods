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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
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

	//private ProgressDialog pd;
	
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
			btn_login_choice_direct.setVisibility(View.VISIBLE);
			btn_login_choice_login.setVisibility(View.VISIBLE);
		} else {
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
		setButtonParams();
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
	private void setButtonParams() {
		btn_login_choice_direct.setY(DisplayUtils.getScreenHeightPixels(this)/3*2);
		btn_login_choice_login.setY(DisplayUtils.getScreenHeightPixels(this)/3*2);
	}
}
