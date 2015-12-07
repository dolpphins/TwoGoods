package com.lym.twogoods.cache;

import java.util.List;

import com.lym.twogoods.local.bean.LocalGoods;

/**
 * 商品缓存接口实现抽象类
 * 
 * @author 麦灿标
 *
 */
public abstract class BaseGoodsDiskCache implements GoodsDiskCache{
	
	@Override
	public boolean reSave(List<LocalGoods> goodsList) {
		if(clear()) {
			if(save(goodsList)) {
				return true;
			}
		}
		return false;
	}
}
