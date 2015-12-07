package com.lym.twogoods.user;

import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Login;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.utils.StringUtil;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * 登录器
 * 
 * @author 麦灿标
 *
 */
public class Loginer {

	private final static String TAG = "Loginer";
	
	private final static Loginer sLoginer = new Loginer();
	
	/** 错误码 */
	/** 未知错误 */
	public final static int LOGIN_UNKNOWN = 0;
	/** 非法参数 */
	public final static int LOGIN_ILLEGALARGUMENTS = 1;
	/** 用户名为空 */
	public final static int LOGIN_USERNAME_EMPTY = 2;
	/** 用户名不合法 */
	public final static int LOGIN_USERNAME_ILLEGAL = 3;
	/** 密码为空 */
	public final static int LOGIN_PASSWORD_EMPTY = 4;
	/** 密码不合法 */
	public final static int LOGIN_PASSWORD_ILLEGAL = 5;
	/** 手机号码为空 */
	public final static int LOGIN_PHONE_EMPTY = 6;
	/** 手机号码非法 */
	public final static int LOGIN_PHONE_ILLEGAL = 7;
	/** 验证码为空 */
	public final static int LOGIN_VERTIFY_CODE_EMPTY = 8;
	/** 验证码不正确 */
	public final static int LOGIN_VERTIFY_CODE_INCORRECT = 9;
	/** 用户名或者密码不正确 */
	public final static int LOGIN_USERNAME_PASSWORD_INCORRECT = 10;
	
	public static Loginer getInstance() {
		return sLoginer;
	}
	
	/**
	 * 登录
	 * 
	 * @param context 上下文
	 * @param item 包含登录信息的登录实体类对象,注意密码必须是MD5加密串
	 */
	public void login(Context context, Login item, LoginListener listener) {
		if(check(context, item, listener)) {
			startLogin(context, item, listener);
		}
	}
	
	//检查登录信息的合法性
	private boolean check(Context context, Login item, LoginListener listener) {
		int errorCode = -1;
		if(context == null || item == null) {
			errorCode = LOGIN_ILLEGALARGUMENTS;
		} else {
			if(!TextUtils.isEmpty(item.getUsername())) {
				if(!StringUtil.isErHuoNumber(item.getUsername())) {
					errorCode = LOGIN_USERNAME_ILLEGAL;
				} else {
					if(TextUtils.isEmpty(item.getPassword())) {
						errorCode = LOGIN_PASSWORD_EMPTY;
					} else if(!LoginValidator.isPassword(item.getPassword())) {
						errorCode = LOGIN_PASSWORD_ILLEGAL;
					} else {
						return true;
					}
				}
			} else if(!TextUtils.isEmpty(item.getPhone())) {
				if(!StringUtil.isPhoneNumber(item.getPhone())) {
					errorCode = LOGIN_PHONE_ILLEGAL;
				} else {
					if(TextUtils.isEmpty(item.getPassword())) {
						errorCode = LOGIN_PASSWORD_EMPTY;
					} else if(!LoginValidator.isPassword(item.getPassword())) {
						errorCode = LOGIN_PASSWORD_ILLEGAL;
					} else {
						return true;
					}
				}
			} else {
				errorCode = LOGIN_USERNAME_EMPTY;
			}
		}
		if(errorCode != -1 && listener != null) {
			listener.onError(errorCode);
		}
		return false;
	}
	
	private void startLogin(Context context, Login item, LoginListener listener) {
		BmobQuery<User> query = new BmobQuery<User>();
		if(LoginValidator.isErHuoNumber(item.getUsername())) {
			query.addWhereEqualTo("username", item.getUsername());
		} else if(LoginValidator.isPhoneNumber(item.getPhone())) {
			query.addWhereEqualTo("phone", item.getPhone());
		}
		query.addWhereEqualTo("password", item.getPassword());
		if(listener != null) {
			listener.onStart();
		}
		queryBmobForLogin(context, query, listener);
	}
	
	private void queryBmobForLogin(Context context, BmobQuery<User> query, final LoginListener listener) {
		query.findObjects(context, new FindListener<User>() {
			
			@Override
			public void onError(int code, String message) {
				if(listener != null) {
					listener.onError(LOGIN_UNKNOWN);
				}
			}

			@Override
			public void onSuccess(List<User> userList) {
				if(userList == null || userList.size() <= 0) {
					if(listener != null) {
						listener.onError(LOGIN_USERNAME_PASSWORD_INCORRECT);
					}
				} else if(userList.size() == 1) {
					//登录成功
					if(listener != null) {
						listener.onSuccess(userList.get(0));
					}
				} else {
					if(listener != null) {
						listener.onError(LOGIN_UNKNOWN);
					}
				}
			}
		});
	}
	
	/**
	 * 通过错误码获取错误描述信息
	 * 
	 * @param context 上下文
	 * @param errorCode 错误码
	 * @return 获取成功返回相应的错误描述信息,获取失败返回null.
	 */
	public static String getErrorMessage(Context context, int errorCode) {
		if(context == null) {
			return null;
		}
		Resources res = context.getResources();
		switch (errorCode) {
		case LOGIN_UNKNOWN:
			return res.getString(R.string.app_fast_login_unknown_tip);
		case LOGIN_ILLEGALARGUMENTS:
			return res.getString(R.string.app_fast_login_illegalarguments_tip);
		case LOGIN_USERNAME_EMPTY:
			return res.getString(R.string.app_fast_login_usernameempty_tip);
		case LOGIN_USERNAME_ILLEGAL:
			return res.getString(R.string.app_fast_login_usernameillegal_tip);
		case LOGIN_PASSWORD_EMPTY:
			return res.getString(R.string.app_fast_login_passwordempty_tip);
		case LOGIN_PASSWORD_ILLEGAL:
			return res.getString(R.string.app_fast_login_passwordillegal_tip);
		case LOGIN_PHONE_EMPTY:
			return res.getString(R.string.app_fast_login_phoneempty_tip);
		case LOGIN_PHONE_ILLEGAL:
			return res.getString(R.string.app_fast_login_phoneillegal_tip);
		case LOGIN_VERTIFY_CODE_EMPTY:
			return res.getString(R.string.app_fast_login_vertifycodeempty_tip);
		case LOGIN_VERTIFY_CODE_INCORRECT:
			return res.getString(R.string.app_fast_login_vertifycodeincorrect_tip);
		case LOGIN_USERNAME_PASSWORD_INCORRECT:
			return res.getString(R.string.app_fast_login_username_password_incorrect_tip);
		}
		return null;
	}
	
	/**
	 * 登录监听器
	 */
	public interface LoginListener {
		
		/**
		 * 当开始登录时回调该方法
		 */
		void onStart();
		
		/**
		 * 当登录错误时回调该方法
		 * 
		 * @param errorCode 错误码
		 */
		void onError(int errorCode);
		
		/**
		 * 当登录成功时回调该方法
		 * 
		 * @param user 用户信息
		 */
		void onSuccess(User user);
	}
}
