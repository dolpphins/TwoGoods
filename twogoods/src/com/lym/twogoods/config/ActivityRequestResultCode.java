package com.lym.twogoods.config;

/**
 * Activty请求码,结果码配置
 *  
 */
public class ActivityRequestResultCode {

	/**
	 * 请求码
	 */
	
	/** 我的更换头像拍照返回请求码 */
	public final static int MINE_HEAD_PICTURE_TAKE_PHOTO_REQUESTCODE = 1;
	
	/** 我的更换头像裁切返回请求码 */
	public final static int MINE_HEAD_PICTURE_CROP_PHOTO_REQUESTCODE = 2;
	
	/** 我的更换头像相册返回请求码 */
	public final static int MINE_HEAD_PICTURE_GALLERY_PHOTO_REQUESTCODE = 3;
	
	/**
	 * 结果码
	 */
	
	/** 我的更换头像拍照返回结果码 */
	public final static int MINE_HEAD_PICTURE_TAKE_PHOTO_RESULTCODE = 1000;
	
	/** 我的更换头像裁切返回结果码 */
	public final static int MINE_HEAD_PICTURE_CROP_PHOTO_RESULTCODE = 1001;
	
	/** 我的更换头像相册返回结果码 */
	public final static int MINE_HEAD_PICTURE_GALLERY_PHOTO_RESULTCODE = 1002;
	
}
