package com.lym.twogoods.cache.impl;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.DatabaseConnection;
import com.lym.twogoods.cache.BaseGoodsDiskCache;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.local.bean.LocalGoods;

import android.content.Context;

/**
 * 基于数据库的缓存类
 * 
 * @author 麦灿标
 *
 */
public class DBGoodsDiskCache extends BaseGoodsDiskCache{

	private Context mContext;
	
	private OrmDatabaseHelper dbHelper;
	
	public DBGoodsDiskCache(Context context) {
		mContext = context;
		dbHelper = new OrmDatabaseHelper(mContext);
	}
	
	@Override
	public List<LocalGoods> readAll() {
		Dao<LocalGoods, Integer> dao = dbHelper.getGoodsDao();
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean save(List<LocalGoods> goodsList) {
		if(goodsList == null) {
			return false;
		}
		Dao<LocalGoods, Integer> dao = dbHelper.getGoodsDao();
		DatabaseConnection dc = null;
		try {
			dc = dao.startThreadConnection();
			dao.setAutoCommit(dc, false);
			for(LocalGoods goods : goodsList) {
				dao.create(goods);
			}
			dao.commit(dc);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				dao.rollBack(dc);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

	@Override
	public boolean clear() {
		return dbHelper.clearTable(LocalGoods.class);
	}


}
