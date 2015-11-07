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
		ArrayList<String> picturesUrlList = new ArrayList<String>();
		picturesUrlList.add("http://img3.imgtn.bdimg.com/it/u=4031489709,1229250988&fm=21&gp=0.jpg");
		picturesUrlList.add("http://img3.imgtn.bdimg.com/it/u=2350191774,2043707413&fm=21&gp=0.jpg");
		item1.setPictureUrlList(picturesUrlList);
		
		Goods item2 = new Goods();
		item2.setUsername("user1");
		item2.setDescription("小米手机");
		item2.setGUID(DatabaseHelper.getUUID().toString());
		item2.setHead_url(DiskCacheManager.getInstance(context).getUserHeadPictureCachePath() + "/" + UserConfiguration.USER_DEFAULT_HEAD_NAME);
		item2.setPrice(123);
		item2.setPublish_location("广州市天河区");
		item2.setPublish_time(System.currentTimeMillis() - 7200);
		picturesUrlList = new ArrayList<String>();
		picturesUrlList.add("http://img4.imgtn.bdimg.com/it/u=4184347257,1846405169&fm=21&gp=0.jpg");
		picturesUrlList.add("http://fun.datang.net/uploadpic/c34fab3ba8b04dc2a47e73e622926a6f.jpg");
		picturesUrlList.add("http://img3.imgtn.bdimg.com/it/u=2372246127,2834803378&fm=21&gp=0.jpg");
		picturesUrlList.add("http://img0.imgtn.bdimg.com/it/u=646289101,591518669&fm=21&gp=0.jpg");
		item2.setPictureUrlList(picturesUrlList);
		
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
		picturesUrlList = new ArrayList<String>();
		picturesUrlList.add("http://d.hiphotos.baidu.com/zhidao/pic/item/472309f790529822090ecdf3d5ca7bcb0a46d4c5.jpg");
		item4.setPictureUrlList(picturesUrlList);
		
		goodsList.add(item1);
		goodsList.add(item2);
		goodsList.add(item3);
		goodsList.add(item4);
		
		return goodsList;
	}
}
