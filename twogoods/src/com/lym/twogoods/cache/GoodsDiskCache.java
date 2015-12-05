package com.lym.twogoods.cache;

import java.util.List;

import com.lym.twogoods.local.bean.LocalGoods;

/**
 * 商品缓存接口
 * 
 * @author 麦灿标
 *
 */
public interface GoodsDiskCache {

	/**
	 * 读取所有缓存记录
	 * 
	 * @return 读取成功返回所有缓存记录列表,失败返回null.
	 */
	List<LocalGoods> readAll();
	
	/**
	 * 保存缓存成功返回,该方法采用追加的方式进行保存
	 * 
	 * @param goodsList 要保存的商品信息集合
	 * @return 保存成功返回true，失败返回false.
	 */
	boolean save(List<LocalGoods> goodsList);
	
	/**
	 * 保存缓存成功返回,该方法采用覆盖的方式进行保存
	 * 
	 * @param goodsList 要保存的商品信息集合
	 * @return 保存成功返回true，失败返回false.
	 */
	boolean reSave(List<LocalGoods> goodsList);
	
	/**
	 * 清除缓存
	 * 
	 * @return 清除成功返回true,失败返回false
	 */
	boolean clear();
}
