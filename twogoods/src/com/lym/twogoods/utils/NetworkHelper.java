package com.lym.twogoods.utils;

import android.content.Context;  
import android.net.ConnectivityManager;  
import android.net.NetworkInfo;  
import android.net.Uri;  
import android.net.NetworkInfo.State;  
import android.telephony.TelephonyManager;  
import android.util.Log;  
  
public class NetworkHelper {  
    public static final int NETWORN_NONE = 0;  
    public static final int NETWORN_WIFI = 1;  
    public static final int NETWORN_MOBILE = 2;  
    private static String LOG_TAG = "NetWorkHelper";  
  
    public static Uri uri = Uri.parse("content://telephony/carriers");  
  
    /**  
     * 判断是否有网络  
     * @param 上下文
     * 
     * @return true 手机有网络可用 
     * @author yao
     */  
    public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager connectivity = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
  
        if (connectivity == null) {  
            Log.w(LOG_TAG, "couldn't get connectivity manager");  
        } else {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();  
            if (info != null) {  
                for (int i = 0; i < info.length; i++) {  
                    if (info[i].isAvailable()) {  
                        Log.d(LOG_TAG, "network is available");  
                        return true;  
                    }  
                }  
            }  
        }  
        Log.d(LOG_TAG, "network is not available");  
        return false;  
    }  
    /**  
     * 检查网络连接状态  
     * @param context  
     * @return  false 没有连接网络
     * 
     * @author yao
     */  
    public static boolean checkNetState(Context context){  
        boolean netstate = false;  
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if(connectivity != null)  
        {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();  
            if (info != null) {  
                for (int i = 0; i < info.length; i++)  
                {  
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)   
                    {  
                        netstate = true;  
                        break;  
                    }  
                }  
            }  
        }  
        return netstate;  
    }  
  
    
    /**
     * 获取网络的连接类型
     * @param context
     * @return
     * 
     * @author yao
     */
  public static int getNetworkState(Context context){  
      ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  

      //Wifi  
      State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();  
      if(state == State.CONNECTED||state == State.CONNECTING){  
          return NETWORN_WIFI;  
      }  

      //3G  
      state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
      if(state == State.CONNECTED||state == State.CONNECTING){  
          return NETWORN_MOBILE;  
      }  
      return NETWORN_NONE;  
  }  
    
    
    /**  
     * 判断MOBILE网络是否可用  
     *   
     * @param context  
     * @return  
     * @throws Exception  
     * 
     * @author yao
     */  
    public static boolean isMobileDataEnable(Context context) {  
        ConnectivityManager connectivityManager = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        boolean isMobileDataEnable = false;  
  
        isMobileDataEnable = connectivityManager.getNetworkInfo(  
                ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();  
  
        return isMobileDataEnable;  
    }  
  
      
    /**  
     * 判断wifi 是否可用  
     * @param context  
     * @return  
     * @throws Exception  
     * 
     * @author yao
     */  
    public static boolean isWifiDataEnable(Context context) {  
        ConnectivityManager connectivityManager = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        boolean isWifiDataEnable = false;  
        isWifiDataEnable = connectivityManager.getNetworkInfo(  
                ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
        return isWifiDataEnable;  
    }  
  
     
}  
