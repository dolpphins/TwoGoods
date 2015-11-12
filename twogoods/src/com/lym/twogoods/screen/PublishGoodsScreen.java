package com.lym.twogoods.screen;

import com.lym.twogoods.bean.PictureThumbnailSpecification;

import android.app.Activity;

/**
 * <p>
 * 与发布商品有关的屏幕适配,展示图片的规格
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishGoodsScreen {

	/**
	 * <p>
	 * 获取用户头像缩略图规格,包括宽高等信息.
	 * </p>
	 * 
	 * @param at
	 * 
	 * @return 用户头像缩略图规格
	 * */

	public static PictureThumbnailSpecification getPublishPictureThumbnailSpecification(
			Activity at) {
		if(at==null){
			return null;
		}
		PictureThumbnailSpecification specification=new PictureThumbnailSpecification();
		int screenWidth=DisplayUtils.getScreenWidthPixels(at);
		int pictureLength=screenWidth/4;
		specification.setHeight(pictureLength);
		specification.setWidth(pictureLength);
		return specification;
	}

}
