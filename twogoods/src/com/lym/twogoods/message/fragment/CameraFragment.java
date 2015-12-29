package com.lym.twogoods.message.fragment;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lym.twogoods.R;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.screen.BaseScreen;
import com.lym.twogoods.utils.ImageUtil;

public class CameraFragment extends BaseFragment{
	
	
	private ImageView photo;
	
	private String localCameraDir;
	private String localCameraName;
	
	public String mImagePath; 

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.message_chat_take_photo, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
	}

	
	private void initView() {
		photo = (ImageView) getView().findViewById(R.id.
				message_chat_take_photo_iv);
	
		mImagePath = localCameraDir+File.separator+localCameraName;

		photo.setImageBitmap(ImageUtil.decodeSampledBitmapFromFile(mImagePath,
				BaseScreen.getScreenWidth(getActivity()), BaseScreen.getScreenHeight(getActivity())));
		DisplayMetrics dm = BaseScreen.getScreenInfo(getActivity());
	}

	public void setImageDir(String dir)
	{
		localCameraDir = dir;
	}
	public void setImageName(String name)
	{
		localCameraName = name;
	}
	/*
	 * 拿到刚刚拍的照片的路径
	 */
	public String getImagePath()
	{
		return localCameraDir+File.separator+localCameraName;
	}


}
