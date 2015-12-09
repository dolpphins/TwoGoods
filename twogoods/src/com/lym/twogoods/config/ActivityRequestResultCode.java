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
	
	/** 商品列表查看商品详情请求码 */
	public final static int GOODS_ITEM_DETAIL_REQUESTCODE = 4;
	
	/** 我的页面查看更多信息请求码 */
	public final static int MINE_MORE_REQUESTCODE = 5;
	
	/** 我的页面跳转到登录页面请求码 */
	public final static int MINE_LOGIN_REQUESTCODE = 6;
	
	/**
	 * 结果码
	 */
	
	/** 我的更换头像拍照返回结果码 */
	public final static int MINE_HEAD_PICTURE_TAKE_PHOTO_RESULTCODE = 1000;
	
	/** 我的更换头像裁切返回结果码 */
	public final static int MINE_HEAD_PICTURE_CROP_PHOTO_RESULTCODE = 1001;
	
	/** 我的更换头像相册返回结果码 */
	public final static int MINE_HEAD_PICTURE_GALLERY_PHOTO_RESULTCODE = 1002;
	
	/** 商品列表查看商品详情返回结果码 */
	public final static int GOODS_ITEM_DETAIL_RESULTCODE = 1003;
	
	/** 我的页面查看更多信息结果码 */
	public final static int MINE_MORE_RESULTCODE = 1004;
	
}
