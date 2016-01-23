package com.lym.twogoods.ui;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.VerifySMSCodeListener;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.Login;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.ui.base.BackActivity;
import com.lym.twogoods.ui.utils.LoginUtils;
import com.lym.twogoods.user.Loginer;
import com.lym.twogoods.user.listener.DefaultLoginListener;
import com.lym.twogoods.utils.EncryptHelper;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.StringUtil;
import com.lym.twogoods.utils.VerificationUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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
	private TextView tv_login_forget;
	private TextView tv_app_login_activity_title;

	// 定义actionbar控件
	private TextView actionbarRightTextView;

	// 定义变量来看是否查找成功。
	private BmobQuery<User> bmobQuery;
	// 通过用户名来取得手机号码
	private String phone = "";
	// User对象，根据登陆的用户来获得
	private User user;
	//登陆加载器
	private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_login_activity_replace);
		initActionbar();
		init();
		clickEvent();
	}

	private void init() {
		VerificationUtil.configClear();
		et_login_erhuo = (EditText) findViewById(R.id.et_login_erhuo);
		et_login_password = (EditText) findViewById(R.id.et_login_password);
		et_login_code = (EditText) findViewById(R.id.et_login_code);
		btn_login_code_get = (Button) findViewById(R.id.btn_login_code_get);
		btn_login_land = (Button) findViewById(R.id.btn_login_land);
		tv_login_forget = (TextView) findViewById(R.id.tv_login_forget);
		tv_app_login_activity_title=(TextView) findViewById(R.id.tv_app_login_activity_title);
		StringUtil.setTextFont(this, tv_app_login_activity_title, "fonts/huawenxingkai.ttf");
		
		user = new User();
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("正在登陆");
		progressDialog.setMessage("稍等一下......");
		progressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(true);
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
										}else {
											Toast.makeText(
													getApplicationContext(),
													"短信发送失败",
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
										}else {
											Toast.makeText(
													getApplicationContext(),
													"短信发送失败",
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
					progressDialog.show();
					Login loginItem=new Login();
					loginItem.setUsername(et_login_erhuo.getText().toString().trim());
					loginItem.setPhone(et_login_erhuo.getText().toString().trim());
					loginItem.setPassword(EncryptHelper.getMD5(et_login_password.getText().toString().trim()));
					Loginer.getInstance().login(getApplicationContext(),loginItem,new NormalLoginListener(getApplicationContext()));
					
					//////从這里开始删
					//
					// 对贰货号，密码和验证码的合理性进行检测。
					/*if (StringUtil.isErHuoNumber(et_login_erhuo.getText()
							.toString())
							|| StringUtil.isPhoneNumber(et_login_erhuo
									.getText().toString())) {
						if (StringUtil.isPassword(et_login_password.getText()
								.toString())) {*/
//							if (StringUtil.isSecurityCode(et_login_code
//									.getText().toString())) {
////////恢复这句								judgePassword();	//到时候需要验证码的时候，只需要留下codeMatch();
//								codeMatch();
//								if (find_succeed && codeVerify) {
									/*Intent intent = new Intent(
											LoginActivity.this,
											MainActivity.class);
									startActivity(intent);
									finish();*/
//								}*/
//								progressDialog.show();
//								codeMatch();
//							} else {
//								Toast.makeText(getApplicationContext(),
//										"验证码格式有误", Toast.LENGTH_SHORT).show();
//							}
						/*} else {

							Toast.makeText(getApplicationContext(), "密码格式有误",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(), "贰货号格式有误",
								Toast.LENGTH_SHORT).show();
					}*/

				}
				///一直删到這里
				else {
					Toast.makeText(getApplicationContext(), "当前网络不佳",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 忘记密码按钮点击事件
		tv_login_forget.setOnClickListener(new OnClickListener() {

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

			}

			@Override
			public void onSuccess(List<User> list) {
				if (list.size() != 0) {
					user = list.get(0);
					if (EncryptHelper.getMD5(
							et_login_password.getText().toString()).equals(
							user.getPassword())) {
						writeSharePreference(user);
						progressDialog.dismiss();
						Intent intent = new Intent(
								LoginActivity.this,
								MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						progressDialog.dismiss();
						Toast.makeText(getApplicationContext(), "密码错误",
								Toast.LENGTH_SHORT).show();
					}
					// 搜索不成功的情况
				} else {
					// 判断手机号码作为账号的情况
					bmobQuery = new BmobQuery<User>();
					bmobQuery.addWhereEqualTo("phone", et_login_erhuo.getText()
							.toString());
					bmobQuery.findObjects(getApplicationContext(),
							new FindListener<User>() {

								@Override
								public void onError(int arg0, String arg1) {
									// 不清楚什么时候为error
								}

								@Override
								public void onSuccess(List<User> list) {
									if (list.size() != 0) {
										user = list.get(0);
										if (EncryptHelper.getMD5(
												et_login_password.getText()
														.toString()).equals(
												user.getPassword())) {
											writeSharePreference(user);
											progressDialog.dismiss();
											Intent intent = new Intent(
													LoginActivity.this,
													MainActivity.class);
											startActivity(intent);
											finish();

										} else {
											progressDialog.dismiss();
											Toast.makeText(
													getApplicationContext(),
													"密码错误", Toast.LENGTH_SHORT)
													.show();
										}
									} else {
										progressDialog.dismiss();
										Toast.makeText(getApplicationContext(),
												"用户名不存在", Toast.LENGTH_SHORT)
												.show();
									}
								}
							});
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
						judgePassword();
				} else {
					progressDialog.dismiss();
					Toast.makeText(getApplicationContext(), "验证码错误", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	/**
	 * <p>
	 * 本地存储用户信息,保存到UserInfoManger中
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private void writeSharePreference(User user) {
		this.user=user;
		//user.setHead_url(UserConfiguration.USER_DEFAULT_HEAD);
		UserInfoManager.getInstance().setmCurrent(user);
		UserInfoManager.getInstance().writeLoginToSP(getApplicationContext(), user);
	}

	/**
	 * <p>
	 * 通过用户名返回用户的手机号码
	 * </p>
	 * 
	 * @author 龙宇文
	 **/
	private String usernameToPhone() {
		if (user.getPhone() == null) {
			bmobQuery = new BmobQuery<User>();
			bmobQuery.addWhereEqualTo("username", et_login_erhuo.getText()
					.toString());
			bmobQuery.findObjects(getApplicationContext(),
					new FindListener<User>() {

						@Override
						public void onError(int arg0, String arg1) {
							Toast.makeText(getApplicationContext(), "用户名不存在",
									Toast.LENGTH_SHORT).show();
							phone = null;
						}

						@Override
						public void onSuccess(List<User> list) {
							phone = list.get(0).getPhone();
						}
					});
		} else {
			phone = user.getPhone();
		}
		return phone;
	}
	
	private class NormalLoginListener extends DefaultLoginListener{

		public NormalLoginListener(Context context) {
			super(context);
		}
		@Override
		public void onError(int errorCode) {
			super.onError(errorCode);
			progressDialog.dismiss();
			Toast.makeText(getApplicationContext(), "登陆失败", Toast.LENGTH_SHORT).show();
		}
		@Override
		public void onSuccess(User user) {
			super.onSuccess(user);
			progressDialog.dismiss();
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
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
