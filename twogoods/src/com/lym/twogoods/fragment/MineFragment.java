package com.lym.twogoods.fragment;

import java.util.ArrayList;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ActivityRequestResultCode;
import com.lym.twogoods.eventbus.event.UserStatus;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.manager.ImageLoaderHelper;
import com.lym.twogoods.mine.ui.MineFocusActivity;
import com.lym.twogoods.mine.ui.MinePublishActivity;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.ui.LoginActivity;
import com.lym.twogoods.ui.PersonalityInfoActivity;
import com.lym.twogoods.ui.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.greenrobot.event.EventBus;


/**
 * <p>
 * 	我的Fragment
 * </p>
 * 
 * @author 麦灿标
 * */
public class MineFragment extends BaseFragment{

	private final static String TAG = "MineFragment";
	
	private View mView;
	
	//头部控件
	private TextView user_detail_head_more;
	private ImageView user_detail_head_head_picture;
	private TextView user_detail_head_username;
	//private TextView user_detail_head_browse_num;
	private TextView user_detail_head_description;
	
	//底部控件
	private RelativeLayout mine_publish_goods;
	private RelativeLayout mine_focus_goods;
	private RelativeLayout mine_settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = LayoutInflater.from(mAttachActivity).inflate(R.layout.mine_fragment_layout, null);

		initHeaderView();
		initFooterView();
		setOnClickEventForView();

		return mView;
		
	}

	private void initHeaderView() {
		
		user_detail_head_more = (TextView) mView.findViewById(R.id.user_detail_head_more);
		user_detail_head_head_picture = (ImageView) mView.findViewById(R.id.user_detail_head_head_picture);
		user_detail_head_username = (TextView) mView.findViewById(R.id.user_detail_head_username);
		//user_detail_head_browse_num = (TextView) mView.findViewById(R.id.user_detail_head_browse_num);
		user_detail_head_description = (TextView) mView.findViewById(R.id.user_detail_head_description);
		
		updateHeaderUI(UserInfoManager.getInstance().getmCurrent());
	}
	
	private void initFooterView() {
		mine_publish_goods = (RelativeLayout) mView.findViewById(R.id.mine_publish_goods);
		mine_focus_goods = (RelativeLayout) mView.findViewById(R.id.mine_focus_goods);
		mine_settings = (RelativeLayout) mView.findViewById(R.id.mine_settings);
	}

	private void setOnClickEventForView() {
		//更多
		user_detail_head_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkIsLogining()) {
					//跳转到个人更多信息页面
					Intent intent = new Intent(mAttachActivity, PersonalityInfoActivity.class);
					//intent.putExtra("user", UserInfoManager.getInstance().getmCurrent());
					startActivityForResult(intent, ActivityRequestResultCode.MINE_MORE_REQUESTCODE);
				}
			}
		});
		user_detail_head_head_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!UserInfoManager.getInstance().isLogining()) {
					goToLogin();
				} else {
					Intent intent = new Intent(mAttachActivity, DisplayPicturesActivity.class);
					ArrayList<String> picturesUrlList = new ArrayList<String>();
					User user = UserInfoManager.getInstance().getmCurrent();
					picturesUrlList.add(user.getHead_url());
					intent.putStringArrayListExtra("picturesUrlList", picturesUrlList);
					mAttachActivity.startActivity(intent);
				}
			}
		});
		user_detail_head_username.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!UserInfoManager.getInstance().isLogining()) {
					goToLogin();
				}
			}
		});
		mine_publish_goods.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkIsLogining()) {
					Intent intent = new Intent(mAttachActivity, MinePublishActivity.class);
					User user = UserInfoManager.getInstance().getmCurrent();
					intent.putExtra("username", user.getUsername());
					mAttachActivity.startActivity(intent);
				}
			}
		});
		mine_focus_goods.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkIsLogining()) {
					Intent intent = new Intent(mAttachActivity, MineFocusActivity.class);
					User user = UserInfoManager.getInstance().getmCurrent();
					intent.putExtra("username", user.getUsername());
					mAttachActivity.startActivity(intent);
				}
			}
		});
		mine_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转到设置界面
				Intent intent = new Intent(mAttachActivity, SettingsActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	private boolean checkIsLogining() {
		if(UserInfoManager.getInstance().isLogining()) {
			return true;
		} else {
			//Toast.makeText(mAttachActivity, "请先登录", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	private void goToLogin() {
		Intent intent = new Intent(mAttachActivity, LoginActivity.class);
		startActivityForResult(intent, ActivityRequestResultCode.MINE_LOGIN_REQUESTCODE);
	}
	
	private void updateHeaderUI(User user) {
		//已登录
		if(user != null) {
			ImageLoaderHelper.loadUserHeadPictureThumnail(mAttachActivity, user_detail_head_head_picture, user.getHead_url(), null);
			user_detail_head_username.setText(user.getUsername());
			user_detail_head_description.setText(user.getDeclaration());
		//未登录
		} else {
			ImageLoaderHelper.loadUserHeadPictureThumnail(mAttachActivity, user_detail_head_head_picture, null, null);
			user_detail_head_username.setText(mAttachActivity.getResources().getString(R.string.please_login));
			user_detail_head_description.setText("");
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == ActivityRequestResultCode.MINE_MORE_REQUESTCODE
				&& resultCode == ActivityRequestResultCode.MINE_MORE_RESULTCODE
				&& data != null) {
			User user = (User) data.getSerializableExtra("user");
			if(user != null) {
				//更新UI
				updateHeaderUI(user);
			}
		} else if(requestCode == ActivityRequestResultCode.MINE_LOGIN_REQUESTCODE) {
			updateHeaderUI(UserInfoManager.getInstance().getmCurrent());
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().unregister(this);
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
				updateHeaderUI(status.getUser());
				break;
			case EXIT:
				updateHeaderUI(null);
				break;
			}
		}
	}
}
