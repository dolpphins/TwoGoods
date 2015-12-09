package com.lym.twogoods.network;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.bean.Goods;

import android.content.Context;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Bmob更新工具类
 * 
 * @author 麦灿标
 */
public class BmobUpdateHelper {

	/**
	 * 更新指定商品的头像地址信息
	 * 
	 * @param context 上下文
	 * @param goodsList 要更新的商品列表
	 */
	public static void updateHeadUrlForGoods(Context context, List<Goods> goodsList) {
		if(context == null || goodsList == null || goodsList.size() <= 0) {
			return;
		}
		List<BmobObject> batchUpdate = new ArrayList<BmobObject>();
		batchUpdate.addAll(goodsList);
		new BmobObject().updateBatch(context, batchUpdate, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
			}
		});
	}
}
