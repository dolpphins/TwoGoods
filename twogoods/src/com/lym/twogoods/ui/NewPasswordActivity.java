package com.lym.twogoods.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.listener.UpdateListener;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.ui.base.BackActivity;
import com.lym.twogoods.utils.EncryptHelper;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.StringUtil;

/**
 * <p>
 * App重置密码Activity
 * </p>
 * 
 * @author 龙宇文
 **/
public class NewPasswordActivity extends BackActivity {

	// 定义布局控件
	private EditText et_new_password_password;
	private EditText et_new_password_again;
	private Button btn_new_password_confirm;

	//
	private Intent intent;

	// Bmob查询
	private boolean update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_new_password_activity);

		// 初始化
		initActionBar();
		init();
		clickEvent();
	}

	private void init() {
		et_new_password_again = (EditText) findViewById(R.id.et_new_password_again);
		et_new_password_password = (EditText) findViewById(R.id.et_new_password_password);
		btn_new_password_confirm = (Button) findViewById(R.id.btn_new_password_confirm);

	}

	private void initActionBar() {
		setCenterTitle("重置密码");
	}

	/**
	 * <p>
	 * 修改Bmob数据
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void resetPassword() {
		if (et_new_password_password.getText().toString()
				.equals(et_new_password_again.getText().toString())) {
			User user = new User();
			user.setPassword(EncryptHelper.getMD5(et_new_password_password
					.getText().toString()));
			user.update(this, getIntent().getStringExtra("id"),
					new UpdateListener() {

						@Override
						public void onSuccess() {
							Toast.makeText(getApplicationContext(), "修改成功",
									Toast.LENGTH_SHORT).show();
							update = true;
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							Toast.makeText(getApplicationContext(), "修改失败",
									Toast.LENGTH_SHORT).show();
							update = false;
						}
					});
		}
	}

	/**
	 * <p>
	 * 点击事件
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void clickEvent() {
		btn_new_password_confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断网络是否可用
				if (NetworkHelper.isMobileDataEnable(getApplicationContext())
						|| NetworkHelper
								.isWifiDataEnable(getApplicationContext())) {
					//检测密码的合法性
					if (StringUtil.isPassword(et_new_password_password.getText().toString())) {
						if (StringUtil.isPassword(et_new_password_again.getText().toString())) {
							resetPassword();
							if (update) {
								intent = new Intent(NewPasswordActivity.this,
										LoginActivity.class);
								startActivity(intent);
								finish();
							}
						}else {
							Toast.makeText(getApplicationContext(), "密码格式不对", Toast.LENGTH_SHORT).show();
						}
					}else {
						Toast.makeText(getApplicationContext(), "密码格式不对", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
