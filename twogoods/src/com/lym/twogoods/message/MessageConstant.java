package com.lym.twogoods.message;

public class MessageConstant {

	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像
	
	public static final int FINISH_LOAD = 4;     // 加载图片完成
	public static final int SEND_PICTURE = 5;      //发送图片
	public static final int OPEN_CAMERA = 6; 		//使用相机拍照
	
	public static final int SEND_CAMERA_PIC = 7; 		//发送相机拍的照
	public static final int SEND_LOCAL_PIC = 8; 		//发送本地的照片
	
	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//位置
	
	public static final String EXTRA_STRING = "extra_string";
	
	
}
