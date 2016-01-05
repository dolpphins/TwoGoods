package com.lym.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lym.twogoods.R;
import com.lym.twogoods.manager.ImageLoaderHelper;
import com.lym.twogoods.network.BmobQueryHelper;
import com.lym.twogoods.network.BmobQueryHelper.OnUsername2HeadPictureListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

/**
 * 统一的头用户像加载器
 * 
 * @author 麦灿标
 *
 */
public class HeadPictureLoader {

	private final static HeadPictureLoader sLoader = new HeadPictureLoader(); 
	
	//头像用户名与url映射缓存,key:username value:url
	private final static Map<String, String> sHeadPictureMap = new LinkedHashMap<String, String>();
	
	private HeadPictureLoader() {}
	
	public static HeadPictureLoader getInstance() {
		return sLoader;
	}
	
	public void submit(final Context context, List<HeadPictureTask> tasks, final OnLoadHeadPictureUrlFinishListener listener) {
		if(tasks == null || tasks.size() <= 0) {
			return;
		}
		Map<String, List<ImageView>> tasksMap = trimTasks(tasks);
		if(tasksMap.size() > 0) {
			//开始执行
			Set<Map.Entry<String, List<ImageView>>> entrys = tasksMap.entrySet();
			for(final Map.Entry<String, List<ImageView>> entry : entrys) {
				//先从缓存中查找
				if(sHeadPictureMap.containsKey(entry.getKey())) {
					String url = sHeadPictureMap.get(entry.getKey());
					ImageLoaderHelper.loadUserHeadPictureThumnail(context, url, entry.getValue(), null);
					if(listener != null) {
						listener.onFinish(entry.getKey(), url);
					}
					continue;
				}
				//没有缓存再通过网络获取
				BmobQueryHelper.queryHeadPictureByUsername(context, entry.getKey(), new OnUsername2HeadPictureListener() {
					
					@Override
					public void onSuccess(String headUrl) {
						//System.out.println(headUrl);
						ImageLoaderHelper.loadUserHeadPictureThumnail(context, headUrl, entry.getValue(), null);
						//添加到缓存中
						if(!TextUtils.isEmpty(entry.getKey()) && !TextUtils.isEmpty(headUrl)) {
							sHeadPictureMap.put(entry.getKey(), headUrl);
						}
						if(listener != null) {
							listener.onFinish(entry.getKey(), headUrl);
						}
					}
					
					@Override
					public void onError(int errorcode, String errormsg) {
					}
				});
			}
		}
	}
	
	//去重优化,Map key:username value:ImageView List
	private Map<String, List<ImageView>> trimTasks(List<HeadPictureTask> tasks) {
		Map<String, List<ImageView>> map = new HashMap<String, List<ImageView>>();
		for(HeadPictureTask task : tasks) {
			String username = task.username;
			ImageView iv = task.getIv();
			if(task != null && !TextUtils.isEmpty(username) && iv != null) {
				if(map.containsKey(username)) {
					map.get(username).add(iv);
				} else {
					List<ImageView> list = new ArrayList<ImageView>(2);
					list.add(iv);
					map.put(username, list);
				}
			}
		}
		return map;
	}
	
	public boolean tryLoadFromMenoryCache(Context context, String username, ImageView imageView) {
		if(context != null && !TextUtils.isEmpty(username) && sHeadPictureMap.containsKey(username)) {
			String url = sHeadPictureMap.get(username);
			ImageLoaderHelper.loadUserHeadPictureThumnail(context, url, imageView);
			return true;
		}
		return false;
	}
	
	public static class HeadPictureTask {
		
		private String GUID;
		private String username;
		private ImageView iv;
		
		public HeadPictureTask() {}
		
		public HeadPictureTask(String GUID, String username, ImageView iv) {
			this.GUID = GUID;
			this.username = username;
			this.iv = iv;
		}
		
		public String getGUID() {
			return GUID;
		}
		public void setGUID(String gUID) {
			GUID = gUID;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		public ImageView getIv() {
			return iv;
		}
		public void setIv(ImageView iv) {
			this.iv = iv;
		}
	}
	
	/**
	 * 头像Url获取回调接口
	 * 
	 * @author mao
	 *
	 */
	public interface OnLoadHeadPictureUrlFinishListener {
		
		/**
		 * 当指定用户名对应的头像url获取完成后调用该方法
		 * 
		 * @param username 用户名
		 * @param url 头像url,可能为null
		 */
		void onFinish(String username, String url);
	}
}
