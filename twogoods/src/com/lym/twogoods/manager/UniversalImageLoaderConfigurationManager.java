package com.lym.twogoods.manager;

import java.io.File;

import com.lym.twogoods.utils.FileUtil;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;

/**
 * <p>
 * 	Universal-Image-Loader配置管理器
 * </p>
 * 
 * */
public class UniversalImageLoaderConfigurationManager {

	/** 默认ImageLoader配置 */
	private static ImageLoaderConfiguration sDefaultImageLoaderConfiguration;
	/** 默认ImageLoader配置相关的锁对象 */
	private static Object sDefaultImageLoaderConfigurationLock = new Object();
	
	/** 商品列表图片缩略图ImageLoader配置 */
	private static ImageLoaderConfiguration sGoodsListPictureThumbnailImageLoaderConfiguration;
	/** 商品列表图片缩略图ImageLoader配置相关的锁对象 */
	private static Object sGoodsListPictureThumbnailImageLoaderConfigurationLock = new Object();
	
	/** 用户头像缩略图ImageLoader配置 */
	private static ImageLoaderConfiguration sUserHeadPictureThumbnailImageLoaderConfiguration;
	/** 用户头像缩略图ImageLoader配置相关的锁对象 */
	private static Object sUserHeadPictureThumbnailImageLoaderConfigurationLock = new Object();
	
	/**
	 * <p>
	 * 	获取默认ImageLoader配置
	 * </p>
	 * <p>
	 * 	该方法是线程安全的
	 * </p>
	 * 
	 * @param context 上下文 
	 * 
	 * @return 默认ImageLoader配置,注意多次获取的是同一实例.
	 * */
	public static ImageLoaderConfiguration getDefaultImageLoaderConfiguration(Context context) {
		if(sDefaultImageLoaderConfiguration == null) {
			synchronized (sDefaultImageLoaderConfigurationLock) {
				if(sDefaultImageLoaderConfiguration == null) {
					sDefaultImageLoaderConfiguration = 
							new ImageLoaderConfiguration.Builder(context)
							.threadPriority(Thread.NORM_PRIORITY - 2)
							.threadPoolSize(3)
							.memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //2MB
							.denyCacheImageMultipleSizesInMemory()
							.tasksProcessingOrder(QueueProcessingType.LIFO)
							.build();
				}
			}
		}
		return sDefaultImageLoaderConfiguration;
	}
	
	/**
	 * <p>
	 * 	获取商品列表图片缩略图ImageLoader配置
	 * </p>
	 * <p>
	 * 	该方法是线程安全的
	 * </p>
	 * 
	 * @param context 上下文 
	 * 
	 * @return 商品列表图片缩略图ImageLoader配置,注意多次获取的是同一实例.
	 * */
	public static ImageLoaderConfiguration getGoodsListPictureThumbnailImageLoaderConfiguration(Context context) {
		if(sGoodsListPictureThumbnailImageLoaderConfiguration == null) {
			synchronized (sGoodsListPictureThumbnailImageLoaderConfigurationLock) {
				if(sGoodsListPictureThumbnailImageLoaderConfiguration == null) {
					sGoodsListPictureThumbnailImageLoaderConfiguration = 
							new ImageLoaderConfiguration.Builder(context)
							.threadPriority(Thread.NORM_PRIORITY - 2)
							.threadPoolSize(5) // five thread
							.memoryCache(new LruMemoryCache(20 * 1024 * 1024)) //2MB
							.denyCacheImageMultipleSizesInMemory() 
							.tasksProcessingOrder(QueueProcessingType.LIFO) //LIFO
							.diskCache(new UnlimitedDiskCache(new File(DiskCacheManager.getInstance(context).getGoodsPictureCachePath())))
							.build();
				}
			}
		}
		return sGoodsListPictureThumbnailImageLoaderConfiguration;
	}
	
	/**
	 * <p>
	 * 	获取头像缩略图ImageLoader配置
	 * </p>
	 * <p>
	 * 	该方法是线程安全的
	 * </p>
	 * 
	 * @param context 上下文 
	 * 
	 * @return 头像缩略图ImageLoader配置,注意多次获取的是同一实例.
	 * */
	public static ImageLoaderConfiguration getUserHeadPictureThumbnailImageLoaderConfiguration(Context context) {
		if(sUserHeadPictureThumbnailImageLoaderConfiguration == null) {
			synchronized (sUserHeadPictureThumbnailImageLoaderConfigurationLock) {
				if(sUserHeadPictureThumbnailImageLoaderConfiguration == null) {
					sUserHeadPictureThumbnailImageLoaderConfiguration = 
							new ImageLoaderConfiguration.Builder(context)
							.threadPriority(Thread.NORM_PRIORITY - 2)
							.threadPoolSize(5) // five thread
							.memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //2MB
							.denyCacheImageMultipleSizesInMemory() 
							.tasksProcessingOrder(QueueProcessingType.LIFO) //LIFO
							.diskCache(new UnlimitedDiskCache(new File(DiskCacheManager.getInstance(context).getUserHeadPictureCachePath())))
							.build();
				}
			}
		}
		return sUserHeadPictureThumbnailImageLoaderConfiguration;
	}
	
}
