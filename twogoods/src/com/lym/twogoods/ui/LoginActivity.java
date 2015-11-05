package com.lym.twogoods.ui;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.ui.base.BackActivity;
import com.lym.twogoods.utils.EncryptHelper;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.SharePreferencesManager;
import com.lym.twogoods.utils.StringUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * App登陆Activity
 * </p>
 * 
 * @author 龙宇文
 **/
public class LoginActivity extends BackActivity {

	// 定义布局控件
	private EditText et_login_erhuo;
	private EditText et_login_password;
	private EditText et_login_code;
	private Button btn_login_code_get;
	private Button btn_login_land;
	private Button btn_login_forget;

	// 定义actionbar控件
	private TextView actionbarRightTextView;

	// 定义存储数据
	private SharePreferencesManager sSpManager;
	// 定义变量来看是否查找成功。
	private BmobQuery<User> bmobQuery;
	private boolean find_succeed = false;
	private boolean codeVerify = false;
	// 通过用户名来取得手机号码
	private String phone = "";

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
		btn_login_code_get = (Button) findViewById(R.id.btn_login_code_get);
		btn_login_land = (Button) findViewById(R.id.btn_login_land);
		btn_login_forget = (Button) findViewById(R.id.btn_login_forget);

		// 存储数据相关
		sSpManager = SharePreferencesManager.getInstance();
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
		btn_login_code_get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetworkHelper.isMobileDataEnable(getApplicationContext())
						|| NetworkHelper
								.isWifiDataEnable(getApplicationContext())) {
					if (StringUtil.isPhoneNumber(et_login_erhuo.getText()
							.toString())) {
						phone = et_login_erhuo.getText().toString();
						BmobSMS.requestSMSCode(getApplicationContext(),
								et_login_erhuo.getText().toString(), "登录验证",
								new RequestSMSCodeListener() {

									@Override
									public void done(Integer smsId,
											BmobException ex) {
										if (ex == null) {
											Toast.makeText(
													getApplicationContext(),
													"短信已发送，请注意查看",
													Toast.LENGTH_SHORT).show();
										}
									}

								});
					} else if (StringUtil.isErHuoNumber(et_login_erhuo
							.getText().toString())) {
						phone = usernameToPhone();
						BmobSMS.requestSMSCode(getApplicationContext(), phone,
								"登录验证", new RequestSMSCodeListener() {

									@Override
									public void done(Integer smsId,
											BmobException ex) {
										if (ex == null) {
											Toast.makeText(
													getApplicationContext(),
													"短信已发送，请注意查看",
													Toast.LENGTH_SHORT).show();
										}
									}

								});
					} else {
						Toast.makeText(getApplicationContext(), "用户名不存在",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "当前网络不佳",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 登录按钮点击事件
		btn_login_land.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断网络是否可用
				if (NetworkHelper.isMobileDataEnable(getApplicationContext())
						|| NetworkHelper
								.isWifiDataEnable(getApplicationContext())) {
					//
					// 对贰货号，密码和验证码的合理性进行检测。
					if (StringUtil.isErHuoNumber(et_login_erhuo.getText()
							.toString())
							|| StringUtil.isPhoneNumber(et_login_erhuo
									.getText().toString())) {
						if (StringUtil.isPassword(et_login_password.getText()
								.toString())) {
							if (StringUtil.isSecurityCode(et_login_code
									.getText().toString())) {
								judgePassword();
								codeMatch();
								if (find_succeed && codeVerify) {
									Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									finish();
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"验证码格式有误", Toast.LENGTH_SHORT).show();
							}

						} else {

							Toast.makeText(getApplicationContext(), "密码格式有误",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "贰货号格式有误",
								Toast.LENGTH_SHORT).show();
					}

				}

				//
				else {
					Toast.makeText(getApplicationContext(), "当前网络不佳",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 忘记密码按钮点击事件
		btn_login_forget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetworkHelper.isMobileDataEnable(getApplicationContext())
						|| NetworkHelper
								.isWifiDataEnable(getApplicationContext())) {
					Intent intent = new Intent(LoginActivity.this,
							ResetPasswordActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "当前网络不佳",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initActionbar() {
		// 设置actionbar样式
		setCenterTitle("登录");

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

	/**
	 * <p>
	 * 校验账号与密码是否匹配
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void judgePassword() {
		bmobQuery = new BmobQuery<User>();
		bmobQuery.addWhereEqualTo("username", et_login_erhuo.getText()
				.toString());
		bmobQuery.findObjects(this, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				// 判断手机号码作为账号的情况
				bmobQuery = new BmobQuery<User>();
				bmobQuery.addWhereEqualTo("phone", et_login_erhuo.getText()
						.toString());
				bmobQuery.findObjects(getApplicationContext(),
						new FindListener<User>() {

							@Override
							public void onError(int arg0, String arg1) {
								Toast.makeText(getApplicationContext(),
										"用户名不存在", Toast.LENGTH_SHORT).show();
								find_succeed = false;
							}

							@Override
							public void onSuccess(List<User> list) {
								if (EncryptHelper.getMD5(
										et_login_password.getText().toString())
										.equals(list.get(0).getPassword())) {
									writeSharePreference();
									find_succeed = true;
								} else {
									Toast.makeText(getApplicationContext(),
											"密码错误", Toast.LENGTH_SHORT).show();
								}
							}
						});
			}

			@Override
			public void onSuccess(List<User> list) {
				if (EncryptHelper
						.getMD5(et_login_password.getText().toString()).equals(
								list.get(0).getPassword())) {
					writeSharePreference();
					find_succeed = true;
				} else {
					Toast.makeText(getApplicationContext(), "密码错误",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * <p>
	 * 判断验证码是否匹配
	 * </p>
	 * 
	 * @author 龙宇文
	 **/

	private void codeMatch() {
		BmobSMS.verifySmsCode(getApplicationContext(), phone, et_login_code
				.getText().toString(), new VerifySMSCodeListener() {

			@Override
			public void done(BmobException ex) {
				if (ex == null) {
					codeVerify = true;
				} else {
					codeVerify = false;
				}
			}
		});
	}

	/**
	 * <p>
	 * 本地存储用户信息
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void writeSharePreference() {
		sSpManager.putString(this, "loginmessage(MD5).xml", MODE_PRIVATE,
				"username", et_login_erhuo.getText().toString());
		sSpManager.putString(this, "loginmessage(MD5).xml", MODE_PRIVATE,
				"password",
				EncryptHelper.getMD5(et_login_password.getText().toString()));
	}

	/**
	 * <p>
	 * 通过用户名返回用户的手机号码
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private String usernameToPhone() {
		bmobQuery = new BmobQuery<User>();
		bmobQuery.addWhereEqualTo("username", et_login_erhuo.getText()
				.toString());
		bmobQuery.findObjects(getApplicationContext(),
				new FindListener<User>() {

					@Override
					public void onError(int arg0, String arg1) {
						Toast.makeText(getApplicationContext(), "用户名不存在",
								Toast.LENGTH_SHORT).show();
						Log.v("hello", "a1a1aaaaaaaaaaaaaaaaaaaaaaaa");
						phone = null;
					}

					@Override
					public void onSuccess(List<User> list) {
						phone = list.get(0).getPhone();
					}
				});
		return phone;
	}
}
