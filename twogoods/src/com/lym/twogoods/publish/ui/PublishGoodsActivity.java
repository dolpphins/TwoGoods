package com.lym.twogoods.publish.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lym.twogoods.R;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.eventbus.event.FinishRecordEvent;
import com.lym.twogoods.fragment.PublishFragment;
import com.lym.twogoods.message.config.MessageConfig;
import com.lym.twogoods.message.config.RecordConfig;
import com.lym.twogoods.message.listener.RecordPlayClickListener;
import com.lym.twogoods.message.view.ImageCusView;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.publish.util.DataMangerUtils;
import com.lym.twogoods.ui.SendPictureActivity;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;
import com.lym.twogoods.widget.WrapContentViewPager;

import de.greenrobot.event.EventBus;

/**
 * <p>
 * 发布商品Activity
 * </p>
 * 
 * 
 * */
public class PublishGoodsActivity extends BottomDockBackFragmentActivity {
	private String TGA = "PublishGoodsActivity";

	private TextView publish;
	private PublishFragment publishFragment;
	// 底部控件
	private View publish_bottom;
	private LinearLayout ll_publish_fragment_add_photo;
	private LinearLayout ll_publish_fragment_add_smile;
	private LinearLayout ll_publish_fragment_add_voice;
	private WrapContentViewPager vp_publish_fragement_emoji;
	private LinearLayout ll_publish_fragment_bottom_chat;
	private ImageCusView imageCusView;
	// 表情适配器
	private EmotionViewPagerAdapter emotionViewPagerAdapter;
	// 表情布局和语音是否显示的标志
	private boolean mEmotionLayoutIsShowing = false;
	private boolean mChatLayoutIsShowing = false;

	@Override
	public View onCreateBottomView() {
		publish_bottom = getLayoutInflater().inflate(
				R.layout.publish_fragment_bottom_replace, null);
		if (publish_bottom != null) {
			ll_publish_fragment_add_photo = (LinearLayout) publish_bottom
					.findViewById(R.id.ll_publish_fragment_add_photo);
			ll_publish_fragment_add_smile = (LinearLayout) publish_bottom
					.findViewById(R.id.ll_publish_fragment_add_smile);
			ll_publish_fragment_add_voice = (LinearLayout) publish_bottom
					.findViewById(R.id.ll_publish_fragment_add_voice);
			
			vp_publish_fragement_emoji = (WrapContentViewPager) publish_bottom
					.findViewById(R.id.vp_publish_fragment_emoji);
			ll_publish_fragment_bottom_chat = (LinearLayout) publish_bottom
					.findViewById(R.id.ll_publish_fragment_bottom_chat);
			imageCusView=(ImageCusView) publish_bottom.findViewById(R.id.message_chat_record_view);
			imageCusView.setFileSavePath(RecordConfig.GOOD_RECORD);
		}
		return publish_bottom;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		init();
		emotionViewPagerAdapter = new EmotionViewPagerAdapter(
				PublishGoodsActivity.this, null);
		vp_publish_fragement_emoji.setAdapter(emotionViewPagerAdapter);
		initEvent();
	}

	private void init() {
		publish = setRightTitle("发布");
		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (publishFragment.judgeDescription()) {
					publishFragment.pictureUpload();
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
		emotionViewPagerAdapter.attachEditext(publishFragment
				.getEditTextDescription());
		hideBottomLayout();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		imageCusView.release();
	}

	/**
	 * 通过eventBus来监听完成录音的操作，
	 * 
	 * @param event
	 *            完成录音的事件
	 */
	public void onEventMainThread(FinishRecordEvent event) {
		String path = event.getPath();
		PublishConfigManger.voicePath = path;
		updateVoiceImageView(publishFragment.getVoiceImageView());
		Log.v(TGA, path + "是录音的路径");
	}

	private void hideBottomLayout() {

		/**
		 * 点击最外层布局隐藏底部控件
		 */
		publishFragment.getRelativeLayoutMain().setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						// 隐藏键盘
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
						hideEmotionLayout();
						hideChatLayout();
					}
				});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {  
	        View v = getCurrentFocus();  
	        if (isShouldHideInput(v, ev)) {  
	  
	            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	            if (imm != null) {  
	                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
	            }  
	        }  
	        return super.dispatchTouchEvent(ev);  
	    }  
	    // 必不可少，否则所有的组件都不会有TouchEvent了  
	    if (getWindow().superDispatchTouchEvent(ev)) {  
	        return true;  
	    }  
	    return onTouchEvent(ev);  
	}
	
	private void initEvent() {
		ll_publish_fragment_add_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PublishGoodsActivity.this,
						SendPictureActivity.class);
				intent.putExtra("picCount", PublishConfigManger.PICTURE_COUNT);
				startActivityForResult(intent, PublishConfigManger.requestCode);
			}
		});
		ll_publish_fragment_add_smile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 防止表情和语音冲突
				if (mChatLayoutIsShowing) {
					hideChatLayout();
				}
				toggleEmotionLayout();
				if (mEmotionLayoutIsShowing) {
					// 隐藏键盘
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});
		ll_publish_fragment_add_voice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mEmotionLayoutIsShowing) {
					hideEmotionLayout();
				}
				toggleChatLayout();
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

	public void hideEmotionLayout() {
		vp_publish_fragement_emoji.setVisibility(View.GONE);
		mEmotionLayoutIsShowing = false;
	}

	/**
	 * 
	 * 语音切换
	 * 
	 */
	private void toggleChatLayout() {
		if (mChatLayoutIsShowing) {
			hideChatLayout();
		} else {
			showChatLayout();
		}
	}

	private void showChatLayout() {
		ll_publish_fragment_bottom_chat.setVisibility(View.VISIBLE);
		mChatLayoutIsShowing = true;
	}

	private void hideChatLayout() {
		ll_publish_fragment_bottom_chat.setVisibility(View.GONE);
		mChatLayoutIsShowing = false;
	}

	public WrapContentViewPager attrachEmotionViewPager() {
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
			PublishConfigManger.publishPictureUrl.add(data.getExtras()
					.getString("picture"));
			publishFragment
					.notifyGridView(PublishConfigManger.publishPictureUrl);
			break;

		case MessageConfig.SEND_LOCAL_PIC:
			for (int i = 0; i < data.getExtras().getStringArrayList("pictures")
					.size(); i++) {
				PublishConfigManger.publishPictureUrl.add(data.getExtras()
						.getStringArrayList("pictures").get(i));
			}
			publishFragment
					.notifyGridView(PublishConfigManger.publishPictureUrl);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (DataMangerUtils.goodsDescriptionIsWrote(this,
					publishFragment.getEditTextDescription(),
					publishFragment.getEditTextTel(),
					publishFragment.getEditTextPrice(),
					publishFragment.getTextViewLocation())) {
				new AlertDialog.Builder(this)
						.setMessage("你真的要放弃发布吗......")
						.setPositiveButton("我确定...",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								})
						.setNegativeButton("我后悔了...",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).show();
				return true;
			} else {
				finish();
				return true;
			}
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * <p>
	 * 当用户使用了语音功能，这时候语音控件会显示出来，并设置语音控件相关点击事件。
	 * </p>
	 * 
	 * @param imageView
	 *            语音控件
	 */
	private void updateVoiceImageView(ImageView imageView) {
		// 有发表语音
		if (!PublishConfigManger.voicePath.equals("")) {
			imageView.setVisibility(View.VISIBLE);
			imageView.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					v.setVisibility(View.GONE);
					PublishConfigManger.voicePath = "";
					return true;
				}
			});
			RecordPlayClickListener recordPlayClickListener = new RecordPlayClickListener(
					getApplicationContext());
			recordPlayClickListener.setFilePath(PublishConfigManger.voicePath);
			recordPlayClickListener.setImageView(imageView);
			recordPlayClickListener.setIsChatMsg(false);
			// imageView.setOnClickListener(recordPlayClickListener);
		}
	}
	/**
	 *<p>
	 *		判断是否需要隐藏键盘
	 *</p>
	 * @param v		触发的view	
	 * @param event	触发的事件
	 * @return		
	 */
	public  boolean isShouldHideInput(View v, MotionEvent event) {  
	    if (v != null && (v instanceof EditText)) {  
	        int[] leftTop = { 0, 0 };  
	        //获取输入框当前的location位置  
	        v.getLocationInWindow(leftTop);  
	        int left = leftTop[0];  
	        int top = leftTop[1];  
	        int bottom = top + v.getHeight();  
	        int right = left + v.getWidth();  
	        if (event.getX() > left && event.getX() < right  
	                && event.getY() > top && event.getY() < bottom) {  
	            // 点击的是输入框区域，保留点击EditText的事件  
	            return false;  
	        } else {  
	            return true;  
	        }  
	    }  
	    return false;  
	}
	
}
