package com.lym.twogoods.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.bmob.v3.listener.SaveListener;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.ui.base.BackActivity;

public class RegisterActivity extends BackActivity {

	// 定义布局控件
	private EditText et_register_erhuo;
	private EditText et_register_password;
	private EditText et_register_password_again;
	private EditText et_register_phone;
	private EditText et_register_code;
	private ImageButton btn_register_code_get;
	private Button btn_register_register;

	// 用户注册信息表数据
	private User user_data;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private String user_name_valus = "user_name";
	private String GUID_valus = "GUID";
	private String phone_valuse = "phone";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_register_activity);

		// 初始化
		initActionBar();
		init();
		clickEvent();

	}

	private void init() {
		et_register_erhuo = (EditText) findViewById(R.id.et_register_erhuo);
		et_register_password = (EditText) findViewById(R.id.et_register_password);
		et_register_password_again = (EditText) findViewById(R.id.et_register_password_again);
		et_register_phone = (EditText) findViewById(R.id.et_register_phone);
		et_register_code = (EditText) findViewById(R.id.et_register_code);
		btn_register_code_get = (ImageButton) findViewById(R.id.btn_register_code_get);
		btn_register_register = (Button) findViewById(R.id.btn_register_register);

		user_data = new User();

		// SharedPreferences的初始化
		preferences = getSharedPreferences("data", MODE_PRIVATE);
		editor = preferences.edit();
	}

	// 控件点击事件
	private void clickEvent() {
		// 获取验证码按钮点击事件
		btn_register_code_get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 暂时为空
			}
		});

		// 注册按钮点击事件
		btn_register_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 只有在两次输入密码都一样的时候才进行注册操作(还需要判断验证码)
				if (et_register_password
						.getText()
						.toString()
						.equals(et_register_password_again.getText().toString())
						&& codeMatch()) {
					// 将用户注册的数据取到放在UserBean中
					getRegisterData();
					// 上传数据到Bmob中
					sendData();
					// 存储数据
					/*writeRegisterData();
					Toast.makeText(RegisterActivity.this, "注册成功",
							Toast.LENGTH_SHORT).show();*/
					Intent intent = new Intent(RegisterActivity.this,
							LoginActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(RegisterActivity.this, "密码不一致",
							Toast.LENGTH_SHORT).show();

				}
			}
		});
	}

	private void initActionBar() {
		setCenterTitle("注册");
	}

	// 取得注册信息，并把它放到userBean中。
	private void getRegisterData() {
		user_data.setUsername(et_register_erhuo.getText().toString());
		// 要改，把取得的密码转为GUID，然后再存。
		user_data.setGUID(et_register_password.getText().toString());
		user_data.setPhone(et_register_phone.getText().toString());
	}

	// 本地存储
	private void writeRegisterData() {
		editor.putString(user_name_valus, user_data.getUsername());
		editor.putString(GUID_valus, user_data.getGUID());
		editor.putString(phone_valuse, user_data.getPhone());
		editor.commit();
	}

	// 判断验证码是否匹配
	private boolean codeMatch() {
		return true;
	}

	// 上传数据到Bmob中
	private void sendData() {
		user_data.save(getApplicationContext(), new SaveListener() {
			
			@Override
			public void onSuccess() {
				Toast.makeText(getApplicationContext(), "添加数据成功", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
