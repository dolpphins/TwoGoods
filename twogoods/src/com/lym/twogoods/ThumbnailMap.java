package com.lym.twogoods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.j256.ormlite.dao.Dao;
import com.lym.twogoods.bean.ThumbnailBean;
import com.lym.twogoods.db.OrmDatabaseHelper;

import android.content.Context;
import android.text.TextUtils;

/**
 * 缩略图Map,为解决Bmob无法直接得到缩略图可访问路径
 * 注意:在应用程序开始时必须调用{@link #init()}方法进行初始化,该方法会
 * 从数据库中读取数据,在应用程序退出时必须调用{@link #save()}方法将数据
 * 保存到数据库中.
 * 
 * @author 麦灿标
 *
 */
public class ThumbnailMap {

	/** 键:Bmob File 值:访问路径 */
	private final static Map<String, String> thumbnailMap = new HashMap<String, String>();
	
	/**
	 * 初始化ThumbnailMap,你必须在应用程序启动时调用该方法进行初始化.
	 * 
	 * @param context 上下文
	 */
	public static void init(Context context) {
		OrmDatabaseHelper helper = new OrmDatabaseHelper(context); 
		Dao<ThumbnailBean, Integer> dao = helper.getThumbnailBeanDao();
		try {
			List<ThumbnailBean> list = dao.queryForAll();
			for(ThumbnailBean item : list) {
				thumbnailMap.put(item.getBmobFileName(), item.getVisitUrl());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 保存ThumbnailMap到数据库中,你必须在应用程序退出时调用该方法将数据
	 * 保存到数据中.
	 * 
	 * @param context 上下文
	 */
	public static void save(Context context) {
		OrmDatabaseHelper helper = new OrmDatabaseHelper(context);
		if(!helper.clearTable(ThumbnailBean.class)) {
			return;
		}
		Set<Map.Entry<String, String>> set = thumbnailMap.entrySet();
		if(set != null && set.size() > 0) {
			Dao<ThumbnailBean, Integer> dao = helper.getThumbnailBeanDao();
			if(dao == null) {
				return;
			}
			for(Map.Entry<String, String> entry : set) {
				ThumbnailBean item = new ThumbnailBean();
				item.setBmobFileName(entry.getKey());
				item.setVisitUrl(entry.getValue());
				try {
					dao.create(item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 判断是否包含指定的键值对
	 * 
	 * @param key 指定的键,不能为空
	 * @return 包含指定的键值对返回true,否则返回false.
	 */
	public static boolean contain(String key) {
		if(TextUtils.isEmpty(key)) {
			return false;
		}
		return thumbnailMap.containsKey(key);
	}
	
	/**
	 * 获取指定键对应的值
	 * 
	 * @param key 指定的键,不能为空
	 * @return 存在返回相应的值,不存在返回null.
	 */
	public static String getValue(String key) {
		if(contain(key)) {
			return thumbnailMap.get(key);
		} else {
			return null;
		}
	}
	
	/**
	 * 添加指定的键值对
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * @return 添加成功返回true,否则返回false.
	 */
	public static synchronized boolean put(Context context, String key, String value) {
		return put(context, key, value, false);
	}
	
	/**
	 * 添加指定的键值对
	 * 
	 * @param context 上下文
	 * @param key 键
	 * @param value 值
	 * @param force true表示马上进行持久化
	 * @return 添加成功返回true,否则返回false.
	 */
	public static synchronized boolean put(Context context, String key, String value, boolean force) {
		if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
			return false;
		}
		thumbnailMap.put(key, value);
		if(force) {
			return saveOne(context, key, value);
		}
		return true;
	}
	
	//添加一条记录到数据库,key重复的会被覆盖掉
	private static boolean saveOne(Context context, String key, String value) {
		if(context == null) {
			return false;
		}
		try {
			ThumbnailBean item = new ThumbnailBean();
			item.setBmobFileName(key);
			item.setVisitUrl(value);
			OrmDatabaseHelper helper = new OrmDatabaseHelper(context);
			Dao<ThumbnailBean, Integer> dao = helper.getThumbnailBeanDao();
			List<ThumbnailBean> list = dao.queryForEq(ThumbnailBean.getDbBmobFileNameColumnName(), key);
			if(list != null) {
				if(list.size() > 0) {
					dao.delete(list);
				}
			}
			dao.create(item);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
