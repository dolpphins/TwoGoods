package com.lym.twogoods.message.config;



/**与消息模块相关的消息参数，主要用于handler处理数据*/
public class MessageConfig {

	/**拍照修改头像*/
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;
	/**本地相册修改头像*/
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;
	/**系统裁剪头像*/
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;
	
	/**加载图片完成*/
	public static final int FINISH_LOAD = 4;    
	/**发送图片*/
	public static final int SEND_PICTURE = 5;     
	/**使用相机拍照*/
	public static final int OPEN_CAMERA = 6; 		
	
	/**发送相机拍的照片*/
	public static final int SEND_CAMERA_PIC = 7; 		
	/**发送本地的照片*/
	public static final int SEND_LOCAL_PIC = 8; 	
	
	/**拍照*/
	public static final int REQUESTCODE_TAKE_CAMERA = 1;
	/**本地图片*/
	public static final int REQUESTCODE_TAKE_LOCAL = 2;
	/**位置*/
	public static final int REQUESTCODE_TAKE_LOCATION = 3;
	
	
	/**开始录音*/
	public static final int START_RECORD= 0;   
	/**录音完毕*/
	public static final int FINISH_RECORD = 1;     
	/**放弃录音*/
	public static final int ABANDON_RECORD = 2;     
	
	
	/**消息发送成功*/
	public static final int SEND_MESSAGE_SUCCEED= 0;    
	/**消息发送失败*/
	public static final int SEND_MESSAGE_FAILED = 1;    
	/**正在发送*/
	public static final int SEND_MESSAGE_ING = 2;        
	/**开始发送*/
	public static final int SEND_MESSAGE_START = 3;       
	/**对方已接收*/
	public static final int SEND_MESSAGE_RECEIVERED = 4;        
	
	/**隐藏底部布局*/
	public static final int HIDE_BOTTOM = 10;      
	
	/***/
	public static final String EXTRA_STRING = "extra_string";
	
	
	/**对方已经接收到消息*/
	public static final int MESSAGE_RECEIVED = 0;       
	/**对方还未接收到消息*/
	public static final int MESSAGE_NOT_RECEIVED = 2;    
	 
}
