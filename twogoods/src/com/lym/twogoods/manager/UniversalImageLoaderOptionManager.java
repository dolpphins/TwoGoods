package com.lym.twogoods.manager;

import com.baidu.platform.comapi.map.w;
import com.lym.twogoods.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * <p>
 * 	Universal-Image-Loader Option管理器
 * </p>
 * 
 * 
 * */
public class UniversalImageLoaderOptionManager {

//	/** 默认UIL DisplayImageOption */
//	private static DisplayImageOptions sDefaultDisplayImageOption; 
//	private static Object sDefaultDisplayImageOptionLock = new Object();
//	
//	/** Goods UIL DisplayImageOption */
//	private static DisplayImageOptions sGoodsListDisplayImageOption; 
//	private static Object sGoodsListDisplayImageOptionLock = new Object();
//	
//	/** 头像 UIL DisplayImageOption */
//	private static DisplayImageOptions sHeadPictureDisplayImageOption; 
//	//private static Object sHeadPictureDisplayImageOptionLock = new Object();
	
	/**
	 * <p>
	 * 	获取默认UIL的DisplayImageOption,其中指定的有:内存缓存,
	 * 	磁盘缓存,线性渐入显示,加载失败显示R.drawable.empty_picture图片
	 * </p>
	 * 
	 * @return 返回默认UIL的DisplayImageOption
	 * */
	public static DisplayImageOptions getDefaultDisplayImageOption() {
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
							.showImageOnLoading(R.drawable.goods_empty_picture)
							.showImageForEmptyUri(R.drawable.goods_empty_picture)
							.showImageOnFail(R.drawable.goods_empty_picture)
							.cacheInMemory(true)
							.cacheOnDisk(true)
							.considerExifParams(true)
							.displayer(new FadeInBitmapDisplayer(200))
							.build();
		return options;
	}
	
	/**
	 * <p>
	 * 	获取商品UIL的DisplayImageOption,其中指定的有:内存缓存,
	 * 	磁盘缓存,线性渐入显示,加载失败显示R.drawable.empty_picture图片
	 * </p>
	 * 
	 * @return 返回商品UIL的DisplayImageOption
	 * */
	public static DisplayImageOptions getGoodsListDisplayImageOption() {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
							.showImageOnLoading(R.drawable.empty_picture)
							.showImageForEmptyUri(R.drawable.empty_picture)
							.showImageOnFail(R.drawable.empty_picture)
							.cacheInMemory(true)
							.cacheOnDisk(true)
							.considerExifParams(true)
							.displayer(new FadeInBitmapDisplayer(200))
							.build();
		return options;
	}
	
	/**
	 * <p>
	 * 	获取头像UIL的DisplayImageOption,其中指定的有:内存缓存,
	 * 	磁盘缓存,线性渐入显示,加载失败显示R.drawable.user_default_head图片
	 * </p>
	 * 
	 * @return 返回头像UIL的DisplayImageOption
	 * */
	public static DisplayImageOptions getHeadPictureDisplayImageOption(BitmapFactory.Options options, int width, int height) {

		BitmapFactory.Options decodingOptions = null;
		if(options != null && width > 0 && height > 0) {
			int realWidth = options.outWidth;
			int realHeight = options.outHeight;
			if(realWidth > 0 && realHeight > 0) {
				decodingOptions = new BitmapFactory.Options();
				decodingOptions.inSampleSize = calculateInSampleSize(realWidth, realHeight, width, height);
			}
		}
		
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
								.showImageOnLoading(R.drawable.user_default_head)
								.showImageForEmptyUri(R.drawable.user_default_head)
								.showImageOnFail(R.drawable.user_default_head)
								.cacheInMemory(true)
								.cacheOnDisk(true)
								.considerExifParams(true)
								.displayer(new FadeInBitmapDisplayer(200))
								.decodingOptions(decodingOptions)
								.bitmapConfig(Bitmap.Config.RGB_565)
								.build();
						
		return displayImageOptions;
	}
	
	private static int calculateInSampleSize(int realWidth, int realHeight, int width, int height) {
		if(realWidth <= 0 || realHeight <= 0 || width <= 0 || height <= 0) {
			return 1;
		}
		//需要缩小
		if(realWidth > width || realHeight > height) {
			int widthRatio = Math.round((float) realWidth / (float) width);
			int heightRatio = Math.round((float) realHeight / (float) height);
			return widthRatio < heightRatio ? widthRatio : heightRatio;  
		} else {
			return 1;
		}
	}
}
