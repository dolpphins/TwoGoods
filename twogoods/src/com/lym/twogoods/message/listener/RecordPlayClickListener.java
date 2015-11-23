package com.lym.twogoods.message.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.ChatDetailBean;
import com.lym.twogoods.manager.DiskCacheManager;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.widget.ImageView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.config.BmobConfig;
import cn.bmob.im.util.BmobLog;
import cn.bmob.im.util.BmobUtils;



/**
 * 播放录音的监听器
 * @author yao
 *
 */
public class RecordPlayClickListener implements View.OnClickListener {
	
	ChatDetailBean voiceMessage;
	ImageView iv_voice;
	private AnimationDrawable anim = null;
	Context context;
	String currentUsername = "";
	MediaPlayer mediaPlayer = null;
	public static boolean isPlaying = false;
	public static RecordPlayClickListener currentPlayListener = null;
	static ChatDetailBean currentMsg = null;// 用于区分两个不同语音的播放


	public RecordPlayClickListener(Context context, ChatDetailBean msg,
			ImageView voice) {
		this.iv_voice = voice;
		this.voiceMessage = msg;
		this.context = context;
		currentMsg = msg;
		currentPlayListener = this;
		currentUsername = UserInfoManager.getInstance().getmCurrent().getUsername();
		iv_voice.setOnClickListener(this);
	}

	/**
	 * 播放语音
	 * 
	 * @Title: playVoice
	 * @Description: TODO
	 * @param @param filePath
	 * @param @param isUseSpeaker
	 * @return void
	 * @throws
	 */
	@SuppressWarnings("resource")
	public void startPlayRecord(String filePath, boolean isUseSpeaker) {
		if (!(new File(filePath).exists())) {
			System.out.println("voice文件不存在");
			return;
		}
		System.out.println("voice开始播放语音");
		AudioManager audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		mediaPlayer = new MediaPlayer();
		if (isUseSpeaker) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			audioManager.setMode(AudioManager.MODE_IN_CALL);
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_VOICE_CALL);
		}
		

		try {
			mediaPlayer.reset();
			// 单独使用此方法会报错播放错误:setDataSourceFD failed.: status=0x80000000
			// mediaPlayer.setDataSource(filePath);
			// 因此采用此方式会避免这种错误
			FileInputStream fis = new FileInputStream(new File(filePath));
			mediaPlayer.setDataSource(fis.getFD());
			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer arg0) {
					// TODO Auto-generated method stub
					isPlaying = true;
					currentMsg = voiceMessage;
					arg0.start();
					startRecordAnimation();
				}
			});
			mediaPlayer
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							stopPlayRecord();
						}

					});
			currentPlayListener = this;
		} catch (Exception e) {
			BmobLog.i("播放错误:" + e.getMessage());
		}
		System.out.println("voice结束播放语音");
	}

	/**
	 * 停止播放
	 * 
	 * @Title: stopPlayRecord
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	public void stopPlayRecord() {
		stopRecordAnimation();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;
	}

	/**
	 * 开启播放动画
	 * 
	 * @Title: startRecordAnimation
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void startRecordAnimation() {
		if (voiceMessage.getUsername().equals(currentUsername)) {
			iv_voice.setImageResource(R.anim.anim_chat_voice_right);
		} else {
			iv_voice.setImageResource(R.anim.anim_chat_voice_left);
		}
		anim = (AnimationDrawable) iv_voice.getDrawable();
		anim.start();
	}

	/**
	 * 停止播放动画
	 * 
	 * @Title: stopRecordAnimation
	 * @Description: TODO
	 * @param
	 * @return void
	 * @throws
	 */
	private void stopRecordAnimation() {
		if (voiceMessage.getUsername().equals(currentUsername)) {
			iv_voice.setImageResource(R.drawable.message_chat_voice_left3);
		} else {
			iv_voice.setImageResource(R.drawable.message_chat_voice_right3);
		}
		if (anim != null) {
			anim.stop();
		}
	}

	@Override
	public void onClick(View arg0) {
		if (isPlaying) {
			currentPlayListener.stopPlayRecord();
			if (currentMsg != null
					&& currentMsg.hashCode() == voiceMessage.hashCode()) {
				currentMsg = null;
				return;
			}
		}
		System.out.println("voiceClick");
		if (voiceMessage.getUsername().equals(currentUsername)) {// 如果是自己发送的语音消息，则播放本地地址
			String localPath = voiceMessage.getMessage();
			startPlayRecord(localPath, true);
		} else {// 如果是收到的消息，则需要先下载后播放
			String localPath = getDownLoadFilePath(voiceMessage);
			System.out.println("voice"+ "收到的语音存储的地址:" + localPath);
			startPlayRecord(localPath, true);
		}
	}

	public String getDownLoadFilePath(ChatDetailBean msg) {
		
		
		File dir = new File(DiskCacheManager.getInstance(this.context)
				+ currentUsername + File.separator + msg.getUsername());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// 在当前用户的目录下面存放录音文件
		File audioFile = new File(dir.getAbsolutePath() + File.separator
				+ msg.getPublish_time() + ".amr");
		try {
			if (!audioFile.exists()) {
				audioFile.createNewFile();
			}
		} catch (IOException e) {
		}
		return audioFile.getAbsolutePath();
	}

}
