package com.lym.twogoods.message.view;

import java.io.File;

import com.lym.twogoods.R;
import com.lym.twogoods.eventbus.event.FinishRecordEvent;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.message.config.RecordConfig;
import com.lym.twogoods.utils.FileUtil;
import com.lym.twogoods.utils.TimeUtil;



import de.greenrobot.event.EventBus;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 录音的布局，其中实现了录音功能，直接调用，通过EventBus来获取录的声音的路径。
 * @author yao
 *
 */
public class ImageCusView extends LinearLayout implements View.OnTouchListener{
	
	private String TAG = "ImageCusView";
	private Context mContext;
	//提示按住开始录音
	private TextView press_tip;
	//录音时的提示
	private LinearLayout recond_tips;
	//录音时音量改变动画
	private ImageView show_volumn;
	//按住录音
	private ImageView iv_recond;
	
	private static int count = 0;
	/**用来判断录下的语音应该存储在什么缓存文件，如果是RecordConfig.CHATRECORD，存储在聊天文件目录下的语音文件中，
	 	如果是RecordConfig.GOODRECORD,则存储在商品文件目录下的语音文件中.默认是存储在聊天文件目录下的语音文件中*/
	private int whichPath = 1;
	
	/**录音*/
	private MediaRecorder mMediaRecorder;
	/**放录音的文件*/
	File audioFile;
	/**录音文件的存放路径*/
	String filename = null;
	
	/**点击录音图标时的动画*/
	private Animation scaleAnimation;
	
	FinishRecordEvent finishRecordEvent = new FinishRecordEvent();

	public ImageCusView(Context context) {
		this(context, null);
	}

	public ImageCusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mMediaRecorder = new MediaRecorder();
		//初始化  
		scaleAnimation = new ScaleAnimation(0.5f, 1.0f,0.5f,1.0f,
		Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);  
		//设置动画时间  
		scaleAnimation.setDuration(500);  
		
		View view = LayoutInflater.from(context).inflate(R.layout.app_chat_record_content, this, true);
		iv_recond = (ImageView) view.findViewById(R.id.message_chat_record);
		press_tip = (TextView) view.findViewById(R.id.message_chat_recond_press_tip);
		recond_tips =  (LinearLayout) view.findViewById(R.id.message_chat_finish_recond_tip_tv);
		show_volumn = (ImageView) findViewById(R.id.message_chat_show_is_recording_iv);
		iv_recond.setOnTouchListener(this);
	}

	/**
	 * 设置图片资源
	 */
	public void setImageResource(int resId) {
		iv_recond.setImageResource(resId);
	}

	/**
	 * 设置显示的文字
	 */
	public void setTextViewText(String text) {
		press_tip.setText(text);
	}
	/**
	 * @param i 该参数的取值只能在RecordConfig中选择。
	 */
	public void setFilePath(int i){
		if(i<1||i>2)
			return;
		whichPath = i;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(v.getId()==R.id.message_chat_record){
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN://按下手指时
				try {
					v.setPressed(true);
					v.startAnimation(scaleAnimation);
					press_tip.setVisibility(View.INVISIBLE);
					show_volumn.setVisibility(View.VISIBLE);
					recond_tips.setVisibility(View.VISIBLE);
					String sBaseDiskCachePath;
					if(whichPath==RecordConfig.CHAT_RECORD){
						sBaseDiskCachePath = DiskCacheManager.getInstance(mContext).getChatVoiceCachePath();
					}else{
						if(whichPath==RecordConfig.GOOD_RECORD){
							sBaseDiskCachePath = DiskCacheManager.getInstance(mContext).getGoodsVoiceCachePath();
						}else{
							sBaseDiskCachePath = DiskCacheManager.getInstance(mContext).getChatVoiceCachePath();
						}
					}
					//创建一个临时的音频输出文件  
					String name = TimeUtil.getCurrentMilliSecond()+count+".amr";
					filename = sBaseDiskCachePath+name;
					finishRecordEvent.setPath(filename);
					audioFile = FileUtil.createFile(filename); 
					
					//设置音频来源（MIC表示麦克风）  
					mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  
					//设置音频输出格式（默认的输出格式）  
					mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);  
					//设置音频编码方式（默认的编码方式）  
					mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);  
					
					//指定音频输出文件  
					mMediaRecorder.setOutputFile(audioFile.getAbsolutePath());  
					//调用prepare方法  
					mMediaRecorder.prepare();  
					//调用start方法开始录音  
					mMediaRecorder.start(); 
					updateVolume();  
				} catch (Exception e) {
				}
				return true;
			case MotionEvent.ACTION_MOVE: //滑动手指时
					if (event.getY() < 0) {
						press_tip.setVisibility(View.VISIBLE);
						show_volumn.setVisibility(View.INVISIBLE);
						recond_tips.setVisibility(View.INVISIBLE);
						audioFile.delete();
						Toast.makeText(mContext, "放弃录音", Toast.LENGTH_SHORT).show();
					}
					return true;
			case MotionEvent.ACTION_UP://放开手指时
				v.setPressed(false);
				count++;
				press_tip.setVisibility(View.VISIBLE);
				show_volumn.setVisibility(View.INVISIBLE);
				recond_tips.setVisibility(View.INVISIBLE);
				Message msg = new Message();
				Bundle data = new Bundle();
				data.putString("filename", filename);
				msg.setData(data);
				mMediaRecorder.setOnErrorListener(null);
				mMediaRecorder.setOnInfoListener(null);
				mHandler.removeCallbacks(mUpdateMicStatusTimer);
				try{
					mMediaRecorder.stop();
				}catch(Exception e){
					Log.i(TAG,"调用录音结束时的stop()函数出现异常");
				}
				try {
					if (event.getY() < 0) {// 放弃录音
						audioFile.delete();
						Toast.makeText(mContext, "放弃录音", Toast.LENGTH_SHORT).show();
					}else{
						EventBus.getDefault().post(  
				                finishRecordEvent); 
					}
				} catch (Exception e) {
				}
				return true;
			default:
				return false;
			}
		}
		return false;
	}
	
	 private final Handler mHandler = new Handler();  
	 private Runnable mUpdateMicStatusTimer = new Runnable() {  
	    public void run() {  
	    	updateVolume();  
	    }  
	};  
	
	private int BASE = 600;  
    private int SPACE = 200;// 间隔取样时间  
	
	public void updateVolume(){
        if (mMediaRecorder != null && show_volumn != null) {  
	        int ratio = mMediaRecorder.getMaxAmplitude() / BASE;  
	        int db = 0;// 分贝  
	        if (ratio > 1)  
	            db = (int) (20 * Math.log10(ratio));  
	        switch (db / 6) {  
	        case 0:  
	        	show_volumn.setImageResource(R.drawable.message_chat_voice_right);  
	            break;  
	        case 1:  
	        	show_volumn.setImageResource(R.drawable.message_chat_voice_right1);  
	            break;  
	        case 2:  
	        	show_volumn.setImageResource(R.drawable.message_chat_voice_right2);  
	            break;  
	        case 3:  
	        	show_volumn.setImageResource(R.drawable.message_chat_voice_right3);  
	            break;  
	        default:  
	        	show_volumn.setImageResource(R.drawable.message_chat_voice_right);  
	            break;  
	        }  
	        Log.i(TAG,"分贝值："+db); 
	        mHandler.postDelayed(mUpdateMicStatusTimer, SPACE); 
        }  
	}
	/**
	 * 释放资源
	 */
	public void release(){
		mMediaRecorder.release();
		mMediaRecorder = null;
		Log.i(TAG,"release()");
	}

}
