package com.lym.twogoods.mine.fragment;

import com.lym.twogoods.bean.User;
import com.lym.twogoods.fragment.base.BaseListFragment;
import com.lym.twogoods.mine.adapter.PersonalityInfoAdapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 我的更多信息界面Fragment
 * 
 * @author 麦灿标
 * */
public class PersonalityInfoFragment extends BaseListFragment {

	private User data;
	private PersonalityInfoAdapter mPersonalityInfoAdapter;
	
	public PersonalityInfoFragment(User user) {
		data = user;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		initView();
		
		return v;
	}
	
	private void initView() {
		mPersonalityInfoAdapter = new PersonalityInfoAdapter(mAttachActivity, data);
		setAdapter(mPersonalityInfoAdapter);
	}
}
