package com.lym.twogoods.config;

import android.content.Context;
import android.os.Environment;

/**
 * <p>
 * 	持久性缓存管理类
 * </p>
 * <p>
 * 	单例模式
 * </p>
 * 
 * @author 麦灿标
 * */
public class DiskCacheManager {

	/** 磁盘缓存基本路径 */
	private String sBaseDiskCachePath;
	
	/** 头像缓存子目录 */
	private final String sHeadPictureCachePath = "/user/header/";
	
	/** 商品图片缓存子目录 */
	private final String sGoodsPictureCachePath = "/goods/picture/";
	
	/** 商品语音缓存子目录 */
	private final String sGoodsVoiceCachePath = "/goods/voice/";
	
	/** 聊天图片缓存子目录 */
	private final String sChatPictureCachePath = "/chat/picture/";
	
	/** 聊天语音缓存子目录 */
	private final String sChatVoiceCachePath = "/chat/voice/";
	
	private static DiskCacheManager diskCacheManager = new DiskCacheManager();
	
	private Object lock = new Object();
	
	private DiskCacheManager(){}
	
	/**
	 * <p>
	 * 	获取DiskCacheManager唯一实例
	 * </p>
	 * 
	 * @return DiskCacheManager唯一实例
	 * */
	public static DiskCacheManager getInstance(Context context) {
		diskCacheManager.initBaseDiskCachePath(context);
		return diskCacheManager;
	}
	
	private void initBaseDiskCachePath(Context context) {
		if(sBaseDiskCachePath == null) {
			synchronized (lock) {
				if(sBaseDiskCachePath == null) {
					if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
						sBaseDiskCachePath = context.getExternalCacheDir().getAbsolutePath();
					} else {
						sBaseDiskCachePath = context.getCacheDir().getAbsolutePath();
					}
				}
			}
		}
	}
	
	/**
	 * <p>
	 * 	获取磁盘缓存基本路径
	 * </p>
	 * 
	 * @return 磁盘缓存基本路径
	 * */
	public String getBaseDiskCachePath() {
		return sBaseDiskCachePath;
	}
	
	/**
	 * <p>
	 * 	获取用户头像缓存路径
	 * </p>
	 * 
	 * @return 用户头像缓存路径
	 * */
	public String getUserHeadPictureCachePath() {
		return sBaseDiskCachePath + sHeadPictureCachePath;
	}
	
	/**
	 * <p>
	 * 	获取商品图片缓存路径
	 * </p>
	 * 
	 * @return 商品图片缓存路径
	 * */
	public String getGoodsPictureCachePath() {
		return sBaseDiskCachePath + sGoodsPictureCachePath;
	}
	
	/**
	 * <p>
	 * 	获取商品语音缓存路径
	 * </p>
	 * 
	 * @return 商品语音缓存路径
	 * */
	public String getGoodsVoiceCachePath() {
		return sBaseDiskCachePath + sGoodsVoiceCachePath;
	}
	
	/**
	 * <p>
	 * 	获取聊天图片缓存路径
	 * </p>
	 * 
	 * @return 聊天图片缓存路径
	 * */
	public String getChatPictureCachePath() {
		return sBaseDiskCachePath + sChatPictureCachePath;
	}
	
	/**
	 * <p>
	 * 	获取聊天语音缓存路径
	 * </p>
	 * 
	 * @return 聊天语音缓存路径
	 * */
	public String getChatVoiceCachePath() {
		return sBaseDiskCachePath + sChatVoiceCachePath;
	}
	
}
