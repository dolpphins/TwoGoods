package com.lym.twogoods.test.mcb;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.DiskCacheManager;
import com.lym.twogoods.config.UserConfiguration;
import com.lym.twogoods.utils.DatabaseHelper;

import android.content.Context;
import android.database.DatabaseUtils;

public class GoodsData {

	public static List<Goods> getGoodsData(Context context) {
		List<Goods> goodsList = new ArrayList<Goods>();	
		
		Goods item1 = new Goods();
		item1.setUsername("user1");
		item1.setDescription("小米手机");
		item1.setGUID(DatabaseHelper.getUUID().toString());
		item1.setHead_url(DiskCacheManager.getInstance(context).getUserHeadPictureCachePath() + "/" + UserConfiguration.USER_DEFAULT_HEAD_NAME);
		item1.setPrice(123);
		item1.setPublish_location("广州市天河区");
		item1.setPublish_time(System.currentTimeMillis() - 7200);
		
		Goods item2 = new Goods();
		item2.setUsername("user1");
		item2.setDescription("小米手机");
		item2.setGUID(DatabaseHelper.getUUID().toString());
		item2.setHead_url(DiskCacheManager.getInstance(context).getUserHeadPictureCachePath() + "/" + UserConfiguration.USER_DEFAULT_HEAD_NAME);
		item2.setPrice(123);
		item2.setPublish_location("广州市天河区");
		item2.setPublish_time(System.currentTimeMillis() - 7200);
		
		Goods item3 = new Goods();
		item3.setUsername("user1");
		item3.setDescription("小米手机");
		item3.setGUID(DatabaseHelper.getUUID().toString());
		item3.setHead_url(DiskCacheManager.getInstance(context).getUserHeadPictureCachePath() + "/" + UserConfiguration.USER_DEFAULT_HEAD_NAME);
		item3.setPrice(123);
		item3.setPublish_location("广州市天河区");
		item3.setPublish_time(System.currentTimeMillis() - 7200);
		
		Goods item4 = new Goods();
		item4.setUsername("user1");
		item4.setDescription("小米手机");
		item4.setGUID(DatabaseHelper.getUUID().toString());
		item4.setHead_url(DiskCacheManager.getInstance(context).getUserHeadPictureCachePath() + "/" + UserConfiguration.USER_DEFAULT_HEAD_NAME);
		item4.setPrice(123);
		item4.setPublish_location("广州市天河区");
		item4.setPublish_time(System.currentTimeMillis() - 7200);
		
		goodsList.add(item1);
		goodsList.add(item2);
		goodsList.add(item3);
		goodsList.add(item4);
		
		return goodsList;
	}
}
