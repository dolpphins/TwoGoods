package com.lym.twogoods.db;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.GoodsComment;
import com.lym.twogoods.bean.GoodsFocus;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.local.bean.LocalGoods;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;


/**
 * <p>
 * 	数据库操作帮助类,该类继承自{@link OrmLiteSqliteOpenHelper},使用的是ORM开源
 * 	框架Ormlite.
 * </p>
 * <p>
 * 	该数据库帮助类提供了一系列的getXXXDao方法,用于对相应的数据库表进行操作,可以操作的数据库表包括:
 * 	<li>
 * 		user表
 *  </li>
 *  <li>
 * 		localgoods表
 *  </li>
 *  <li>
 * 		goods_comment表
 *  </li>
 *  <li>
 *  	goods_focus表
 *  </li>
 *  <li>
 *  	chat_detail表
 *  </li>
 *  <li>
 *  	chat_snapshot表
 *  </li>
 *	<pre>
 *	Example:											
 *		OrmDatabaseHelper helper = new OrmDatabaseHelper(); 
 *		User user = new User();<br />
 *		Dao<User, Integer> userDao = helper.getUserDao();   
 *		try {												
 *			userDao.create(user);							
 *  	} catch(Exception e) {								
 *		e.printStackTrace();							
 *	}													
 *	</pre>
 * </p>
 * 
 * @see #getUserDao()
 * @see #getGoodsDao()
 * @see #getGoodsCommentDao()
 * @see #getGoodsFocusDao()
 * @see #getChatDetailDao()
 * @see #getChatSnapshotDao()
 * 
 * @author 麦灿标
 * */
public class OrmDatabaseHelper extends OrmLiteSqliteOpenHelper{

	/** 数据库名 */
	private final static String DB_NAME = "two_goods.db";
	/** 数据库版本 */
	private final static int DB_VERSION = 1;
	
	/** user表DAO */
	private Dao<User, Integer> userDAO;
	/** goods表DAO */
	private Dao<LocalGoods, Integer> goodsDAO;
	/** goods_comment表DAO */
	private Dao<GoodsComment, Integer> goodsCommentDAO;
	/** goods_focus表DAO */
	private Dao<GoodsFocus, Integer> goodsFocusDAO;
	/** chat_detail表DAO */
	private Dao<ChatDetailBean, Integer> chatDetailBeanDAO;
	/** chat_snapshot表DAO */
	private Dao<ChatSnapshot, Integer> chatSnapshotDAO;
	
	public OrmDatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}
	
	public OrmDatabaseHelper(Context context, String databaseName, CursorFactory factory, int databaseVersion) {
		super(context, databaseName, factory, databaseVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, LocalGoods.class);
			TableUtils.createTable(connectionSource, GoodsComment.class);
			TableUtils.createTable(connectionSource, GoodsFocus.class);
			TableUtils.createTable(connectionSource, ChatDetailBean.class);
			TableUtils.createTable(connectionSource, ChatSnapshot.class);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		
	}
	
	/**
	 * <p>
	 * 	获取user表DAO
	 * </p>
	 * 
	 * @return 获取成功返回相应的Dao,获取失败返回null
	 * */
	public Dao<User, Integer> getUserDao() {
		if(userDAO == null) {
			try {
				userDAO = getDao(User.class);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return userDAO;
	}
	
	/**
	 * <p>
	 * 	获取goods表DAO
	 * </p>
	 * 
	 * @return 获取成功返回相应的Dao,获取失败返回null
	 * */
	public Dao<LocalGoods, Integer> getGoodsDao() {
		if(goodsDAO == null) {
			try {
				goodsDAO = getDao(LocalGoods.class);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return goodsDAO;
	}
	
	/**
	 * <p>
	 * 	获取goods_comment表DAO
	 * </p>
	 * 
	 * @return 获取成功返回相应的Dao,获取失败返回null
	 * */
	public Dao<GoodsComment, Integer> getGoodsCommentDao() {
		if(goodsCommentDAO == null) {
			try {
				goodsCommentDAO = getDao(GoodsComment.class);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return goodsCommentDAO;
	}
	
	/**
	 * <p>
	 * 	获取goods_focus表DAO
	 * </p>
	 * 
	 * @return 获取成功返回相应的Dao,获取失败返回null
	 * */
	public Dao<GoodsFocus, Integer> getGoodsFocusDao() {
		if(goodsFocusDAO == null) {
			try {
				goodsFocusDAO = getDao(GoodsFocus.class);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return goodsFocusDAO;
	}
	
	/**
	 * <p>
	 * 	获取chat_detail表DAO
	 * </p>
	 * 
	 * @return 获取成功返回相应的Dao,获取失败返回null
	 * */
	public Dao<ChatDetailBean, Integer> getChatDetailDao() {
		if(chatDetailBeanDAO == null) {
			try {
				chatDetailBeanDAO = getDao(ChatDetailBean.class);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return chatDetailBeanDAO;
	}
	
	/**
	 * <p>
	 * 	获取chat_snapshot表DAO
	 * </p>
	 * 
	 * @return 获取成功返回相应的Dao,获取失败返回null
	 * */
	public Dao<ChatSnapshot, Integer> getChatSnapshotDao() {
		if(chatSnapshotDAO == null) {
			try {
				chatSnapshotDAO = getDao(ChatSnapshot.class);
				System.out.println(chatDetailBeanDAO);
			} catch(Exception e) {
				System.out.println("getChatSnapshotDao()");
				e.printStackTrace();
			}
		}
		return chatSnapshotDAO;
	}
	
	/**
	 * 清除指定表的所有记录
	 * 
	 * @param 与该表相关联的实体类的Class对象
	 * 
	 * @return 成功返回true,失败返回false.
	 * */
	public <T> boolean clearTable(Class<T> clazz) {
		ConnectionSource cs = getConnectionSource();
		try {
			//返回受影响的行数
			TableUtils.clearTable(cs, clazz);
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void close() {
		super.close();
	}
}
