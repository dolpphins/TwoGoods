package com.lym.twogoods.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.ThumbnailMap;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.Location;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.dialog.FastLoginDialog;
import com.lym.twogoods.eventbus.event.ExitChatEvent;
import com.lym.twogoods.eventbus.event.UserStatus;
import com.lym.twogoods.fragment.IndexFragment;
import com.lym.twogoods.fragment.MessageFragment;
import com.lym.twogoods.fragment.MineFragment;
import com.lym.twogoods.fragment.NearbyFragment;
import com.lym.twogoods.index.ui.GoodsSearchActivity;
import com.lym.twogoods.nearby.ui.SelectCityActivity;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.publish.ui.PublishGoodsActivity;
import com.lym.twogoods.receiver.NetworkTipsBroadcastReceiver;
import com.lym.twogoods.screen.DisplayUtils;
import com.lym.twogoods.screen.GoodsScreen;
import com.lym.twogoods.service.ChatService;
import com.lym.twogoods.ui.base.BottomDockFragmentActivity;
import com.lym.twogoods.viewholder.TabViewHolder;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * <p>App主Activity</p>
 * 
 * @author 麦灿标
 * */
public class MainActivity extends BottomDockFragmentActivity implements View.OnClickListener{

	private final static String TAG = "MainActivity";
	
//	/**
//	 * <p>底部Tab枚举类</p>
//	 * */
//	private static enum Tab {
//		
//		INDEX,NEARBY,PUBLISH,MESSAGE,MINE
//	}
	
	/** Tab数 */
	private final int tabCount = 4;
	
	/** 底部Tab Fragment */
	private Fragment[] mTabFragments = new Fragment[tabCount]; 
	
	private MessageFragment mMessageFragment;
	
	/** 底部Tab布局 */
	private View mTabView;

	/** 底部Tab ViewHolder */
	private TabViewHolder mTabViewHolder;
	/** TextView数组存储 */
	private TextView[] tabTextView = new TextView[tabCount];
	/** 底部Tab文字 */
	private String[] tabText = new String[tabCount];
	
	/** 当前选择Tab索引 */
	private int currentTabIndex = 0;
	/**为连接网络的广播接受者*/
	private NetworkTipsBroadcastReceiver mNetworkTipsBroadcastReceiver = new NetworkTipsBroadcastReceiver();
	/**要显示的Tab*/
	private int showTabIndex = 0;
	/**接收新消息的服务*/
	Intent chatService;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		appInit();
		
		initTabFragment();
		startService();
		
		if(!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
	}

	//App相关初始化
	public void appInit() {
		GoodsScreen.init(this);
	}
	
	@Override
	public View onCreateBottomView() {
		mTabView = getLayoutInflater().inflate(R.layout.app_main_bottomdock_tab, null);
		setTabClickEvent();
		return mTabView;
	}
	
	@Override
	protected void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		//开始时显示首页
		resetTab(showTabIndex);
		showSelectTabFragment(showTabIndex);
		//初始化ActionBar
		setActionBar(showTabIndex);
	}


	/**
	 * 开启后台service,
	 */
	private void startService() {
		
		if(UserInfoManager.getInstance().isLogining()){//如果用户已经登录,开启service,否则不开启
			chatService = new Intent(MainActivity.this,ChatService.class);
			this.startService(chatService);
		}
	}
	
	private void initTabFragment() {
		mTabFragments[0] = new IndexFragment();
		mTabFragments[1] = new NearbyFragment();
		mTabFragments[2] = new MessageFragment();
		mMessageFragment = (MessageFragment) mTabFragments[2];
		mTabFragments[3] = new MineFragment();
		
		addFragment(mTabFragments[0]);
		addFragment(mTabFragments[1]);
		addFragment(mTabFragments[2]);
		addFragment(mTabFragments[3]);
		
		tabText[0] = getResources().getString(R.string.home);
		tabText[1] = getResources().getString(R.string.nearby);
		tabText[2] = getResources().getString(R.string.message);
		tabText[3] = getResources().getString(R.string.mine);
	}
	
	private void setTabClickEvent() {
		if(mTabView != null) {
			mTabViewHolder = new TabViewHolder();
			mTabViewHolder.tab_home_btn = (TextView) mTabView.findViewById(R.id.tab_home_btn);
			mTabViewHolder.tab_nearby_btn = (TextView) mTabView.findViewById(R.id.tab_nearby_btn);
			mTabViewHolder.publish_btn = (ImageView) mTabView.findViewById(R.id.publish_btn);
			mTabViewHolder.tab_message_btn = (TextView) mTabView.findViewById(R.id.tab_message_btn);
			mTabViewHolder.tab_mine_btn = (TextView) mTabView.findViewById(R.id.tab_mine_btn);
			
			//便于设置选择状态
			tabTextView[0] = mTabViewHolder.tab_home_btn;
			tabTextView[1] = mTabViewHolder.tab_nearby_btn;
			tabTextView[2] = mTabViewHolder.tab_message_btn;
			tabTextView[3] = mTabViewHolder.tab_mine_btn;
			
			if(mTabViewHolder.tab_home_btn != null) {
				mTabViewHolder.tab_home_btn.setOnClickListener(this);
			}
			if(mTabViewHolder.tab_nearby_btn != null) {
				mTabViewHolder.tab_nearby_btn.setOnClickListener(this);
			}
			if(mTabViewHolder.publish_btn != null) {
				mTabViewHolder.publish_btn.setOnClickListener(this);
			}
			if(mTabViewHolder.tab_message_btn != null) {
				mTabViewHolder.tab_message_btn.setOnClickListener(this);
			}
			if(mTabViewHolder.tab_mine_btn != null) {
				mTabViewHolder.tab_mine_btn.setOnClickListener(this);
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		
		int selectTabIndex = -1;
		switch(v.getId()) {
		//点击底部Tab首页
		case R.id.tab_home_btn:
			selectTabIndex = 0;
			break;
		//点击底部Tab附近
		case R.id.tab_nearby_btn:
			selectTabIndex = 1;
			break;
		//点击底部发布
		case R.id.publish_btn:
			//特殊处理,开启Activity
			if (!UserInfoManager.getInstance().isLogining()) {
				FastLoginDialog dialog = new FastLoginDialog(this);
				dialog.setOnFastLoginListener(new PublishFastLoginListener());
				dialog.show();
				return;
			}
			Intent intent = new Intent(this, PublishGoodsActivity.class);
			startActivity(intent);
			break;
		//点击底部Tab消息
		case R.id.tab_message_btn:
			selectTabIndex = 2;
			break;
		//点击底部Tab我的
		case R.id.tab_mine_btn:
			selectTabIndex = 3;
			break;
		}
		if(selectTabIndex != -1 && selectTabIndex != currentTabIndex) {
			if(selectTabIndex < 0 || selectTabIndex >= tabCount) {
				Log.e(TAG, "selectTabIndex is " + selectTabIndex + " and out of 0 ~ " + (tabCount-1));
			} else {
				//重新设置Tab
				resetTab(selectTabIndex);
				//显示对应的Fragment
				showSelectTabFragment(selectTabIndex);
				//重新设置ActionBar
				setActionBar(selectTabIndex);
			}
		}
	}
	
	private void resetTab(int selectTabIndex) {
		//原先选择的Tab设置为不选择
		tabTextView[currentTabIndex].setSelected(false);
		//设置新的Tab
		tabTextView[selectTabIndex].setSelected(true);
		currentTabIndex = selectTabIndex;
		TextView tv = setCenterTitle(tabText[currentTabIndex]);
		tv.setCompoundDrawables(null, null, null, null);
		showTabIndex = currentTabIndex;
	}
	
	private void showSelectTabFragment(int selectTabIndex) {
		showFragment(mTabFragments[selectTabIndex]);
	}
	
	private void setActionBar(int index) {
		//主页
		if(index == 0) {
			Drawable indexSearchIcon = getResources().getDrawable(R.drawable.index_search_icon); 
			ImageView indexSearchIconIv = setRightDrawable(indexSearchIcon);
			if(indexSearchIconIv != null) {
				if(!setClickEventForImageView(indexSearchIconIv, new IndexActionBarSearchIconClickListener())) {
					Log.w(TAG, "set click event for index actionbar search icon fail");
				}
			}
		} else {
			setRightDrawable(null);
			//附近
			if(index == 1){
				Location location = UserInfoManager.getInstance().getCurrentLocation();
				if(location != null) {
					TextView tv = setCenterTitle(location.getDescription());
					Drawable left = getResources().getDrawable(R.drawable.nearly_location_icon);
					if(left != null) {
						left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
						tv.setCompoundDrawables(left, null, null, null);
						tv.setCompoundDrawablePadding(DisplayUtils.dp2px(getApplicationContext(), 5));
					}
					tv.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
							intent.putExtra(PublishConfigManger.publishActivityIdentificationKey, "MainActivity");
							startActivity(intent);
						}
					});
				}
			}
		}
	}
	
	//设置成功返回true,设置失败返回false
	private boolean setClickEventForImageView(ImageView iv, View.OnClickListener l) {
		if(iv == null || l == null) {
			return false;
		}
		iv.setOnClickListener(l);
		return true;
	}
	
	
	/**
	 * <p>
	 * 	首页ActionBar右边搜索图标点击事件监听类
	 * </p>
	 * */
	private class IndexActionBarSearchIconClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, GoodsSearchActivity.class);
			startActivity(intent);
			//可以设置动画
		}
	}
	
	/**
	 * <p>
	 * 	附近ActionBar右边搜索图标点击事件监听类
	 * </p>
	 * */
	private class NearByActionBarSearchIconClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(MainActivity.this, GoodsSearchActivity.class);
			startActivity(intent);
			//可以设置动画
		}
	}
	
	public void onEventMainThread(ExitChatEvent event) {  
		// showSelectTabFragment(2); 
		showTabIndex = 2;
		 System.out.println("onEventMainThread");
    }  
	
	@Override
	protected void onDestroy() {
		// TODO 自动生成的方法存根
		super.onDestroy();
		if(EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
		}
		
		//保存缩略图映射缓存
		ThumbnailMap.save(getApplicationContext());
	}
	
	//发布按钮快速登录监听器
	private class PublishFastLoginListener implements FastLoginDialog.OnFastLoginListener {
		
		@Override
		public void onError(int errorCode) {
		}
		
		@Override
		public void onSuccess(User user) {
			Intent intent = new Intent(MainActivity.this, PublishGoodsActivity.class);
			startActivity(intent);
		}
	}
	
	/**
	 * 当用户登录或者退出登录时会回调该方法
	 * 
	 * @param status
	 */
	public void onEventMainThread(UserStatus status) {
		if(status != null) {
			switch (status.getStatus()) {
			case LOGIN:
				Log.i(TAG,"登录");
				startService();
				mMessageFragment.setLoginState(true);
				mMessageFragment.init();
				break;
			case EXIT:
				Log.i(TAG,"退出登录");
				stopService();
				mMessageFragment.setLoginState(false);
				mMessageFragment.setAdapter();
				break;
			}
		}
	}
	//关闭后台服务
	public void stopService(){
		Log.i(TAG,"关闭服务");
		stopService(chatService);
	}
}
