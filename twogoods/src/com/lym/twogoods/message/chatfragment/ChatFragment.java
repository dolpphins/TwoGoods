package com.lym.twogoods.message.chatfragment;

import android.os.Bundle;

import com.lym.twogoods.R;
import com.lym.twogoods.fragment.base.PullListFragment;
import com.lym.twogoods.message.adapter.TestAdapter;

public class ChatFragment extends PullListFragment{
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	private void initView() {
		
		setMode(Mode.BOTH);
		setAdapter();
	}

	private void setAdapter() {
		int images[] = {R.drawable.user_default_head,R.drawable.user_default_head,
				R.drawable.user_default_head,R.drawable.user_default_head,
				R.drawable.user_default_head};
		TestAdapter adapter = new TestAdapter(getActivity(), images);
		super.setAdapter(adapter);
	}
	

}
