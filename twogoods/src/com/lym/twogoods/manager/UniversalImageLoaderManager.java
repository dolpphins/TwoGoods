package com.lym.twogoods.manager;

import java.util.HashMap;
import java.util.Map;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * <p>
 * 	Universal-Image-Loader管理器
 * </p>
 * 
 * @author 麦灿标
 * */
public class UniversalImageLoaderManager {

	private final static String TAG = "UniversalImageLoaderManager";
	
	/** 默认的ImageLoader */
	private static ImageLoader sDefaultImageLoader = ImageLoader.getInstance();
	
	/** ImageLoader map */
	private static Map<ImageLoaderConfiguration, ImageLoader> sImageLoaderMap = new HashMap<ImageLoaderConfiguration, ImageLoader>();
	
	/**
	 * <p>
	 * 	获取ImageLoader对象,并通过configuration初始化其配置,如果configuration是同一对象,那么将返回同一个ImageLoader对象.
	 * </p>
	 * 
	 * @param configuration ImageLoader配置
	 * 
	 * @return 如果configuration为null将返回null，否则返回相应的ImageLoader对象
	 * */
	public static ImageLoader getImageLoader(ImageLoaderConfiguration configuration) {
		if(configuration == null) {
			return null; 
		}
		if(sImageLoaderMap.containsKey(configuration)) {
			return sImageLoaderMap.get(configuration);
		}
		ImageLoader imageLoader = new ExtendUniversalImageLoader();
		imageLoader.init(configuration);
		sImageLoaderMap.put(configuration, imageLoader);
		return imageLoader;
	}
	
	/**
	 * <p>
	 * 	获取默认的ImageLoader,注意在调用该方法之前要先调用{@link #initDefaultImageLoader(ImageLoaderConfiguration)}
	 * 	方法初始化默认ImageLoader的配置,一般是在应用程序的Application中调用该初始化方法的.
	 * </p>
	 * 
	 * @return 返回默认ImageLoader
	 * 
	 * @throws IllegalStateException 如果默认ImageLoader未被初始化那么抛出该异常
	 * */
	public static ImageLoader getDefaultImageLoader() throws IllegalStateException {
		if(!sDefaultImageLoader.isInited()) {
			throw new IllegalStateException("default image loader don't init,please invoke "
					+ "ImageLoaderManager's initDefaultImageLoader to init the default image "
					+ "loader");
		}
		return sDefaultImageLoader;
	}
	
	/**
	 * <p>
	 * 	初始化默认ImageLoader的配置,一般是在应用程序的Application中调用该初始化方法的.
	 * </p>
	 * 
	 * @param configuration ImageLoader配置,注意不能未空.
	 * */
	public static void initDefaultImageLoader(ImageLoaderConfiguration configuration) {
		sDefaultImageLoader.init(configuration);
	}
	
	
	
	//ImageLoader扩展类
	private static class ExtendUniversalImageLoader extends ImageLoader{
		
	}
}
