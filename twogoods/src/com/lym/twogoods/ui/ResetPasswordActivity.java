package com.lym.twogoods.ui;

import java.util.List;

import android.R.string;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.a.a.there;
import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.ui.base.BackActivity;

public class ResetPasswordActivity extends BackActivity {

	// 定义布局控件
	private EditText et_reset_password__phone;
	private EditText et_reset_password_code;
	private ImageButton btn_reset_password_code_get;
	private Button btn_reset_password_verification;

	// 定义Bmob查询变量
	private BmobQuery<User> query;
	private boolean find_succeed = false;
	private String objectId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_reset_password_activity);

		// 初始化
		initActionBar();
		init();
		clickEvent();
	}

	private void init() {
		et_reset_password__phone = (EditText) findViewById(R.id.et_reset_password__phone);
		et_reset_password_code = (EditText) findViewById(R.id.et_reset_password_code);
		btn_reset_password_code_get = (ImageButton) findViewById(R.id.btn_reset_password_code_get);
		btn_reset_password_verification = (Button) findViewById(R.id.btn_reset_password_verification);
	}

	private void clickEvent() {

		// 获取验证码按钮点击事件
		btn_reset_password_code_get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 暂时为空
			}
		});

		// 验证点击事件
		btn_reset_password_verification
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						usernameMatch();
						// 判断验证码是否正确

						if (find_succeed) {
							// 传输数据到下一个activity
							Bundle data = new Bundle();
							data.putString("id", objectId);
							Intent intent = new Intent(
									ResetPasswordActivity.this,
									NewPasswordActivity.class);
							intent.putExtras(data);
							startActivity(intent);
						}
					}
				});
	}

	private void initActionBar() {
		setCenterTitle("重置密码");
	}

	// 判断验证码是否匹配
	private boolean codeMatch() {
		return true;
	}

	private void usernameMatch() {
		query = new BmobQuery<User>();
		query.addWhereEqualTo("phone", et_reset_password__phone.getText()
				.toString());
		query.findObjects(this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> list) {
				if (codeMatch()) {
					objectId = list.get(0).getObjectId();
					find_succeed = true;
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(getApplicationContext(), "用户不存在",
						Toast.LENGTH_SHORT).show();
				find_succeed = false;
			}
		});
	}
}
