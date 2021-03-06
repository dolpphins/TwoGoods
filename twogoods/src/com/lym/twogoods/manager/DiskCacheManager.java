package com.lym.twogoods.manager;

import com.lym.twogoods.utils.FileUtil;

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
	
	/** 应用SD卡目录 */
	private String appSdPath;
	
	
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
	
	/** 默认图片缓存目录 */
	private final String defaultPictureCachePath = "/default/pictures/";
	
	/** 默认发送的图片缓存目录 */
	private final String sendPictureCachePath = "/sent/pictures/";
	
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
						appSdPath = Environment.getExternalStorageDirectory().getPath() + "/twogoods/";
					} else {
						sBaseDiskCachePath = context.getCacheDir().getAbsolutePath();
						appSdPath = "";
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
	
	/**
	 * <p>
	 * 	获取图片缓存默认存放路径
	 * </p>
	 * 
	 * @return 图片缓存默认存放路径
	 * */
	public String getDefaultPictureCachePath() {
		return sBaseDiskCachePath + defaultPictureCachePath;
	}
	
	/**
	 * <p>
	 * 	获取发送的图片缓存默认存放路径
	 * </p>
	 * 
	 * @return 发送的图片缓存默认存放路径
	 * */
	public String getSendPictureCachePath(){
		return sBaseDiskCachePath+sendPictureCachePath;
	}
	
	/**
	 * 获取缓存大小
	 * 
	 * @return 返回缓存大小,以字节为单位
	 */
	public long getCacheSize() {
		return FileUtil.getFolderSize(sBaseDiskCachePath);
	}
	
	/**
	 * 获取应用SD卡目录
	 * 
	 * @return 返回应用SD卡目录 
	 */
	public String getAppSdPath() {
		return appSdPath;
	}
}
