package com.lym.twogoods.manager;

import com.lym.twogoods.screen.UserScreen;
import com.lym.twogoods.utils.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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

	/**
	 * 加载用户头像缩略图,如果访问网络失败会自动设置默认头像缩略图
	 * 
	 * @param context 上下文
	 * @param iv 要显示头像缩略图的ImageView控件,不能为null
	 * @param url 头像缩略图的网络url地址
	 * @param listener 加载监听器
	 * */
	public static void loadUserHeadPictureThumnail(Context context, ImageView iv, String url, ImageLoadingListener listener) {
		if(iv == null) {
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
		
		imageLoader.displayImage(url, iv, options, listener);
				
	}
}
