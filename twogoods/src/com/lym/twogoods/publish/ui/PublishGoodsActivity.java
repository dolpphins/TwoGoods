package com.lym.twogoods.publish.ui;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.fragment.PublishFragment;
import com.lym.twogoods.message.MessageConfig;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.ui.SendPictureActivity;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;
import com.lym.twogoods.widget.WrapContentViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
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
	// 底部控件
	private ImageView iv_publish_fragment_add_photo;
	private ImageView iv_publish_fragment_add_smile;
	private ImageView iv_publish_fragment_add_voice;
	private WrapContentViewPager vp_publish_fragement_emoji;
	
	//表情适配器
	private EmotionViewPagerAdapter emotionViewPagerAdapter;
	
	//表情布局是否显示的标志
	private boolean mEmotionLayoutIsShowing=false;
	
	@Override
	public View onCreateBottomView() {
		View publish_bottom = getLayoutInflater().inflate(
				R.layout.publish_fragment_bottom, null);
		if (publish_bottom != null) {
			iv_publish_fragment_add_photo = (ImageView) publish_bottom
					.findViewById(R.id.iv_publish_fragment_add_photo);
			iv_publish_fragment_add_smile = (ImageView) publish_bottom
					.findViewById(R.id.iv_publish_fragment_add_smile);
			iv_publish_fragment_add_voice = (ImageView) publish_bottom
					.findViewById(R.id.iv_publish_fragment_add_voice);
			vp_publish_fragement_emoji = (WrapContentViewPager) publish_bottom
					.findViewById(R.id.vp_publish_fragment_emoji);
		}
		return publish_bottom;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		emotionViewPagerAdapter=new EmotionViewPagerAdapter(PublishGoodsActivity.this, null);
		vp_publish_fragement_emoji.setAdapter(emotionViewPagerAdapter);
		initEvent();
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
	@Override
	protected void onStart() {
		super.onStart();
		emotionViewPagerAdapter.attachEditext(publishFragment.getEditTextDescription());
	}

	private void initEvent() {
		iv_publish_fragment_add_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PublishGoodsActivity.this,SendPictureActivity.class);
				intent.putExtra("picCount", PublishConfigManger.PICTURE_COUNT);
				startActivityForResult(intent, PublishConfigManger.requestCode);
			}
		});
		iv_publish_fragment_add_smile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleEmotionLayout();
				if (mEmotionLayoutIsShowing) {
					// 隐藏键盘
					InputMethodManager imm = (InputMethodManager) 
							getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});
	}
	
	/*
	 * 
	 * 表情键盘切换与隐藏的关系
	 */
	private void toggleEmotionLayout() {
		if (mEmotionLayoutIsShowing) {
			hideEmotionLayout();
		} else {
			showEmotionLayout();
		}
	}

	private void showEmotionLayout() {
		vp_publish_fragement_emoji.setVisibility(View.VISIBLE);
		mEmotionLayoutIsShowing = true;
	}

	private void hideEmotionLayout() {
		vp_publish_fragement_emoji.setVisibility(View.GONE);
		mEmotionLayoutIsShowing = false;
	}
	
	public  WrapContentViewPager attrachEmotionViewPager() {
		return vp_publish_fragement_emoji;
	}
	/*
	 * 回调函数，取得照片路径
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case MessageConfig.SEND_CAMERA_PIC:
			PublishConfigManger.publishPictureUrl.add(data.getExtras().getString("picture"));
			break;

		case MessageConfig.SEND_LOCAL_PIC:
			for (int i = 0; i < data.getExtras().getStringArrayList(
					"pictures").size(); i++) {
				PublishConfigManger.publishPictureUrl.add(data.getExtras().getStringArrayList(
					"pictures").get(i));
			}
			publishFragment.notifyGridView(PublishConfigManger.publishPictureUrl);
			break;
		default:
			break;
		}
	}
}
