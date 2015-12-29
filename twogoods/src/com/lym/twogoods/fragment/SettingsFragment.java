package com.lym.twogoods.fragment;

import com.lym.twogoods.AppManager;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.eventbus.event.UserStatus;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.utils.StringUtil;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * 设置Fragment
 * 
 * @author 麦灿标
 *
 */
public class SettingsFragment extends BaseFragment implements View.OnClickListener{

	private View mView;
	private RelativeLayout settings_version;
	private RelativeLayout settings_clear_cache;
	private RelativeLayout settings_feedback;
	private RelativeLayout settings_manual;
	private TextView settings_version_tv;
	private TextView settings_clear_cache_tv;
	private TextView settings_exit_button;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = LayoutInflater.from(mAttachActivity).inflate(R.layout.app_settings_layout, container, false);
		
		initView();
		
		setOnClickEvent();
		
		//初始化数据,包括读取缓存大小等
		initData();
		
		return mView;
	}
	
	private void initView() {
		settings_version = (RelativeLayout) mView.findViewById(R.id.settings_version);
		settings_clear_cache = (RelativeLayout) mView.findViewById(R.id.settings_clear_cache);
		settings_feedback = (RelativeLayout) mView.findViewById(R.id.settings_feedback);
		settings_manual = (RelativeLayout) mView.findViewById(R.id.settings_manual);
		
		settings_version_tv = (TextView) mView.findViewById(R.id.settings_version_tv);
		settings_clear_cache_tv = (TextView) mView.findViewById(R.id.settings_clear_cache_tv);
		
		settings_exit_button = (TextView) mView.findViewById(R.id.settings_exit_button);
		
		//判断是否需要隐藏退出账号按钮
		if(!UserInfoManager.getInstance().isLogining()) {
			settings_exit_button.setVisibility(View.GONE);
		} else {
			settings_exit_button.setVisibility(View.VISIBLE);
		}
	}
	
	private void setOnClickEvent() {
		settings_version.setOnClickListener(this);
		settings_clear_cache.setOnClickListener(this);
		settings_feedback.setOnClickListener(this);
		settings_manual.setOnClickListener(this);
		settings_exit_button.setOnClickListener(this);
	}
	
	private void initData() {
		settings_version_tv.setText(AppManager.getVersionName(mAttachActivity));
		new MeasureCacheTask().execute(new Void[]{});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		//版本更新
		case R.id.settings_version:
			
			break;
		//清除缓存
		case R.id.settings_clear_cache:
			
			break;
		//意见反馈
		case R.id.settings_feedback:
	
			break;
		//用户须知
		case R.id.settings_manual:
	
			break;
		//退出账号
		case R.id.settings_exit_button:
			exitAccount();
			break;
		}
	}
	
	private void exitAccount() {
		if(UserInfoManager.getInstance().isLogining()) {
			User user = UserInfoManager.getInstance().getmCurrent(); 
			UserInfoManager.getInstance().setmCurrent(null);
			UserInfoManager.getInstance().writeLoginToSP(mAttachActivity.getApplicationContext(), null);
			//发送EventBus退出登录事件
			EventBus.getDefault().post(new UserStatus(UserStatus.LoginStatus.EXIT, user));
			settings_exit_button.setVisibility(View.GONE);
			mAttachActivity.finish();
		}
	}
	
	//计算缓存大小异步任务
	private class MeasureCacheTask extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			String tip = getResources().getString(R.string.settings_cache_tip);
			settings_clear_cache_tv.setText(tip);
		}
		
		@Override
		protected String doInBackground(Void... params) {
			long size = DiskCacheManager.getInstance(mAttachActivity).getCacheSize();
			if(size < 0) {
				size = 0;
			}
			return StringUtil.byteSize2String(size);
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			settings_clear_cache_tv.setText(result);
		}
		
	}
}
