package com.lym.twogoods.config;

import android.os.Environment;

/**
 * <p>
 * 	持久性缓存管理类
 * </p>
 * 
 * @author 麦灿标
 * */
public class DiskCacheManager {

	/** 磁盘缓存基本路径 */
	private final static String sBaseDiskCachePath;
	
	/** 头像缓存子目录 */
	private final static String sHeadPictureCachePath = "/user/header";
	
	/** 商品图片缓存子目录 */
	private final static String sGoodsPictureCachePath = "/goods/picture";
	
	/** 商品语音缓存子目录 */
	private final static String sGoodsVoiceCachePath = "/goods/voice";
	
	/** 聊天图片缓存子目录 */
	private final static String sChatPictureCachePath = "/chat/picture";
	
	/** 聊天语音缓存子目录 */
	private final static String sChatVoiceCachePath = "/chat/voice";
	
	static {
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			sBaseDiskCachePath = Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			sBaseDiskCachePath = Environment.getDataDirectory().getAbsolutePath() + "/cache";
		}
	}
	
	/**
	 * <p>
	 * 	获取磁盘缓存基本路径
	 * </p>
	 * 
	 * @return 磁盘缓存基本路径
	 * */
	public static String getBaseDiskCachePath() {
		return sBaseDiskCachePath;
	}
	
	/**
	 * <p>
	 * 	获取用户头像缓存路径
	 * </p>
	 * 
	 * @return 用户头像缓存路径
	 * */
	public static String getUserHeadPictureCachePath() {
		return sBaseDiskCachePath + sHeadPictureCachePath;
	}
	
	/**
	 * <p>
	 * 	获取商品图片缓存路径
	 * </p>
	 * 
	 * @return 商品图片缓存路径
	 * */
	public static String getGoodsPictureCachePath() {
		return sBaseDiskCachePath + sGoodsPictureCachePath;
	}
	
	/**
	 * <p>
	 * 	获取商品语音缓存路径
	 * </p>
	 * 
	 * @return 商品语音缓存路径
	 * */
	public static String getGoodsVoiceCachePath() {
		return sBaseDiskCachePath + sGoodsVoiceCachePath;
	}
	
	/**
	 * <p>
	 * 	获取聊天图片缓存路径
	 * </p>
	 * 
	 * @return 聊天图片缓存路径
	 * */
	public static String getChatPictureCachePath() {
		return sBaseDiskCachePath + sChatPictureCachePath;
	}
	
	/**
	 * <p>
	 * 	获取聊天语音缓存路径
	 * </p>
	 * 
	 * @return 聊天语音缓存路径
	 * */
	public static String getChatVoiceCachePath() {
		return sBaseDiskCachePath + sChatVoiceCachePath;
	}
	
}
