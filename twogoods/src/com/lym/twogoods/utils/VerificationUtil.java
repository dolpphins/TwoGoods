package com.lym.twogoods.utils;

import java.util.List;

import com.lym.twogoods.bean.User;

import android.content.Context;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.RequestSMSCodeListener;

/**
 * <p>
 * 登陆，忘记密码，修改密码等验证操作相关类，使用此验证工具类里面的方法都都要先调用configClear()方法。
 * </p>
 * 
 * @author lyw
 * 
 */
public class VerificationUtil {
	// 贰货名是否已注册
	public static Boolean usernameNameIsHave = false;
	// 通过用户名找到的手机号码
	public static String phoneNumberByUsername = "";

	/**
	 * <p>
	 * 清空工具类里面的配置属性，使其恢复到原来的默认值。
	 * </p>
	 */
	public static void configClear() {
		phoneNumberByUsername = "";
		usernameNameIsHave = false;
	}

	/**
	 * <p>
	 * 向手机发送验证码
	 * </p>
	 * 
	 * @param context
	 *            上下文
	 * @param phone
	 *            手机号码
	 * @param template
	 *            审核通过的短信内容Key，不同的标记对应不同的短信内容。
	 */

	public static void codeVerification(final Context context, String phone,
			String template) {
		BmobSMS.requestSMSCode(context, phone, template,
				new RequestSMSCodeListener() {

					@Override
					public void done(Integer smsId, BmobException ex) {
						if (ex == null) {
							Toast.makeText(context, "短信已发送，请注意查看",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(context, "短信发送失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	/**
	 * <p>
	 * 向已注册的手机用户发送验证码
	 * </p>
	 * 
	 * @param context
	 * @param phone
	 */
	public static void sendCodeByPhone(final Context context, final String phone) {
		if (NetworkHelper.isNetworkAvailable(context)) {
			BmobQuery<User> bmobQuery = new BmobQuery<User>();
			bmobQuery.addWhereEqualTo("phone", phone);
			bmobQuery.findObjects(context, new FindListener<User>() {
				@Override
				public void onError(int arg0, String arg1) {
					Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onSuccess(List<User> list) {
					codeVerification(context, phone, "重置密码验证");
				}
			});
		}else {
			Toast.makeText(context, "当前网络不佳",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * <p>
	 * 判断贰货号是否已经注册
	 * </p>
	 * 
	 * @param context
	 * @param username
	 *            贰货号
	 * @return
	 */
	public static boolean usernameIsHave(final Context context, String username) {
		BmobQuery<User> bmobQuery = new BmobQuery<User>();
		bmobQuery.addWhereEqualTo("username", username);
		bmobQuery.findObjects(context, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(context, "用户不存在", Toast.LENGTH_SHORT).show();
				usernameNameIsHave = false;
			}

			@Override
			public void onSuccess(List<User> arg0) {
				usernameNameIsHave = true;
			}
		});

		return usernameNameIsHave;
	}

	/**
	 * <p>
	 * 通过用户名来找到用户的手机号码
	 * </p>
	 * 
	 * @param context
	 * @param username
	 *            用户名
	 * @return 返回手机号码
	 */
	public static String findPhoneNumberByUsername(final Context context,
			String username) {
		BmobQuery<User> bmobQuery = new BmobQuery<User>();
		bmobQuery.addWhereEqualTo("username", username);
		bmobQuery.findObjects(context, new FindListener<User>() {

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(context, "用户名不存在", Toast.LENGTH_SHORT).show();
				phoneNumberByUsername = "";
			}

			@Override
			public void onSuccess(List<User> list) {
				phoneNumberByUsername = list.get(0).getPhone();
			}
		});
		return phoneNumberByUsername;
	}
}
