package com.lym.twogoods.mine.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.bmob.BmobProFile;
import com.bmob.btp.callback.UploadListener;
import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.Goods;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ActivityRequestResultCode;
import com.lym.twogoods.fragment.base.BaseListFragment;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.mine.adapter.PersonalityInfoAdapter;
import com.lym.twogoods.utils.FileUtil;
import com.lym.twogoods.utils.ImageUtil;
import com.lym.twogoods.utils.IoUtils;
import com.lym.twogoods.utils.MethodCompat;
import com.lym.twogoods.utils.PicturesHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaRouter.UserRouteInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TimePicker;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;


/**
 * 我的更多信息界面Fragment
 * 
 * @author 麦灿标
 * */
public class PersonalityInfoFragment extends BaseListFragment {

	private final static String TAG = "PersonalityInfoFragment";
	
	private User mUser;
	private PersonalityInfoAdapter mPersonalityInfoAdapter;
	
	private Uri mHeadPictureUri;
	
	/***
	 * 回滚使用
	 */
	private String preHeadFilename;
	private String preHeadUrl;
	private String preSex;
	private String prePhone;
	
	private static enum UpdateType {
		HP, SEX, PHONE
	}
	
	public PersonalityInfoFragment(User user) {
		mUser = user;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		initView();
		//如果是当前用户才设置点击事件
		if(UserInfoManager.getInstance().isLogining() 
				&& mUser.getUsername().equals(UserInfoManager.getInstance().getmCurrent().getUsername())) {
			setClickEventForListView();
		}
		
		return v;
	}
	
	private void initView() {
		mPersonalityInfoAdapter = new PersonalityInfoAdapter(mAttachActivity, mUser);
		setAdapter(mPersonalityInfoAdapter);
	}
	
	private void setClickEventForListView() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				//更换头像
				case 1:
					handleForClickHead();
					break;
				//更换性别
				case 2:
					handleForClickSex();
					break;
				//更换手机
				case 3:
					handleForClickPhone();
					break;
				default:
					break;
				}
			}
		});
	}
	
	private void handleForClickHead() {
		//上一次更新头像未结束
		if(!TextUtils.isEmpty(preHeadFilename) || !TextUtils.isEmpty(preHeadUrl)) {
			return;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(mAttachActivity);
		String[] options = new String[]{"拍照", "从相册中选择"};
		builder.setTitle("请选择");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String filePath = DiskCacheManager.getInstance(mAttachActivity).getAppSdPath();
				String fileName = mUser.getUsername();
				switch(which)
				{
				// 调用系统相机拍照更换头像
				case 0:
					mHeadPictureUri = PicturesHelper.takePhotoByCamera(PersonalityInfoFragment.this, filePath, fileName, ActivityRequestResultCode.MINE_HEAD_PICTURE_TAKE_PHOTO_REQUESTCODE);
					break;
				// 从相册中选择更换头像
				case 1:
					PicturesHelper.takePhotoByGallery(PersonalityInfoFragment.this, filePath, fileName, ActivityRequestResultCode.MINE_HEAD_PICTURE_GALLERY_PHOTO_REQUESTCODE);
					break;
				}
			}
		});
		builder.show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == -1) {
			//拍照
			if (requestCode == ActivityRequestResultCode.MINE_HEAD_PICTURE_TAKE_PHOTO_REQUESTCODE) {
				PicturesHelper.crop(this, mHeadPictureUri, ActivityRequestResultCode.MINE_HEAD_PICTURE_CROP_PHOTO_REQUESTCODE);
			//从相册选择
			} else if(requestCode == ActivityRequestResultCode.MINE_HEAD_PICTURE_GALLERY_PHOTO_REQUESTCODE && data != null) {
				PicturesHelper.crop(this, data.getData(), ActivityRequestResultCode.MINE_HEAD_PICTURE_CROP_PHOTO_REQUESTCODE);
			//裁切
			} else if(requestCode == ActivityRequestResultCode.MINE_HEAD_PICTURE_CROP_PHOTO_REQUESTCODE && data != null) {
				Bitmap bm = data.getParcelableExtra("data");
				String path = saveBitmap(bm);
				if(!TextUtils.isEmpty(path)) {
					//上传
					startChangeHeadPicture(path);
				}
			}
		}
	}
	
	//保存位图到头像缓存文件夹中，保存成功返回相应的路径，失败返回null
	private String saveBitmap(Bitmap bm) {
		if(bm == null) {
			return null;
		}
		String filePath = DiskCacheManager.getInstance(mAttachActivity).getAppSdPath();
		String fileName = mUser.getUsername();
		String path = filePath + fileName;
		File f = new File(path);
		OutputStream os = null;
		try {
			if(!f.exists() && !f.createNewFile()) {
				return null;
			} else {
				os = new FileOutputStream(f);
				if(bm.compress(CompressFormat.JPEG, getHeadPictureCompressQuality(bm), os)) {
					return path;
				} else {
					return null;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if(os != null) {
					os.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private int getHeadPictureCompressQuality(Bitmap bm) {
		long size = ImageUtil.sizeOfBitmap(bm);
		//(0,128KB)
		if(size < 128 * 1024) {
			return 100;
		//[128kB, 512KB)
		} else if(size <  512 * 1024) {
			return 50;
		//[512KB, 10MB)
		} else if(size < 10 * 1024 * 1024) {
			return 20;
		//[10MB, ...)
		} else {
			return 5;
		}
	}
	
	//处理更换头像
	private void startChangeHeadPicture(String path) {
		uploadFile(path);
	}
	
	//上传文件,path:本地文件路径
	private void uploadFile(final String path) {
		BmobProFile.getInstance(mAttachActivity).upload(path, new UploadListener() {
			
			@Override
			public void onError(int statuscode, String errormsg) {
				//删除文件
				FileUtil.deleteFile(path);
			}
			
			@Override
			public void onSuccess(String filename, String url, BmobFile file) {
				deleteFile(mUser.getHead_filename());
				preHeadFilename = mUser.getHead_filename();
				preHeadUrl = mUser.getHead_url();
				mUser.setHead_filename(filename);
				mUser.setHead_url(file.getUrl());
				//更新数据
				updateUserData(UpdateType.HP);
				//删除文件
				FileUtil.deleteFile(path);
			}
			
			@Override
			public void onProgress(int progress) {
			}
		});
	}
	
	//删除文件,path:Bmob上传文件名
	private void deleteFile(String filename) {
		BmobProFile.getInstance(mAttachActivity).deleteFile(filename, null);
	}
	
	private void handleForClickSex() {
		//上一次修改性别未结束
		if(!TextUtils.isEmpty(preSex)) {
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mAttachActivity);
		
		Resources res = getResources();
		builder.setTitle(R.string.select_sex_tip);
		final String[] items = new String[]{res.getString(R.string.male), res.getString(R.string.female)};
		builder.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which < items.length) {
					preSex = mUser.getSex();
					mUser.setSex(items[which]);
					updateUserData(UpdateType.SEX);
				}
			}
		});
		builder.show();
	}
	
	private void handleForClickPhone() {
		//上一次修改联系方式未结束
		if(!TextUtils.isEmpty(prePhone)) {
			return;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mAttachActivity);
		
		Resources res = getResources();
		builder.setTitle(res.getString(R.string.change_phone_tip));
		final EditText et = new EditText(mAttachActivity);
		et.setText(mUser.getPhone());
		et.setSelection(et.length());
		et.setMaxLines(16);
		et.setSingleLine();
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		Drawable background = res.getDrawable(R.drawable.app_common_edittext_bg);
		MethodCompat.setBackground(et, background);
		builder.setView(et);
		builder.setNegativeButton(R.string.cancel, null);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String phone = et.getText().toString().trim();
				if(!TextUtils.isEmpty(phone)) {
					prePhone = mUser.getPhone();
					mUser.setPhone(phone);
					updateUserData(UpdateType.PHONE);
				}
			}
		});
		
		builder.show();
	}
	
	//更新服务器上用户数据
	private void updateUserData(final UpdateType type) {
		mUser.update(mAttachActivity, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				updateUI();
				clearPre(type);
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				//回滚
				rollback(type);
				clearPre(type);
			}
		});
	}
	
	private void updateUI() {
		mPersonalityInfoAdapter.notifyDataSetChanged();
	}
	
	private void rollback(UpdateType type) {
		if(type == null) {
			return;
		}
		switch (type) {
		case HP:
			mUser.setHead_filename(preHeadFilename);
			mUser.setHead_url(preHeadUrl);
			break;
		case SEX:
			mUser.setSex(preSex);
			break;
		case PHONE:
			mUser.setPhone(prePhone);
			break;
		}
	}
	
	private void clearPre(UpdateType type) {
		if(type == null) {
			return;
		}
		switch (type) {
		case HP:
			preHeadFilename = null;
			preHeadUrl = null;
			break;
		case SEX:
			preSex = null;
			break;
		case PHONE:
			prePhone = null;
			break;
		}
	}
}
