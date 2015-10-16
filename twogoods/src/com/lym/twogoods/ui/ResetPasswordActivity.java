package com.lym.twogoods.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.ui.base.BackActivity;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.StringUtil;

/**
 * <p>
 * App重置密码验证Activity
 * </p>
 * 
 * @author 龙宇文
 **/
public class ResetPasswordActivity extends BackActivity {

	// 定义布局控件
	private EditText et_reset_password__phone;
	private EditText et_reset_password_code;
	private Button btn_reset_password_code_get;
	private Button btn_reset_password_verification;

	// 定义Bmob查询变量
	private BmobQuery<User> query;
	private boolean find_succeed = false;
	private String objectId;
	// 验证码是否验证成功
	private boolean codeVerify = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		btn_reset_password_code_get = (Button) findViewById(R.id.btn_reset_password_code_get);
		btn_reset_password_verification = (Button) findViewById(R.id.btn_reset_password_verification);
	}

	/**
	 * <p>
	 * 点击事件函数
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void clickEvent() {

		// 获取验证码按钮点击事件
		btn_reset_password_code_get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetworkHelper.isMobileDataEnable(getApplicationContext())
						|| NetworkHelper
						.isWifiDataEnable(getApplicationContext())) {
					if (StringUtil.isPhoneNumber(et_reset_password__phone
							.getText().toString())) {
						BmobSMS.requestSMSCode(getApplicationContext(),
								et_reset_password__phone.getText().toString(),
								"重置密码验证", new RequestSMSCodeListener() {

									@Override
									public void done(Integer smsId,
											BmobException ex) {
										if (ex == null) {
											Toast.makeText(
													getApplicationContext(),
													"短信已发送，请注意查看",
													Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(
													getApplicationContext(),
													"系统出错", Toast.LENGTH_SHORT)
													.show();
										}
									}
								});
					} else {
						Toast.makeText(getApplicationContext(), "手机格式不对",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "当前网络不佳",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 验证点击事件
		btn_reset_password_verification
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 判断网络是否可用
						if (NetworkHelper
								.isMobileDataEnable(getApplicationContext())
								|| NetworkHelper
										.isWifiDataEnable(getApplicationContext())) {
							// 对手机号和验证码的合法性进行检测
							if (StringUtil
									.isPhoneNumber(et_reset_password__phone
											.getText().toString())) {
								if (StringUtil
										.isSecurityCode(et_reset_password_code
												.getText().toString())) {
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
								} else {
									Toast.makeText(getApplicationContext(),
											"验证码格式不对", Toast.LENGTH_SHORT)
											.show();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"手机号码格式不对", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), "当前网络不佳",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void initActionBar() {
		setCenterTitle("重置密码");
	}

	/**
	 * <p>
	 * 判断验证码是否匹配
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void codeMatch() {
		BmobSMS.verifySmsCode(getApplicationContext(), et_reset_password__phone
				.getText().toString(), et_reset_password_code.getText()
				.toString(), new VerifySMSCodeListener() {

			@Override
			public void done(BmobException ex) {
				if (ex == null) {
					codeVerify = true;
				}
			}
		});
	}

	/**
	 * <p>
	 * 判断手机号存在
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void usernameMatch() {
		query = new BmobQuery<User>();
		query.addWhereEqualTo("phone", et_reset_password__phone.getText()
				.toString());
		query.findObjects(this, new FindListener<User>() {

			@Override
			public void onSuccess(List<User> list) {
				codeMatch();
				if (codeVerify) {
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
