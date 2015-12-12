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
import com.lym.twogoods.message.ChatSetting;
import com.lym.twogoods.message.JudgeConfig;
import com.lym.twogoods.message.config.MessageConfig;
import com.lym.twogoods.message.ui.ChatActivity;
import com.lym.twogoods.ui.MainActivity;
import com.lym.twogoods.utils.NetworkHelper;
import com.lym.twogoods.utils.TimeUtil;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.preference.TwoStatePreference;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews.RemoteView;

/**
 * * 此service用来检查是否有新消息
 */
public class ChatService extends Service {
	
	private String TAG = "ChatService";
	
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
		return new MsgBinder();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO 自动生成的方法存根
		init();
		MyThread thread = new MyThread();
		thread.start();
		return super.onStartCommand(intent, flags, startId);
	}

	
	private void init() {
		
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
		
		mNotificationManager = (NotificationManager) getApplicationContext().
				getSystemService(Context.NOTIFICATION_SERVICE); 
		mBuilder = new NotificationCompat.Builder(getApplicationContext()); 
	}


	class MyThread extends Thread {  
		
        public void run() {  
        	while(true){
        		if(NetworkHelper.isNetConnected(getApplicationContext())){
	    			query.addWhereGreaterThan("message_read_status", 1);
	        		//执行查询方法
	        		query.findObjects(getApplicationContext(), new FindListener<ChatDetailBean>() {
	        		        @Override
	        		        public void onError(int code, String msg) {
	        		        	Log.i(TAG,"service查询失败");
	        		        }
							@Override
							public void onSuccess(List<ChatDetailBean> object) {
								Log.i(TAG,"service search---------");
								if(object.size()>0){//有新消息,对新消息根据发送者进行分类，然后保存到本地ChatSnapshot表
									List<String>listOfName = new ArrayList<String>();
									for(ChatDetailBean obj : object){//获取有哪些用户发信息过来
										if(!listOfName.contains(obj.getUsername())){
											listOfName.add(obj.getUsername());
										}
									}
									for(int f=0;f<listOfName.size();f++){
										Log.i(TAG,"service 新接收的消息发送人为:"+listOfName.get(f));
									}
									if(listOfName.size()>0){
										Map<String,List<ChatDetailBean>> map = new HashMap<String, List<ChatDetailBean>>();
										List<ChatDetailBean>list;
										//根据发送消息的用户名来进行整理
										for (ChatDetailBean obj : object) {
											Log.i(TAG,"servicesize"+object.size());
											Log.i(TAG,"service"+obj.getUsername());
											Log.i(TAG,"service"+obj.getMessage());
											Log.i(TAG,"service"+obj.getPublish_time());
											Log.i(TAG,"service"+obj.getPublish_time());
			        		            	obj.setMessage_read_status(MessageConfig.MESSAGE_RECEIVED);
			        		            	try {
												mChatDetailDao.create(obj);
											} catch (SQLException e) {
												e.printStackTrace();
												Log.i(TAG,"service接收的新消息插入到本地数据库失败");
											}
			        		            	//update新查询到数据
			        		            	ChatDetailBean cdb = new ChatDetailBean();
			        		            	cdb.setMessage_read_status(MessageConfig.MESSAGE_RECEIVED);
			        		            	cdb.setPublish_time(obj.getPublish_time());
			        		            	cdb.setLast_Message_Status(obj.getLast_Message_Status());
			        		            	cdb.setMessage_type(obj.getMessage_type());
			        		            	cdb.update(getApplicationContext(),obj.getObjectId(),new UpdateListener() {
												@Override
												public void onSuccess() {
													Log.i(TAG,"service 更新成功");
												}
												@Override
												public void onFailure(int arg0, String arg1) {
													Log.i(TAG,"service 更新失败");
												}
											});
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
											//发送的对象名字，这里是接收新消息，所以是当前用户的名字
											chatSnapshot.setOther_username(currentUser.getUsername());
											//发送者的名字，
											chatSnapshot.setUsername(userName);
											chatSnapshot.setLast_time(newestChatDetailBean.getPublish_time());
											chatSnapshot.setlast_message_status(MessageConfig.SEND_MESSAGE_RECEIVERED);
											chatSnapshot.setLast_message_type(type);
											int unread_num = 0;
											List<ChatSnapshot> listCss = null;
											//发来的新消息数目加上之前未读的数目
											try {
												listCss= mChatSnapshotDao.queryBuilder().where().eq("username",userName).or().eq("other_username", userName).query();
											} catch (SQLException e1) {
												e1.printStackTrace();
											}
											if(listCss!=null&&listCss.size()>0){
												unread_num = listCss.get(0).getUnread_num();
											}
											chatSnapshot.setUnread_num(unread_num+listChat.size());
											chatSnapshot.save(getApplicationContext(),new SaveListener() {
												
												@Override
												public void onSuccess() {
													try {
														DeleteBuilder<ChatSnapshot, Integer> deleteBuilder =mChatSnapshotDao.deleteBuilder();
											            deleteBuilder.where().eq("username",userName).or().eq("other_username", userName);
											            deleteBuilder.delete();
														mChatSnapshotDao.create(chatSnapshot);
														Log.i(TAG,"service bmob将数据插入到本地数据库中");
													} catch (Exception e) {
														Log.i(TAG,"service bmob将数据插入到本地数据库失败");
														e.printStackTrace();
													}
												}
												@Override
												public void onFailure(int arg0, String arg1) {
													chatSnapshot.setlast_message_status(MessageConfig.SEND_MESSAGE_FAILED);
													try {
														mChatSnapshotDao.create(chatSnapshot);
														Log.i(TAG,"service bmob将数据插入到本地数据库中");
													} catch (SQLException e) {
														e.printStackTrace();
														Log.i(TAG,"service bmob将最近一条消息插入到ChatSnapshot表中失败");
													}
												}
											});
										}
										Log.i(TAG,"service 发送notification");
										//通过Status Bar Notification通知用户
										for(int n = 0;n<listOfName.size();n++){
											final String username = listOfName.get(n);
											List<ChatDetailBean> chatList = map.get(username);
											if(!ChatSetting.otherUserName.equals(username)){
												//拿到最近的一条新消息
												final ChatDetailBean newChatDetailBean = chatList.get(chatList.size()-1);
												mBuilder.setContentTitle(listOfName.get(n))//设置通知栏标题  
											    .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL,newChatDetailBean)) //设置通知栏点击意图  
											    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
											    .setPriority(Notification.PRIORITY_HIGH) //设置该通知优先级  
											    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
											    .setDefaults(Notification.DEFAULT_ALL)
											    .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON 
												
												if(newChatDetailBean.getMessage_type()== ChatConfiguration.TYPE_MESSAGE_VOICE)
												{
													mBuilder.setTicker(listOfName.get(n)+":"+"[语音]");
													mBuilder.setContentText("[语音]");
												}else{
													if(newChatDetailBean.getMessage_type()== ChatConfiguration.TYPE_MESSAGE_PICTURE){
														mBuilder.setTicker(listOfName.get(n)+":"+"[图片]");
														mBuilder.setContentText("[图片]");
													}else{
														mBuilder.setTicker(listOfName.get(n)+":"+newChatDetailBean.getMessage());
														mBuilder.setContentText(newChatDetailBean.getMessage());
													}
												}
												Notification notification = mBuilder.build();  
												notification.flags = Notification.FLAG_AUTO_CANCEL; 
												mNotificationManager.notify(1, notification);
											}else{
												//如果接收到的新消息是当前聊天对象发来的，则直接更新聊天列表
												if((ChatSetting.isShow)&&(ChatSetting.otherUserName.equals(username))){
													if(mChatActivity!=null){
														mChatActivity.refreshFragment(chatList);
													}
												}
											}
										}
									}
								}
							}
	        			});
	        		try {
						sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
						Log.i(TAG,"service sleep 出现异常");
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
	 */
	public PendingIntent getDefalutIntent(int flags,ChatDetailBean chatDetailBean){  
		Intent intent = new Intent(this,ChatActivity.class);
		User user = new User();
		user.setUsername(chatDetailBean.getUsername());
		//user.setHead_url(head_url);
		intent.putExtra("otherUser", user);
		intent.putExtra("from", JudgeConfig.FRAM_NOTIFICTION);
	    PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, intent ,flags);  
	    return pendingIntent;  
	}  
	
	
	public class MsgBinder extends Binder{  
        /** 
         * 获取当前Service的实例 
         */  
        public ChatService getService(){  
            return ChatService.this;  
        }  
    } 
	 
	/**接收新发的消息已经被浏览的信息，如果已经被浏览，需要取消notification*/
	public void cancelAllNotification() {
		Log.i(TAG,"cancelAllNotification");
		mNotificationManager.cancelAll();
	}
	
	/**拿到ChatActivity的引用*/
	private ChatActivity mChatActivity;
	
	public void setActivity(ChatActivity activity){
		Log.i(TAG,"setActivity");
		mChatActivity = activity;
	}
}
	
