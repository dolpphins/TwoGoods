package com.lym.twogoods.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.message.JudgeConfig;
import com.lym.twogoods.message.MessageDialog;
import com.lym.twogoods.message.MessageDialog.MyItemOnClickListener;
import com.lym.twogoods.message.adapter.MessageListAdapter;
import com.lym.twogoods.message.adapter.TestAdapter;
import com.lym.twogoods.message.ui.ChatActivity;
import com.lym.twogoods.utils.SharePreferencesManager;
import com.lym.twogoods.utils.TimeUtil;


/**
 * 消息的fragment，列出最近全部最近联系的人。
 * @author yao
 *
 */

public class MessageFragment extends PullListFragment implements 
	OnItemClickListener,OnItemLongClickListener{
	private String TAG = "MessageFragment";
	private Boolean isLogining;

	//xListView的adapter
	private MessageListAdapter mMessageListAdapter;
	/*用来保存上一次刷新的时间*/
	private SharePreferencesManager mSharePreferenceManager;
	
	private List<String> dialogTips;
	/**最近消息列表的全部信息*/
	private List<ChatSnapshot> chatSnapshotList;
	/**最近消息表*/
	private Dao<ChatSnapshot,Integer> mChatSnapshotDao;
	/**当前用户*/
	private User currentUser;
	
	OrmDatabaseHelper mDatabaseHelper;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		init();
	}
	

	private void init() {
		mSharePreferenceManager = SharePreferencesManager.getInstance();
		isLogining = UserInfoManager.getInstance().isLogining();
		intiUser();
		initData();
		initView();
	}
	//初始化当前用户的信息
	private void intiUser() {
		currentUser = UserInfoManager.getInstance().getmCurrent();
	}
	
	/**
	 * 初始化消息列表的数据
	 */
	private void initData() {
		mDatabaseHelper = new OrmDatabaseHelper(getActivity());
		mChatSnapshotDao = mDatabaseHelper.getChatSnapshotDao();
		chatSnapshotList = new ArrayList<ChatSnapshot>();
		chatSnapshotList = queryRecent();
	}


	/**
	 * 初始化mListView
	 */
	private void initView() {
		setMode(Mode.PULLDOWN);
		setAdapter();
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
	}


	protected void setAdapter() {
		if(isLogining){
			mMessageListAdapter = new MessageListAdapter(getActivity(),
					R.layout.message_list_item_conversation, chatSnapshotList);
			super.setAdapter(mMessageListAdapter);
			
		}else{
			int images[] = {R.drawable.user_default_head,R.drawable.user_default_head,
					R.drawable.user_default_head,R.drawable.user_default_head,
					R.drawable.user_default_head};
			TestAdapter adapter = new TestAdapter(getActivity(), images);
			super.setAdapter(adapter);
		}
	}
	
	/**
	 *查询全部最近的消息
	 * @return
	 */
	public List<ChatSnapshot> queryRecent(){
		List<ChatSnapshot> list = null;
		OrmDatabaseHelper helper = new OrmDatabaseHelper(getActivity());
		Dao<ChatSnapshot,Integer> mChatDao = helper.getChatSnapshotDao();
		try {
			list = mChatDao.queryBuilder().orderBy("last_time", false).query();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 获取当前用户名
	 * @return
	 */
	public String getCurrentUsername() {
		if(currentUser==null){
			currentUser = UserInfoManager.getInstance().getmCurrent();
		}
		return currentUser.getUsername();
	}
	
	public User getCurrentUser(){
		if(currentUser==null){
			currentUser = UserInfoManager.getInstance().getmCurrent();
		}
		return currentUser;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,int position, long id) {
		if(isLogining){
			ChatSnapshot recent = mMessageListAdapter.getItem(position);
			showDeleteDialog(recent);
		}
		return true;
	}
	
	public void showDeleteDialog(final ChatSnapshot recent) {
		dialogTips = new ArrayList<String>();
		dialogTips.add("删除会话");
		
		final MessageDialog mDialog = new MessageDialog(getActivity(), dialogTips);
		mDialog.show();
		mDialog.setItemOnClickListener(new MyItemOnClickListener() {
			@Override
			public void itemOnClick(int position) {
				String name = recent.getUsername();
				if(name.equals(currentUser.getUsername())){
					try {
						DeleteBuilder<ChatSnapshot, Integer> deleteBuilder = mChatSnapshotDao.deleteBuilder();
						deleteBuilder.where().eq("other_username", recent.getOther_username());
						deleteBuilder.delete();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}else{
					try {
						DeleteBuilder<ChatSnapshot, Integer> deleteBuilder = mChatSnapshotDao.deleteBuilder();
						deleteBuilder.where().eq("username", recent.getUsername());
						deleteBuilder.delete();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
				refreshMessage();
				mMessageListAdapter.notifyDataSetChanged();
				mDialog.cancel();
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
			Intent intent = new Intent(getActivity(), ChatActivity.class);
			
			ChatSnapshot chatSnapshot = (ChatSnapshot) mMessageListAdapter.getItem(position);
			//有未读的消息点击后要将unread_num置为0
			if(chatSnapshot.getUnread_num()>0){
				if(chatSnapshot.getUsername().equals(getCurrentUsername())){//如果最近一条消息是用户自己发出去的
					try {
						String name = chatSnapshot.getOther_username();
						int i = mChatSnapshotDao.updateRaw("UPDATE chatsnapshot SET unread_num = 0 WHERE other_username = '"+name+"'");
						Log.i(TAG,i+"返回值");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else{
					try {
						String name = chatSnapshot.getUsername();
						mChatSnapshotDao.updateRaw("UPDATE chatsnapshot SET unread_num = 0 WHERE username = '"+name+"'");
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
			User otherUser = new User();
			String name;
			if(chatSnapshot.getUsername().equals(getCurrentUsername())){
				name = chatSnapshot.getOther_username();
			}else{
				name = chatSnapshot.getUsername();
			}
			otherUser.setHead_url(chatSnapshot.getHead_url());
			otherUser.setUsername(name);
			
			intent.putExtra("otherUser", otherUser);	
			intent.putExtra("from", JudgeConfig.FRAM_MESSAGE_LIST);
			startActivity(intent);
		
	}

	/**判断当前fragment是否显示在屏幕上，如果显示，则刷新界面*/
	private boolean ishidden;
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.ishidden = hidden;
		if(isLogining){
			if(!ishidden){
				refreshMessage();
			}
		}
	}

	/*
	 * 向下滑动时调用
	 * @auther yao
	 */
	
	@Override
	public void onRefresh() {
		super.onRefresh();
		//获取上一次刷新的时间
		String lasttime_refresh = mSharePreferenceManager.getString(
				getActivity(), "lasttime_refresh", "failure");
		if(lasttime_refresh=="failure"){
			lasttime_refresh = TimeUtil.getCurrentTime(null);
		}
		String time = TimeUtil.getDescriptionTimeFromTimestamp(
				TimeUtil.stringToLong(lasttime_refresh, null));
		mSharePreferenceManager.putString(getActivity(), 
				"lasttime_refresh", TimeUtil.getCurrentTime(null));
		//设置刷新时间
		setLastRefreshTime(time);
		
		refreshMessage();
		
		stopRefresh();
	}
	
	/*
	 * 刷新message列表
	 * @auther yao
	 */
	public void refreshMessage()
	{
		try {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					
					chatSnapshotList = queryRecent();
					mMessageListAdapter = new MessageListAdapter(getActivity(), 
							R.layout.message_list_item_conversation, chatSnapshotList);
					setAdapter(mMessageListAdapter);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void stopRefresh() {
		super.stopRefresh();
	}
	@Override
	public void onResume() {
		//refreshMessage();
		super.onResume();
	}
	
}
