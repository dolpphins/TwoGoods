package com.lym.twogoods.publish.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.fragment.PublishFragment;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * <p>
 * 发布商品Activity
 * </p>
 * 
 * 
 * */
public class PublishGoodsActivity extends BottomDockBackFragmentActivity {

	private TextView publish;
	private PublishFragment publishFragment;

	@Override
	public View onCreateBottomView() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		publish = setRightTitle("发布");
		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (publishFragment.judgeDescription()) {
					publishFragment.publishGoods();
				}
			}
		});
		publishFragment = new PublishFragment();
		addFragment(publishFragment);
		showFragment(publishFragment);
	}
}
