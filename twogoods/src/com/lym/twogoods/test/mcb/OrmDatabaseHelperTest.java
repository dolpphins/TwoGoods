package com.lym.twogoods.test.mcb;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.db.OrmDatabaseHelper;

import android.content.Context;

/**
 * <p>
 * 	OrmDatabaseHelper测试类
 * </p>
 * 
 * */
public class OrmDatabaseHelperTest {

	public static void testDao(Context context) {
		OrmDatabaseHelper helper = new OrmDatabaseHelper(context);
		Dao<User, Integer> userDao = helper.getUserDao();
		User user = new User();
		user.setPhone("15603005716");
		try {
			userDao.create(user);
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
