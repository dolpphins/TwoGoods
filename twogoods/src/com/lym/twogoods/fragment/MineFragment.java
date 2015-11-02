package com.lym.twogoods.fragment;

import com.lym.twogoods.R;
import com.lym.twogoods.UserInfoManager;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.manager.UniversalImageLoaderConfigurationManager;
import com.lym.twogoods.manager.UniversalImageLoaderManager;
import com.lym.twogoods.manager.UniversalImageLoaderOptionManager;
import com.lym.twogoods.screen.UserScreen;
import com.lym.twogoods.ui.PersonalityInfoActivity;
import com.lym.twogoods.ui.SettingsActivity;
import com.lym.twogoods.utils.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * <p>
 * 	我的Fragment
 * </p>
 * 
 * @author 麦灿标
 * */
public class MineFragment extends BaseFragment{

	private View mView;
	
	//头部控件
	private TextView mine_more;
	private ImageView mine_head_picture;
	private TextView mine_username;
	private TextView mine_browse_num;
	private TextView mine_description;
	
	//底部控件
	private RelativeLayout mine_publish_goods;
	private RelativeLayout mine_focus_goods;
	private RelativeLayout mine_settings;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
//		//测试使用
//		User mCurrent = new User();
//		mCurrent.setUsername("hello");
//		mCurrent.setDeclaration("专卖小米手机");
//		mCurrent.setHead_url("http://d.hiphotos.baidu.com/zhidao/pic/item/472309f790529822090ecdf3d5ca7bcb0a46d4c5.jpg");
//		UserInfoManager.getInstance().setmCurrent(mCurrent);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		mView = LayoutInflater.from(mAttachActivity).inflate(R.layout.mine_fragment_layout, null);
		
		mine_more = (TextView) mView.findViewById(R.id.mine_more);
		mine_head_picture = (ImageView) mView.findViewById(R.id.mine_head_picture);
		mine_username = (TextView) mView.findViewById(R.id.mine_username);
		mine_browse_num = (TextView) mView.findViewById(R.id.mine_browse_num);
		mine_description = (TextView) mView.findViewById(R.id.mine_description);
		
		mine_publish_goods = (RelativeLayout) mView.findViewById(R.id.mine_publish_goods);
		mine_focus_goods = (RelativeLayout) mView.findViewById(R.id.mine_focus_goods);
		mine_settings = (RelativeLayout) mView.findViewById(R.id.mine_settings);
		
		initHeaderView();
		setOnClickEventForView();

		return mView;
		
	}

	private void initHeaderView() {
		//已登录
		if(UserInfoManager.getInstance().isLogining()) {
			User user = UserInfoManager.getInstance().getmCurrent();
			
			loadPictureThumnail(user.getHead_url());
			mine_username.setText(user.getUsername());
			mine_browse_num.setText("浏览数:" + user.getBrowse_num());
			mine_description.setText(user.getDeclaration());
		//未登录
		} else {
			mine_username.setText(mAttachActivity.getResources().getString(R.string.please_login));
		}
	}

	private void setOnClickEventForView() {
		mine_more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkIsLogining()) {
					//跳转到个人更多信息页面
					Intent intent = new Intent(mAttachActivity, PersonalityInfoActivity.class);
					startActivity(intent);
				}
			}
		});
		mine_head_picture.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!UserInfoManager.getInstance().isLogining()) {
					goToLogin();
				} else {
					
				}
			}
		});
		mine_username.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!UserInfoManager.getInstance().isLogining()) {
					goToLogin();
				}
			}
		});
		mine_publish_goods.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkIsLogining()) {
					
				}
			}
		});
		mine_focus_goods.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(checkIsLogining()) {
					
				}
			}
		});
		mine_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//跳转到设置界面
				Intent intent = new Intent(mAttachActivity, SettingsActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	private boolean checkIsLogining() {
		if(UserInfoManager.getInstance().isLogining()) {
			return true;
		} else {
			Toast.makeText(mAttachActivity, "请先登录", Toast.LENGTH_SHORT).show();
			return false;
		}
	}
	
	private void goToLogin() {
		
	}
	
	//获取用户头像缩略图
	private void loadPictureThumnail(String url) {
		if(TextUtils.isEmpty(url)) {
			return;
		}
		ImageLoaderConfiguration configuration = UniversalImageLoaderConfigurationManager
				.getUserHeadPictureThumbnailImageLoaderConfiguration(mAttachActivity);
		ImageLoader imageLoader = UniversalImageLoaderManager.getImageLoader(configuration);
		
		//使用Options无效果!必须改
		int w = UserScreen.getMineHeadPictureSize(mAttachActivity);
		BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
		decodingOptions.outHeight = w;
		decodingOptions.outWidth = w;
		DisplayImageOptions options = UniversalImageLoaderOptionManager.getHeadPictureDisplayImageOption(decodingOptions, w, w);
		
		imageLoader.displayImage(url, mine_head_picture, options, new SimpleImageLoadingListener(){
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				System.out.println(ImageUtil.sizeOfBitmap(loadedImage));
			}
		});
	}
}
