package com.lym.twogoods.screen;

import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.utils.ImageUtil;

import android.app.Activity;
import android.content.Context;

/**
 * <p>
 * 	与商品有关的屏幕适配,包括头像缩略图宽高等
 * </p>
 * <p>
 * 	注意：你必须在应用启动时调用init方法进行初始化
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsScreen {

	/** 屏幕宽度 */
	private static int SCREEN_WIDTH;
	
	/** 屏幕高度 */
	private static int SCREEN_HEIGHT;
	
	public static void init(Activity activity) {
		SCREEN_WIDTH = DisplayUtils.getScreenWidthPixels(activity);
		SCREEN_HEIGHT = DisplayUtils.getScreenHeightPixels(activity);
	}
	
	/**
	 * <p>
	 * 	获取用户头像缩略图规格,包括宽高等信息.
	 * </p>
	 * 
	 * @param at
	 * 
	 * @return 用户头像缩略图规格
	 * */
	public static PictureThumbnailSpecification getUserHeadPictureThumbnailSpecification(Context context) {
		if(context == null) {
			return null;
		}
		PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
		specification.setWidth(ImageUtil.dp2px(context, 45));
		specification.setHeight(ImageUtil.dp2px(context, 45));
		return specification;
	}
	
	/**
	 * <p>
	 * 	获取首页商品列表图片缩略图规格
	 * </p>
	 *  
	 *  @return 商品列表图片缩略图规格
	 * */
	public static PictureThumbnailSpecification getIndexGoodsPictureThumbnailSpecification() {
		PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
		int sideLength = SCREEN_WIDTH * 2 / 5;
		specification.setWidth(sideLength);
		specification.setHeight(sideLength);
		return specification;
	}
	
	/**
	 * <p>
	 * 	获取附近商品图片缩略图规格
	 * </p>
	 * 
	 *  @param at
	 *  
	 *  @return 商品列表图片缩略图规格
	 * */
	public static PictureThumbnailSpecification getNearbyGoodsPictureThumbnailSpecification(int horizontalExtraDistance) {
		PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
		int sideLength = (SCREEN_WIDTH - horizontalExtraDistance) / 2;
		specification.setWidth(sideLength);
		specification.setHeight(sideLength);
		return specification;
	}
	
	
}
