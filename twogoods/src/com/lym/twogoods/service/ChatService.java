package com.lym.twogoods.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ChatConfiguration;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.message.MessageConfig;
import com.lym.twogoods.message.ui.ChatActivity;
import com.lym.twogoods.ui.MainActivity;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.TimeUtil;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.preference.TwoStatePreference;
import android.support.v4.app.NotificationCompat;

/**
 * * 此service用来检查是否有新消息
 */
public class ChatService extends Service {
	
	/**保存上一次的加载时间*/
	private long loadTime;
	/**判断是否刚刚打开程序，第一次加载消息*/
	private Boolean isFirstLoad;
	/**保存本地数据库中最近一条发送或者接收成功的消息*/
	ChatDetailBean chatDetailBean;
	
	User currentUser;
	/**是否是第一次提示网络未连接*/
	private Boolean netConnectTip;
	
	/**本地数据库管理*/
	private OrmDatabaseHelper mOrmDatabaseHelper;
	/**聊天表*/
	private Dao<ChatDetailBean, Integer> mChatDetailDao;
	/**最近聊天表*/
	private Dao<ChatSnapshot, Integer> mChatSnapshotDao;
	/**查询服务器上面ChatDetailBean表的数据*/
	BmobQuery<ChatDetailBean> query;
	
	/**查询本地SnapShot数据表*/
	QueryBuilder<ChatSnapshot, Integer>queryBuilderOfSnapshotDao ;
	Where<ChatSnapshot, Integer>whereOfSnapShotDao ;
	
	/**Notification通知管理器*/
	NotificationManager mNotificationManager;
	NotificationCompat.Builder mBuilder;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO 自动生成的方法存根
		return null;
	}
	
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO 自动生成的方法存根
		super.onStart(intent, startId);
		System.out.println("serviceonStart");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自动生成的方法存根
		System.out.println("serviceonStartCommand");
		init();
		MyThread thread = new MyThread();
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	
	private void init() {
		
		System.out.println("service init---------");
		currentUser = UserInfoManager.getInstance().getmCurrent();
		mOrmDatabaseHelper = new OrmDatabaseHelper(getApplicationContext());
		mChatDetailDao = mOrmDatabaseHelper.getChatDetailDao();
		mChatSnapshotDao = mOrmDatabaseHelper.getChatSnapshotDao();
		queryBuilderOfSnapshotDao = mChatSnapshotDao.queryBuilder();
		whereOfSnapShotDao = queryBuilderOfSnapshotDao.where();
		
		isFirstLoad = true;
		netConnectTip = true;
		
		query = new BmobQuery<ChatDetailBean>();
		//查询发送对象是当前用户的数据
		query.addWhereEqualTo("other_username", currentUser.getUsername());
		//返回50条数据，如果不加上这条语句，默认返回10条数据
		query.setLimit(50);
		
		System.out.println("service current username "+currentUser.getUsername());
		mNotificationManager = (NotificationManager) getApplicationContext().
				getSystemService(Context.NOTIFICATION_SERVICE); 
		mBuilder = new NotificationCompat.Builder(getApplicationContext()); 
	}


	class MyThread extends Thread {  
		
		@Override
		/*public void run() {
			query.findObjects(getApplicationContext(), new FindListener<ChatDetailBean>() {
		        
		        @Override
		        public void onError(int code, String msg) {
		            // TODO Auto-generated method stub
		        	System.out.println("service查询失败");
		        }
				@Override
				public void onSuccess(List<ChatDetailBean> object) {
					System.out.println("service search---------");
					System.out.println("service new message Size "+object.size());
					if(object.size()>0){//有新消息,对新消息根据发送者进行分类，然后保存到本地ChatSnapshot表
						
						List<String>listOfName = new ArrayList<String>();
						for(ChatDetailBean obj : object){//获取有哪些用户发信息过来
							if(!listOfName.contains(obj.getUsername())){
								listOfName.add(obj.getUsername());
							}
						}
						
						if(listOfName.size()>0){
							System.out.println("service 有新消息---------");
							Map<String,List<ChatDetailBean>> map = new HashMap<String, List<ChatDetailBean>>();
							List<ChatDetailBean>list;
							//根据发送消息的用户名来进行整理
							for (ChatDetailBean obj : object) {
								System.out.println("servicesize"+object.size());
        		            	System.out.println("service"+obj.getUsername());
        		            	System.out.println("service"+obj.getMessage());
        		            	System.out.println("service"+obj.getCreatedAt());
							}
						}
					}
				}
			});
		}
		*/
  
        public void run() {  
        	while(true){
        		if(NetworkHelper.isNetConnected(getApplicationContext())){
	    			if(isFirstLoad){//加载在本地数据库中最近一条发送或者接收的消息的时间以后的全部消息
	    				System.out.println("service qurry 第一次加载新消息---------");
	    				QueryBuilder<ChatDetailBean, Integer>queryBuilder = mChatDetailDao.queryBuilder();
	            		/**
	            		 * 此处需要进行详细的测试，需要拿到的数据是本地数据库中最近一条发送或者接收的消息，而且消息状态是成功的。
	            		 */
	            		try {
	    				//	where.eq("last_message_status", MessageConfig.SEND_MESSAGE_SUCCEED);
	            			//where = where.eq("username", currentUser.getUsername());
	    					//queryBuilder.setWhere(where);
	            			queryBuilder.where().eq("username", currentUser.getUsername());
	    	        		chatDetailBean = queryBuilder.queryForFirst();
	    				} catch (SQLException e1) {
	    					// TODO 自动生成的 catch 块
	    					e1.printStackTrace();
	    				}
	            		
	    				if(chatDetailBean==null){//如果本地数据库为空,即是第一次使用此软件聊天。则查找所有发送对象是当前用户的消息
	    					query.addWhereGreaterThan("publish_time", 0);
	    					System.out.println("service qurry 是第一次加载新消息,数据库为空---0");
	    				}else{//只查询本地聊天数据库中没有的消息，需要减去加载数据的时间间隔.查询本地聊天数据库的第一条消息，需要得到最近一条消息的时间
	    					query.addWhereGreaterThan("publish_time", chatDetailBean.getPublish_time());
	    					System.out.println("service qurry 是第一次加载新消息"+chatDetailBean.getPublish_time());
	    				}
	    			}else{
	    				System.out.println("service qurry 不是第一次加载新消息，数据库不为空---------");
	    				query.addWhereGreaterThan("publish_time", loadTime);
	    			}
	    			System.out.println("service qurry inited---------");
	        		//执行查询方法
	        		query.findObjects(getApplicationContext(), new FindListener<ChatDetailBean>() {
	        		        
	        		        @Override
	        		        public void onError(int code, String msg) {
	        		            // TODO Auto-generated method stub
	        		        	System.out.println("service查询失败");
	        		        }
							@Override
							public void onSuccess(List<ChatDetailBean> object) {
								System.out.println("service search---------");
								
								if(object.size()>0){//有新消息,对新消息根据发送者进行分类，然后保存到本地ChatSnapshot表
									
									List<String>listOfName = new ArrayList<String>();
									for(ChatDetailBean obj : object){//获取有哪些用户发信息过来
										if(!listOfName.contains(obj.getUsername())){
											listOfName.add(obj.getUsername());
										}
									}
									
									if(listOfName.size()>0){
										System.out.println("service 有新消息---------");
										Map<String,List<ChatDetailBean>> map = new HashMap<String, List<ChatDetailBean>>();
										List<ChatDetailBean>list;
										//根据发送消息的用户名来进行整理
										for (ChatDetailBean obj : object) {
											System.out.println("servicesize"+object.size());
			        		            	System.out.println("service"+obj.getUsername());
			        		            	System.out.println("service"+obj.getMessage());
			        		            	System.out.println("service"+obj.getCreatedAt());
			        		            	try {
												mChatDetailDao.create(obj);
											} catch (SQLException e) {
												// TODO 自动生成的 catch 块
												e.printStackTrace();
												System.out.println("service接收的新消息插入到本地数据库失败");
											}
			        		            	
			        		            	list = map.get(obj.getUsername());
			        		            	if(list==null){
			        		            		list = new ArrayList<ChatDetailBean>();
			        		            		list.add(obj);
			        		            		map.put(obj.getUsername(), list);
			        		            	}else{
			        		            		list.add(obj);
			        		            		map.put(obj.getUsername(), list);
			        		            	}
				        		          }
										for(int i = 0;i<listOfName.size();i++){
										
											//把新消息根据发送的用户名保存到本地和服务器上的ChatSnapshot表中
											final ChatSnapshot chatSnapshot = new ChatSnapshot();
											final String userName = listOfName.get(i);
											List<ChatDetailBean> listChat = map.get(userName);
											//拿到最近的一条新消息
											final ChatDetailBean newestChatDetailBean = listChat.get(listChat.size()-1);
											
											int type = newestChatDetailBean.getMessage_type();
											switch (type) {
											case ChatConfiguration.TYPE_MESSAGE_TEXT:
												String last_message = newestChatDetailBean.getMessage();
												chatSnapshot.setLast_message(last_message);
												break;
											case ChatConfiguration.TYPE_MESSAGE_PICTURE:
												chatSnapshot.setLast_message("[图片]");
												break;
											case ChatConfiguration.TYPE_MESSAGE_VOICE:
												chatSnapshot.setLast_message("[语音]");	
												break;
											case ChatConfiguration.TYPE_MESSAGE_LOCATION:
												chatSnapshot.setLast_message("[位置]");	
												break;
											default:
												break;
											}
											System.out.println("service set new message");
											//发送的对象名字，这里是接收新消息，所以是当前用户的名字
											chatSnapshot.setOther_username(currentUser.getUsername());
											//发送者的名字，
											chatSnapshot.setUsername(userName);
											chatSnapshot.setLast_time(newestChatDetailBean.getPublish_time());
											chatSnapshot.setlast_message_status(MessageConfig.SEND_MESSAGE_RECEIVERED);
											chatSnapshot.setLast_message_type(type);
											
											//发来的新消息数目加上之前未读的数目
											int unread_num = 0;
											//unread_num = mChatSnapshotDao.queryForEq("username", userName).get(0).getUnread_num();
											System.out.println("service get local unread_num");
											chatSnapshot.setUnread_num(unread_num+listChat.size());
											chatSnapshot.save(getApplicationContext(),new SaveListener() {
												
												@Override
												public void onSuccess() {
													// TODO 自动生成的方法存根
													try {
														
														DeleteBuilder<ChatSnapshot, Integer> deleteBuilder =mChatSnapshotDao.deleteBuilder();
											            deleteBuilder.where().eq("username",userName);
											            deleteBuilder.delete();
														mChatSnapshotDao.create(chatSnapshot);
														System.out.println("service bmob将数据插入到本地数据库中");
													} catch (Exception e) {
														// TODO 自动生成的 catch 块
														System.out.println("service bmob将数据插入到本地数据库失败");
														e.printStackTrace();
														System.out.println("service bmob将数据插入到本地数据库失败");
													}
												}
												
												@Override
												public void onFailure(int arg0, String arg1) {
													
													chatSnapshot.setlast_message_status(MessageConfig.SEND_MESSAGE_FAILED);
													try {
														mChatSnapshotDao.create(chatSnapshot);
														System.out.println("service bmob将数据插入到本地数据库中");
													} catch (SQLException e) {
														// TODO 自动生成的 catch 块
														e.printStackTrace();
														System.out.println("service bmob将最近一条消息插入到ChatSnapshot表中失败");
													}
												}
											});
										}
										System.out.println("service 发送notification");
										//通过Status Bar Notification通知用户
										for(int n = 0;n<listOfName.size();n++){
											final String username = listOfName.get(n);
											List<ChatDetailBean> chatList = map.get(username);
											//拿到最近的一条新消息
											final ChatDetailBean newChatDetailBean = chatList.get(chatList.size()-1);
											mBuilder.setContentTitle(listOfName.get(n))//设置通知栏标题  
										    .setContentText(newChatDetailBean.getMessage()) //设置通知栏显示内容  
										    .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL,newChatDetailBean)) //设置通知栏点击意图  
										    .setTicker(listOfName.get(n)+":"+newChatDetailBean.getMessage()) //通知首次出现在通知栏，带上升动画效果的  
										    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
										    .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级  
										    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
										    .setDefaults(Notification.DEFAULT_ALL)
										    .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON  
											Notification notification = mBuilder.build();  
											notification.flags = Notification.FLAG_AUTO_CANCEL; 
											
											mNotificationManager.notify(1, notification);
										}
										
									}
								}
							}
	        			});
	        		try {
	        			loadTime = TimeUtil.getCurrentMilliSecond();
						sleep(2000);
					} catch (InterruptedException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
	        		isFirstLoad = false;
        		}
        		else{
        			//需要告知用户连接网络,但是只提示一次
        			if(netConnectTip){
	        			Intent intent  = new Intent();
	        			intent.setAction("networkDisconnect");
	        			sendBroadcast(intent);
        			}
        		}
        		netConnectTip = false;
          }  
       }  
		
	}
	/**
	 * 点击notification触发的事件，跳转到ChatActivity
	 * 
	 * @param flags notification的标志，这里是点击后自动消失
	 * @param otherUsername 聊天对象的名字
	 * @return
	 */
	public PendingIntent getDefalutIntent(int flags,ChatDetailBean chatDetailBean){  
		Intent intent = new Intent(this,ChatActivity.class);
		User user = new User();
		user.setUsername(chatDetailBean.getUsername());
		//user.setHead_url(head_url);
		intent.putExtra("otherUser", user);
		//点击了notification后，跳转到聊天界面，同时本地最近聊天数据表应该更新数据，把未读字段改为0
		try {
			ChatSnapshot oneChatSnapshot = mChatSnapshotDao.queryForEq("username", chatDetailBean.getUsername()).get(0);
			System.out.println("service----- "+oneChatSnapshot.getUnread_num());
			
			mChatSnapshotDao.delete(oneChatSnapshot);
			oneChatSnapshot.setUnread_num(0);
			mChatSnapshotDao.create(oneChatSnapshot);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	    PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, intent ,flags);  
	    return pendingIntent;  
	}  
}
	
