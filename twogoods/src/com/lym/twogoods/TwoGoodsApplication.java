package com.lym.twogoods;

import com.lym.twogoods.config.UserConfiguration;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.manager.UniversalImageLoaderConfigurationManager;
import com.lym.twogoods.manager.UniversalImageLoaderManager;
import com.lym.twogoods.utils.FileUtil;
import com.lym.twogoods.utils.ImageUtil;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * <p>自定义Application</p>
 * 
 * @author 麦灿标
 * */
public class TwoGoodsApplication extends Application{

	@Override
	public void onCreate() {
		super.onCreate();
		
		initConfig();
		
		prepare();
	}
	
	//初始化应用配置
	private void initConfig() {
		//初始化ImageLoader默认配置
		ImageLoaderConfiguration configuration = UniversalImageLoaderConfigurationManager.getDefaultImageLoaderConfiguration(getApplicationContext());
		UniversalImageLoaderManager.initDefaultImageLoader(configuration);
	}
	
	//进行一些准备工作
	private void prepare() {
		//创建文件夹
		DiskCacheManager dcm = DiskCacheManager.getInstance(this);
		try {
			FileUtil.createFolder(dcm.getChatPictureCachePath());
			FileUtil.createFolder(dcm.getChatVoiceCachePath());
			FileUtil.createFolder(dcm.getGoodsPictureCachePath());
			FileUtil.createFolder(dcm.getGoodsVoiceCachePath());
			FileUtil.createFolder(dcm.getUserHeadPictureCachePath());
			FileUtil.createFolder(dcm.getDefaultPictureCachePath());
		} catch(Exception e) {
			e.printStackTrace();
		}
		

	}
	
	
}
