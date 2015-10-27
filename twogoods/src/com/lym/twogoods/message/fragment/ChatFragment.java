package com.lym.twogoods.message.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;

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
		initXlistView();
	}

	private void initXlistView() {
		// TODO 自动生成的方法存根
		setMode(Mode.PULLDOWN);
		setAdapter();
		
		mListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				hideSoftInputView();
				
				return false;
			}
		});
	}
	
	
	/*
	 * 隐藏软键盘
	 * 
	 */
	protected void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager)getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.
				LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getActivity().getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 
						InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private void setAdapter() {
		int images[] = {R.drawable.user_default_head,R.drawable.user_default_head,
				R.drawable.user_default_head,R.drawable.user_default_head,
				R.drawable.user_default_head};
		TestAdapter adapter = new TestAdapter(getActivity(), images);
		super.setAdapter(adapter);
	}
	

}
