package com.lym.twogoods.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.fragment.IndexFragment;
import com.lym.twogoods.fragment.MessageFragment;
import com.lym.twogoods.fragment.MineFragment;
import com.lym.twogoods.fragment.NearbyFragment;
import com.lym.twogoods.index.ui.GoodsSearchActivity;
import com.lym.twogoods.nearby.ui.SelectCityActivity;
import com.lym.twogoods.publish.ui.PublishGoodsActivity;
import com.lym.twogoods.receiver.NetworkTipsBroadcastReceiver;
import com.lym.twogoods.service.ChatService;
import com.lym.twogoods.ui.base.BottomDockFragmentActivity;
import com.lym.twogoods.viewholder.TabViewHolder;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
	
	/** 底部Tab布局 */
	private View mTabView;

	/** 底部Tab ViewHolder */
	private TabViewHolder mTabViewHolder;
	/** TextView数组存储 */
	private TextView[] tabTextView = new TextView[tabCount];
	/** 底部Tab文字 */
	private String[] tabText = new String[tabCount];
	
	/** 当前选择Tab索引 */
	private int currentTabIndex;
	/**为连接网络的广播接受者*/
	private NetworkTipsBroadcastReceiver mNetworkTipsBroadcastReceiver = new NetworkTipsBroadcastReceiver();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initTabFragment();
		startService();
		//开始时显示首页
		resetTab(0);
		showSelectTabFragment(0);
		//初始化ActionBar
		setActionBar(0);
	}
	

	@Override
	public View onCreateBottomView() {
		mTabView = getLayoutInflater().inflate(R.layout.app_main_bottomdock_tab, null);
		setTabClickEvent();
		return mTabView;
	}
	/**
	 * 开启service,
	 */
	private void startService() {
		
		if(!UserInfoManager.getInstance().isLogining()){//如果用户已经登录,开启service,否则不开启
			Intent chatService = new Intent(MainActivity.this,ChatService.class);
			this.startService(chatService);
		}
	}
	
	private void initTabFragment() {
		mTabFragments[0] = new IndexFragment();
		mTabFragments[1] = new NearbyFragment();
		mTabFragments[2] = new MessageFragment();
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
			Intent intent1 = new Intent(this, SelectCityActivity.class);
			startActivity(intent1);
			selectTabIndex = 1;
			break;
		//点击底部发布
		case R.id.publish_btn:
			//特殊处理,开启Activity
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
		setCenterTitle(tabText[currentTabIndex]);
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
		//附近
		} else if(index == 1){
			Drawable nearbySearchIcon = getResources().getDrawable(R.drawable.index_search_icon);//跟主页搜索图标一样 
			ImageView nearbySearchIconIv = setRightDrawable(nearbySearchIcon);
			if(nearbySearchIconIv != null) {
				if(!setClickEventForImageView(nearbySearchIconIv, new NearByActionBarSearchIconClickListener())) {
					Log.w(TAG, "set click event for nearby actionbar search icon fail");
				}
			}
		} else {
			setRightDrawable(null);
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
}
