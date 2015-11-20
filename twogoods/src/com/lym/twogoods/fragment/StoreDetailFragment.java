package com.lym.twogoods.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lym.twogoods.R;
import com.lym.twogoods.adapter.StoreDetailGoodsListAdapter;
import com.lym.twogoods.adapter.base.BaseGoodsListAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.local.bean.LocalGoods;
import com.lym.twogoods.manager.ImageLoaderHelper;
import com.lym.twogoods.network.DefaultOnLoaderListener;
import com.lym.twogoods.network.ListViewLoader;
import com.lym.twogoods.ui.DisplayPicturesActivity;
import com.lym.twogoods.ui.GoodsDetailActivity;
import com.lym.twogoods.ui.PersonalityInfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;


/**
 * <p>
 * 	某一用户详情界面Fragment
 * </p>
 * 
 * @author 麦灿标
 * */
public class StoreDetailFragment extends PullListFragment {

	private final static String TAG = "StoreDetailFragment";
	
	private User mUser;
	
	//头部布局
	private View mHeaderView;
	//头部控件
	private TextView user_detail_head_more;
	private ImageView user_detail_head_head_picture;
	private TextView user_detail_head_username;
	private TextView user_detail_head_browse_num;
	private TextView user_detail_head_description;
	private ImageView user_detail_head_back;
	
	private ListViewLoader mListViewLoader;
	private BaseGoodsListAdapter mAdapter;
	private List<Goods> mGoodsList;
	private int perPageCount = 10;
	private ListViewLoader.OnLoaderListener mOnLoaderListener;
	
	public StoreDetailFragment(User user) {
		mUser = user;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		mHeaderView = LayoutInflater.from(mAttachActivity).inflate(R.layout.user_detail_head_layout, null);

		initHeaderView();
		
		initListView();
		mListView.addHeaderView(mHeaderView);
		
		setOnClickEventForView();
		
		loadDataInit();
		
		return v;
	}
	
	private void initHeaderView() {
		if(mHeaderView != null && mUser != null) {
			user_detail_head_more = (TextView) mHeaderView.findViewById(R.id.user_detail_head_more);
			user_detail_head_head_picture = (ImageView) mHeaderView.findViewById(R.id.user_detail_head_head_picture);
			user_detail_head_username = (TextView) mHeaderView.findViewById(R.id.user_detail_head_username);
			user_detail_head_browse_num = (TextView) mHeaderView.findViewById(R.id.user_detail_head_browse_num);
			user_detail_head_description = (TextView) mHeaderView.findViewById(R.id.user_detail_head_description);
			user_detail_head_back = (ImageView) mHeaderView.findViewById(R.id.user_detail_head_back);
			
			user_detail_head_back.setVisibility(View.VISIBLE);
			
			if(mUser != null) {
				ImageLoaderHelper.loadUserHeadPictureThumnail(mAttachActivity, user_detail_head_head_picture, mUser.getHead_url(), null);
				user_detail_head_username.setText(mUser.getUsername());
				user_detail_head_browse_num.setText("浏览数:" + mUser.getBrowse_num());
				user_detail_head_description.setText(mUser.getDeclaration());
			}
		}
	}
	
	private void initListView() {
		mListView.clearHeader();
		setMode(Mode.PULLDOWN);
		
		mGoodsList = new ArrayList<Goods>();
		mAdapter = new StoreDetailGoodsListAdapter(mAttachActivity, mGoodsList);
		mListViewLoader = new ListViewLoader(mAttachActivity, mListView, mAdapter, mGoodsList);
		mOnLoaderListener = new DefaultOnLoaderListener(this, mListViewLoader);
		mListViewLoader.setOnLoaderListener(mOnLoaderListener);
		//mListViewLoader.setLoadCacheFromDisk(true);
		//mListViewLoader.setSaveCacheToDisk(true);
		mListView.setAdapter(mAdapter);
	}
	
	private void setOnClickEventForView() {
		if(user_detail_head_more != null) {
			user_detail_head_more.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("onClick");
					//跳转到个人更多信息页面
					Intent intent = new Intent(mAttachActivity, PersonalityInfoActivity.class);
					intent.putExtra("user", mUser);
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
		if(user_detail_head_head_picture != null) {
			user_detail_head_head_picture.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mAttachActivity, DisplayPicturesActivity.class);
					ArrayList<String> picturesUrlList = new ArrayList<String>();
					picturesUrlList.add(mUser.getHead_url());
					intent.putStringArrayListExtra("picturesUrlList", picturesUrlList);
					mAttachActivity.startActivity(intent);
				}
			});
		}
		if(mListView != null) {
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Intent intent = new Intent(mAttachActivity, GoodsDetailActivity.class);
					intent.putExtra("goods", mGoodsList.get(position - 1));
					startActivity(intent);
				}
			});
		}
	}
	
	//加载初始化数据
	private void loadDataInit() {
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.setSkip(0);
		query.setLimit(perPageCount);
		query.addWhereEqualTo("username", mUser.getUsername());
		mListViewLoader.requestLoadData(query, null, true, true);
	}
	
	@Override
	public void onRefresh() {
		super.onRefresh();
		BmobQuery<Goods> query = new BmobQuery<Goods>();
		query.setSkip(0);
		query.setLimit(perPageCount);
		query.addWhereEqualTo("username", mUser.getUsername());
		mListViewLoader.requestLoadData(query, null, true, false);
	}
}
