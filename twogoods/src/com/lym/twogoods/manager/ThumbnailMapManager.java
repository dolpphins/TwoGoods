package com.lym.twogoods.manager;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.GetAccessUrlListener;
import com.bmob.btp.callback.ThumbnailListener;
import com.lym.twogoods.ThumbnailMap;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import cn.bmob.v3.datatype.BmobFile;

/**
 * ThumbnailMap管理类,加载商品缩略图工具类
 * 
 * @author 麦灿标
 *
 */
public class ThumbnailMapManager {

	public static enum DisplayType {
		ListViewType, GridViewType
	}
	
	/**
	 * 加载商品图片缩略图(ListView显示)
	 * 
	 * @param context 上下文,不能为null
	 * @param bmobFileName Bmob文件名
	 * @param imageView 要显示的ImageView
	 */
	public static void loadGoodsPictureThumnail(final Context context, final String bmobFileName, final ImageView imageView, final DisplayType type) {
		if(context == null || imageView == null || type == null) {
			return;
		}
		if(TextUtils.isEmpty(bmobFileName)) {
			startLoadGoodsListPictureThumnail(context, null, imageView, type);
			return;
		}
		String cacheUrl = ThumbnailMap.getValue(bmobFileName);
		if(!TextUtils.isEmpty(cacheUrl)) {
			startLoadGoodsListPictureThumnail(context, cacheUrl, imageView, type);
			return;
		}
		
		BmobProFile.getInstance(context.getApplicationContext()).submitThumnailTask(bmobFileName, 2, new ThumbnailListener() {
			
			@Override
			public void onError(int statuscode, String errormsg) {
				startLoadGoodsListPictureThumnail(context, ThumbnailMap.getValue(bmobFileName), imageView, type);
			}
			
			@Override
			public void onSuccess(String thumbnailName,String thumbnailUrl) {
				
				BmobProFile.getInstance(context.getApplicationContext()).getAccessURL(thumbnailName, new GetAccessUrlListener() {
					
					@Override
					public void onError(int arg0, String arg1) {
						startLoadGoodsListPictureThumnail(context, ThumbnailMap.getValue(bmobFileName), imageView, type);
					}
					
					@Override
					public void onSuccess(BmobFile file) {
						if(file != null) {
							startLoadGoodsListPictureThumnail(context, file.getUrl(), imageView, type);
							ThumbnailMap.put(context, bmobFileName, file.getUrl(), true);
						} else {
							startLoadGoodsListPictureThumnail(context, ThumbnailMap.getValue(bmobFileName), imageView, type);
						}
					}
				});
				
			}
		});
	}
	
	private static void startLoadGoodsListPictureThumnail(Context context, String url, ImageView iv, DisplayType type) {
		if(type == null) {
			return;
		}
		switch (type) {
		case ListViewType:
			ImageLoaderHelper.loadGoodsListPictureThumnail(context, url, iv);
			break;
		case GridViewType:
			ImageLoaderHelper.loadGoodsGridViewPictureThumnail(context, url, iv);
			break;
		}
		
	}
}
