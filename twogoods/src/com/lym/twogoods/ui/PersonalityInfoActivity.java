package com.lym.twogoods.ui;

import java.io.File;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ActivityRequestResultCode;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.mine.fragment.PersonalityInfoFragment;
import com.lym.twogoods.ui.base.BackFragmentActivity;
import com.lym.twogoods.utils.PicturesHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;


/**
 * <p>
 * 	个人更多信息Activity
 * </p>
 * <p>
 * 	必须传递用户数据:<pre>intent.putExtra("user", user);</pre>
 * </p>
 * 
 * @author 麦灿标
 * */
public class PersonalityInfoActivity extends BackFragmentActivity{

	private final static String TAG = "PersonalityInfoActivity";

	private User mUser;
	
	private PersonalityInfoFragment mPersonalityInfoFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterTitle(getResources().getString(R.string.more_personality));
		
		//mUser = (User) getIntent().getSerializableExtra("user");
		//没有登录
		if(!UserInfoManager.getInstance().isLogining()) {
			Toast.makeText(getApplicationContext(), "请先登录", Toast.LENGTH_SHORT).show();
			finish();
		}
		mUser = UserInfoManager.getInstance().getmCurrent();
		mPersonalityInfoFragment = new PersonalityInfoFragment(mUser);
		showFragment(mPersonalityInfoFragment);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			setResultData();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onActionBarBack() {
		setResultData();
		super.onActionBarBack();
	}
	
	private void setResultData() {
		Intent data = new Intent();
		data.putExtra("user", mUser);
		setResult(ActivityRequestResultCode.MINE_MORE_RESULTCODE, data);
	}
}
