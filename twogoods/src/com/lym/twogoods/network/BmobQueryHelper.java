package com.lym.twogoods.network;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.bean.User;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Bmob查询工具类
 * 
 * @author 麦灿标
 *
 */
public class BmobQueryHelper {

	/***
	 * 通过用户名获取头像地址
	 * 
	 * @param context 上下文
	 * @param username 用户名
	 * @param listener 监听器
	 */
	public static void queryHeadPictureByUsername(Context context, String username,
											OnUsername2HeadPictureListener listener) {
		if(context == null || TextUtils.isEmpty(username)) {
			return;
		}
		final OnUsername2HeadPictureListener fListener = listener;
		BmobQuery<User> query = new BmobQuery<User>();
		query.addQueryKeys("head_url");
		query.addWhereEqualTo("username", username);
		query.findObjects(context, new FindListener<User>() {

			@Override
			public void onError(int errorcode, String errormsg) {
				if(fListener != null) {
					fListener.onError(errorcode, errormsg);
				}
			}

			@Override
			public void onSuccess(List<User> userList) {
				if(fListener != null && userList != null) {
					if(userList.size() > 0) {
						fListener.onSuccess(userList.get(0).getHead_url());
					}
				}
			}
		});
	}
	
	/**
	 * 通过用户名获取头像地址监听接口
	 */
	public interface OnUsername2HeadPictureListener {
		
		void onSuccess(String headUrl);
		
		void onError(int errorcode, String errormsg);
	}
}
