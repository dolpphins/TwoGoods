package com.lym.twogoods.screen;

import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.utils.ImageUtil;

import android.app.Activity;
import android.graphics.Rect;

/**
 * <p>
 * 	与商品有关的屏幕适配,包括头像缩略图宽高等
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsScreen {

	/**
	 * <p>
	 * 	获取用户头像缩略图信息,包括宽高等信息.
	 * </p>
	 * 
	 * @param at
	 * 
	 * @return 用户头像缩略图信息
	 * */
	public static PictureThumbnailSpecification getUserHeadPictureThumbnailSpecification(Activity at) {
		if(at == null) {
			return null;
		}
		PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
		specification.setWidth(ImageUtil.dp2px(at, 45));
		specification.setHeight(ImageUtil.dp2px(at, 45));
		return specification;
	}
}
