package com.lym.twogoods.utils;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 图片相关工具类
 * 
 * @author mao
 *
 */
public class PicturesHelper {

	/**
	 * 调用系统相机拍照
	 * 
	 * @param activity 当前的Activity对象
	 * @param filePath 拍照后保存的文件夹路径,为null表示不保存
	 * @param fileName 文件名
	 * @param requestCode 请求码
	 * */
	public static void takePhotoByCamera(Activity activity,String filePath,String fileName,int requestCode)
	{
		if(activity == null)
		{
			return;
		}
		
		try
		{
			File file = new File(Environment.getExternalStorageDirectory(),filePath);
			if(!file.exists())
			{
				boolean isSuccess = file.mkdirs();
				if(!isSuccess) {
					return;
				}
			}
			file = new File(Environment.getExternalStorageDirectory(),filePath + fileName);
			Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			Uri uri = Uri.fromFile(file);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			activity.startActivityForResult(intent, requestCode);
		}
		catch(ActivityNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * 调用系统裁切功能
	 * 
	 * @param activity 当前的Activity对象
	 * @param uri 文件Uri对象
	 * @param requestCode 请求码
	 * */
	public static void crop(Activity activity,Uri uri,int requestCode) {
		
		if(activity == null)
		{
			return;
		}
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		
		intent.putExtra("outputX", 250);
		intent.putExtra("outputY", 250);
		
		intent.putExtra("outputFormat", "JPEG");
		intent.putExtra("noFaceDetection", true);
		intent.putExtra("return-data", true);
		activity.startActivityForResult(intent, requestCode);
	}
	
	/**
	 * 调用相册选择图片
	 * 
	 * @param activity 当前的Activity对象
	 * @param filePath 文件夹路径,为null表示不保存
	 * @param fileName 文件名
	 * @param requestCode 请求码
	 * */
	public static void takePhotoByGallery(Activity activity,String filePath,String fileName,int requestCode)
	{
		if(activity == null)
		{
			return;
		}
		try
		{
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType("image/*");
			activity.startActivityForResult(intent, requestCode);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
