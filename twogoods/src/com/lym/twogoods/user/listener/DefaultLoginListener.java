package com.lym.twogoods.user.listener;

import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.SharePreferencesConfiguration;
import com.lym.twogoods.user.Loginer;
import com.lym.twogoods.utils.SharePreferencesManager;

import android.content.Context;

/**
 * 默认的LoginListener监听器
 * 
 * @author 麦灿标
 *
 */
public class DefaultLoginListener implements Loginer.LoginListener{

	public Context mContext;
	
	public DefaultLoginListener(Context context) {
		mContext = context;
	}
	
	@Override
	public void onStart() {
	}
	
	@Override
	public void onError(int errorCode) {
	}

	@Override
	public void onSuccess(User user) {
		UserInfoManager.getInstance().setmCurrent(user);
		//写入到SharePreferences
		UserInfoManager.getInstance().writeLoginToSP(mContext, user);
	}	
}
