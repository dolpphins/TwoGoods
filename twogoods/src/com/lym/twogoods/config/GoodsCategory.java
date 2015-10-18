package com.lym.twogoods.config;

import com.lym.twogoods.R;

import android.content.Context;
import android.content.res.Resources;

/**
 * <p>
 * 	商品分类管理类
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsCategory {

	/**
	 * <p>商品分类枚举</p>
	 * 
	 * */
	public enum Category {
		
		/** 全部 */
		ALL,
		
		/** 家用电器 */
		ElECTRIC_APPLIANCE,
		
		/** 电子产品 */
		ElECTRIC_PRODUCT,
		
		/** 男装女装 */
		CLOTHING,
		
		/** 交通工具 */
		VEHICLE,
		
		/** 鞋包配饰 */
		DECORATION,
		
		/** 书籍文体 */
		BOOK,
		
		/** 居家用品 */
		HOUSEHOLD,
		
		/** 其它 */
		OTHERS
	}
	
	/**
	 * <p>
	 *  通过分类枚举获取对应的字符串
	 * </p>
	 * 
	 * @param context 上下文
	 * @param category 类别
	 * 
	 * @return 获取成功返回相应的字符串,失败返回null.
	 * */
	public static String getString(Context context, Category category) {
		Resources res = context.getResources();
		
		switch (category) {
		case ALL:
			return res.getString(R.string.category_all);
		case ElECTRIC_APPLIANCE:
			return res.getString(R.string.electric_appliance);
		case ElECTRIC_PRODUCT:
			return res.getString(R.string.electric_product);
		case CLOTHING:
			return res.getString(R.string.clothing);
		case VEHICLE:
			return res.getString(R.string.vehicle);
		case DECORATION:
			return res.getString(R.string.decoration);
		case BOOK:
			return res.getString(R.string.book);
		case HOUSEHOLD:
			return res.getString(R.string.household);
		case OTHERS:
			return res.getString(R.string.others);
		default:
			return null;
		}
	}
}
