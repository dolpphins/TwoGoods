package com.lym.twogoods.manager;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.screen.UserScreen;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

/**
 * <p>
 * 	图片加载帮助类(为减少重复代码和简化加载图片流程考虑)
 * </p>
 * 
 * @author 麦灿标
 * */
public class ImageLoaderHelper {


	public static void loadUserHeadPictureThumnail(Context context, ImageView iv, String url, ImageLoadingListener listener) {
		if(iv != null) {
			List<ImageView> list = new ArrayList<ImageView>(1);
			list.add(iv);
			loadUserHeadPictureThumnail(context, url, list, listener);
		}
	}
	
	public static void loadUserHeadPictureThumnail(Context context, String url, final List<ImageView> ivList, final ImageLoadingListener listener) {
		if(ivList == null || ivList.size() <= 0) {
			return;
		}
		ImageLoaderConfiguration configuration = UniversalImageLoaderConfigurationManager
				.getUserHeadPictureThumbnailImageLoaderConfiguration(context);
		ImageLoader imageLoader = UniversalImageLoaderManager.getImageLoader(configuration);
		
		//使用Options无效果!必须改
		int w = UserScreen.getMineHeadPictureSize(context);
		BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
		decodingOptions.outHeight = w;
		decodingOptions.outWidth = w;
		DisplayImageOptions options = UniversalImageLoaderOptionManager.getHeadPictureDisplayImageOption(decodingOptions, w, w);
		
//		ivList.get(0).setImageResource(R.drawable.common_m1);
//		if(true) {
//			return;
//		}
		
		//必须实现为多个ImageView加载同一幅图片的功能,ImageLoader没有提供
		imageLoader.displayImage(url, ivList.get(0), options, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				if(listener != null) {
					listener.onLoadingCancelled(arg0, arg1);
				}
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				if(listener != null) {
					listener.onLoadingFailed(arg0, arg1, arg2);
				}
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				if(listener != null) {
					listener.onLoadingComplete(arg0, arg1, arg2);
				}
				//arg2可能为null(比如获取失败时)
				if(arg2 != null) {
					for(ImageView iv : ivList) {
						//注意要判空
						if(iv != null) {
							iv.setImageBitmap(arg2);
						}
					}
				}
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				if(listener != null) {
					listener.onLoadingCancelled(arg0, arg1);
				}
			}
		});
				
	}
	
	/**
	 * 加载一张商品缩略图(该缩略图在ListView中显示)
	 * 
	 * @param context 上下文
	 * @param url 缩略图图片路径
	 * @param imageView 要显示的ImageView
	 */
	public static void loadGoodsListPictureThumnail(Context context, String url, ImageView imageView) {
		
		if(context == null || imageView == null) {
			return;
		}
		
		ImageLoaderConfiguration configuration = UniversalImageLoaderConfigurationManager
				.getGoodsListPictureThumbnailImageLoaderConfiguration(context.getApplicationContext());
		ImageLoader imageLoader = UniversalImageLoaderManager.getImageLoader(configuration);
		
		DisplayImageOptions options = UniversalImageLoaderOptionManager.getGoodsListDisplayImageOption();
		
		imageLoader.displayImage(url, imageView, options);
	}
	
	/**
	 * 加载一张商品缩略图(该缩略图在GridView中显示)
	 * 
	 * @param context 上下文
	 * @param url 缩略图图片路径
	 * @param imageView 要显示的ImageView
	 */
	public static void loadGoodsGridViewPictureThumnail(Context context, String url, ImageView imageView) {
		
		if(context == null || imageView == null) {
			return;
		}
		
		ImageLoaderConfiguration configuration = UniversalImageLoaderConfigurationManager
				.getGoodsListPictureThumbnailImageLoaderConfiguration(context.getApplicationContext());
		ImageLoader imageLoader = UniversalImageLoaderManager.getImageLoader(configuration);
		
		DisplayImageOptions options = UniversalImageLoaderOptionManager.getGoodsGridViewDisplayImageOption();
		
		imageLoader.displayImage(url, imageView, options);
	}
}
