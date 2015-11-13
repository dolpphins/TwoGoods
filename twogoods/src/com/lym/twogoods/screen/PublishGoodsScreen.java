package com.lym.twogoods.screen;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.publish.manger.PublishConfigManger;

import android.R.integer;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;

/**
 * <p>
 * 与发布商品有关的屏幕适配,展示图片的规格
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishGoodsScreen {

	private String TGA = "PublishGoodsScreen";

	/**
	 * <p>
	 * 获取货品缩略图规格,包括宽高等信息.
	 * </p>
	 * 
	 * @param at
	 * 
	 * @return 货品缩略图规格
	 * */

	public static PictureThumbnailSpecification getPublishPictureThumbnailSpecification(
			Activity at) {
		if (at == null) {
			return null;
		}
		PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
		int screenWidth = DisplayUtils.getScreenWidthPixels(at);
		int gridViewWidth = screenWidth
				- at.getResources()
						.getDimensionPixelSize(
								R.dimen.app_publish_fragment_gridview_left_right_padding)
				* 2;
		int pictureLength = gridViewWidth
				/ (PublishConfigManger.PICTURE_RATE * 3 + 1)
				* PublishConfigManger.PICTURE_RATE;
		specification.setHeight(pictureLength);
		specification.setWidth(pictureLength);
		return specification;
	}

}
