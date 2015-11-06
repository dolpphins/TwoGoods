package com.lym.twogoods.message;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.j256.ormlite.dao.Dao;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.db.OrmDatabaseHelper;

/**
 * 用来接收新消息,使用一个service来扫描服务器数据库查看是否有用户发新消息给当前用户
 * @author yao
 *
 */
public class NewMessageReceiver {
	
	OrmDatabaseHelper mOrmDatabaseHelper;
	/**聊天信息表*/
	private Dao<ChatDetailBean, Integer> mChatDetailDao;
	/**最近聊天表*/
	private Dao<ChatSnapshot, Integer> mChatSnapshotDao;
	
	/**新消息数目*/
	public static int newMsgNums = 0;
	
	/**
	 * 获取当前用户与聊天对象的聊天记录信息
	 * @return
	 */
	/*public List<ChatDetailBean> initMsgData()
	{
		//查询用户名是当前用户，发送对象是当前聊天对象；或者用户名是当前聊天对象，发送对象是当前用户的消息
		BmobQuery<ChatDetailBean> eq1 = new BmobQuery<ChatDetailBean>();
		eq1.addWhereContains("username", currentUser.getUsername());
		eq1.addWhereContains("other_username", otherUser.getUsername());
		
		
		BmobQuery<ChatDetailBean> eq2 = new BmobQuery<ChatDetailBean>();
		eq2.addWhereContains("username", otherUser.getUsername());
		eq2.addWhereContains("other_username", currentUser.getUsername());

		List<BmobQuery<ChatDetailBean>> queries = new ArrayList<BmobQuery<ChatDetailBean>>();
		queries.add(eq1);
		queries.add(eq2);
		
		BmobQuery<ChatDetailBean> mainQuery = new BmobQuery<ChatDetailBean>();
		BmobQuery<ChatDetailBean> query = mainQuery.or(queries);
		
		
		query.findObjects(getActivity(), new FindListener<ChatDetailBean>() {
		    @Override
		    public void onSuccess(List<ChatDetailBean> object) {
		        // TODO Auto-generated method stub
		        System.out.println("查询用户名是当前用户，发送对象是当前聊天对象；或者用户名是当前聊天对象，" +
		        		"发送对象是当前用户的消息个数："+object.size());
		        list = object;
		        
		    }
		    @Override
		    public void onError(int code, String msg) {
		        // TODO Auto-generated method stub
		    	System.out.println("复合与查询失败："+code+",msg:"+msg);
		    }
		});
		return list;
	}*/
	
	/**
	 * 把接收到的新消息插入到本地相关数据库中
	 */
	public void insertNewMessage2LocalDb(ChatDetailBean msg)
	{
		insert2DetailDb(msg);
		insert2SnapshotDb(msg);
	}
	/**
	 * 把接收到的新消息插入到本地聊天数据表中
	 * @param msg
	 */
	private void insert2SnapshotDb(ChatDetailBean msg) {
		// TODO 自动生成的方法存根
		
	}
	/**
	 * 把接收到的新消息插入到本地最近聊天数据表中
	 * @param msg
	 */
	private void insert2DetailDb(ChatDetailBean msg) {
		// TODO 自动生成的方法存根
		
	}
}
