package com.lym.twogoods.message.ui;

import java.io.File;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.util.BmobLog;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BTPFileResponse;
import com.bmob.BmobProFile;
import com.bmob.btp.callback.GetAccessUrlListener;
import com.bmob.btp.callback.UploadListener;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.db.DatabaseType;
import com.j256.ormlite.field.DataPersister;
import com.j256.ormlite.field.FieldConverter;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ChatConfiguration;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.eventbus.event.ExitChatEvent;
import com.lym.twogoods.message.MessageConfig;
import com.lym.twogoods.message.fragment.ChatFragment;
import com.lym.twogoods.message.listener.RecondTouchListener;
import com.lym.twogoods.message.view.EmoticonsEditText;
import com.lym.twogoods.publish.ui.PublishGoodsActivity;
import com.lym.twogoods.ui.MainActivity;
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
	
	/** 底部布局*/
	private View bottomView;
	
	private LinearLayout moreLinearLayout,emoLinearLayout,voiceLinearLayout,addLinearLayout;
	/**聊天编辑框*/
	private EmoticonsEditText edit_user_comment;
	/**按住录音*/
	private ImageView iv_recondVoice;
	/**话筒动画*/
	private Drawable[] drawable_Anims;
	/**正在录音的动画*/
	private ImageView iv_showIsReconding;
	/**按下开始录音提示和正在录音的提示*/
	private TextView tv_recondPressTip;
	private LinearLayout tv_finish_recond_tip;
	
	
	private int SEND_PICTURE = 0;
	//聊天对象的id
	private String targetId = "";
	
	/**当前用户*/
	private User currentUser;
	/**当前的聊天对象*/
	private User otherUser = null;
	
	/**本地数据库管理*/
	private OrmDatabaseHelper mOrmDatabaseHelper;
	/**聊天表*/
	private Dao<ChatDetailBean, Integer> mChatDetailDao;
	/**录音监听器*/
	private RecondTouchListener mRecondTouchListener;
	
	/**消息的总页数*/
	int MsgPagerNum;	
	/** 收到消息*/
	public static final int NEW_MESSAGE = 0x001;
	/**上传文件到服务器后返回的文件名*/
	private String fileName = null;
	/**图片的有效url*/
	private String url = null;
	
	/**用来暂存语音在本地的路径*/
	private String voicePath = null;
	
	ChatFragment mChatFragment;
	/**表情*/
	WrapContentViewPager mWrapContentViewPager;
	
	
	/**ChatActivity的Handler*/
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch(msg.what){
			//开始录音
			case MessageConfig.START_RECORD:
				iv_showIsReconding.setVisibility(View.VISIBLE);
				tv_recondPressTip.setVisibility(View.INVISIBLE);
				tv_finish_recond_tip.setVisibility(View.VISIBLE);
				break;
			//放弃录音
			case MessageConfig.ABANDON_RECORD:
				Toast.makeText(ChatActivity.this, "放弃录音", Toast.LENGTH_LONG).show();
				iv_showIsReconding.setVisibility(View.INVISIBLE);
				tv_recondPressTip.setVisibility(View.INVISIBLE);
				tv_finish_recond_tip.setVisibility(View.INVISIBLE);
				break;
			//结束录音	
			case MessageConfig.FINISH_RECORD:
				iv_showIsReconding.setVisibility(View.INVISIBLE);
				tv_recondPressTip.setVisibility(View.VISIBLE);
				tv_finish_recond_tip.setVisibility(View.INVISIBLE);
				Bundle data = msg.getData();
				//拿到音频文件的路径
				String path = data.getString("filename");
				voicePath = path;
				
				sendVoiceMessage(path);
				
				break;
				
			case MessageConfig.HIDE_BOTTOM:
				hideBottom();
			}
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		initBmob();
		init();
	}
	//初始化与Bmob相关的成员
	private void initBmob()
	{
		
		MsgPagerNum = 0;
	} 
	
	//进行初始化
	private void init() {
		// TODO 自动生成的方法存根
		
		initOtherUserInfo();
		
		UserInfoManager mUserInfoManager = UserInfoManager.getInstance();
		currentUser = mUserInfoManager.getmCurrent();
		
		mOrmDatabaseHelper = new OrmDatabaseHelper(this);
		mChatDetailDao = mOrmDatabaseHelper.getChatDetailDao();
		
		
		initFragment();
		initBottom();
		initVoice();
		
	}
	//拿到跳转到这个activity的相关用户信息
	private void initOtherUserInfo() {
		// TODO 自动生成的方法存根
		Intent intent = getIntent();
		otherUser = (User) intent.getExtras().get("otherUser");
		//聊天对象的名字即贰货号
		String other_username = otherUser.getUsername();
		setCenterTitle("与"+other_username+"聊天");
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
		// TODO 自动生成的方法存根
		moreLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_more);
		emoLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_emo_content);
		voiceLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_voice_content);
		addLinearLayout = (LinearLayout) bottomView.findViewById(R.id.chat_layout_add_content);
		
		edit_user_comment = (EmoticonsEditText) bottomView.findViewById(R.id.message_chat_edit_user_comment);
		mWrapContentViewPager = (WrapContentViewPager) findViewById(R.id.message_chat_emoji_viewpager);
		
		EmotionViewPagerAdapter emotionViewPagerAdapter=new EmotionViewPagerAdapter(ChatActivity.this, edit_user_comment);
		mWrapContentViewPager.setAdapter(emotionViewPagerAdapter);
	
	}
	
	//初始化与录音相关的东西
	private void initVoice() {
		
		//初始化跟录音相关的控件
		iv_showIsReconding = (ImageView) bottomView.findViewById(R.id.message_chat_show_is_recording_iv);
		tv_recondPressTip = (TextView)bottomView.findViewById(R.id.message_chat_recond_press_tip);
		tv_finish_recond_tip = (LinearLayout) bottomView.findViewById(R.id.message_chat_finish_recond_tip_tv);
		iv_recondVoice = (ImageView) bottomView.findViewById(R.id.message_chat_record);
		//给录音控件设置触摸事件
		mRecondTouchListener = new RecondTouchListener(ChatActivity.this,mHandler);
		iv_recondVoice.setOnTouchListener(mRecondTouchListener);
		
		//初始化在录音时音量改变的动画
		drawable_Anims = new Drawable[] {
				getResources().getDrawable(R.drawable.message_chat_reconding_audio1),
				getResources().getDrawable(R.drawable.message_chat_reconding_audio2),
				getResources().getDrawable(R.drawable.message_chat_reconding_audio3) };
		
	
	}
	
	

	@Override
	public View onCreateBottomView() {
		bottomView = getLayoutInflater().inflate(R.layout.
				message_chat_bottom_bar, null);
		return bottomView;
	}
	//实现控件点击事件
	public void toAction(View view)
	{
		
		switch(view.getId()){
		case R.id.message_chat_btn_send:
			if(edit_user_comment.getText().equals("")){
				Toast.makeText(ChatActivity.this, "消息内容不能为空", Toast.LENGTH_SHORT).show();
				return ;
			}
			sendTextMessage();
			break;
		case R.id.message_chat_edit_user_comment://点击编辑框
				System.out.println("click编辑框");
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
				System.out.println("click添加按钮");
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
		case R.id.message_chat_tv_location://点击发送位置
			Toast.makeText(this, "点击了发送位置", Toast.LENGTH_LONG).show();
			break;
		case R.id.message_chat_tv_picture://点击发送图片
			Intent intent = new Intent(this,SendPictureActivity.class);
			//设置一次可以发30张相片
			intent.putExtra("picCount", 30);
			startActivityForResult(intent, SEND_PICTURE);
			break;
		case R.id.message_chat_tv_voice://点击发送声音
			Toast.makeText(this, "点击了发送声音", Toast.LENGTH_LONG).show();
			addLinearLayout.setVisibility(View.GONE);
			voiceLinearLayout.setVisibility(View.VISIBLE);
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
		
		final ChatDetailBean chatDetailBean = new ChatDetailBean();
		String username = currentUser.getUsername();
		chatDetailBean.setUsername(username);
		chatDetailBean.setGUID(DatabaseHelper.getUUID().toString());
		chatDetailBean.setMessage(edit_user_comment.getText().toString());
		chatDetailBean.setOther_username(otherUser.getUsername());
		chatDetailBean.setMessage_type(ChatConfiguration.TYPE_MESSAGE_TEXT);
		chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_SUCCEED);
		chatDetailBean.setMessage_read_status(MessageConfig.MESSAGE_NOT_RECEIVED);
		chatDetailBean.setPublish_time(TimeUtil.getCurrentMilliSecond());
		chatDetailBean.save(this, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO 自动生成的方法存根
				 try {
					//如果消息发送到服务器上面的数据库成功，消息的status默认为true，然后插入到本地数据库
					 
					mChatDetailDao.create(chatDetailBean);
					refreshRecent(chatDetailBean);
				} catch (SQLException e) {
					System.out.println("chatactivity将聊天信息插入本地数据库失败1");
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				//如果消息发送到服务器上面的数据库失败，则将消息的status设置为false，然后插入到本地数据库
				chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_FAILED);
				try {
					mChatDetailDao.create(chatDetailBean);
					refreshRecent(chatDetailBean);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					System.out.println("chatactivity将聊天信息插入本地数据库失败2");
					e.printStackTrace();
				}
			}
		});
		edit_user_comment.setText(null);
		mChatFragment.sendNewMessage(chatDetailBean);
	}
	
	
	/**用来保存发送的图片在本地的路径*/
	public String localPicturePath = null;
	
	/**
	 * 发送picture信息
	 * @param picPath 图片的路径
	 * @author yao
	 */
	private void sendPictureMessage(String picPath) {
		
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
		mChatFragment.sendNewMessage(chatDetailBean);
		
		
		BmobProFile.getInstance(
				ChatActivity.this).upload(picPath, new UploadListener() {
			
			@Override
			public void onError(int arg0, String arg1) {
				System.out.println("onError");
				System.out.println("upload e"+arg0);
				System.out.println("upload e"+arg1);
				//发送图片失败
				sendPicture2Db(false,chatDetailBean);
				
			}
			
			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				System.out.println("onSuccess");
				fileName = arg0; //获取文件上传成功后的文件名
				System.out.println("URL:" + url);
				BmobProFile.getInstance(ChatActivity.this).getAccessURL(fileName, new GetAccessUrlListener() {

		            @Override
		            public void onError(int errorcode, String errormsg) {
		                Log.i("bmob","获取文件的服务器地址失败："+errormsg+"("+errorcode+")");
		                //获取不了文件的可访问网络地址也被认作是发送失败
		                sendPicture2Db(false,chatDetailBean);
		            }

		            @Override
		            public void onSuccess(BmobFile file) {
		            	
		            	url = file.getUrl();//获取文件的有效网络url;
		                Log.i("bmob", "源文件名："+file.getFilename()+"，可访问的地址："+url);
		                sendPicture2Db(true,chatDetailBean);
		            }
		        });
			}
			
			@Override
			public void onProgress(int arg0) {
				System.out.println("onProgress");
				System.out.println(arg0);
			}
		});
		
		
		
	}
	
	
	//把数据传到数据库
	private void sendPicture2Db(Boolean isUpload,final ChatDetailBean chatDetailBean)
	{
		//文件上传成功
		if(isUpload){
			chatDetailBean.setMessage(url);
			chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_SUCCEED);
			chatDetailBean.save(this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO 自动生成的方法存根
					 try {
						 chatDetailBean.setMessage(localPicturePath);
						mChatDetailDao.create(chatDetailBean);
						refreshRecent(chatDetailBean);
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// 插入聊天信息到服务器的数据库中失败也当做是发送失败
					chatDetailBean.setMessage(localPicturePath);
					chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_FAILED);
					try {
						mChatDetailDao.create(chatDetailBean);
						refreshRecent(chatDetailBean);
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						System.out.println("将聊天信息插入本地数据库失败");
						e.printStackTrace();
					}
				}
			});
		}else{//文件上传失败
			chatDetailBean.setMessage(localPicturePath);
			chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_FAILED);
			try {
				mChatDetailDao.create(chatDetailBean);
				refreshRecent(chatDetailBean);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 发送语音消息
	 * @param localPath 语音文件在本地的路径
	 */
	private void sendVoiceMessage(String localPath) {
		// TODO 自动生成的方法存根
		BmobProFile.getInstance(
				ChatActivity.this).upload(localPath, new UploadListener() {
			
			@Override
			public void onError(int arg0, String arg1) {
				System.out.println("onError");
				System.out.println("upload e"+arg0);
				System.out.println("upload e"+arg1);
				sendVoice2Db(false);
			}
			
			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				System.out.println("onSuccess");
				System.out.println("uploadsfilename"+arg0);
				System.out.println("uploadsurl"+arg1);
				fileName = arg0; //获取文件上传成功后的文件名
				System.out.println("URL:" + url);
				BmobProFile.getInstance(ChatActivity.this).getAccessURL(fileName, new GetAccessUrlListener() {

		            @Override
		            public void onError(int errorcode, String errormsg) {
		                // TODO Auto-generated method stub
		                Log.i("bmob","获取文件的服务器地址失败："+errormsg+"("+errorcode+")");
		                sendVoice2Db(false);
		            }

		            @Override
		            public void onSuccess(BmobFile file) {
		                // TODO Auto-generated method stub
		            	url = file.getUrl();//获取文件的有效url;
		                Log.i("bmob", "源文件名："+file.getFilename()+"，可访问的地址："+url);
		                sendVoice2Db(true);
		            }
		        });

			}
			
			@Override
			public void onProgress(int arg0) {
				System.out.println("onProgress");
				System.out.println(arg0);
			}
		});
		
	}
	/**
	 * 将voice消息插入到数据库中
	 */
	private void sendVoice2Db(Boolean isUpload) {
		// TODO 自动生成的方法存根
		//chatDetailBean = new ChatDetailBean();
		final ChatDetailBean chatDetailBean = new ChatDetailBean();
		String username = currentUser.getUsername();
		chatDetailBean.setUsername(username);
		chatDetailBean.setGUID(DatabaseHelper.getUUID().toString());
		chatDetailBean.setOther_username(otherUser.getUsername());
		chatDetailBean.setMessage_type(ChatConfiguration.TYPE_MESSAGE_VOICE);
		chatDetailBean.setMessage_read_status(MessageConfig.MESSAGE_NOT_RECEIVED);
		chatDetailBean.setPublish_time(System.currentTimeMillis());
		//语音文件上传到服务器成功
		if(isUpload){
			chatDetailBean.setMessage(url);
			chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_SUCCEED);
			chatDetailBean.save(this, new SaveListener() {
				
				@Override
				public void onSuccess() {
					// TODO 自动生成的方法存根
					 try {
						 System.out.println("bmob将语音插入到服务器的数据库成功");
						 mChatDetailDao.create(chatDetailBean);
						 refreshRecent(chatDetailBean);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO 自动生成的方法存根
					chatDetailBean.setMessage(voicePath);
					chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_FAILED);
					try {
						System.out.println("bmob将语音插入到服务器的数据库失败");
						mChatDetailDao.create(chatDetailBean);
						refreshRecent(chatDetailBean);
					} catch (SQLException e) {
						// TODO 自动生成的 catch 块
						System.out.println("将聊天信息插入本地数据库失败");
						e.printStackTrace();
					}
				}
			});
		}else{//语音文件上传到服务器失败
			chatDetailBean.setMessage(voicePath);
			chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_FAILED);
			try {
				mChatDetailDao.create(chatDetailBean);
				refreshRecent(chatDetailBean);
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				System.out.println("将聊天信息插入本地数据库失败");
				e.printStackTrace();
			}
		}
		mChatFragment.sendNewMessage(chatDetailBean);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 自动生成的方法存根
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
			for(String pic:pics)
				System.out.println(pic);
			for(String pic:pics)
				sendPictureMessage(pic);
			break;

		default:
			break;
		}
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
		// TODO 自动生成的方法存根
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
		if (getWindow().getAttributes().softInputMode != WindowManager.
				LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	// 显示软键盘
	public void showSoftInputView() {
		if (getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
						.showSoftInput(edit_user_comment, 0);
		}
		hideBottom();
	}
	
	
	private void showEditEmoState(boolean isEmo) {
		// TODO 自动生成的方法存根
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
	public String getCurrenUsername()
	{
		return currentUser.getUsername();
	}
	
	
	/**
	 * 获得聊天对象的名字，即贰货号
	 * @return
	 */
	public String getOther_Username()
	{
		return otherUser.getUsername();
	}
	
	public User getOtherUser()
	{
		return otherUser;
	}
	
	
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
	}
	@Override
	protected void onRestart() {
		// TODO 自动生成的方法存根
		super.onRestart();
	}
	
	
	//
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
	}
	
	
	/**
	 * 如果调用此函数时正在播放语音，需要暂停播放
	 */
	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
	}
	@Override
	protected void onStop() {
		// TODO 自动生成的方法存根
		super.onStop();
	}
	
	/**
	 * 重定义actionbar的返回方法
	 */
	@Override
	public void onActionBarBack() {
		// TODO 自动生成的方法存根
		super.onActionBarBack();
		 EventBus.getDefault().post(  
                 new ExitChatEvent("back btn clicked"));  
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO 自动生成的方法存根
		super.onBackPressed();
		EventBus.getDefault().post(  
                new ExitChatEvent("back btn clicked")); 
	}
	/**
	 * 需要释放资源，聊天界面被destory时需要刷新消息列表
	 **/
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//释放录音的资源
		mRecondTouchListener.release();
		
	}
	
	
	/**
	 * 刷新消息列表
	 **/
	private void refreshRecent(ChatDetailBean chatDetailBean) {
		
		final Dao<ChatSnapshot, Integer> mChatSnapshotDao = mOrmDatabaseHelper.getChatSnapshotDao();
		
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
		mChatSnapshot.save(this,new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO 自动生成的方法存根
				try {
					DeleteBuilder<ChatSnapshot, Integer> deleteBuilder =mChatSnapshotDao.deleteBuilder();
		            deleteBuilder.where().eq("other_username",other_username).or().eq("username", other_username);
		            deleteBuilder.delete();
					System.out.println("bmob将数据插入到本地数据库中123");
					mChatSnapshotDao.create(mChatSnapshot);
					System.out.println("bmob将数据插入到本地数据库中");
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					System.out.println("bmob将数据插入到本地数据库失败或者删除数据失败");
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO 自动生成的方法存根
				mChatSnapshot.setlast_message_status(MessageConfig.SEND_MESSAGE_FAILED);
				try {
					DeleteBuilder<ChatSnapshot, Integer> deleteBuilder =mChatSnapshotDao.deleteBuilder();
		            deleteBuilder.where().eq("other_username",other_username);
		            deleteBuilder.delete();
					mChatSnapshotDao.create(mChatSnapshot);
					System.out.println("bmob将数据插入到本地数据库中");
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					System.out.println("bmob将最近一条消息插入到ChatSnapshot表中失败");
				}
			}
		});
	}
}