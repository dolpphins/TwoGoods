package com.lym.twogoods.async;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import com.jakewharton.disklrucache.DiskLruCache;
import com.jakewharton.disklrucache.DiskLruCache.Snapshot;
import com.lym.twogoods.AppManager;
import com.lym.twogoods.async.base.BaseAsyncTask;
import com.lym.twogoods.manager.DiskCacheManager;
import com.lym.twogoods.utils.EncryptHelper;
import com.lym.twogoods.utils.ImageUtil;
import com.lym.twogoods.utils.StringUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

/**
 * <p>
 * 	基本的图片加载异步任务
 * </p>
 * <p>
 * 	该类具有内存缓存、硬盘缓存功能.
 * </p>
 * 
 * @author 麦灿标
 * */
public class PicturesAsyncTask extends BaseAsyncTask<String, Integer, Bitmap>{
	
	private final static String TAG = "PicturesAsyncTask";
	
	/* 由于在UIL中也有图片内存缓存和硬盘缓存,因此可能会出现两倍缓存造成内存空间及硬盘空间浪费,后期优化必须统一缓存位置(可改造UIL缓存机制)
	 * 目前暂时保留两份缓存
	 */
	
	private static int maxMemory = (int) Runtime.getRuntime().maxMemory();//heap区最大大小
	private static int maxSize = maxMemory / 8;
	
	/*
	 * 内部使用的是LinkedHashMap,键和值都允许为null,因此必须避免这种情况出现.
	 * maxSize默认表示最大item数,重写为总大小(单位:字节)
	 * 要声明为类变量
	 */
	private final static LruCache<String, Bitmap> memoryCache = new LruCache<String, Bitmap>(maxSize) {
		
		@Override
		protected int sizeOf(String key, Bitmap value) {
			return ImageUtil.sizeOfBitmap(value);
		}
	};
	
	/** 磁盘缓存目录,默认为{@link DiskCacheManager#getDefaultPictureCachePath()} */
	private String diskCacheDir = DiskCacheManager.getInstance(mContext).getDefaultPictureCachePath();
	
	/** 磁盘缓存单文件最大大小,单位为字节 ,默认为20MB*/
	private long maxDiskCacheSize = 20 * 1024 * 1024;
	
	private enum Source {
		UNKNOWN, MEMORY, DISK, LOCAL_DISK, NETWORK
	}
	
	/** 标记当前图片来自哪里 */
	private Source mSource = Source.UNKNOWN;
	
	public PicturesAsyncTask(Context context) {
		super(context);
	}
	
	/**
	 * 设置磁盘缓存目录
	 * 
	 * @param dir 指定的磁盘缓存目录
	 * */
	public void setDiskCacheDir(String dir) {
		diskCacheDir = dir;
	}
	
	/**
	 * 获取磁盘缓存目录
	 * 
	 * @return 磁盘缓存目录
	 * */
	public String getDiskCacheDir() {
		return diskCacheDir;
	}
	
	/**
	 * <p>
	 * 	设置 磁盘缓存单文件最大大小,单位为字节
	 * </p>
	 * 
	 * @param size 指定的大小
	 * */
	public void setMaxDiskCacheSize(long size) {
		maxDiskCacheSize = size;
	}
	
	/**
	 * <p>
	 * 	获取磁盘缓存单文件最大大小
	 * </p>
	 * 
	 * @return 返回磁盘缓存单文件最大大小
	 * */
	public long getMaxDiskCacheSize() {
		return maxDiskCacheSize;
	}
	
	//如果同时加载多张位图则返回最后一张位图
	@Override
	protected Bitmap doInBackground(String... params) {
		if(params != null) {
			try {
				Bitmap bitmap = null;
				for(String url : params) {
					bitmap = load(url);
					saveBitmapToMemoryCache(url, bitmap);
					saveBitmapToDiskCache(url, bitmap);
				}
				return bitmap;
			} catch(Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	//加载图片,使用二级缓存机制
	private Bitmap load(String url) {
		Bitmap bitmap = null;
		bitmap = tryLoadfromMemoryCache(url);
		if(bitmap == null) {
			System.out.println("memory cache not exists");
			bitmap = tryLoadfromDiskCache(url);
			if(bitmap == null) {
				System.out.println("disk cache not exists");
				bitmap = tryLoadfromLocalDisk(url);
				if(bitmap == null) {
					bitmap = tryLoadfromNetwork(url);
					if(bitmap == null) {
						mSource = Source.UNKNOWN;//加载失败
					} else {
						mSource = Source.NETWORK;//来自网络
					}
				} else {
					mSource = Source.LOCAL_DISK;//来自本地磁盘
				}
			} else {
				mSource = Source.DISK;//来自硬盘缓存
			}
		} else {
			mSource = Source.MEMORY;//来自内存缓存
		}
		return bitmap;
	}
	
	//尝试从内存缓存加载
	private Bitmap tryLoadfromMemoryCache(String url) {
		if(url == null) {
			return null;
		}
		return memoryCache.get(url);
	}
	
	//尝试从硬盘缓存加载
	private Bitmap tryLoadfromDiskCache(String url) {
		File f = new File(diskCacheDir);
		if(!f.exists()) {
			f.mkdirs();
		}
		try {
			DiskLruCache diskLruCache = DiskLruCache.open(f, AppManager.getAppVersion(mContext), 1, maxDiskCacheSize);
			String key = generateDiskCacheKey(url);
			Snapshot snapshot = diskLruCache.get(key);
			InputStream is = snapshot.getInputStream(0);
			return BitmapFactory.decodeStream(is);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//可能是本地url，尝试从本地存储加载
	private Bitmap tryLoadfromLocalDisk(String url) {
		File f = new File(url);
		if(f.exists()) {
			try {
				return BitmapFactory.decodeFile(url);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//尝试从网络加载
	private Bitmap tryLoadfromNetwork(String url) {
		return ImageUtil.decodeBitmapFromNet(url);
	}
	
	//将位图保存到内存缓存中
	private boolean saveBitmapToMemoryCache(String url, Bitmap bitmap) {
		//其实LruCache已经排除掉这种情况了
		if(url == null || bitmap == null) {
			return false;
		}
		//不存在才放入内存缓存
		if(memoryCache.get(url) == null) {
			//判断内存是否紧张,如果是则不缓存该位图
			if(canPutMemoryCache(bitmap)) {
				memoryCache.put(url, bitmap);
			}
		}
		return true;
	}
	
	private boolean canPutMemoryCache(Bitmap bitmap) {
		if(bitmap == null) {
			return false;
		}
		int size = ImageUtil.sizeOfBitmap(bitmap);
		if(size <= 0) {
			return false;
		}
		Runtime runtime = Runtime.getRuntime();
		long freeMaxSize = runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory();
		Log.d(TAG, "remainder heap size:" + StringUtil.byteSize2String(freeMaxSize));
		if(freeMaxSize / size < 8) {
			return false;
		} else {
			return true;
		}
	}
	
	//将位图保存到磁盘缓存中
	private boolean saveBitmapToDiskCache(String url, Bitmap bitmap) {
		if(url == null || bitmap == null) {
			return false;
		}
		String key = generateDiskCacheKey(url);
		//是本地文件，或者已经缓存到磁盘了,直接返回
		if(mSource == Source.LOCAL_DISK || existInDisk(key)) {
			return false;
		}
		File f = new File(diskCacheDir);
		if(!f.exists()) {
			f.mkdirs();
		}
		try {
			DiskLruCache diskLruCache = DiskLruCache.open(f, AppManager.getAppVersion(mContext), 1, maxDiskCacheSize);
			DiskLruCache.Editor editor = diskLruCache.edit(key);
			OutputStream os = editor.newOutputStream(0);
			boolean b = bitmap.compress(CompressFormat.JPEG, 100, os);
			editor.commit();
			return b;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//由于DiskLruCache对key格式有要求,且需要对文件key进行一定的加密
	private String generateDiskCacheKey(String url) {
		return EncryptHelper.getMD5(url);
	}
	
	//判断是否已缓存在磁盘中
	private boolean existInDisk(String key) {
		File f = new File(diskCacheDir);
		if(!f.exists()) {
			return false;
		}
		try {
			DiskLruCache diskLruCache = DiskLruCache.open(f, AppManager.getAppVersion(mContext), 1, maxDiskCacheSize);
			Snapshot snapshot = diskLruCache.get(key);
			if(snapshot != null) {
				return true;
			}
			return false;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}

