package com.lym.twogoods.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * <p>
 * 	网络未连接广播接收者
 * </p>
 * 
 * @author yao
 * */
public class NetworkTipsBroadcastReceiver extends BroadcastReceiver{

	private final static String TAG = "NetworkBroadcastReceiver";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if(!TextUtils.isEmpty(action)) {
			//"android.net.conn.CONNECTIVITY_CHANGE"
			if(action.equals("networkDisconnect")) {
				Log.i(TAG, "network is disconnected");
				Toast.makeText(context,"请检查网络设置",Toast.LENGTH_LONG).show();
			}
		}
	}
}
