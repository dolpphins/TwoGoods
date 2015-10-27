package com.lym.twogoods.message.fragment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.lym.twogoods.R;
import com.lym.twogoods.fragment.base.BaseFragment;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.message.ImageFloder;
import com.lym.twogoods.message.MessageConstant;
import com.lym.twogoods.message.adapter.ImageAdapter;
import com.lym.twogoods.message.adapter.MyAdapter;
import com.lym.twogoods.message.view.ListImageDirPopupWindow;
import com.lym.twogoods.message.view.ListImageDirPopupWindow.OnImageDirSelected;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PictureFragment extends BaseFragment implements OnImageDirSelected{
	
	private ProgressDialog mProgressDialog;

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	/**
	 * 某个目录下的所有的图片
	 */
	private List<String> mImgs;
	
	/**
	 * 设置一次可以发多少张相片,默认是20张
	 */
	private int selectCount = 20;
	
	//已经被选择的图片
	public List<String> selectedPics = null;
	//所有的图片
	private List<String>allImgs;

	private GridView mGirdView;
	
	private MyAdapter adapter;
	private ImageAdapter mImageAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private List<String> mDirPaths = new ArrayList<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	private RelativeLayout mBottomLy;

	private TextView mChooseDir;
	private TextView mImageCount;
	//全部图片的数量
	int totalCount = 0;

	private int mScreenHeight;

	private ListImageDirPopupWindow mListImageDirPopupWindow;
	//判断是否有点击底部进行选择目录，默认为false，即是为点击
	private Boolean isClickBottom = false;
	
	
	public static String localCameraDir;
	public static String localCameraName;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch(msg.what){
			case MessageConstant.FINISH_LOAD:
				mProgressDialog.dismiss();
				// 为View绑定数据
				data2View();
				// 初始化展示文件夹的popupWindw
				initListDirPopupWindw();
				break;
			case MessageConstant.OPEN_CAMERA:
				selectImageFromCamera();
				break;
			}
		}
	};
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.message_chat_send_local_picture, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		
		DisplayMetrics outMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;

		initView();
		getImages();
		
		initEvent();

	}
	/*
	 * 拍照发送
	 */
	public void selectImageFromCamera() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File dir = new File(DiskCacheManager.getInstance(getActivity()).getChatPictureCachePath());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		String time = String.valueOf(System.currentTimeMillis());
		File file = new File(dir, time
				+ ".jpg");
		localCameraDir = dir.getPath();
		localCameraName = time+".jpg";
		Uri imageUri = Uri.fromFile(file);
		openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		getActivity().startActivityForResult(openCameraIntent,
				MessageConstant.OPEN_CAMERA);
	}
	
	


	/**
	 * 为View绑定数据
	 */
	private void data2View()
	{
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mImageAdapter = new ImageAdapter(getActivity().getApplicationContext(),selectedPics
				,R.layout.message_chat_grid_item,allImgs,mHandler);
		mImageAdapter.setSelectCount(selectCount);
		mGirdView.setAdapter(mImageAdapter);
		mImageCount.setText(allImgs.size() + "张");
	};

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getActivity().getApplicationContext())
						.inflate(R.layout.message_chat_list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
				lp.alpha = 1.0f;
				getActivity().getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}


	
	
	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(getActivity(), "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(getActivity(), null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = getActivity()
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				Log.e("TAG", mCursor.getCount() + "");
				System.out.println("mCursor.getCount()= "+mCursor.getCount() );
				//Toast.makeText(getApplicationContext(), ""+mCursor.getCount(), Toast.LENGTH_LONG).show();
				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath))
					{
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter()
					{
						@Override
						public boolean accept(File dir, String filename)
						{
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					
					totalCount += picSize;

					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize)
					{
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				allImgs = new ArrayList<String>();
				if (mImgDir == null)
				{
					Toast.makeText(getActivity().getApplicationContext(), "一张图片都没扫描到",
							Toast.LENGTH_SHORT).show();
					return;
				}
				for(int i=0;i<mDirPaths.size();i++){

					File file = new File(mDirPaths.get(i));
					List<String> pics = Arrays.asList(file.list());
					
					int index = 0;
					
					String parentPath = file.getAbsolutePath();
					
					String path;
					
					while(index<pics.size()){
						path = parentPath + File.separator + pics.get(index);
						allImgs.add(path);
						index++;
					}
				}
				
				ImageFloder floder = new ImageFloder();
				floder.setFlag(1);
				floder.setCount(allImgs.size());
				floder.setDir(null);
				floder.setName("所有图片");
				floder.setFirstImagePath(allImgs.get(1));
				mImageFloders.add(floder);
				// 通知Handler扫描图片完成
				Message msg = new Message();
				msg.what = MessageConstant.FINISH_LOAD;
				mHandler.sendMessage(msg);
	
			}
		}).start();

	}

	/**
	 * 初始化View
	 */
	private void initView()
	{
		mGirdView = (GridView) getView().findViewById(R.id.id_gridView);
		mChooseDir = (TextView) getView().findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) getView().findViewById(R.id.id_total_count);

		mBottomLy = (RelativeLayout) getView().findViewById(R.id.id_bottom_ly);

	}

	private void initEvent()
	{
		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */
		mBottomLy.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				selectedPics = mImageAdapter.getSelectPics();
				mListImageDirPopupWindow
						.setAnimationStyle(R.style.anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
				lp.alpha = .3f;
				getActivity().getWindow().setAttributes(lp);
			}
		});
	}

	@Override
	public void selected(ImageFloder floder)
	{
		int flag = floder.getFlag();
		if(flag==0){
			isClickBottom = true;
			mImgDir = new File(floder.getDir());
			mImgs = Arrays.asList(mImgDir.list(new FilenameFilter()
			{
				@Override
				public boolean accept(File dir, String filename)
				{
					if (filename.endsWith(".jpg") || filename.endsWith(".png")
							|| filename.endsWith(".jpeg"))
						return true;
					return false;
				}
			}));
			/**
			 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
			 */
			adapter = new MyAdapter(getActivity().getApplicationContext(),selectedPics, mImgs,
					R.layout.message_chat_grid_item, mImgDir.getAbsolutePath());
			mGirdView.setAdapter(adapter);
			// mImageAdapter.notifyDataSetChanged();
			mImageCount.setText(floder.getCount() + "张");
			mChooseDir.setText(floder.getName());
			mListImageDirPopupWindow.dismiss();
		}else{
			isClickBottom = false;
			mGirdView.setAdapter(mImageAdapter);
			mImageCount.setText(allImgs.size() + "张");
			mChooseDir.setText("所有图片");
			mListImageDirPopupWindow.dismiss();
		}
		
	}
	
	public List<String> getSelectPics()
	{
		if(isClickBottom){
			return adapter.getSelectedPics();
		}else{
			return mImageAdapter.getSelectPics();
		}
	}
	/**
	 * 设置一次最多可以发送多少张图片
	 * 
	 * @param count 最多可以发送图片数量
	 */
	public void setSelectCount(int count)
	{
		selectCount = count;
	}

}
