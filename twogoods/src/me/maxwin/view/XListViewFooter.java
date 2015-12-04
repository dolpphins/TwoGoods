/**
 * @file XFooterView.java
 * @create Mar 31, 2012 9:33:43 PM
 * @author Maxwin
 * @description XListView's footer
 */
package me.maxwin.view;

import com.lym.twogoods.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class XListViewFooter extends LinearLayout {
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	private Context mContext;

	private LinearLayout mMoreView;
	private View mContentView;
	private View mProgressBar;
	
	public XListViewFooter(Context context) {
		super(context);
		initView(context);
	}
	
	public XListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	
	public void setState(int state) {
//		mProgressBar.setVisibility(View.INVISIBLE);
//		if (state == STATE_READY) {
//			
//		} else if (state == STATE_LOADING) {
//			mProgressBar.setVisibility(View.VISIBLE);
//		} else {
//
//		}
	}
	
	public void setBottomMargin(int height) {
//		if (height < 0) return ;
//		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
//		lp.bottomMargin = height;
//		mContentView.setLayoutParams(lp);
	}
	
	public int getBottomMargin() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
		return lp.bottomMargin;
	}
	
	
	/**
	 * normal status
	 */
	public void normal() {
		//mProgressBar.setVisibility(View.GONE);
	}
	
	
	/**
	 * loading status 
	 */
	public void loading() {
		//mProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
//		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
//		lp.height = 0;
//		mContentView.setLayoutParams(lp);
		if(mMoreView != null) {
			mMoreView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * show footer
	 */
	public void show() {
//		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)mContentView.getLayoutParams();
//		lp.height = LayoutParams.WRAP_CONTENT;
//		mContentView.setLayoutParams(lp);
		if(mMoreView != null) {
			mMoreView.setVisibility(View.VISIBLE);
		}
	}
	
	private void initView(Context context) {
		mContext = context;
		mMoreView = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.xlistview_footer, null);
		addView(mMoreView);
		mMoreView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		mContentView = mMoreView.findViewById(R.id.xlistview_footer_content);
		mProgressBar = mMoreView.findViewById(R.id.xlistview_footer_progressbar);
	}
	
}
