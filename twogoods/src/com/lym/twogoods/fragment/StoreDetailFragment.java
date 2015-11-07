package com.lym.twogoods.fragment;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.manager.ImageLoaderHelper;
import com.lym.twogoods.ui.PersonalityInfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * <p>
 * 	某一用户详情界面Fragment
 * </p>
 * 
 * @author 麦灿标
 * */
public class StoreDetailFragment extends PullListFragment {

	private final static String TAG = "StoreDetailFragment";
	
	private User data;
	
	//头部布局
	private View mHeaderView;
	//头部控件
	private TextView user_detail_head_more;
	private ImageView user_detail_head_head_picture;
	private TextView user_detail_head_username;
	private TextView user_detail_head_browse_num;
	private TextView user_detail_head_description;
	private ImageView user_detail_head_back;
	
	private BaseGoodsListAdapter mAdapter;
	
	public StoreDetailFragment(User user) {
		data = user;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		initListView();
		
		mHeaderView = LayoutInflater.from(mAttachActivity).inflate(R.layout.user_detail_head_layout, null);

		initHeaderView();
		setOnClickEventForView();
		
		mListView.addHeaderView(mHeaderView);
		mAdapter = new BaseGoodsListAdapter(mAttachActivity, null);
		mListView.setAdapter(mAdapter);
		
		getData();
		
		return v;
	}
	
	private void initListView() {
		mListView.clearHeader();
	}
	
	private void initHeaderView() {
		if(mHeaderView != null && data != null) {
			user_detail_head_more = (TextView) mHeaderView.findViewById(R.id.user_detail_head_more);
			user_detail_head_head_picture = (ImageView) mHeaderView.findViewById(R.id.user_detail_head_head_picture);
			user_detail_head_username = (TextView) mHeaderView.findViewById(R.id.user_detail_head_username);
			user_detail_head_browse_num = (TextView) mHeaderView.findViewById(R.id.user_detail_head_browse_num);
			user_detail_head_description = (TextView) mHeaderView.findViewById(R.id.user_detail_head_description);
			user_detail_head_back = (ImageView) mHeaderView.findViewById(R.id.user_detail_head_back);
			
			user_detail_head_back.setVisibility(View.VISIBLE);
			
//			ImageLoaderHelper.loadUserHeadPictureThumnail(mAttachActivity, user_detail_head_head_picture, data.getHead_url(), null);
//			user_detail_head_username.setText(data.getUsername());
//			user_detail_head_browse_num.setText("浏览数:" + data.getBrowse_num());
//			user_detail_head_description.setText(data.getDeclaration());
		}
	}
	
	private void setOnClickEventForView() {
		if(user_detail_head_more != null) {
			user_detail_head_more.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("onClick");
					//跳转到个人更多信息页面
					Intent intent = new Intent(mAttachActivity, PersonalityInfoActivity.class);
					intent.putExtra("user", data);
					mAttachActivity.startActivity(intent);
				}
			});
		}
		if(user_detail_head_back != null) {
			user_detail_head_back.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mAttachActivity.onBackPressed();
				}
			});
		}
	}
	
	private void getData() {
		
	}
	
	@Override
	public void onRefresh() {
		super.onRefresh();
		Log.i(TAG, "onRefresh");
	}
}
