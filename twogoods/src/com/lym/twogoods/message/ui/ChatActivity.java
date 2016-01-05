package com.lym.twogoods.message.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.GetAccessUrlListener;
import com.bmob.btp.callback.UploadListener;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ChatConfiguration;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.eventbus.event.ChangeHeadPicEvent;
import com.lym.twogoods.eventbus.event.ExitChatEvent;
import com.lym.twogoods.eventbus.event.FinishRecordEvent;
import com.lym.twogoods.message.ChatSetting;
import com.lym.twogoods.message.JudgeConfig;
import com.lym.twogoods.message.adapter.ChatAddViewPagerAdapter;
import com.lym.twogoods.message.config.ChatBottomConfig;
import com.lym.twogoods.message.config.MessageConfig;
import com.lym.twogoods.message.fragment.ChatFragment;
import com.lym.twogoods.message.view.EmoticonsEditText;
import com.lym.twogoods.message.view.ImageCusView;
import com.lym.twogoods.service.ChatService;
import com.lym.twogoods.ui.SendPictureActivity;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;
import com.lym.twogoods.utils.DatabaseHelper;
import com.lym.twogoods.utils.TimeUtil;
import com.lym.twogoods.widget.WrapContentViewPager;

import de.greenrobot.event.EventBus;



/**
 * <p>
 * 	聊天Activity.这里会用到发送语音,发送语音使用到的布局文件是app_chat_record_content.xml,
 * 使用时直接在你的布局中include这个布局文件就可以了</p>
 * <p>录音需要set RecondTouchListener,这个类已经包装好，直接new一个然后就可以使用。
 * </p>
 * 
 * 
 * */
public class ChatActivity extends BottomDockBackFragmentActivity{
	
	private final String TAG = "ChatActivity" ;
	
	/** 底部布局和相关控件*/
	private View bottomView;
	private LinearLayout moreLinearLayout,emoLinearLayout,voiceLinearLayout,addLinearLayout;
	private EmoticonsEditText edit_user_comment;
	private ImageCusView mImageCusView;
	
	/**当前用户*/
	private User currentUser;
	/**当前的聊天对象*/
	private User otherUser = null;
	
	/**本地数据库管理*/
	private OrmDatabaseHelper mOrmDatabaseHelper;
	/**聊天表*/
	private Dao<ChatDetailBean, Integer> mChatDetailDao;
	/**最近聊天表*/
	private Dao<ChatSnapshot, Integer> mChatSnapshotDao;
	
	/** 收到消息*/
	public static final int NEW_MESSAGE = 0x001;
	
	/**上传文件到服务器后返回的文件名*/
	private String fileName = null;
	/**图片的有效url*/
	private String url = null;
	/**用来暂存语音在本地的路径*/
	private String voicePath = null;
	
	private int SEND_PICTURE = 0;
	private ChatFragment mChatFragment;
	
	/**表情的viewpager*/
	private WrapContentViewPager mWrapContentViewPagerOfEmoji;
	/**点击添加按钮显示的viewpager*/
	private WrapContentViewPager mWrapContentViewPagerOfAdd;
	/**用来判断是谁启动了ChatActivity*/
	private int from = 0;
	
	private boolean chated = false;
	/**用来标记批量上传图片*/
	private static int count = 0;
	/**ChatActivity的Handler*/
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch(msg.what){
			case MessageConfig.HIDE_BOTTOM:
				hideBottom();
				break;
			//点击发送图片	
			case ChatBottomConfig.MSG_SEND_PIC:
				Intent intent = new Intent(ChatActivity.this,SendPictureActivity.class);
				//设置一次可以发10张相片
				intent.putExtra("picCount", 10);
				startActivityForResult(intent, SEND_PICTURE);
				break;
			//点击发送语音
			case ChatBottomConfig.MSG_SEND_VOICE:
				addLinearLayout.setVisibility(View.GONE);
				voiceLinearLayout.setVisibility(View.VISIBLE);
				break;
				//点击发送位置
			case ChatBottomConfig.MSG_SEND_LOC:
				Toast.makeText(ChatActivity.this, "发送位置", Toast.LENGTH_SHORT).show();
				break;	
			default:break;
				
			}
		}

	};
	private ChatService mService;
	ServiceConnection conn = new ServiceConnection() {  
        @Override  
        public void onServiceDisconnected(ComponentName name) {  
              
        }  
          
        @Override  
        public void onServiceConnected(ComponentName name, IBinder service) {  
            //返回一个MsgService对象  
            mService = ((ChatService.MsgBinder)service).getService();  
            mService.setActivity(ChatActivity.this);  
            mService.cancelAllNotification();
        }  
    };

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}
	
	@Override
	public View onCreateBottomView() {
		bottomView = getLayoutInflater().inflate(R.layout.message_chat_bottom_bar, null);
		return bottomView;
	}
	
	private void init() {
		//绑定服务
		Intent intent = new Intent(ChatActivity.this,ChatService.class);
		bindService(intent, conn, BIND_AUTO_CREATE);
		
		EventBus.getDefault().register(this);
		mOrmDatabaseHelper = new OrmDatabaseHelper(this);
		mChatDetailDao = mOrmDatabaseHelper.getChatDetailDao();
		mChatSnapshotDao = mOrmDatabaseHelper.getChatSnapshotDao();
		initUserInfo();
		initFragment();
		initBottom();
	}
	
	//拿到跳转到这个activity的相关用户信息
	private void initUserInfo() {
		Intent intent = getIntent();
		otherUser = (User) intent.getExtras().get("otherUser");
		String other_username = otherUser.getUsername();
		setCenterTitle("与"+other_username+"聊天");
		
		ChatSetting.isShow = true;
		ChatSetting.otherUserName = other_username;
		
		from = intent.getExtras().getInt("from");
		//从notifiction处启动ChatActivity，刷新最近聊天列表
		if(from==JudgeConfig.FRAM_NOTIFICTION){
			try {
				mChatSnapshotDao.updateRaw("UPDATE chatsnapshot SET unread_num = 0 WHERE username = '"+other_username+"'");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		UserInfoManager mUserInfoManager = UserInfoManager.getInstance();
		currentUser = mUserInfoManager.getmCurrent();
	}
	
	//初始化聊天的fragment
	private void initFragment() {
		mChatFragment = new ChatFragment();
		mChatFragment.initOtherUser(otherUser);
		mChatFragment.setHandler(mHandler);
		addFragment(mChatFragment);
		showFragment(mChatFragment);
	}
	
	//初始化底部布局
	private void initBottom() {
		moreLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_more);
		emoLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_emo_content);
		voiceLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_voice_content);
		addLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_add_content);
		
		mImageCusView = (ImageCusView) findViewById(R.id.message_chat_record_view);
		
		edit_user_comment = (EmoticonsEditText) bottomView.findViewById(R.id.message_chat_edit_user_comment);
		mWrapContentViewPagerOfEmoji = (WrapContentViewPager) findViewById(R.id.message_chat_emoji_viewpager);
		
		EmotionViewPagerAdapter emotionViewPagerAdapter=new EmotionViewPagerAdapter(ChatActivity.this, edit_user_comment);
		mWrapContentViewPagerOfEmoji.setAdapter(emotionViewPagerAdapter);
	
		mWrapContentViewPagerOfAdd = (WrapContentViewPager) findViewById(R.id.message_chat_add_viewpager);
		ChatAddViewPagerAdapter chatAddViewPagerAdapter = new ChatAddViewPagerAdapter(ChatActivity.this,mHandler);
		mWrapContentViewPagerOfAdd.setAdapter(chatAddViewPagerAdapter);
	}
	
	//实现控件点击事件
	public void toAction(View view)
	{
		switch(view.getId()){
		case R.id.message_chat_btn_send:
			if(edit_user_comment.getText().toString().equals("")||edit_user_comment.getText().toString().equals(null)){
				Toast.makeText(ChatActivity.this, "消息内容不能为空", Toast.LENGTH_SHORT).show();
				return ;
			}
			sendTextMessage();
			break;
		case R.id.message_chat_edit_user_comment://点击编辑框
				moreLinearLayout.setVisibility(View.GONE);
				emoLinearLayout.setVisibility(View.GONE);
				addLinearLayout.setVisibility(View.GONE);
				voiceLinearLayout.setVisibility(View.GONE);
			break;
		case R.id.message_chat_btn_add://点击添加按钮
			edit_user_comment.requestFocus();
			if (moreLinearLayout.getVisibility() == View.GONE) {
				moreLinearLayout.setVisibility(View.VISIBLE);
				addLinearLayout.setVisibility(View.VISIBLE);
				voiceLinearLayout.setVisibility(View.GONE);
				emoLinearLayout.setVisibility(View.GONE);
				hideSoftInputView();
			} else {
				if (emoLinearLayout.getVisibility() == View.VISIBLE) {
					emoLinearLayout.setVisibility(View.GONE);
					addLinearLayout.setVisibility(View.VISIBLE);
				} else {
					if(voiceLinearLayout.getVisibility()==View.VISIBLE){
						voiceLinearLayout.setVisibility(View.GONE);
						addLinearLayout.setVisibility(View.VISIBLE);
					}else{
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.showSoftInput(edit_user_comment, 0);
					moreLinearLayout.setVisibility(View.GONE);
					}
				}
			}
			break;
		case R.id.message_chat_btn_emo://点击表情按钮
			if (moreLinearLayout.getVisibility() == View.GONE) {
				showEditEmoState(true);
			} else {
				if (addLinearLayout.getVisibility() == View.VISIBLE) {
					addLinearLayout.setVisibility(View.GONE);
					emoLinearLayout.setVisibility(View.VISIBLE);
				} else {
					if(voiceLinearLayout.getVisibility()==View.VISIBLE){
						voiceLinearLayout.setVisibility(View.GONE);
						emoLinearLayout.setVisibility(View.VISIBLE);
					}else{
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.showSoftInput(edit_user_comment, 0);
					moreLinearLayout.setVisibility(View.GONE);
					}
				}
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * 发text信息
	 * @author yao
	 */
	private void sendTextMessage() {
		chated = true;
		final ChatDetailBean chatDetailBean = new ChatDetailBean();
		String username = currentUser.getUsername();
		chatDetailBean.setUsername(username);
		chatDetailBean.setGUID(DatabaseHelper.getUUID().toString());
		chatDetailBean.setMessage(edit_user_comment.getText().toString());
		chatDetailBean.setOther_username(otherUser.getUsername());
		chatDetailBean.setMessage_type(ChatConfiguration.TYPE_MESSAGE_TEXT);
		chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_ING);
		chatDetailBean.setMessage_read_status(MessageConfig.MESSAGE_NOT_RECEIVED);
		chatDetailBean.setPublish_time(TimeUtil.getCurrentMilliSecond());
		try {
			mChatDetailDao.create(chatDetailBean);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		mChatFragment.sendNewMessage(chatDetailBean);
		
		chatDetailBean.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				 try {
					 mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 0 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
					 mChatFragment.notifyChange(MessageConfig.SEND_MESSAGE_SUCCEED);
					 refreshRecent(chatDetailBean);
				} catch (SQLException e) {
					Log.i(TAG,"chatactivity将聊天信息插入本地数据库失败1");
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				//如果消息发送到服务器上面的数据库失败，则将消息的status设置为false，然后插入到本地数据库
				try {
					mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
					mChatFragment.notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
					refreshRecent(chatDetailBean);
				} catch (SQLException e) {
					Log.i(TAG,"chatactivity将聊天信息插入本地数据库失败2");
					e.printStackTrace();
				}
			}
		});
		edit_user_comment.setText(null);
	}
	
	
	/**用来保存发送的图片在本地的路径*/
	public String localPicturePath = null;
	
	/**
	 * 发送picture信息
	 * @param picPath 图片的路径
	 * @author yao
	 */
	private void sendPictureMessage(String picPath) {
		chated = true;
		localPicturePath = picPath;
		String username = currentUser.getUsername();
		
		final ChatDetailBean chatDetailBean = new ChatDetailBean();
		chatDetailBean.setUsername(username);
		chatDetailBean.setGUID(DatabaseHelper.getUUID().toString());
		chatDetailBean.setOther_username(otherUser.getUsername());
		chatDetailBean.setMessage_type(ChatConfiguration.TYPE_MESSAGE_PICTURE);
		chatDetailBean.setPublish_time(System.currentTimeMillis());
		chatDetailBean.setMessage_read_status(MessageConfig.MESSAGE_NOT_RECEIVED);
		chatDetailBean.setMessage(localPicturePath);
		chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_ING);
		try {
			mChatDetailDao.create(chatDetailBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		refreshRecent(chatDetailBean);
		mChatFragment.sendNewMessage(chatDetailBean);
		
		BmobProFile.getInstance(ChatActivity.this).upload(picPath, new UploadListener() {
			@Override
			public void onError(int arg0, String arg1) {
				//发送图片失败
				sendPicture2Db(false,chatDetailBean);
			}
			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				Log.i(TAG,"onSuccess");
				fileName = arg0; //获取文件上传成功后的文件名
				BmobProFile.getInstance(ChatActivity.this).getAccessURL(fileName, new GetAccessUrlListener() {
		            @Override
		            public void onError(int errorcode, String errormsg) {
		                Log.i(TAG,"获取文件的服务器地址失败："+errormsg+"("+errorcode+")");
		                //获取不了文件的可访问网络地址也被认作是发送失败
		                sendPicture2Db(false,chatDetailBean);
		            }

		            @Override
		            public void onSuccess(BmobFile file) {
		            	url = file.getUrl();//获取文件的有效网络url;
		                Log.i(TAG, "源文件名："+file.getFilename()+"，可访问的地址："+url);
		                sendPicture2Db(true,chatDetailBean);
		            }
		        });
			}
			@Override
			public void onProgress(int arg0) {
			}
		});
	}
	
	
	//把数据传到数据库
	private void sendPicture2Db(Boolean isUpload,final ChatDetailBean chatDetailBean){
		if(isUpload){//文件上传成功
			chatDetailBean.setMessage(url);
			chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_SUCCEED);
			chatDetailBean.save(this, new SaveListener() {
				@Override
				public void onSuccess() {
					 try {
						 mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 0 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
						 chatDetailBean.setMessage(localPicturePath);
						 mChatFragment.notifyChange(MessageConfig.SEND_MESSAGE_SUCCEED);
						 refreshRecent(chatDetailBean);
						 
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					// 插入聊天信息到服务器的数据库中失败也当做是发送失败
					try {
						mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
						 chatDetailBean.setMessage(localPicturePath);
						 mChatFragment.notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
						refreshRecent(chatDetailBean);
					} catch (SQLException e) {
						Log.i(TAG,"将聊天信息插入本地数据库失败");
						e.printStackTrace();
					}
				}
			});
		}else{//文件上传失败
			try {
				mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
				mChatFragment.notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
				
				refreshRecent(chatDetailBean);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 发送语音消息,由于是发的语音消息，本地数据库只需要保存发的语音在本地的路径
	 * @param localPath 语音文件在本地的路径
	 */
	private void sendVoiceMessage(String localPath) {
		chated = true;
		voicePath = localPath;
		final ChatDetailBean chatDetailBean = new ChatDetailBean();
		String username = currentUser.getUsername();
		chatDetailBean.setUsername(username);
		chatDetailBean.setGUID(DatabaseHelper.getUUID().toString());
		chatDetailBean.setOther_username(otherUser.getUsername());
		chatDetailBean.setMessage_type(ChatConfiguration.TYPE_MESSAGE_VOICE);
		chatDetailBean.setMessage_read_status(MessageConfig.MESSAGE_NOT_RECEIVED);
		chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_ING);
		chatDetailBean.setPublish_time(System.currentTimeMillis());
		chatDetailBean.setMessage(localPath);
		
		try {
			mChatDetailDao.create(chatDetailBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mChatFragment.sendNewMessage(chatDetailBean);
		refreshRecent(chatDetailBean);
		
		BmobProFile.getInstance(ChatActivity.this).upload(localPath,new UploadListener() {
			public void onError(int arg0, String arg1) {
				sendVoice2Db(false,chatDetailBean);
			}
			//上传语音文件到服务器成功
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				
				fileName = arg0; //获取文件上传成功后的文件名
				Log.i(TAG,"URL:" + url);
				BmobProFile.getInstance(ChatActivity.this).getAccessURL(fileName, new GetAccessUrlListener() {
		            @Override
		            public void onError(int errorcode, String errormsg) {
		                Log.i(TAG,"获取文件的服务器地址失败："+errormsg+"("+errorcode+")");
		                sendVoice2Db(false,chatDetailBean);
		            }

		            @Override
		            public void onSuccess(BmobFile file) {
		            	//获取文件的有效url;
		            	url = file.getUrl();
		                Log.i(TAG, "源文件名："+file.getFilename()+"，可访问的地址："+url);
		                sendVoice2Db(true,chatDetailBean);
		            }
		        });
			}
			@Override
			public void onProgress(int arg0) {
				Log.i(TAG,"onProgress");
			}
		});
	}
	/**
	 * 将voice消息插入到数据库中
	 */
	private void sendVoice2Db(Boolean isUpload,final ChatDetailBean chatDetailBean ) {
		//语音文件上传到服务器成功
		if(isUpload){
			chatDetailBean.setMessage(url);
			chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_SUCCEED);
			chatDetailBean.save(this, new SaveListener() {
				@Override
				public void onSuccess() {
					 try {
						 Log.i(TAG,"bmob将语音插入到服务器的数据库成功");
						 mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 0 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
						 mChatFragment.notifyChange(MessageConfig.SEND_MESSAGE_SUCCEED);
						 refreshRecent(chatDetailBean);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					try {
						Log.i(TAG,"bmob将语音插入到服务器的数据库失败");
						mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
						mChatFragment.notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
						refreshRecent(chatDetailBean);
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						Log.i(TAG,"bmob将语音插入到服务器的数据库失败");
						e.printStackTrace();
					}
				}
			});
		}else{//语音文件上传到服务器失败
			try {
				mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
				mChatFragment.notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
				refreshRecent(chatDetailBean);
			} catch (SQLException e) {
				Log.i(TAG,"bmob将语音插入到服务器的数据库失败");
				e.printStackTrace();
			}
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case MessageConfig.SEND_CAMERA_PIC:
			String picPath = data.getExtras().getString("picture");
			Toast.makeText(this, picPath, Toast.LENGTH_LONG).show();
			sendPictureMessage(picPath);
			break;
		case MessageConfig.SEND_LOCAL_PIC:
			ArrayList<String> pics = data.getExtras().getStringArrayList("pictures");
			Toast.makeText(this, "共发送本地图片"+pics.size()+"张", Toast.LENGTH_LONG).show();
			if(pics.size()==1){
				sendPictureMessage(pics.get(0));
			}else{
				sendPicturesMessage(pics);
			}
			break;
		default:
			break;
		}
	} 
	
	/**
	 * 一次2张或以上图片
	 * @param list
	 */
	public void sendPicturesMessage(final List<String>list){
		count = 0;
		final ChatDetailBean[]mChatDetailBeans =  new ChatDetailBean[list.size()];
		String[]files = new String[list.size()];
		for(int j = 0;j<list.size();j++){
			files[j] = list.get(j);
		}
		
		chated = true;
		
		for(int i = 0;i<list.size();i++){
			localPicturePath = list.get(i);
			String username = currentUser.getUsername();
			
			mChatDetailBeans[i] = new ChatDetailBean();
			mChatDetailBeans[i].setUsername(username);
			mChatDetailBeans[i].setGUID(DatabaseHelper.getUUID().toString());
			mChatDetailBeans[i].setOther_username(otherUser.getUsername());
			mChatDetailBeans[i].setMessage_type(ChatConfiguration.TYPE_MESSAGE_PICTURE);
			mChatDetailBeans[i].setPublish_time(System.currentTimeMillis());
			mChatDetailBeans[i].setMessage_read_status(MessageConfig.MESSAGE_NOT_RECEIVED);
			mChatDetailBeans[i].setMessage(localPicturePath);
			mChatDetailBeans[i].setLast_Message_Status(MessageConfig.SEND_MESSAGE_ING);
			try {
				mChatDetailDao.create(mChatDetailBeans[i]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			refreshRecent(mChatDetailBeans[i]);
			mChatFragment.sendNewMessage(mChatDetailBeans[i]);
		}
		
		Bmob.uploadBatch(ChatActivity.this, files, new UploadBatchListener() {
			
			@Override
			public void onSuccess(List<BmobFile> arg0, List<String> arg1) {
				Log.i(TAG,"批量上传文件成功");
				Log.i(TAG,"count="+count);
				url = arg1.get(count);
				localPicturePath = list.get(count);
				sendPicture2Db(true, mChatDetailBeans[count]);
				count++;
			}
			
			@Override
			public void onProgress(int arg0, int arg1, int arg2, int arg3) {
				Log.e(TAG,"表示当前第几个文件正在上传"+arg0);
				Log.e(TAG,"表示当前上传文件的进度值"+arg1);
				Log.e(TAG,"表示总的上传文件数"+arg2);
				Log.e(TAG,"表示总的上传进度"+arg3);
			}
			
			@Override
			public void onError(int arg0, String arg1) {
				Log.e(TAG,"批量上传图片失败");
				if(count<list.size()-1){
					for(int i=count;i<list.size();i++){
						sendPicture2Db(false, mChatDetailBeans[i]);
					}
				}
			}
		});
		
	}
	/**
	 * 通过eventBus来监听完成录音的操作，
	 * @param event 完成录音的事件
	 */
	public void onEventMainThread(FinishRecordEvent event) {  
		 String path = event.getPath();
		 Log.i(TAG,"录音的路径："+path);
		 sendVoiceMessage(path);
   }  
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0
				&&moreLinearLayout.getVisibility()==View.VISIBLE) { 
			//按下的如果是返回键，同时没有重复，而且底部布局看得见的话则是先隐藏底部布局。
		    moreLinearLayout.setVisibility(View.GONE);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void hideBottom() {
		moreLinearLayout.setVisibility(View.GONE);
		addLinearLayout.setVisibility(View.GONE);
		emoLinearLayout.setVisibility(View.GONE);
		voiceLinearLayout.setVisibility(View.GONE);
		hideSoftInputView();
	}
	/*
	 * 隐藏软键盘
	 * 
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	// 显示软键盘
	public void showSoftInputView() {
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null){
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(edit_user_comment, 0);
			}
		}
		hideBottom();
	}
	
	
	private void showEditEmoState(boolean isEmo) {
		edit_user_comment.requestFocus();
		if (isEmo) {
			moreLinearLayout.setVisibility(View.VISIBLE);
			emoLinearLayout.setVisibility(View.VISIBLE);
			addLinearLayout.setVisibility(View.GONE);
			hideSoftInputView();
		} else {
			moreLinearLayout.setVisibility(View.GONE);
			showSoftInputView();
		}
	}
	/**
	 * 获得当前用户的名字，即贰货号
	 * @return
	 */
	public String getCurrenUsername(){
		return currentUser.getUsername();
	}
	
	/**
	 * 获得聊天对象的名字，即贰货号
	 * @return
	 */
	public String getOther_Username(){
		return otherUser.getUsername();
	}
	
	public User getOtherUser(){
		return otherUser;
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	}
	//
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * 如果调用此函数时正在播放语音，需要暂停播放
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onStop() {
		super.onStop();
		if(moreLinearLayout.getVisibility()==View.VISIBLE){
			moreLinearLayout.setVisibility(View.GONE);
			voiceLinearLayout.setVisibility(View.GONE);
			addLinearLayout.setVisibility(View.GONE);
		}
	}
	/**更新界面*/
	public void refreshFragment(List<ChatDetailBean> list){
		Log.i(TAG,"接收到的新消息是当前聊天对象发来的更新聊天列表");
		mChatFragment.receiveNewMessage(list);
		try {
			mChatSnapshotDao.updateRaw("UPDATE chatsnapshot SET unread_num = 0 WHERE username = '"+otherUser.getUsername()+"'");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 重定义actionbar的返回方法
	 */
	@Override
	public void onActionBarBack() {
		super.onActionBarBack();
		if(from==JudgeConfig.FRAM_GOODS&&!chated){
			
		}else{
			EventBus.getDefault().post(new ExitChatEvent("back btn clicked"));  
		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if(from==JudgeConfig.FRAM_GOODS&&!chated){
			
		}else{
			EventBus.getDefault().post(new ExitChatEvent("back btn clicked"));  
		}
	}
	/**
	 * 需要释放资源，聊天界面被destory时需要刷新消息列表
	 **/
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(conn);
		EventBus.getDefault().unregister(this);
		mImageCusView.release();
		ChatSetting.isShow = false;
		ChatSetting.otherUserName = "";
	}
	
	/**
	 * 刷新消息列表
	 **/
	private void refreshRecent(ChatDetailBean chatDetailBean) {
		final ChatSnapshot mChatSnapshot = new ChatSnapshot();
		int type = chatDetailBean.getMessage_type();
		
		switch (type) {
			case ChatConfiguration.TYPE_MESSAGE_TEXT:
				String last_message = chatDetailBean.getMessage();
				mChatSnapshot.setLast_message(last_message);
				break;
			case ChatConfiguration.TYPE_MESSAGE_PICTURE:
				mChatSnapshot.setLast_message("[图片]");
				break;
			case ChatConfiguration.TYPE_MESSAGE_VOICE:
				mChatSnapshot.setLast_message("[语音]");	
				break;
			case ChatConfiguration.TYPE_MESSAGE_LOCATION:
				mChatSnapshot.setLast_message("[位置]");	
				break;
			default:
				break;
		}
		
		final String other_username = chatDetailBean.getOther_username();
		mChatSnapshot.setOther_username(other_username);
		mChatSnapshot.setUsername(currentUser.getUsername());
		mChatSnapshot.setLast_time(chatDetailBean.getPublish_time());
		mChatSnapshot.setUnread_num(0);
		mChatSnapshot.setlast_message_status(chatDetailBean.getLast_Message_Status());
		mChatSnapshot.setLast_message_type(type);
		
		DeleteBuilder<ChatSnapshot, Integer> deleteBuilder =mChatSnapshotDao.deleteBuilder();
        try{
		deleteBuilder.where().eq("other_username",other_username).or().eq("username", other_username);
        deleteBuilder.delete();
		mChatSnapshotDao.create(mChatSnapshot);
        }catch(Exception e) {
			e.printStackTrace();
		}
	}
}