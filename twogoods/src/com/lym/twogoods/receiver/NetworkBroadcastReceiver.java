package com.lym.twogoods.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;

/**
 * <p>
 * 	网络状态改变广播接收者
 * </p>
 * 
 * @author 麦灿标
 * */
public class NetworkBroadcastReceiver extends BroadcastReceiver{

	private final static String TAG = "NetworkBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(!TextUtils.isEmpty(action)) {
			//"android.net.conn.CONNECTIVITY_CHANGE"
			if(action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
				Log.i(TAG, "network status has changed");
			}
		}
	}
}
