package com.lym.twogoods.ui.base;

import com.lym.twogoods.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

/**
 * <p>带有返回箭头的ActionBar的Activity,该类实现了{@link IBackable}接口,该Activity
 * 界面显示使用Fragment,注意与{@link BackActivity}类的区别</p>
 * 
 * @author 麦灿标
 * */
public abstract class BackFragmentActivity extends FragmentActivity 
						implements IBackable{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//设置ActionBar返回箭头及点击事件
		initActionBarBack();
	}
	
	private void initActionBarBack() {
		ImageView actionBarLeftiv = (ImageView) findViewById(R.id.app_common_actionbar_left_iv);
		if(actionBarLeftiv != null) {
			actionBarLeftiv.setVisibility(View.VISIBLE);//可能为不可见
			actionBarLeftiv.setImageResource(R.drawable.app__actionbar_backarrow);
			actionBarLeftiv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					onActionBarBack();
				}
			});
		}
	}
	
	/**
	 * <p>
	 * 当点击ActionBar返回箭头时调用该方法,默认实现为调用返回键方法{@link Activity#onBackPressed()},
	 * 你可以重写该方法以便在点击ActionBar返回箭头时实现你自己的逻辑
	 * </p>
	 * */
	@Override
	public void onActionBarBack() {
		onBackPressed();
	}
}
