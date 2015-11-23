package com.lym.twogoods.message.listener;

import java.io.File;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.message.MessageConfig;
import com.lym.twogoods.utils.FileUtil;
import com.lym.twogoods.utils.TimeUtil;

public class RecondTouchListener implements View.OnTouchListener {
	
	private Context mContext;
	
	/**录音*/
	private MediaRecorder mediaRecorder;
	/**放录音的文件*/
	File audioFile;
	/**录音文件的存放路径*/
	String filename = null;
	/**用来传递录音时的事件*/
	private Handler mHandler;
	
	private static int count = 0;
	/**点击录音图标时的动画*/
	private Animation scaleAnimation;
	
	public RecondTouchListener(Context context,Handler handler)
	{
		mContext = context;
		mHandler = handler;
		init();
	}
	private void init() {
		mediaRecorder = new MediaRecorder();
		//初始化  
		scaleAnimation = new ScaleAnimation(0.5f, 1.0f,0.5f,1.0f,
				Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
		//设置动画时间  
		scaleAnimation.setDuration(500);  
		                
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN://按下手指时
			try {
				v.setPressed(true);
				
				v.startAnimation(scaleAnimation);
				//创建一个临时的音频输出文件  
				filename = DiskCacheManager.getInstance(mContext).getChatVoiceCachePath()+TimeUtil.getCurrentMilliSecond()+count+".amr";
				audioFile = FileUtil.createFile(filename); 
				Message msg = new Message();
				msg.what = MessageConfig.START_RECORD;
				mHandler.sendMessage(msg);
				
				//设置音频来源（MIC表示麦克风）  
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
				//设置音频输出格式（默认的输出格式）  
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);  
				//设置音频编码方式（默认的编码方式）  
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);  
				
				//指定音频输出文件  
				mediaRecorder.setOutputFile(audioFile.getAbsolutePath());  
				//调用prepare方法  
				mediaRecorder.prepare();  
				//调用start方法开始录音  
				mediaRecorder.start();  
				// 开始录音
			} catch (Exception e) {
			}
			return true;
		case MotionEvent.ACTION_MOVE: //滑动手指时
				if (event.getY() < 0) {
					Message msg = new Message();
					msg.what = MessageConfig.ABANDON_RECORD;
					mHandler.sendMessage(msg);
					audioFile.delete();
				} else {
					//tv_voice_tips.setText(getString(R.string.voice_up_tips));
				//	tv_voice_tips.setTextColor(Color.WHITE);
				}
				return true;
		case MotionEvent.ACTION_UP://放开手指时
			v.setPressed(false);
			count++;
			mediaRecorder.setOnErrorListener(null);
			mediaRecorder.setOnInfoListener(null);
			mediaRecorder.stop();
			try {
				if (event.getY() < 0) {// 放弃录音
					audioFile.delete();
				} else {
					Message msg = new Message();
					msg.what = MessageConfig.FINISH_RECORD;
					Bundle data = new Bundle();
					data.putString("filename", filename);
					msg.setData(data);
					mHandler.sendMessage(msg);
				}
			} catch (Exception e) {
			}
			return true;
		default:
			return false;
		}
	}
	
	
	public void release()
	{
		mediaRecorder.release();
		mediaRecorder = null;
	}
}
