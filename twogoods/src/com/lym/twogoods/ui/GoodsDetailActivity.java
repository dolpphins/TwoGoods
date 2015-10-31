package com.lym.twogoods.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;

import android.graphics.drawable.Drawable;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * <p>
 * 	商品详情Activity
 * </p>
 * 
 * @author 麦灿标
 * */
public class GoodsDetailActivity extends BottomDockBackFragmentActivity{

	//底部评论控件
	private ImageView app_goods_detail_write_comment_add_emotion_icon_iv;
	private EditText app_goods_detail_write_comment_input;
	private ImageView app_goods_detail_write_comment_send;
	private ViewPager app_goods_detail_write_comment_emotion_viewpager;
	private LinearLayout app_goods_detail_write_comment_viewpager_tip;
	
	@Override
	public View onCreateBottomView() {
		View v = getLayoutInflater().inflate(R.layout.app_goods_detail_write_comment, null);
		if(v != null) {
			app_goods_detail_write_comment_add_emotion_icon_iv = (ImageView) v.findViewById(R.id.app_goods_detail_write_comment_add_emotion_icon_iv);
			app_goods_detail_write_comment_input = (EditText) v.findViewById(R.id.app_goods_detail_write_comment_input);
			app_goods_detail_write_comment_send = (ImageView) v.findViewById(R.id.app_goods_detail_write_comment_send);
			app_goods_detail_write_comment_emotion_viewpager = (ViewPager) v.findViewById(R.id.app_goods_detail_write_comment_emotion_viewpager);
			app_goods_detail_write_comment_viewpager_tip = (LinearLayout) v.findViewById(R.id.app_goods_detail_write_comment_viewpager_tip);
			initEmotionViewPager();
		}
		return v;
	}
	
	//初始化表情列表
	private void initEmotionViewPager() {
		EmotionViewPagerAdapter adapter = new EmotionViewPagerAdapter(this, app_goods_detail_write_comment_input);
		app_goods_detail_write_comment_emotion_viewpager.setAdapter(adapter);
	}
}
