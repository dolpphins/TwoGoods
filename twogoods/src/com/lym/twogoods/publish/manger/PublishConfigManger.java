package com.lym.twogoods.publish.manger;

import java.util.ArrayList;

import android.R.integer;

/**
 * <p>
 * 发布信息相关配置
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishConfigManger {

	public final static int picCount=9;
	
	//启动获取图片activity
	public final static int requestCode=0;
	
	//发布照片的路径
	public static String picPath;
	public static ArrayList<String> picsPath=new ArrayList<String>();
}
