package com.lym.twogoods.ui;

import java.io.File;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ActivityRequestResultCode;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.mine.fragment.PersonalityInfoFragment;
import com.lym.twogoods.ui.base.BackFragmentActivity;
import com.lym.twogoods.utils.PicturesHelper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


/**
 * <p>
 * 	个人更多信息Activity
 * </p>
 * <p>
 * 	必须传递用户数据:<pre>intent.putExtra("user", user);</pre>
 * </p>
 * 
 * @author 麦灿标
 * */
public class PersonalityInfoActivity extends BackFragmentActivity{

	private final static String TAG = "PersonalityInfoActivity";

	private User mUser;
	
	private PersonalityInfoFragment mPersonalityInfoFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterTitle(getResources().getString(R.string.more_personality));
		
		mUser = (User) getIntent().getSerializableExtra("user");
		mPersonalityInfoFragment = new PersonalityInfoFragment(mUser);
		showFragment(mPersonalityInfoFragment);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "onActivityResult requestCode:" + requestCode + " resultCode:" + resultCode);
		
		//更换头像时从系统相机返回
		if(requestCode == ActivityRequestResultCode.MINE_HEAD_PICTURE_TAKE_PHOTO_REQUESTCODE && resultCode == -1) {
			
			File file = new File(DiskCacheManager.getInstance(this).getUserHeadPictureCachePath(), mUser.getUsername());
			Uri uri = Uri.fromFile(file);
			int cropRequestCode = ActivityRequestResultCode.MINE_HEAD_PICTURE_CROP_PHOTO_REQUESTCODE;
			PicturesHelper.crop(this, uri, cropRequestCode);
			
		//更换头像时从系统相册返回
		} else if(requestCode == ActivityRequestResultCode.MINE_HEAD_PICTURE_GALLERY_PHOTO_REQUESTCODE
				&& resultCode == -1 && data != null) {
			
			Uri uri = data.getData();
			int cropRequestCode = ActivityRequestResultCode.MINE_HEAD_PICTURE_CROP_PHOTO_REQUESTCODE;
			PicturesHelper.crop(this, uri, cropRequestCode);
			
		} else if(requestCode == ActivityRequestResultCode.MINE_HEAD_PICTURE_CROP_PHOTO_REQUESTCODE 
				&& requestCode == -1 && data != null) {
			Bitmap bitmap = data.getParcelableExtra("data");
			if(bitmap != null) {
				//上传头像到服务器
			
			}
			
		}
	}
	
	private void uploadHeadPicture() {
		
	}
}
