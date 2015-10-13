package com.lym.twogoods.utils;

import android.content.Context;  
import android.net.ConnectivityManager;  
import android.net.NetworkInfo;  
import android.net.Uri;  
import android.net.NetworkInfo.State;  
import android.telephony.TelephonyManager;  
import android.util.Log;  
/**
 * 与网络相关的工具类
 * 需要添加android.permission.ACCESS_NETWORK_STATE权限
 * @author yao
 *
 * */  
public class NetworkHelper {  
    public static final int NETWORN_NONE = 0;  
    public static final int NETWORN_WIFI = 1;  
    public static final int NETWORKTYPE_3G = 2;
    public static final int NETWORKTYPE_2G = 3;
    private static String LOG_TAG = "NetWorkHelper";  
  
    public static Uri uri = Uri.parse("content://telephony/carriers");  
  
    /**  
     * 判断是否有网络  
     * @param 上下文
     * 
     * @return true 手机有网络可用  false 无网络可用
     * @author yao
     */  
    public static boolean isNetworkAvailable(Context context) {  
        ConnectivityManager connectivity = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
  
        if (connectivity == null) {  
            Log.w(LOG_TAG, "couldn't get connectivity manager");  
            return false;
        } 
        
        NetworkInfo ni = connectivity.getActiveNetworkInfo();
        
        if(ni==null||!ni.isAvailable()){
        	Log.d(LOG_TAG, "network is not available");
        	return false; 
        }
        return true;  
    } 
    
    
    /**  
     * 检查网络连接状态  
     * @param context  
     * @return  false 没有连接网络 true 已连接网络
     * 
     * @author yao
     */  
    public static boolean isNetConnected(Context context){  
        ConnectivityManager connectivity = (ConnectivityManager)context.
        		getSystemService(Context.CONNECTIVITY_SERVICE);  
        NetworkInfo ni = connectivity.getActiveNetworkInfo();
        
        if(ni!=null&&ni.isConnected())
        	return true;  
        return false;
    }  
  
    
    /**
     * 获取网络的连接类型
     * @param context
     * @return 
     * 
     * @author yao
     */
  public static int getNetworkState(Context context){  
	  
	  int netType = -1;
	  
	  ConnectivityManager manager = (ConnectivityManager) context.
			  getSystemService(Context.CONNECTIVITY_SERVICE);
	  
	  //manager为空的情况下当做无网络的状态
	  if(manager==null)
		  return NETWORN_NONE;
	  
      NetworkInfo networkInfo = manager.getActiveNetworkInfo();
      if (networkInfo != null && networkInfo.isConnected()) {
          String type = networkInfo.getTypeName();
          if (type.equalsIgnoreCase("WIFI")) {
              netType = ConnectivityManager.TYPE_WIFI;
          } else if (type.equalsIgnoreCase("MOBILE")) {
              netType = isFastMobileNetwork(context) ? NETWORKTYPE_3G : NETWORKTYPE_2G;
          }
      } else {
          netType = NETWORN_NONE;
      }
      return netType;
  }  
  /**
   * 判断当前网络是3g还是2g
   * @param context
   * @return true 当前网络是3g网络，false 当前网络是2g网络 
   * 
   */
  
  public static boolean isFastMobileNetwork(Context context) {
	  TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
	      switch (telephonyManager.getNetworkType()) {
	          case TelephonyManager.NETWORK_TYPE_1xRTT:
	              return false; // ~ 50-100 kbps
	          case TelephonyManager.NETWORK_TYPE_CDMA:
	              return false; // ~ 14-64 kbps
	          case TelephonyManager.NETWORK_TYPE_EDGE:
	              return false; // ~ 50-100 kbps
	          case TelephonyManager.NETWORK_TYPE_EVDO_0:
	              return true; // ~ 400-1000 kbps
	          case TelephonyManager.NETWORK_TYPE_EVDO_A:
	              return true; // ~ 600-1400 kbps
	          case TelephonyManager.NETWORK_TYPE_GPRS:
	              return false; // ~ 100 kbps
	          case TelephonyManager.NETWORK_TYPE_HSDPA:
	              return true; // ~ 2-14 Mbps
	          case TelephonyManager.NETWORK_TYPE_HSPA:
	              return true; // ~ 700-1700 kbps
	          case TelephonyManager.NETWORK_TYPE_HSUPA:
	              return true; // ~ 1-23 Mbps
	          case TelephonyManager.NETWORK_TYPE_UMTS:
	              return true; // ~ 400-7000 kbps
	          case TelephonyManager.NETWORK_TYPE_EHRPD:
	              return true; // ~ 1-2 Mbps
	          case TelephonyManager.NETWORK_TYPE_EVDO_B:
	              return true; // ~ 5 Mbps
	          case TelephonyManager.NETWORK_TYPE_HSPAP:
	              return true; // ~ 10-20 Mbps
	          case TelephonyManager.NETWORK_TYPE_IDEN:
	              return false; // ~25 kbps
	          case TelephonyManager.NETWORK_TYPE_LTE:
	              return true; // ~ 10+ Mbps
	          case TelephonyManager.NETWORK_TYPE_UNKNOWN:
	              return false;
	          default:
	              return false;
	          }
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
