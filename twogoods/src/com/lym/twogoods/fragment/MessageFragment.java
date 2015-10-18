package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.List;

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
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

import com.lym.twogoods.R;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.message.MessageDialog;
import com.lym.twogoods.message.MessageDialog.MyItemOnClickListener;
import com.lym.twogoods.message.adapter.MessageAdapter;
import com.lym.twogoods.message.adapter.TestAdapter;
import com.lym.twogoods.message.ui.ChatActivity;
import com.lym.twogoods.utils.SharePreferenceManager;
import com.lym.twogoods.utils.TimeUtil;


/**
 * 消息的fragment，列出最近全部最近联系的人。
 * @author yao
 *
 */

public class MessageFragment extends PullListFragment implements 
	OnItemClickListener,OnItemLongClickListener{
	
	public BmobUserManager userManager;
	public BmobChatManager chatManager;
	
	private MessageAdapter adapter;
	/*用来保存上一次刷新的时间*/
	private SharePreferenceManager mSharePreferenceManager;
	
	private List<String> dialogTips;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		userManager = BmobUserManager.getInstance(getActivity());
		chatManager = BmobChatManager.getInstance(getActivity());
		
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
		initView();
	}
	

	private void initView() {
		mSharePreferenceManager = SharePreferenceManager.getInstance();
		setMode(Mode.PULLDOWN);
		setAdapter();
		System.out.println("ref initView");
		
		mListView.setOnItemClickListener(this);
		mListView.setOnItemLongClickListener(this);
		System.out.println("ref"+getMode());
	}

	
	protected void setAdapter() {
	
//		 adapter = new MessageAdapter(getActivity(),
//				R.layout.message_list_item_conversation,BmobDB.
//				create(getActivity()).queryRecents());
		int images[] = {R.drawable.user_default_head,R.drawable.user_default_head,
				R.drawable.user_default_head,R.drawable.user_default_head,
				R.drawable.user_default_head};
		TestAdapter adapter = new TestAdapter(getActivity(), images);
		 
		super.setAdapter(adapter);
	}
	

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		BmobRecent recent = adapter.getItem(position);
		showDeleteDialog(recent);
		return true;
	}
	
	public void showDeleteDialog(final BmobRecent recent) {
		
		final List<BmobRecent> list = BmobDB.create(getActivity()).queryRecents();
		
		dialogTips = new ArrayList<String>();
		dialogTips.add("删除会话");
		dialogTips.add("消息置顶");
		
		final MessageDialog mDialog = new MessageDialog(getActivity(), dialogTips);
		mDialog.show();
		mDialog.setItemOnClickListener(new MyItemOnClickListener() {

			@Override
			public void itemOnClick(int position) {
				
				BmobRecent mBmobRecent = list.get(position);
				
				adapter.remove(mBmobRecent); // 此处只是模仿数据删除，如果mTestList用的是数据库的数据或者是别的，需先删除数据库，否则下次进入程序还是会出现删除的数据
				
				BmobDB.create(getActivity()).deleteRecent(mBmobRecent.getTargetid());
				adapter.notifyDataSetChanged();
				
				mDialog.cancel();
			}
		});

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
//		
//		BmobRecent recent = adapter.getItem(position);
//		//重置未读消息
//		BmobDB.create(getActivity()).resetUnread(recent.getTargetid());
//		//组装聊天对象
//		BmobChatUser user = new BmobChatUser();
//		user.setAvatar(recent.getAvatar());
//		user.setNick(recent.getNick());
//		user.setUsername(recent.getUserName());
//		user.setObjectId(recent.getTargetid());
		Intent intent = new Intent(getActivity(), ChatActivity.class);
//		intent.putExtra("user", user);
		startActivity(intent);
	}

	private boolean ishidden;
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		this.ishidden = hidden;
		if(!hidden){
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
					TestAdapter adapter = new TestAdapter(getActivity(), images);
					
//					adapter = new MessageAdapter(getActivity(), R.layout.message_list_item_conversation, 
//							BmobDB.create(getActivity()).queryRecents());
//					setAdapter(adapter);
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
