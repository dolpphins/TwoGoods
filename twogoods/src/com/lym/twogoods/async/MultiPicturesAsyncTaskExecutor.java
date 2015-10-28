package com.lym.twogoods.async;

import java.util.ArrayList;
import java.util.List;

import com.lym.twogoods.async.MultiPicturesAsyncTask.OnMultiPicturesAsyncTaskListener;
import com.lym.twogoods.manager.DiskCacheManager;

import android.content.Context;

/**
 * <p>
 * 	多图片加载异步任务类
 * </p>
 * 
 * @author 麦灿标
 * */
public class MultiPicturesAsyncTaskExecutor{

	private Context mContext;
	
	/** 任务列表 */
	private List<MultiPicturesAsyncTask> taskList;
	
	/** 标记是否已经调用过execute方法,该方法只能被调用一次 */
	private boolean mExecuted = false;
	
	/** 异步任务加载监听器 */
	private OnMultiPicturesAsyncTaskListener multiPicturesAsyncTaskListener;
	
	/** 磁盘缓存目录,默认为{@link DiskCacheManager#getDefaultPictureCachePath()} */
	private String diskCacheDir = DiskCacheManager.getInstance(mContext).getDefaultPictureCachePath();
	
	public MultiPicturesAsyncTaskExecutor(Context context) {
		mContext = context;
	}
	
	/**
	 * <p>
	 * 	执行获取多张图片异步任务
	 * </p>
	 * 
	 * @param 要获取的图片的url集合
	 * 
	 * @param 如果params为null那么返回null,否则如果之前调用过该方法那么直接返回任务列表,
	 *        否则执行所有任务并返回任务列表.
	 * */
	public List<MultiPicturesAsyncTask> execute(String...params) {
		//之前已经调用过execute
		if(params == null) {
			return null;
		} else if(mExecuted) {
			return taskList;
		} else {
			taskList = new ArrayList<MultiPicturesAsyncTask>();
			int i = 0;
			for(String url : params) {
				PicturesAsyncTask picturesTask = new PicturesAsyncTask(mContext);
				picturesTask.setDiskCacheDir(diskCacheDir);//设置磁盘缓存目录
				MultiPicturesAsyncTask task = new MultiPicturesAsyncTask(i, mContext, picturesTask);
				//注册监听器
				if(multiPicturesAsyncTaskListener != null) {
					task.setOnMultiPicturesAsyncTaskListener(multiPicturesAsyncTaskListener);
				}
				task.execute(new String[]{url});
				taskList.add(task);
				i++;
			}
			mExecuted = true;
			return taskList;
		}
	}
	
	/**
	 * <p>
	 * 	取消指定的任务.
	 * </p>
	 * 
	 * @param 指定要取消的任务
	 * @param mayInterruptIfRunning 是否取消正在执行的任务
	 * 
	 * @return 取消成功返回true,失败返回false.
	 * */
	public boolean cancel(int position, boolean mayInterruptIfRunning) {
		if(position < 0 || position >= taskList.size()) {
			return false;
		}
		return taskList.get(position).cancel(mayInterruptIfRunning);
	}
	
	/**
	 * 注册多异步任务监听器,注意必须在{@link #execute(String...)}方法调用之前注册才有效
	 * 
	 * @param l 要注册的监听器
	 * */
	public void setOnMultiPicturesAsyncTaskListener(OnMultiPicturesAsyncTaskListener l) {
		multiPicturesAsyncTaskListener = l;
	}
	
	/**
	 * 获取多异步任务监听器
	 * 
	 * @return 返回多异步任务监听器
	 * */
	public OnMultiPicturesAsyncTaskListener getOnMultiPicturesAsyncTaskListener() {
		return multiPicturesAsyncTaskListener;
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
}
