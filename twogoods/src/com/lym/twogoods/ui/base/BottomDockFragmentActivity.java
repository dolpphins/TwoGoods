package com.lym.twogoods.ui.base;

import com.lym.twogoods.R;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * <p>
 * 	底部停靠,界面通过Fragment显示的Activity
 * </p>
 * 
 * @author 麦灿标
 * */
public abstract class BottomDockFragmentActivity extends FragmentActivity 
						implements IBottomDockable{

	private final static String TAG = "BottomDockFragmentActivity";
	
	/** 底部布局 */
	private View mBottomView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//初始化底部布局
		mBottomView = onCreateBottomView();
		if(mBottomView != null) {
			
			RelativeLayout bottomdock_decorview_content = (RelativeLayout) findViewById(R.id.app_decorview_bottom_tab);
			//bottomdock_decorview_content.removeAllViews();

			//注意一定要设置布局参数,原因目前不清楚
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			bottomdock_decorview_content.addView(mBottomView, lp);
		} else {
			Log.w(TAG, "mBottomView is null");
		}
	}
	
}
