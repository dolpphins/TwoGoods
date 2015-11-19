package com.lym.twogoods.screen;

import android.app.Activity;
import android.util.Log;

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

	private static String TGA = "NearbyScreen";

	/**
	 * <p>
	 * 热门城市规格,包括宽高等信息.
	 * </p>
	 * 
	 * @param at
	 * 
	 * @return 货品缩略图规格
	 * */
	public static PictureThumbnailSpecification getHotCityItemThumbnailSpecification(
			Activity at) {
		if (at == null) {
			return null;
		}
		PictureThumbnailSpecification specification = new PictureThumbnailSpecification();
		int screenWidth = DisplayUtils.getScreenWidthPixels(at);
		Log.v(TGA, "screenWidth="+screenWidth);
		int gridView = screenWidth
				- at.getResources().getDimensionPixelSize(
						R.dimen.nearby_select_city_marginright) * 2 -NearbyConfig.CITY_COLUMNS+1;
		Log.v(TGA, "gridView="+gridView);
		int cityWidth = gridView / NearbyConfig.CITY_COLUMNS;
		Log.v(TGA, "cityWidth="+cityWidth);
		int cityHeigth = (int) (cityWidth / NearbyConfig.WIDTH_LENGTH_RATE);
		Log.v(TGA, "cityHeigth="+cityHeigth);
		specification.setWidth(cityWidth);
		specification.setHeight(cityHeigth);
		return specification;
	}
}
