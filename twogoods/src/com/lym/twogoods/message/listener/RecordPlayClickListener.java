package com.lym.twogoods.message.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.lym.twogoods.R;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.utils.TimeUtil;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * <p>播放录音的监听器.
 * </p><p>这个类必须设置要播放的录音的url和显示录音的相关控件，可通过构造函数
 * RecordPlayClickListener(Context context, String path,ImageView voice,boolean isNet,boolean isChat) 
 * 来声明或者通过setFilePath(String path)和setImageView(ImageView iv)来设置。如果播放的录音的url不是本地文件的路径，需要通过
 * setIsNetUrl(boolean isNet)来声明；如果不是聊天的语音，则需要通过setIsChatMsg(boolean isChat)来声明
 * </p>
 * @author 尧俊锋
 *
 */
public class RecordPlayClickListener implements View.OnClickListener {
	
	String TAG = "RecordPlayClickListener";
	
	/**要播放的录音文件的url*/
	private String mVoiceUrl;
	/**相关控件*/
	private ImageView iv_voice;
	
	private AnimationDrawable anim = null;
	private Context mContext;
	private MediaPlayer mediaPlayer = null;
	
	/**判断是否正在播放录音*/
	public static boolean isPlaying = false;
	
	/**判断是否是聊天时的语音,如果为false说明是商品的语音*/
	public boolean isChatMsg = true;
	/**判断是否是网络url*/
	private boolean isNetUrl = false;
	
	public static RecordPlayClickListener currentPlayListener = null;
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mVoiceUrl = (String) msg.getData().get("data");
		};
	};
	

	/**
	 * @param context 上下文
	 **/
	public  RecordPlayClickListener(Context context){
		mContext = context;
		currentPlayListener = this;
	}
			
	/**
	 * @param context 上下文
	 * @param path 要播放的语音在本地存储的路径
	 * @param voice 显示语音的imageview
	 * @param isNet判断是否为网络url
	 * @param isChat判断是否为聊天的语音
	 * @author 尧俊锋
	 **/
	public RecordPlayClickListener(Context context, String path,ImageView voice,boolean isNet,boolean isChat) {
		this.iv_voice = voice;
		this.mVoiceUrl = path;
		this.mContext = context;
		this.isChatMsg = isChat;
		this.isNetUrl = isNet;
		currentPlayListener = this;
		iv_voice.setOnClickListener(this);
	}
	
	/**
	 * 设置要播放的录音文件路径
	 * @param path 录音文件的路径
	 */
	public void setFilePath(String path){
		this.mVoiceUrl = path;
	}
	
	/**
	 * 设置显示录音的控件
	 * @param iv 显示录音的控件
	 */
	public void setImageView(ImageView iv){
		this.iv_voice = iv;
		this.iv_voice.setOnClickListener(this);
	}
	
	/**
	 * 设置要播放的url是否为网络url true是网络url ，false是本地文件路径
	 * @param isNet
	 */
	public void setIsNetUrl(boolean isNet)
	{
		this.isNetUrl = isNet;
	}
	
	/**
	 * 设置是否为聊天时的语音 
	 * @param isChat 如果true是聊天的语音  ，false是介绍商品的语音
	 */
	public void setIsChatMsg(boolean isChat)
	{
		this.isChatMsg = isChat;
	}
	
	@Override
	public void onClick(View arg0) {
		if (isPlaying) {
			currentPlayListener.stopPlayRecord();
			return ;
		}
		startPlayRecord(mVoiceUrl, true);
	}

	@SuppressWarnings("resource")
	private void startPlayRecord(final String filePath, boolean isUseSpeaker) {
		if(iv_voice==null){
			Log.i(TAG,"未设置显示录音的控件");
			return;
		}
		//要播放的是网络url
		if(isNetUrl){
			Runnable downlaodVoice = new Runnable() {
				public void run() {
					URL url = null;
			        InputStream is = null;
			        FileOutputStream fos = null;
			        try {
			            //构建语音的url地址
			            url = new URL(filePath);
			            //开启连接
			            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			            //设置超时的时间，5000毫秒即5秒
			            conn.setConnectTimeout(5000);
			            //设置获取语音的方式为GET
			            conn.setRequestMethod("GET");
			            //响应码为200，则访问成功
			            if (conn.getResponseCode() == 200) {
			            	 Log.i(TAG,"返回码为："+200);
			                //获取连接的输入流，这个输入流就是语音文件的输入流
			                is = conn.getInputStream();
			                //文件名
			                String name = filePath.substring(40);
			                File file;
			                if(isChatMsg){
				                file = new File(DiskCacheManager.getInstance(mContext).getChatVoiceCachePath()+name);
			                }else{
				                file = new File(DiskCacheManager.getInstance(mContext).getGoodsVoiceCachePath()+name);
			                }
			                if(!file.exists()){
				                fos = new FileOutputStream(file);
				                int len = 0;
				                byte[] buffer = new byte[1024];
				                //将输入流写入到我们定义好的文件中
				                while ((len = is.read(buffer)) != -1) {
				                    fos.write(buffer, 0, len);
				                }
				                //将缓冲刷入文件
				                fos.flush();
			                }
			                Message msg = new Message();
			                Bundle data = new Bundle();
			                data.putString("data", file.getAbsolutePath());
			                msg.setData(data);
			                mHandler.sendMessage(msg);
			            }
			        } catch (Exception e) {
			            e.printStackTrace();
			        } finally {
			            try {
			                if (is != null) {
			                    is.close();
			                }
			                if (fos != null) {
			                    fos.close();
			                }
			            } catch (Exception e) {
			                e.printStackTrace();
			            }
			        }
				}
			};
			new Thread(downlaodVoice).start();
		}
		AudioManager audioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		mediaPlayer = MediaPlayer.create(mContext, Uri.parse(mVoiceUrl));
		
		if (isUseSpeaker) {
			audioManager.setMode(AudioManager.MODE_NORMAL);
			audioManager.setSpeakerphoneOn(true);
		} else {
			audioManager.setSpeakerphoneOn(false);// 关闭扬声器
			audioManager.setMode(AudioManager.MODE_IN_CALL);
		}
		try {
			
			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer arg0) {
					isPlaying = true;
					arg0.start();
					startRecordAnimation();
				}
			});
			mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							stopPlayRecord();
						}
					});
			currentPlayListener = this;
		} catch (Exception e) {
		}
	}

	/**
	 * 停止播放
	 */
	private void stopPlayRecord() {
		stopRecordAnimation();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
		}
		isPlaying = false;
	}

	/**
	 * 开启播放动画
	 */
	private void startRecordAnimation() {
		if(iv_voice==null)
			return;
		if(!isNetUrl){
			iv_voice.setImageResource(R.anim.anim_chat_voice_right);
			anim = (AnimationDrawable) iv_voice.getDrawable();
		}else{
			iv_voice.setImageResource(R.anim.anim_chat_voice_left);
			anim = (AnimationDrawable) iv_voice.getDrawable();
		}
		anim.start();
	}

	/**
	 * 停止播放动画
	 */
	private void stopRecordAnimation() {
		if(!isNetUrl){
			iv_voice.setImageResource(R.drawable.message_chat_voice_left3);
		}else{
			iv_voice.setImageResource(R.drawable.message_chat_voice_right3);
		}
		if (anim != null){
			anim.stop();
		}
	}

}

