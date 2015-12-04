package com.lym.twogoods.publish.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.adapter.EmotionViewPagerAdapter;
import com.lym.twogoods.fragment.PublishFragment;
import com.lym.twogoods.message.MessageConfig;
import com.lym.twogoods.message.listener.RecondTouchListener;
import com.lym.twogoods.publish.manger.PublishConfigManger;
import com.lym.twogoods.ui.SendPictureActivity;
import com.lym.twogoods.ui.base.BottomDockBackFragmentActivity;
import com.lym.twogoods.widget.WrapContentViewPager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * 发布商品Activity
 * </p>
 * 
 * 
 * */
public class PublishGoodsActivity extends BottomDockBackFragmentActivity {
	private String TGA="PublishGoodsActivity";

	private TextView publish;
	private PublishFragment publishFragment;
	// 底部控件
	private View publish_bottom;
	private ImageView iv_publish_fragment_add_photo;
	private ImageView iv_publish_fragment_add_smile;
	private ImageView iv_publish_fragment_add_voice;
	private WrapContentViewPager vp_publish_fragement_emoji;
	private LinearLayout ll_publish_fragment_bottom_chat;
	//语音相关控件
	private ImageView iv_showIsReconding;
	private TextView tv_recondPressTip;
	private LinearLayout tv_finish_recond_tip;
	private ImageView iv_recondVoice;
	/**录音监听器*/
	private RecondTouchListener mRecondTouchListener;
	/**话筒动画*/
	private Drawable[] drawable_Anims;
	// 表情适配器
	private EmotionViewPagerAdapter emotionViewPagerAdapter;

	// 表情布局和语音是否显示的标志
	private boolean mEmotionLayoutIsShowing = false;
	private boolean mChatLayoutIsShowing = false;

	@Override
	public View onCreateBottomView() {
		publish_bottom = getLayoutInflater().inflate(
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
			ll_publish_fragment_bottom_chat = (LinearLayout) publish_bottom
					.findViewById(R.id.ll_publish_fragment_bottom_chat);
		}
		return publish_bottom;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		initVoiceConfig();
	}
	//初始化语音相关配置
	private void initVoiceConfig() {
		iv_showIsReconding=(ImageView) publish_bottom.findViewById(R.id.message_chat_show_is_recording_iv);
		tv_recondPressTip = (TextView)publish_bottom.findViewById(R.id.message_chat_recond_press_tip);
		tv_finish_recond_tip = (LinearLayout) publish_bottom.findViewById(R.id.message_chat_finish_recond_tip_tv);
		iv_recondVoice = (ImageView) publish_bottom.findViewById(R.id.message_chat_record);
		//给录音控件设置触摸事件
		mRecondTouchListener = new RecondTouchListener(getApplicationContext(),mHandler);
		iv_recondVoice.setOnTouchListener(mRecondTouchListener);
		//初始化在录音时音量改变的动画
		drawable_Anims = new Drawable[] {
		getResources().getDrawable(R.drawable.message_chat_reconding_audio1),
		getResources().getDrawable(R.drawable.message_chat_reconding_audio2),
		getResources().getDrawable(R.drawable.message_chat_reconding_audio3) };
	}

	@Override
	protected void onStart() {
		super.onStart();
		emotionViewPagerAdapter.attachEditext(publishFragment
				.getEditTextDescription());
		hideBottomLayout();
	}

	private void hideBottomLayout() {
		/**
		 * 点击最外层布局隐藏底部控件
		 */
		publishFragment.getRelativeLayoutMain().setOnClickListener(new OnClickListener() {
			
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
	private void initEvent() {
		iv_publish_fragment_add_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PublishGoodsActivity.this,
						SendPictureActivity.class);
				intent.putExtra("picCount", PublishConfigManger.PICTURE_COUNT);
				startActivityForResult(intent, PublishConfigManger.requestCode);
			}
		});
		iv_publish_fragment_add_smile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//防止表情和语音冲突
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
		iv_publish_fragment_add_voice.setOnClickListener(new OnClickListener() {

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
	 * 	语音切换
	 * 
	 */
	private void toggleChatLayout() {
		if (mChatLayoutIsShowing) {
			hideChatLayout();
		}else {
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
		if (keyCode==KeyEvent.KEYCODE_BACK) {
			new AlertDialog.Builder(this).setMessage("你真的要放弃发布吗......").setPositiveButton("我确定...", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}).setNegativeButton("我后悔了...", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			}).show();
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	/**ChatActivity的Handler*/
	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch(msg.what){
			//开始录音
			case MessageConfig.START_RECORD:
				iv_showIsReconding.setVisibility(View.VISIBLE);
				tv_recondPressTip.setVisibility(View.INVISIBLE);
				tv_finish_recond_tip.setVisibility(View.VISIBLE);
				break;
			//放弃录音
			case MessageConfig.ABANDON_RECORD:
				Toast.makeText(getApplicationContext(), "放弃录音", Toast.LENGTH_LONG).show();
				iv_showIsReconding.setVisibility(View.INVISIBLE);
				tv_recondPressTip.setVisibility(View.INVISIBLE);
				tv_finish_recond_tip.setVisibility(View.INVISIBLE);
				break;
			//结束录音	
			case MessageConfig.FINISH_RECORD:
				iv_showIsReconding.setVisibility(View.INVISIBLE);
				tv_recondPressTip.setVisibility(View.VISIBLE);
				tv_finish_recond_tip.setVisibility(View.INVISIBLE);
				
				Bundle data = msg.getData();
				//拿到音频文件的路径
				String path = data.getString("filename");
				PublishConfigManger.voicePath=path;
				Toast.makeText(getApplicationContext(), "录音结束", Toast.LENGTH_SHORT).show();
				hideChatLayout();
				updateVoiceImageView(publishFragment.getVoiceImageView());
				break;
				
			case MessageConfig.HIDE_BOTTOM:
				hideChatLayout();
			}
		}

	};
	/**
	 * <p>
	 * 		当用户使用了语音功能，这时候语音控件会显示出来，并设置语音控件相关点击事件。
	 * </p>
	 * @param imageView		语音控件
	 */
	private void updateVoiceImageView(ImageView imageView) {
		//有发表语音
		if(!PublishConfigManger.voicePath.equals("")){
			imageView.setVisibility(View.VISIBLE);
			imageView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					v.setVisibility(View.GONE);
					PublishConfigManger.voicePath="";
					return true;
				}
			});
			imageView.setOnClickListener(new OnClickListener() {
				//播放语音
				@Override
				public void onClick(View v) {
					
				}
			});
		}
	}
}
