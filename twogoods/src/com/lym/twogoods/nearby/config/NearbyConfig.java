package com.lym.twogoods.nearby.config;

import com.lym.twogoods.nearby.ui.SelectCityActivity;

/**
 * <p>
 * 选择城市相关配置
 * </p>
 * 
 * @author 龙宇文
 * */

public class NearbyConfig {

	// 一行显示多少个地点
	public final static int CITY_COLUMNS = 4;
	// 每个位置的长宽比例
	public final static double WIDTH_LENGTH_RATE = 1.33;
	// 默认加载16个地方
	public final static int HOT_CITY_COUNT = 16;
	// 标识启动SelectCityActivity是哪一个Activity，true是PublishGoosActivity，fals是SelectCityActivity。
	public static boolean LAUNCH_ACTIVITY = false;
}
