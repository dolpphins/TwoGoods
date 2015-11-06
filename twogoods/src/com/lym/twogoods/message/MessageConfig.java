package com.lym.twogoods.message;



/**与消息模块相关的消息参数，主要用于handler处理数据*/
public class MessageConfig {

	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像
	
	public static final int FINISH_LOAD = 4;     // 加载图片完成
	public static final int SEND_PICTURE = 5;      //发送图片
	public static final int OPEN_CAMERA = 6; 		//使用相机拍照
	
	public static final int SEND_CAMERA_PIC = 7; 		//发送相机拍的照
	public static final int SEND_LOCAL_PIC = 8; 		//发送本地的照片
	
	public static final int REQUESTCODE_TAKE_CAMERA = 1;//拍照
	public static final int REQUESTCODE_TAKE_LOCAL = 2;//本地图片
	public static final int REQUESTCODE_TAKE_LOCATION = 3;//位置
	
	
	public static final int START_RECORD= 0;     // 开始录音
	public static final int FINISH_RECORD = 1;      //录音完毕
	public static final int ABANDON_RECORD = 2;      //录音完毕
	
	
	public static final int SEND_MESSAGE_SUCCEED= 0;     // 消息发送成功
	public static final int SEND_MESSAGE_FAILED = 1;     //消息发送失败
	public static final int SEND_MESSAGE_ING = 2;        //正在发送
	public static final int SEND_MESSAGE_START = 3;        //开始发送
	public static final int SEND_MESSAGE_RECEIVERED = 4;        //对方已接收
	
	public static final int HIDE_BOTTOM = 10;        //隐藏底部布局
	
	public static final String EXTRA_STRING = "extra_string";
	
	
}
