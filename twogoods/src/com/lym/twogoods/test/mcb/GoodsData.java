package com.lym.twogoods.test.mcb;

import java.util.List;

import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.utils.DatabaseHelper;

import android.database.DatabaseUtils;

public class GoodsData {

	public static List<Goods> getGoodsData() {
			
		Goods item1 = new Goods();
		item1.setUsername("user1");
		item1.setDescription("小米手机");
		item1.setGUID(DatabaseHelper.getUUID().toString());
		item1.setHead_url("");
		item1.setPrice(123);
		item1.setPublish_location("广州市天河区");
		item1.setPublish_time(7200);
		
		
		return null;
	}
}
