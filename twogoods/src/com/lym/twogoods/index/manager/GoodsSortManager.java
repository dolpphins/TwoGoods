package com.lym.twogoods.index.manager;

import com.lym.twogoods.R;

import android.content.Context;
import android.content.res.Resources;

/**
 * <p>
 * 	商品排序管理类
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsSortManager {

	/**
	 * <p>
	 * 	商品排序方式枚举
	 * </p>
	 * */
	public enum GoodsSort {
		
		/** 最新发布*/
		NEWEST_PUBLISH,
		
		/** 价格从低到高 */
		PRICE_ASC,
		
		/** 最多关注 */
		MOST_FOCUS,
		
		/** 最多浏览 */
		MOST_BROWSE
	}
	
	/**
	 * <p>
	 * 	通过排序方式枚举获取对应的字符串
	 * </p>
	 * 
	 * @param context 上下文
	 * @param gs 排序方式枚举
	 * 
	 * @return 获取成功返回相应的字符串,失败返回null.
	 * */
	public static String getString(Context context, GoodsSort gs) {
		Resources res = context.getResources();
		
		switch (gs) {
		case NEWEST_PUBLISH:
			return res.getString(R.string.newst_publish);
		case PRICE_ASC:
			return res.getString(R.string.price_asc);
		case MOST_FOCUS:
			return res.getString(R.string.most_focus);
		case MOST_BROWSE:
			return res.getString(R.string.most_browse);
		default:
			return null;
		}
	}
}
