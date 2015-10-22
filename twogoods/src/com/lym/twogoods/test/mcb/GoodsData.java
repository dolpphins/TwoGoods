package com.lym.twogoods.test.mcb;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.UserConfiguration;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.utils.DatabaseHelper;

import android.content.Context;

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
		item1.setPic_baseurl(DiskCacheManager.getInstance(context).getGoodsPictureCachePath());
		item1.setPic_prefix("abc");
		item1.setPic_num(5);
		
		Goods item2 = new Goods();
		item2.setUsername("user1");
		item2.setDescription("小米手机");
		item2.setGUID(DatabaseHelper.getUUID().toString());
		item2.setHead_url(DiskCacheManager.getInstance(context).getUserHeadPictureCachePath() + "/" + UserConfiguration.USER_DEFAULT_HEAD_NAME);
		item2.setPrice(123);
		item2.setPublish_location("广州市天河区");
		item2.setPublish_time(System.currentTimeMillis() - 7200);
		item2.setPic_baseurl(DiskCacheManager.getInstance(context).getGoodsPictureCachePath());
		item2.setPic_prefix("abc");
		item2.setPic_num(2);
		
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
		item4.setPic_baseurl(DiskCacheManager.getInstance(context).getGoodsPictureCachePath());
		item4.setPic_prefix("abc");
		item4.setPic_num(6);
		
		goodsList.add(item1);
		goodsList.add(item2);
		goodsList.add(item3);
		goodsList.add(item4);
		
		for(int i = 0; i < 10; i++) {
			Goods item = new Goods();
			item.setUsername("user1");
			item.setDescription("小米手机");
			item.setGUID(DatabaseHelper.getUUID().toString());
			item.setHead_url(DiskCacheManager.getInstance(context).getUserHeadPictureCachePath() + "/" + UserConfiguration.USER_DEFAULT_HEAD_NAME);
			item.setPrice(123);
			item.setPublish_location("广州市天河区");
			item.setPublish_time(System.currentTimeMillis() - 7200);
			item.setPic_baseurl(DiskCacheManager.getInstance(context).getGoodsPictureCachePath());
			item.setPic_prefix("abc");
			item.setPic_num(6);
			goodsList.add(item);
		}
		
		return goodsList;
	}
}
