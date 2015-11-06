package com.lym.twogoods.fragment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.maxwin.view.XListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;

import com.j256.ormlite.dao.Dao;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.ChatSnapshot;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.db.OrmDatabaseHelper;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.message.MessageDialog;
import com.lym.twogoods.message.MessageDialog.MyItemOnClickListener;
import com.lym.twogoods.message.adapter.MessageListAdapter;
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
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return super.onCreateView(inflater, container, savedInstanceState);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		init();
	}
	

	private void init() {
		mSharePreferenceManager = SharePreferencesManager.getInstance();
		intiUser();
		initData();
		initView();
	}
	//初始化当前用户的信息
	private void intiUser() {
		// TODO 自动生成的方法存根
		currentUser = UserInfoManager.getInstance().getmCurrent();
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

	/**
	 * 初始化消息列表的数据
	 */
	private void initData() {
		mDatabaseHelper = new OrmDatabaseHelper(getActivity());
		mChatSnapshotDao = mDatabaseHelper.getChatSnapshotDao();
		chatSnapshotList = new ArrayList<ChatSnapshot>();
		try {
			chatSnapshotList = mChatSnapshotDao.queryForAll();
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
	}

	protected void setAdapter() {
	
//		int images[] = {R.drawable.user_default_head,R.drawable.user_default_head,
//				R.drawable.user_default_head,R.drawable.user_default_head,
//				R.drawable.user_default_head};
//		TestAdapter adapter = new TestAdapter(getActivity(), images);
		mMessageListAdapter = new MessageListAdapter(getActivity(), R.layout.message_list_item_conversation, chatSnapshotList);
		
		super.setAdapter(mMessageListAdapter);
	}
	
	/**
	 *查询全部最近的消息
	 * @return
	 */
	public List<ChatSnapshot> queryRecent()
	{
		List<ChatSnapshot> list = null;
		OrmDatabaseHelper helper = new OrmDatabaseHelper(getActivity());
		Dao<ChatSnapshot,Integer> mChatDao = helper.getChatSnapshotDao();
		Map<String, Object>map = new HashMap<String, Object>();
		map.put("username", getCurrentErHuoHao());
		try {
			list = mChatDao.queryForFieldValues(map);
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 获取当前用户名
	 * @return
	 */
	public String getCurrentErHuoHao() {
		if(currentUser==null)
		{
			currentUser = UserInfoManager.getInstance().getmCurrent();
		}
		return currentUser.getUsername();
	}
	
	public User getCurrentUser()
	{
		if(currentUser==null)
		{
			currentUser = UserInfoManager.getInstance().getmCurrent();
		}
			
		return currentUser;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		ChatSnapshot recent = mMessageListAdapter.getItem(position);
		showDeleteDialog(recent);
		return true;
	}
	
	public void showDeleteDialog(final ChatSnapshot recent) {
		
		final List<ChatSnapshot>list = queryRecent();
		dialogTips = new ArrayList<String>();
		dialogTips.add("删除会话");
		dialogTips.add("消息置顶");
		
		final MessageDialog mDialog = new MessageDialog(getActivity(), dialogTips);
		mDialog.show();
		mDialog.setItemOnClickListener(new MyItemOnClickListener() {

			@Override
			public void itemOnClick(int position) {
				
				ChatSnapshot mChatSnapshot = list.get(position);
				
				mMessageListAdapter.remove(mChatSnapshot); // 此处只是模仿数据删除，如果mTestList用的是数据库的数据或者是别的，需先删除数据库，否则下次进入程序还是会出现删除的数据
				
				try {
					mChatSnapshotDao.delete(mChatSnapshot);
				} catch (SQLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					System.out.println("从本地数据库中删除聊天失败");
				}
				mMessageListAdapter.notifyDataSetChanged();
				
				mDialog.cancel();
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	
		Intent intent = new Intent(getActivity(), ChatActivity.class);
		
		ChatSnapshot chatSnapshot = (ChatSnapshot) mAdapter.getItem(position);
		
		User otherUser = new User();
		otherUser.setUsername(chatSnapshot.getUsername());
		otherUser.setHead_url(chatSnapshot.getHead_url());
		
		intent.putExtra("otherUser", otherUser);		
		startActivity(intent);
	}

	/**判断当前fragment是否显示在屏幕上，如果显示，则刷新界面*/
	private boolean ishidden;
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.ishidden = hidden;
		if(!ishidden){
			refreshMessage();
		}
	}

	/*
	 * 向下滑动时调用
	 * （非 Javadoc）
	 * @see com.lym.twogoods.fragment.base.PullListFragment#onRefresh()
	 *
	 * @auther yao
	 */
	
	@Override
	public void onRefresh() {
		super.onRefresh();
		//获取上一次刷新的时间
		String lasttime_refresh = mSharePreferenceManager.getString(
				getActivity(), "lasttime_refresh", "failure");
		if(lasttime_refresh=="failure")
		{
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
	 * 
	 * @auther yao
	 */
	public void refreshMessage()
	{
		System.out.println("refreshMessage<<<<<<<<<<<<<<<<<<<<<<");
		try {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					int images[] = {R.drawable.user_default_head,R.drawable.user_default_head,
							R.drawable.user_default_head,R.drawable.user_default_head,
							R.drawable.user_default_head};
					//TestAdapter adapter = new TestAdapter(getActivity(), images);
					
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
		// TODO 自动生成的方法存根
		super.stopRefresh();
	}
	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		//refreshMessage();
		super.onResume();
	}

}
