package com.lym.twogoods.publish.manger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;

/**
 * <p>
 * 发布信息相关配置
 * </p>
 * 
 * @author 龙宇文
 * */
public class PublishConfigManger {

	// 展示图片最大数
	public final static int PICTURE_COUNT = 9;
	// 发布图片展示列数
	public final static int PUBLISH_PICTURE_GRIDVIEW_COLUMN = 3;
	// 启动获取图片activity
	public final static int requestCode = 0;
	// 图片列表本地url集合
	public final static List<String> publishPictureUrl = new ArrayList<String>();
	// 图片列表网络url集合
	public final static List<String> pictureCloudUrl = new ArrayList<String>();
	// 发布定位RequestCode
	public final static int PUBLISH_REQUESTCODE = 1;
	// 发布定位的时候，Activity标识的Key
	public final static String publishActivityIdentificationKey = "publishLastActivity";
	// 返回Activity标识的Key
	public final static String publishBackActivityIdentificationKey = "publishBackActivityData";
	// 返回Actiity的ResultCode
	public final static int PUBLISH_RESULT_OK = 0;
	// 语音本地文件路径
	public static String voicePath = "";
	// 语音网络url
	public static String voiceUrl = "";
	// 图片之间的距离比
	public final static int PICTURE_RATE = 6;
	// 货品种类
	public final static List<String> publishGoodsType = new ArrayList<String>(
			Arrays.asList("家用电器", "电子产品", "男装女装", "交通工具", "鞋包配饰", "书籍文体",
					"居家用品", "其它"));
	// 加载器（用于上传或者写文件时显示，减少表面等待时间）
	private static ProgressDialog progressDialog;

	/**
	 * <p>
	 * 获得加载器
	 * </p>
	 * 
	 * @param activity
	 * @param isCancel
	 *            该对话框是否可以取消
	 */
	public static ProgressDialog getLoadProgressDialog(Activity activity,
			String title, String message, boolean isCancel) {
		progressDialog = new ProgressDialog(activity);
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.setCancelable(isCancel);
		progressDialog.setProgress(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(false);
		return progressDialog;
	}
}
