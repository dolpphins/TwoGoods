package com.lym.twogoods.mine.fragment;

import com.lym.twogoods.bean.User;
import com.lym.twogoods.config.ActivityRequestResultCode;
import com.lym.twogoods.fragment.base.BaseListFragment;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.mine.adapter.PersonalityInfoAdapter;
import com.lym.twogoods.utils.PicturesHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;


/**
 * 我的更多信息界面Fragment
 * 
 * @author 麦灿标
 * */
public class PersonalityInfoFragment extends BaseListFragment {

	private User data;
	private PersonalityInfoAdapter mPersonalityInfoAdapter;
	
	public PersonalityInfoFragment(User user) {
		data = user;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		initView();
		setClickEventForListView();
		
		return v;
	}
	
	private void initView() {
		mPersonalityInfoAdapter = new PersonalityInfoAdapter(mAttachActivity, data);
		setAdapter(mPersonalityInfoAdapter);
	}
	
	private void setClickEventForListView() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 1:
					handleForClickHead();
					break;

				default:
					break;
				}
			}
		});
	}
	
	private void handleForClickHead() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mAttachActivity);
		String[] options = new String[]{"拍照", "从相册中选择"};
		builder.setTitle("请选择");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String filePath = DiskCacheManager.getInstance(mAttachActivity).getUserHeadPictureCachePath();
				String fileName = data.getUsername();
				switch(which)
				{
				// 调用系统相机拍照更换头像
				case 0:
					PicturesHelper.takePhotoByCamera(mAttachActivity, filePath, fileName, ActivityRequestResultCode.MINE_HEAD_PICTURE_TAKE_PHOTO_REQUESTCODE);
					break;
				// 从相册中选择更换头像
				case 1:
					PicturesHelper.takePhotoByGallery(mAttachActivity, filePath, fileName, ActivityRequestResultCode.MINE_HEAD_PICTURE_GALLERY_PHOTO_REQUESTCODE);
					break;
				}
			}
		});
		builder.show();
	}
}
