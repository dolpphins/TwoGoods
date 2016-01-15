package com.lym.twogoods.ui;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.ui.base.BackActivity;
import com.lym.twogoods.ui.utils.LoginUtils;
import com.lym.twogoods.utils.DatabaseHelper;
import com.lym.twogoods.utils.EncryptHelper;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.SharePreferencesManager;
import com.lym.twogoods.utils.StringUtil;

/**
 * <p>
 * App注册Activity
 * </p>
 * 
 * @author 龙宇文
 **/
public class RegisterActivity extends BackActivity {

	// 定义布局控件
	private EditText et_register_erhuo;
	private EditText et_register_password;
	private EditText et_register_password_again;
	private EditText et_register_phone;
	private EditText et_register_code;
	private Button btn_register_code_get;
	private Button btn_register_register;

	// 用户注册信息表数据
	private User user_data;
	
	// 本地存储
	private SharePreferencesManager sSpManager;
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
		btn_register_code_get = (Button) findViewById(R.id.btn_register_code_get);
		btn_register_register = (Button) findViewById(R.id.btn_register_register);

		user_data = new User();

		// SharedPreferences的初始化
		sSpManager = SharePreferencesManager.getInstance();
		progressDialog = PublishConfigManger.getLoadProgressDialog(
				this, "小二正在注册中......", "稍等一下......如果取消可能会需要重新发送验证码消息哦", true);
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
		btn_register_code_get.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetworkHelper.isMobileDataEnable(getApplicationContext())
						|| NetworkHelper
								.isWifiDataEnable(getApplicationContext())) {
					if (StringUtil.isPhoneNumber(et_register_phone.getText()
							.toString())) {
						// 判断账号或者手机已注册。
						BmobQuery<User> queryUserName = new BmobQuery<User>();
						queryUserName.addWhereEqualTo("username",
								et_register_erhuo.getText().toString().trim());
						queryUserName.findObjects(getApplicationContext(),
								new FindListener<User>() {

									@Override
									public void onSuccess(List<User> list) {
										if (list.size() <= 0) {
											BmobQuery<User> queryPhone = new BmobQuery<User>();
											queryPhone.addWhereEqualTo("phone",
													et_register_phone.getText()
															.toString().trim());
											queryPhone.findObjects(
													getApplicationContext(),
													new FindListener<User>() {

														@Override
														public void onError(
																int arg0,
																String arg1) {
															// TODO
															// Auto-generated
															// method stub

														}

														@Override
														public void onSuccess(
																List<User> list) {
															if (list.size() <= 0) {
																BmobSMS.requestSMSCode(
																		getApplicationContext(),
																		et_register_phone
																				.getText()
																				.toString(),
																		"注册验证",
																		new RequestSMSCodeListener() {
																			@Override
																			public void done(
																					Integer smsId,
																					BmobException ex) {
																				if (ex == null) {
																					Toast.makeText(
																							getApplicationContext(),
																							"短信已发送，请注意查看",
																							Toast.LENGTH_SHORT)
																							.show();
																				}
																			}
																		});
															} else {
																Toast.makeText(
																		getApplicationContext(),
																		"手机已经注册了哦，亲",
																		Toast.LENGTH_SHORT)
																		.show();
															}
														}
													});
										} else {
											Toast.makeText(
													getApplicationContext(),
													"账号已经注册了哦，亲",
													Toast.LENGTH_SHORT).show();
										}
									}

									@Override
									public void onError(int arg0, String arg1) {
										// TODO Auto-generated method stub

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

		// 注册按钮点击事件
		btn_register_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断网络是否可用
				if (NetworkHelper.isMobileDataEnable(getApplicationContext())
						|| NetworkHelper
								.isWifiDataEnable(getApplicationContext())) {
					// 对贰货号，密码和验证码的合法性进行检测。
					if (StringUtil.isErHuoNumber(et_register_erhuo.getText()
							.toString())) {
						if (StringUtil.isPassword(et_register_password
								.getText().toString())) {
							if (StringUtil
									.isPassword(et_register_password_again
											.getText().toString())) {
								if (StringUtil.isSecurityCode(et_register_code
										.getText().toString())) {
									// 只有在两次输入密码都一样的时候才进行注册操作(还需要判断验证码)
									if (et_register_password
											.getText()
											.toString()
											.equals(et_register_password_again
													.getText().toString())) {
										progressDialog.show();
										codeMatch();
									} else {
										Toast.makeText(RegisterActivity.this,
												"密码不一致", Toast.LENGTH_SHORT)
												.show();

									}
								} else {
									Toast.makeText(getApplicationContext(),
											"验证码格式有误", Toast.LENGTH_SHORT)
											.show();
								}

							} else {

								Toast.makeText(getApplicationContext(),
										"密码格式有误", Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getApplicationContext(), "密码格式有误",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "贰货号格式有误",
								Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(getApplicationContext(), "当前网络不佳",
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initActionBar() {
		setCenterTitle("注册");
	}

	/**
	 * <p>
	 * 获取注册信息，并把它放到userBean
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void getRegisterData() {
		user_data.setUsername(et_register_erhuo.getText().toString());
		// 要改，把取得的密码转为GUID，然后再存。
		user_data.setPassword(EncryptHelper.getMD5(et_register_password
				.getText().toString()));
		user_data.setPhone(et_register_phone.getText().toString());
		user_data.setGUID(DatabaseHelper.getUUID().toString());
	}

	/**
	 * <p>
	 * 判断验证码是否匹配
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void codeMatch() {
		BmobSMS.verifySmsCode(getApplicationContext(), et_register_phone
				.getText().toString(), et_register_code.getText().toString(),
				new VerifySMSCodeListener() {

					@Override
					public void done(BmobException ex) {
						if (ex == null) {
							// 将用户注册的数据取到放在UserBean中
							getRegisterData();
							// 上传数据到Bmob中
							sendData();
						} else {
							progressDialog.dismiss();
							Toast.makeText(getApplicationContext(), "验证失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * <p>
	 * 上传数据到Bmob中
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void sendData() {
		user_data.save(getApplicationContext(), new SaveListener() {

			@Override
			public void onSuccess() {
				Toast.makeText(getApplicationContext(), "添加数据成功",
						Toast.LENGTH_SHORT).show();
				// 保存当前用户信息
				UserInfoManager.getInstance().setmCurrent(user_data);
				// 本地存储数据
				writeSharePreference();
				progressDialog.dismiss();
				Intent intent = new Intent(RegisterActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "注册失败",
						Toast.LENGTH_SHORT).show();
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
				"username", et_register_erhuo.getText().toString());
		sSpManager
				.putString(this, "loginmessage(MD5).xml", MODE_PRIVATE,
						"password", EncryptHelper.getMD5(et_register_password
								.getText().toString()));
		sSpManager.putString(this, "loginmessage(MD5).xml", MODE_PRIVATE,
				"phone", et_register_phone.getText().toString());
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (LoginUtils.isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}
}
