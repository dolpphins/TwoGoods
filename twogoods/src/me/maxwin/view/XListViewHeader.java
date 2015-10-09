/**
 * @file XListViewHeader.java
 * @create Apr 18, 2012 5:22:27 PM
 * @author Maxwin
 * @description XListView's header
 */
package me.maxwin.view;

import com.lym.twogoods.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class XListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintTextView;
	private int mState = STATE_NORMAL;

	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	
	private final int ROTATE_ANIM_DURATION = 180;
	
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	//提示文字
	private CharSequence[] hintText = new String[3];
	
	public XListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		// 初始情况，设置下拉刷新view高度为0
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
				R.layout.xlistview_header, null);
		addView(mContainer, lp);
		setGravity(Gravity.BOTTOM);

		mArrowImageView = (ImageView)findViewById(R.id.xlistview_header_arrow);
		mHintTextView = (TextView)findViewById(R.id.xlistview_header_hint_textview);
		mProgressBar = (ProgressBar)findViewById(R.id.xlistview_header_progressbar);
		
		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateDownAnim.setFillAfter(true);
	}

	public void setState(int state) {
		if (state == mState) return ;
		
		if (state == STATE_REFRESHING) {	// 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {	// 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch(state){
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotateDownAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			if(hintText[0] != null) {
				mHintTextView.setText(hintText[0]);
			} else {
				mHintTextView.setText(R.string.xlistview_header_hint_normal);
			}
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateUpAnim);
				if(hintText[1] != null) {
					mHintTextView.setText(hintText[1]);
				} else {
					mHintTextView.setText(R.string.xlistview_header_hint_ready);
				}
			}
			break;
		case STATE_REFRESHING:
			if(hintText[2] != null) {
				mHintTextView.setText(hintText[2]);
			} else {
				mHintTextView.setText(R.string.xlistview_header_hint_loading);
			}
			break;
			default:
		}
		
		mState = state;
	}
	
	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer
				.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		return mContainer.getLayoutParams().height;
	}
	
	//扩展
	
	/**
	 * 设置刚下拉时提示文字
	 * 
	 * @param 要设置的内容,注意如果为""表示不显示任何内容,为null表示显示默认文本
	 * */
	public void setHintNormalText(CharSequence text) {
		hintText[0] = text;
	}
	
	/**
	 * 设置下拉到一定程度提示文字
	 * 
	 * @param 要设置的内容,注意如果为""表示不显示任何内容,为null表示显示默认文本
	 * */
	public void setHintReadyText(CharSequence text) {
		hintText[1] = text;
	}
	
	/**
	 * 设置正在加载时提示文字
	 * 
	 * @param 要设置的内容,注意如果为""表示不显示任何内容,为null表示显示默认文本
	 * */
	public void setHintLodingText(CharSequence text) {
		hintText[2] = text;
	}
	
	/**
	 * 设置上次刷新时间提示文字
	 * 
	 * @param 要设置的内容,注意如果为""表示不显示任何内容,为null表示显示默认文本
	 * */
	public void setHintLastTimeTipText(CharSequence text) {
		if(text == null) {
			return;
		}
		TextView tv = (TextView) findViewById(R.id.xlistview_header_time_tip);
		if(tv != null) {
			tv.setText(text);
		}
	}
	
	/**
	 * 设置箭头图标
	 * 
	 * @param resId 要设置的资源ID
	 * */
	public void setRefreshArrow(int resId) {
		ImageView iv = (ImageView) findViewById(R.id.xlistview_header_arrow);
		if(iv != null) {
			iv.setImageResource(resId);
		}
	}
}
