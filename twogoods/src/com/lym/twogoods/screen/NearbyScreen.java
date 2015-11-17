package com.lym.twogoods.screen;

import android.R.integer;
import android.app.Activity;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.PictureThumbnailSpecification;
import com.lym.twogoods.nearby.config.NearbyConfig;

/**
 * <p>
 * 与定位有关的屏幕适配,展示图片的规格
 * </p>
 * 
 * @author 龙宇文
 * */
public class NearbyScreen {

	private String TGA = "NearbyScreen";
	
	/**
	 * <p>
	 *热门城市规格,包括宽高等信息.
	 * </p>
	 * 
	 * @param at
	 * 
	 * @return 货品缩略图规格
	 * */
	public static PictureThumbnailSpecification getHotCityItemThumbnailSpecification(Activity at) {
		if (at == null) {
			return null;
		}
		PictureThumbnailSpecification specification=new PictureThumbnailSpecification();
		int screenWidth=DisplayUtils.getScreenWidthPixels(at);
		int gridView=screenWidth-at.getResources().getDimensionPixelSize(R.dimen.nearby_select_city_marginright)*2;
		int cityWidth=gridView/NearbyConfig.CITY_COLUMNS;
		int cityHeigth=(int) (cityWidth/NearbyConfig.WIDTH_LENGTH_RATE);
		specification.setWidth(cityWidth);
		specification.setHeight(cityHeigth);
		return specification;
	}
}
