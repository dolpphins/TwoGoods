package com.lym.twogoods.ui;

import java.io.File;
import java.util.ArrayList;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.lym.twogoods.R;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.message.config.MessageConfig;
import com.lym.twogoods.message.fragment.CameraFragment;
import com.lym.twogoods.message.fragment.PictureFragment;
import com.lym.twogoods.ui.base.BackActivity;
import com.lym.twogoods.ui.base.BackFragmentActivity;
import com.lym.twogoods.utils.FileUtil;
import com.lym.twogoods.utils.ImageUtil;
import com.lym.twogoods.utils.TimeUtil;


/**
 * <p>在调用SendPictureActivity的Activity中,重写onActivityResult方法，拿到resultCode.</p>
 * <p>如果resultCode等于MessageConstant.SEND_CAMERA_PIC,说明发送的是相机拍的相片。通过data.getExtras().
 * getString("picture")就可以拿到图片在本地的路径;</p>
 * <p>如果等于MessageConstant.SEND_LOCAL_PIC
 * 说明发送的本地相片。通过data.getExtras().getStringArrayList("pictures")就可以拿到要发送
 * 的本地图片路径的ArrayList。</p>
 * <p>一次最多只能发多少张需要由调用者设置。通过intent.putExtra(String name,
 *  int value)来设置,函数参数中的name的值 必须等于"picCount",value的值调用者决定.具体实现看ChatActivity
 *  中的代码</p>
 * 
 * @author 尧俊锋
 * */

public class SendPictureActivity extends BackFragmentActivity{
	
	private String TAG = "SendPictureActivity";
	
	private CameraFragment mCameraFragment;
	private PictureFragment mPictureFragment;
	
	private TextView confirm;
	//判断当前是哪个fragment,默认是mPictureFragment
	private int tag = 0;
	private int count = 0;
	private List<String> compressFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void init() {
		setCenterTitle(getResources().getString(R.string.chat_select_picture));
		setRightTitle(getResources().getString(R.string.chat_send_picture));
		
		initFragment();
		
		Intent intent = getIntent();
		int count = (Integer) intent.getExtras().get("picCount");
		mPictureFragment.setSelectCount(count);
		
		initEvent();
		
	}
	
	//初始化fragment
	private void initFragment() {
		mCameraFragment = new CameraFragment();
		mPictureFragment = new PictureFragment();
		addFragment(mCameraFragment);
		addFragment(mPictureFragment);
		showFragment(mPictureFragment);
		tag = 0;
	}
	
	//初始化顶部按钮事件
	private void initEvent() {
		confirm = (TextView) findViewById(R.id.app_common_actionbar_right_tv);
		confirm.setClickable(true);
		confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(tag == 0){//发送本地的相片
					if(mPictureFragment.getSelectPics().size()<=0){
						Toast.makeText(SendPictureActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();
						return;
					}
					Animation anim = AnimationUtils.loadAnimation(SendPictureActivity.this, R.anim.display_pictures_loading_anim);
					mPictureFragment.playAnimation(anim);
					//压缩图片
					compressFiles = new ArrayList<String>();//压缩后的图片路径
					final List<String>list = mPictureFragment.getSelectPics();
//					String filename;
//					String filepath;
//					for(int i = 0;i<list.size();i++){
//						count = i;
//						filename = "sent"+TimeUtil.getCurrentMilliSecond()+".jpg";
//						filepath = DiskCacheManager.getInstance(getApplicationContext()).
//								getSendPictureCachePath()+filename;
//						//ImageUtil.saveBitmap(filepath,ImageUtil.compressImage(list.get(i)));
//						new Thread(new Runnable() {
//							@Override
//							public void run() {
//								ImageUtil.saveBitmap(compressFiles.get(count),ImageUtil.compressImage(list.get(count)));
//							}
//						}).start();
//						compressFiles.add(filepath);
//					}
//					sendImagesToFriend(compressFiles);
					sendImagesToFriend(list);
					
				}else{//发送相机拍的相片
					//String filename = "sent"+TimeUtil.getCurrentMilliSecond()+".jpg";
					//String filepath = DiskCacheManager.getInstance(getApplicationContext()).
					//		getSendPictureCachePath()+filename;
					//ImageUtil.saveBitmap(filepath,ImageUtil.compressImage(mCameraFragment.mImagePath));
					
					sendCameraPicToFriend(mCameraFragment.mImagePath);
				}
			}
		});
	}
	
	/*
	 * 将刚刚拍的相片发给对方
	 */
	protected void sendCameraPicToFriend(String picPath) {
    	Intent data=new Intent();  
        data.putExtra("picture", picPath);
        setResult(MessageConfig.SEND_CAMERA_PIC, data);  
        this.finish();
	}

	/*
	 * 将已经选好的本地相片发给对方
	 */
	protected void sendImagesToFriend(List<String> selectedPics) {
		Intent data=new Intent();  
	    ArrayList<String>value = new ArrayList<String>();
	    if(selectedPics.size()==0){
	    	data.putExtra("pictures", "没有相片");
	    }else{
		    for(int i = 0;i<selectedPics.size();i++)
		    {
		    	value.add(selectedPics.get(i));
		    }
	        data.putStringArrayListExtra("pictures", value);
	    }
        //请求代码可以自己设置，这里设置成20  
        setResult(MessageConfig.SEND_LOCAL_PIC, data);  
        //关闭掉这个Activity  
        this.finish();
        
	}

	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case MessageConfig.OPEN_CAMERA:
			
			File file = new File(PictureFragment.localCameraDir, PictureFragment.localCameraName);
			if(file.exists()){
				showFragment(mCameraFragment);
				mCameraFragment.setImageDir(PictureFragment.localCameraDir);
				mCameraFragment.setImageName(PictureFragment.localCameraName);
				setCenterTitle(null);
				setRightTitle(getResources().getString(R.string.chat_finish_camera));
				tag = 1;
			}else{
				this.finish();
			}
			
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	@Override
	public void finish() {
		super.finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
}
