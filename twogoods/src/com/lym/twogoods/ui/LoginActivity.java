package com.lym.twogoods.ui;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.ui.base.BackActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BackActivity {

	// 定义布局控件
	private EditText et_login_erhuo;
	private EditText et_login_password;
	private EditText et_login_code;
	private ImageButton btn_login_code_get;
	private Button btn_login_land;
	private Button btn_login_forget;

	// 定义actionbar控件
	private TextView actionbarRightTextView;

	// 定义存储数据
	private SharedPreferences preferences;
	private String user_name_valus = "user_name";
	private String GUID_valus = "GUID";
	private BmobQuery<User> userbean;
	// 定义变量来看是否查找成功。
	private BmobQuery<User> bmobQuery;
	private boolean find_succeed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_login_activity);
		initActionbar();
		init();
		clickEvent();
	}

	private void init() {
		et_login_erhuo = (EditText) findViewById(R.id.et_login_erhuo);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		et_login_code = (EditText) findViewById(R.id.et_login_code);
		btn_login_code_get = (ImageButton) findViewById(R.id.btn_login_code_get);
		btn_login_land = (Button) findViewById(R.id.btn_login_land);
		btn_login_forget = (Button) findViewById(R.id.btn_login_forget);

		// 存储数据相关
		preferences = getSharedPreferences("data", MODE_PRIVATE);
	}

	// 控件点击事件
	private void clickEvent() {
		// 获取验证码按钮点击事件
		btn_login_code_get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 暂时为空
			}
		});

		// 登录按钮点击事件
		btn_login_land.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				judgePassword();
				if (find_succeed && codeMatch()) {
					Intent intent = new Intent(LoginActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}
			}
		});

		// 忘记密码按钮点击事件
		btn_login_forget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						ResetPasswordActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initActionbar() {
		// 设置actionbar样式
		setCenterTitle("登陆");

		actionbarRightTextView = setRightTitle("注册");
		actionbarRightTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});
	}

	// 校验账号与密码是否匹配
	private void judgePassword() {
		/*
		 * if
		 * ((et_login_erhuo.getText().toString().equals(preferences.getString(
		 * user_name_valus, null))) &&
		 * (et_login_password.getText().toString().equals(preferences
		 * .getString(GUID_valus, null)))) return true; else {
		 * Toast.makeText(LoginActivity.this, "密码不正确", Toast.LENGTH_SHORT)
		 * .show(); return false; }
		 */
		bmobQuery = new BmobQuery<User>();
		bmobQuery.addWhereEqualTo("username", et_login_erhuo.getText()
				.toString());
		bmobQuery.findObjects(this, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(getApplicationContext(), "用户名不存在",
						Toast.LENGTH_SHORT).show();
				find_succeed = false;
			}

			@Override
			public void onSuccess(List<User> list) {
				if (et_login_password.getText().toString()
						.equals(list.get(0).getGUID())) {
					find_succeed = true;
				}
				else{
					Toast.makeText(getApplicationContext(), "密码错误",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	// 判断验证码是否匹配
	private boolean codeMatch() {
		return true;
	}
}
