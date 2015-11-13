package com.lym.twogoods.publish.manger;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 发布信息相关配置
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishConfigManger {

	//展示图片最大数
	public final static int PICTURE_COUNT=9;
	//发布图片展示列数
	public final static int PUBLISH_PICTURE_GRIDVIEW_COLUMN=3;
	//启动获取图片activity
	public final static int requestCode=0;
	//图片列表本地url集合
	public final static List<String> publishPictureUrl=new ArrayList<String>();
	//图片列表本地url集合
	public final static List<String> pictureCloudUrl=new ArrayList<String>();
	//图片之间的距离比
	public final static int PICTURE_RATE=6;
}
