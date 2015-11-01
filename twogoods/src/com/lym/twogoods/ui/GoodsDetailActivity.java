package com.lym.twogoods.ui;

import java.util.ArrayList;

import com.lym.twogoods.R;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.config.ShareConfiguration;
import com.lym.twogoods.fragment.GoodsDetailFragment;
import com.lym.twogoods.index.adapter.GoodsShareListViewAdapter;
import com.lym.twogoods.screen.DisplayUtils;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * <p>
 * 	商品详情Activity
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsDetailActivity extends BottomDockBackFragmentActivity{

	private final static String TAG = "GoodsDetailActivity";
	
	//底部评论控件
	private ImageView app_goods_detail_write_comment_add_emotion_icon_iv;
	private EditText app_goods_detail_write_comment_input;
	private ImageView app_goods_detail_write_comment_send;
	private ViewPager app_goods_detail_write_comment_emotion_viewpager;
	private LinearLayout app_goods_detail_write_comment_viewpager_tip;
	//标记表情布局是否弹出
	private boolean mEmotionLayoutIsShowing = false;
	
	//Actionbar分享图标
	private ImageView shareIcon;
	//标记分享布局是否弹出
	private boolean mShareLayoutIsShowing = false;
	//分享布局
	private PopupWindow mSharePopupWindow;
	
	private GoodsDetailFragment mFragment;
	private Goods mData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//设置ActionBar
		setCenterTitle(getResources().getString(R.string.goods_detail));
		shareIcon = setRightDrawable(getResources().getDrawable(R.drawable.goods_detail_share_icon));
		
		initShareLayout();
		setClickForShareIcon();
		
		
		mData = (Goods) getIntent().getSerializableExtra("goods");
		if(mData == null) {
			Log.i(TAG, "goods data is null");
		}
		//mData不能为空
		//主Fragment
		mFragment = new GoodsDetailFragment(mData);
		showFragment(mFragment);
	}	
	
	private void initShareLayout() {
		View contentView = getLayoutInflater().inflate(R.layout.index_goods_detail_share_layout, null);
		ListView index_goods_detail_share_gv = (ListView) contentView.findViewById(R.id.index_goods_detail_share_lv);
		if(index_goods_detail_share_gv == null) {
			return;
		}
		//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		//index_goods_detail_share_gv.setLayoutParams(params);
		BaseAdapter adapter = new GoodsShareListViewAdapter(this, ShareConfiguration.getShareList());
		index_goods_detail_share_gv.setAdapter(adapter);
		mSharePopupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		mSharePopupWindow.setWidth(DisplayUtils.getScreenWidthPixels(this) / 3);
	}
	
	@Override
	public View onCreateBottomView() {
		View v = getLayoutInflater().inflate(R.layout.index_goods_detail_write_comment, null);
		if(v != null) {
			app_goods_detail_write_comment_add_emotion_icon_iv = (ImageView) v.findViewById(R.id.app_goods_detail_write_comment_add_emotion_icon_iv);
			app_goods_detail_write_comment_input = (EditText) v.findViewById(R.id.app_goods_detail_write_comment_input);
			app_goods_detail_write_comment_send = (ImageView) v.findViewById(R.id.app_goods_detail_write_comment_send);
			app_goods_detail_write_comment_emotion_viewpager = (ViewPager) v.findViewById(R.id.app_goods_detail_write_comment_emotion_viewpager);
			app_goods_detail_write_comment_viewpager_tip = (LinearLayout) v.findViewById(R.id.app_goods_detail_write_comment_viewpager_tip);
			initEmotionViewPager();
			setEventForCommentLayout();
		}
		return v;
	}
	
	private void setEventForCommentLayout() {
		if(app_goods_detail_write_comment_add_emotion_icon_iv != null) {
			app_goods_detail_write_comment_add_emotion_icon_iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toggleEmotionLayout();
					if(mEmotionLayoutIsShowing) {
						//隐藏软键盘
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					}
				}
			});
		}
		if(app_goods_detail_write_comment_input != null) {
			app_goods_detail_write_comment_input.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mEmotionLayoutIsShowing) {
						hideEmotionLayout();
					}
				}
			});
		}
		//发表评论按钮点击事件
		if(app_goods_detail_write_comment_send != null) {
			app_goods_detail_write_comment_send.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
		}
	}
	
	private void toggleEmotionLayout() {
		if(mEmotionLayoutIsShowing) {
			hideEmotionLayout();
		} else {
			showEmotionLayout();
		}
	}
	
	private void showEmotionLayout() {
		app_goods_detail_write_comment_emotion_viewpager.setVisibility(View.VISIBLE);
		mEmotionLayoutIsShowing = true;
	}
	
	private void hideEmotionLayout() {
		app_goods_detail_write_comment_emotion_viewpager.setVisibility(View.GONE);
		mEmotionLayoutIsShowing = false;
	}
	
	//初始化表情列表
	private void initEmotionViewPager() {
		EmotionViewPagerAdapter adapter = new EmotionViewPagerAdapter(this, app_goods_detail_write_comment_input);
		app_goods_detail_write_comment_emotion_viewpager.setAdapter(adapter);
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//如果正在显示评论表情列表则拦截返回键
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(mEmotionLayoutIsShowing) {
				hideEmotionLayout();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//为分享图标设置点击事件
	private void setClickForShareIcon() {
		if(shareIcon != null) {
			shareIcon.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toggleShareLayout();
				}
			});
		}
	}
	
	//显示或隐藏分享布局
	private void toggleShareLayout() {
		if(mShareLayoutIsShowing) {
			hideShareLayout();
		} else {
			showShareLayout();
		}
	}
	
	//显示分享布局
	private void showShareLayout() {
		if(mSharePopupWindow != null) {
			mSharePopupWindow.showAsDropDown(shareIcon);
			mShareLayoutIsShowing = true;
		}
	}
	
	//隐藏分享布局
	private void hideShareLayout() {
		if(mSharePopupWindow != null) {
			mSharePopupWindow.dismiss();
			mShareLayoutIsShowing = false;
		}
	}
}
