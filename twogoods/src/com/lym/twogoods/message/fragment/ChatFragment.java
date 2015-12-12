package com.lym.twogoods.message.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.TextView;

import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.GetAccessUrlListener;
import com.bmob.btp.callback.UploadListener;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ChatConfiguration;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.message.NewMessageReceiver;
import com.lym.twogoods.message.adapter.MessageChatAdapter;
import com.lym.twogoods.message.config.MessageConfig;
import com.lym.twogoods.utils.DatabaseHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;

public class ChatFragment extends PullListFragment{
	
	private final String TAG = "ChatFragment" ;
	OrmDatabaseHelper mOrmDatabaseHelper;
	/**聊天表*/
	private Dao<ChatDetailBean, Integer> mChatDetailDao;
	/**聊天适配器*/
	private MessageChatAdapter mMessageChatAdapter;
	/**聊天对象*/
	private User otherUser;
	/**当前用户*/
	private User currentUser;
	/**当前用户与聊天对象的消息记录*/
	List<ChatDetailBean> mList = null;
	
	Handler mHandler;
	
	public ChatFragment(){
		super();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.i(TAG,"onStart");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private void init() {
		initCurrentUser();
		initData();
		initXlistView();
	}
	
	/**
	 * 初始化数据
	 */
	private void initData() {
		//初始化本地聊天数据库
		mOrmDatabaseHelper = new OrmDatabaseHelper(getActivity());
		mChatDetailDao = mOrmDatabaseHelper.getChatDetailDao();
		mList = initMsgData();
	}

	/**
	 * 初始化当前用户信息
	 */
	private void initCurrentUser() {
		// TODO 自动生成的方法存根
		currentUser = UserInfoManager.getInstance().getmCurrent();
	}
	/**
	 * 初始化聊天对象消息,由ChatActivity来调用
	 * @param user
	 */
	public void initOtherUser(User user) {
		// TODO 自动生成的方法存根
		this.otherUser = user;
	}
	/**
	 * 设置ChatFragment和ChatActivity之间的handler,由ChatActivity来调用
	 * @param handler
	 */
	public void setHandler(Handler handler)
	{
		mHandler = handler;
	}

	private void initXlistView() {
		// TODO 自动生成的方法存根
		setMode(Mode.PULLDOWN);
		
		initOrRefresh();
		//当弹出软键盘时如果ListView最后一条Item可见那么将ListView顶上去(要配合windowSoftInputMode="adjustResize")
		mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		mListView.setFooterDividersEnabled(false);
		mListView.setHeaderDividersEnabled(false);
		mListView.setDivider(null);
		mListView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				hideSoftInputView();
				hideBottom();
				return false;
			}
		});
		// 重发按钮的点击事件
		mMessageChatAdapter.setOnInViewClickListener(R.id.iv_fail_resend,
					new MessageChatAdapter.onInternalClickListener() {
					@Override
					public void OnClickListener(View parentV, View v,Integer position, Object values) {
						// 重发消息
						reSendMessage(parentV, v, values);
					}
				});
	}
	
	/**
	 * 消息发送失败时点击重发按钮
	 * @param parentV
	 * @param v
	 * @param values
	 */
	protected void reSendMessage(View parentV, View v, Object values) {
		
		switch(((ChatDetailBean) values).getMessage_type()){
		case ChatConfiguration.TYPE_MESSAGE_PICTURE:
			resendPicMsg(parentV,values);
			break;
		case ChatConfiguration.TYPE_MESSAGE_VOICE:
			resendVoiceMsg(parentV,values);
			break;
		case ChatConfiguration.TYPE_MESSAGE_TEXT:
			resendTextMsg(parentV, values);
			break;
		}
	}
	
	/**暂存语音的可访问网络url*/
	String voiceUrl;
	/** 上传语音到服务器后返回的文件名字*/
	String voiceFileName;
	/**要重发的语音在本地的路径*/
	String resendVoicePath;
	
	
	/**
	 * 重发语音消息
	 * @param parentV
	 * @param values
	 */
	
	private void resendVoiceMsg(final View parentV, final Object values) {
		resendVoicePath = ((ChatDetailBean)values).getMessage();
		String username = currentUser.getUsername();
		final ChatDetailBean chatDetailBean = new ChatDetailBean();
		chatDetailBean.setUsername(username);
		chatDetailBean.setGUID(DatabaseHelper.getUUID().toString());
		chatDetailBean.setOther_username(otherUser.getUsername());
		chatDetailBean.setMessage_type(ChatConfiguration.TYPE_MESSAGE_VOICE);
		chatDetailBean.setPublish_time(System.currentTimeMillis());
		chatDetailBean.setMessage_read_status(MessageConfig.MESSAGE_NOT_RECEIVED);
		chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_ING);
		chatDetailBean.setMessage(resendVoicePath);
		
		 try {
			 mChatDetailDao.delete((ChatDetailBean)values);
			 mChatDetailDao.create(chatDetailBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		BmobProFile.getInstance(getActivity()).upload(resendVoicePath, new UploadListener() {
			@Override
			public void onError(int arg0, String arg1) {
				sendVoice2Db(false,parentV,chatDetailBean);
			}
			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				voiceFileName = arg0; //获取文件上传成功后的文件名
				BmobProFile.getInstance(getActivity()).getAccessURL(voiceFileName, new GetAccessUrlListener() {
		            @Override
		            public void onError(int errorcode, String errormsg) {
		                Log.i(TAG,"获取文件的服务器地址失败："+errormsg+"("+errorcode+")");
		                sendVoice2Db(false,parentV,chatDetailBean);
		            }
		            @Override
		            public void onSuccess(BmobFile file) {
		            	voiceUrl = file.getUrl();//获取文件的有效url;
		                Log.i(TAG, "源文件名："+file.getFilename()+"，可访问的地址："+voiceUrl);
		                sendVoice2Db(true,parentV,chatDetailBean);
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
	private void sendVoice2Db(Boolean isUpload,final View parentV, final ChatDetailBean chatDetailBean) {
		//语音文件上传到服务器成功
		if(isUpload){
			chatDetailBean.setMessage(voiceUrl);
			chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_SUCCEED);
			chatDetailBean.save(getActivity(), new SaveListener() {
				@Override
				public void onSuccess() {
					 try {
						 parentV.findViewById(R.id.progress_load).setVisibility(View.INVISIBLE);
						 parentV.findViewById(R.id.iv_fail_resend).setVisibility(View.INVISIBLE);
						 parentV.findViewById(R.id.tv_send_status).setVisibility(View.GONE);
						 parentV.findViewById(R.id.tv_voice_length).setVisibility(View.VISIBLE);
						 Log.i(TAG,"bmob将语音插入到服务器的数据库成功");
						 
						 mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 0 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
						 notifyChange(MessageConfig.SEND_MESSAGE_SUCCEED);
					 } catch (SQLException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					parentV.findViewById(R.id.progress_load).setVisibility(View.INVISIBLE);
					parentV.findViewById(R.id.iv_fail_resend).setVisibility(View.VISIBLE);
					parentV.findViewById(R.id.tv_send_status).setVisibility(View.INVISIBLE);
					 try {
						 mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
						 notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
					 } catch (SQLException e1) {
						e1.printStackTrace();
					}
					chatDetailBean.setMessage(resendVoicePath);
					chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_FAILED);
					try {
						mChatDetailDao.create(chatDetailBean);
					} catch (SQLException e) {
						Log.i(TAG,"bmob将语音插入到服务器的数据库失败");
						e.printStackTrace();
					}
				}
			});
		}else{//语音文件上传到服务器失败
			parentV.findViewById(R.id.progress_load).setVisibility(View.INVISIBLE);
			parentV.findViewById(R.id.iv_fail_resend).setVisibility(View.VISIBLE);
			parentV.findViewById(R.id.tv_send_status).setVisibility(View.INVISIBLE);
			try {
				mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
				notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
			} catch (SQLException e) {
				Log.i(TAG,"将聊天信息插入本地数据库失败");
				e.printStackTrace();
			}
		}
	}

	/**
	 * 重发未发送成功的文本消息
	 * @param parentV
	 * @param values
	 */
	private void resendTextMsg(final View parentV, Object values) {
		
		final ChatDetailBean msg = (ChatDetailBean)values;
		msg.setPublish_time(System.currentTimeMillis());
		msg.setLast_Message_Status(MessageConfig.SEND_MESSAGE_ING);
		try {
			mChatDetailDao.delete((ChatDetailBean)values);
			mChatDetailDao.create(msg);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		msg.save(getActivity(),new SaveListener() {
			@Override
			public void onSuccess() {
				parentV.findViewById(R.id.progress_load).setVisibility(View.INVISIBLE);
				parentV.findViewById(R.id.iv_fail_resend).setVisibility(View.INVISIBLE);
				parentV.findViewById(R.id.tv_send_status).setVisibility(View.VISIBLE);
				try {
					mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 0 WHERE GUID = '"+msg.getGUID()+"'");
					notifyChange(MessageConfig.SEND_MESSAGE_SUCCEED);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailure(int arg0, String arg1) {
				parentV.findViewById(R.id.progress_load).setVisibility(View.INVISIBLE);
				parentV.findViewById(R.id.iv_fail_resend).setVisibility(View.VISIBLE);
				parentV.findViewById(R.id.tv_send_status).setVisibility(View.INVISIBLE);
				try {
					mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+msg.getGUID()+"'");
					notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**暂存图片的可访问网络url*/
	String picUrl;
	/** 上传图片到服务器后返回的文件名字*/
	String fileName;
	/**要重发的图片在本地的路径*/
	String resendPicLocalPath;
	
	/**
	 * 重发重发未发送成功的图片和语音消息
	 * @param parentV
	 * @param values
	 */
	private void resendPicMsg(final View parentV, final Object values) {
		final ChatDetailBean msg = (ChatDetailBean)values;
		resendPicLocalPath = msg.getMessage();
		final ChatDetailBean chatDetailBean = new ChatDetailBean();
		String username = currentUser.getUsername();
		chatDetailBean.setUsername(username);
		chatDetailBean.setGUID(DatabaseHelper.getUUID().toString());
		chatDetailBean.setOther_username(otherUser.getUsername());
		chatDetailBean.setMessage_type(ChatConfiguration.TYPE_MESSAGE_PICTURE);
		chatDetailBean.setMessage_read_status(MessageConfig.MESSAGE_NOT_RECEIVED);
		chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_ING);
		chatDetailBean.setPublish_time(System.currentTimeMillis());
		chatDetailBean.setMessage(resendPicLocalPath);
		try {
			 mChatDetailDao.delete((ChatDetailBean)values);
			 mChatDetailDao.create(chatDetailBean);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		BmobProFile.getInstance(
				getActivity()).upload(msg.getMessage(), new UploadListener() {
			
			@Override
			public void onError(int arg0, String arg1) {
				sendPicture2Db(false,parentV,chatDetailBean);
			}
			@Override
			public void onSuccess(String arg0, String arg1, BmobFile arg2) {
				fileName = arg0; //获取文件上传成功后的文件名
				BmobProFile.getInstance(getActivity()).getAccessURL(fileName, new GetAccessUrlListener() {
		            @Override
		            public void onError(int errorcode, String errormsg) {
		                Log.i(TAG,"获取文件的服务器地址失败："+errormsg+"("+errorcode+")");
		                sendPicture2Db(false,parentV,chatDetailBean);
		            }
		            @Override
		            public void onSuccess(BmobFile file) {
		            	picUrl = file.getUrl();//获取文件的有效url;
		                Log.i(TAG, "源文件名："+file.getFilename()+"，可访问的地址："+picUrl);
		                sendPicture2Db(true,parentV,chatDetailBean);
		            }
		        });
			}
			@Override
			public void onProgress(int arg0) {
			}
		});
	}
	
//把数据传到数据库
	private void sendPicture2Db(Boolean isUpload,final View parentV, final ChatDetailBean chatDetailBean)
	{
		//文件上传成功
		if(isUpload){
			chatDetailBean.setMessage(picUrl);
			chatDetailBean.setLast_Message_Status(MessageConfig.SEND_MESSAGE_SUCCEED);
			chatDetailBean.save(getActivity(), new SaveListener() {
				@Override
				public void onSuccess() {
					 parentV.findViewById(R.id.progress_load).setVisibility(View.INVISIBLE);
					 parentV.findViewById(R.id.iv_fail_resend).setVisibility(View.INVISIBLE);
					 parentV.findViewById(R.id.tv_send_status).setVisibility(View.VISIBLE);
					 try {
						 mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 0 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
						 notifyChange(MessageConfig.SEND_MESSAGE_SUCCEED);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				@Override
				public void onFailure(int arg0, String arg1) {
					// 插入聊天信息到服务器的数据库中失败也当做是发送失败
					parentV.findViewById(R.id.progress_load).setVisibility(View.INVISIBLE);
					parentV.findViewById(R.id.iv_fail_resend).setVisibility(View.VISIBLE);
					parentV.findViewById(R.id.tv_send_status).setVisibility(View.INVISIBLE);
					try {
						mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
						notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
					} catch (SQLException e) {
						Log.i(TAG,"将聊天信息插入本地数据库失败");
						e.printStackTrace();
					}
				}
			});
		}else{//文件上传失败
			parentV.findViewById(R.id.progress_load).setVisibility(View.INVISIBLE);
			parentV.findViewById(R.id.iv_fail_resend).setVisibility(View.VISIBLE);
			parentV.findViewById(R.id.tv_send_status).setVisibility(View.INVISIBLE);
			try {
				mChatDetailDao.updateRaw("UPDATE chatdetailbean SET last_message_status = 1 WHERE GUID = '"+chatDetailBean.getGUID()+"'");
				notifyChange(MessageConfig.SEND_MESSAGE_FAILED);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * 初始化和刷新聊天信息数据
	 * @author yao
	 */
	public void initOrRefresh()
	{
		if(mMessageChatAdapter!=null)
		{
			if(NewMessageReceiver.newMsgNums>0){
				int news=  NewMessageReceiver.newMsgNums;//有可能锁屏期间，来了N条消息,因此需要倒叙显示在界面上
				int size = initMsgData().size();
				for(int i=(news-1);i>=0;i--){
					mMessageChatAdapter.add(initMsgData().get(size-(i+1)));// 添加最后一条消息到界面显示
				}
				mListView.setSelection(mAdapter.getCount() - 1);
			} else {
			}
		}else{
			Log.i(TAG,"12345new一个adapter");
			setAdapter();
		}
		mListView.setSelection(mMessageChatAdapter.getCount()-1);
		
	}
	
	public void sendNewMessage(ChatDetailBean chatBean)
	{
		mList.add(chatBean);
		initOrRefresh();
	}
	
	public void receiveNewMessage(List<ChatDetailBean> list)
	{
		for(int i = 0;i<list.size();i++)
		{
			mList.add(list.get(i));
		}
		initOrRefresh();
	}
	/**
	 * 通知消息是否发送成功
	 * @param status
	 */
	public void notifyChange(int status)
	{
		if(status==MessageConfig.SEND_MESSAGE_SUCCEED){
			mList.get(mList.size()-1).setLast_Message_Status(MessageConfig.SEND_MESSAGE_SUCCEED);
		}else{
			if(status==MessageConfig.SEND_MESSAGE_FAILED){
				mList.get(mList.size()-1).setLast_Message_Status(MessageConfig.SEND_MESSAGE_FAILED);
			}
		}
		Log.i(TAG,"执行了notifyChange");
		mMessageChatAdapter.notifyDataSetChanged();
	}

	private void setAdapter() {
		mMessageChatAdapter = new MessageChatAdapter(getActivity(), mList);
		super.setAdapter(mMessageChatAdapter);
	}
	
	/**
	 * 从本地ChatDetailBean数据表获取当前用户与聊天对象的聊天记录信息
	 * @return
	 */
	public List<ChatDetailBean> initMsgData(){
		List<ChatDetailBean> list = null;
		if(mChatDetailDao==null){
			mOrmDatabaseHelper = new OrmDatabaseHelper(getActivity());
			mChatDetailDao = mOrmDatabaseHelper.getChatDetailDao();
		}
		QueryBuilder<ChatDetailBean, Integer>mQueryBuilder = mChatDetailDao.queryBuilder();
		try {
			Where<ChatDetailBean, Integer> where = mQueryBuilder.orderBy("publish_time", true).where().eq("username",otherUser.getUsername()).
					or().eq("other_username", otherUser.getUsername());
			list = where.query();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public void addSendPicture(List<String> paths)
	{
		
	}
	
	/**
	 * 通知ChatActivity隐藏底部布局
	 */
	protected void hideBottom() {
		// TODO 自动生成的方法存根
		Message msg = new Message();
		msg.what = MessageConfig.HIDE_BOTTOM;
		mHandler.sendMessage(msg);
	}
	/**
	 *  隐藏软键盘
	 */
	protected void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.
				LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	@Override
	public void onStop() {
		Log.i(TAG,"onStop");
		super.onStop();
	}
	@Override
	public void onDestroy() {
		Log.i(TAG,"onDestroy");
		super.onDestroy();
	}

}
