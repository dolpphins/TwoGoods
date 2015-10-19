package com.lym.twogoods;

import com.lym.twogoods.config.DiskCacheManager;
import com.lym.twogoods.config.UserConfiguration;
import com.lym.twogoods.utils.ImageUtil;

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
	}
	
	//进行一些准备工作
	private void prepare() {
		//复制默认头像到相应的缓存目录中
		Bitmap defaultHeadBmp = BitmapFactory.decodeResource(getResources(), UserConfiguration.USER_DEFAULT_HEAD);
		ImageUtil.saveBitmap(DiskCacheManager.getInstance(this).getUserHeadPictureCachePath(),
				UserConfiguration.USER_DEFAULT_HEAD_NAME, defaultHeadBmp, true);

	}
	
	
}
