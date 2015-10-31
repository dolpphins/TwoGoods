package com.lym.twogoods.async;

import com.lym.twogoods.async.base.MultiAsyncTask;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * <p>
 * 	多图片加载异步任务类
 * </p>
 * <p>
 * 	采用装饰者模式
 * </p>
 * 
 * @author 麦灿标
 * */
public class MultiPicturesAsyncTask extends MultiAsyncTask<String, Integer, Bitmap>{

	//被装饰对象
	private PicturesAsyncTask mTask;
	
	private OnMultiPicturesAsyncTaskListener multiPicturesAsyncTaskListener;
	
	/**
	 * 构造函数
	 * 
	 * @param position 任务位置
	 * @param task 图片加载异步任务
	 * 
	 * @throws IllegalArgumentException 如果task为null将抛出该异常
	 * */
	public MultiPicturesAsyncTask(int position, Context context, PicturesAsyncTask task) {
		super(position, context);
		if(task == null) {
			throw new IllegalArgumentException("task can't be null");
		}
		mTask = task;
	}
	
	public MultiPicturesAsyncTask(int position, Context context) {
		super(position, context);
	}
	
	@Override
	protected Bitmap doInBackground(String... params) {
		if(mTask != null) {
			return mTask.doInBackground(params);
		} else {
			return null;
		}
	}

	@Override
	public void onPreExecute(int position) {
		if(multiPicturesAsyncTaskListener != null) {
			multiPicturesAsyncTaskListener.onPreExecute(position);
		}
	}

	@Override
	public void onProgressUpdate(int position, Integer... values) {
		if(multiPicturesAsyncTaskListener != null) {
			multiPicturesAsyncTaskListener.onProgressUpdate(position, values);
		}
	}

	@Override
	public void onPostExecute(int position, Bitmap result) {
		if(multiPicturesAsyncTaskListener != null) {
			multiPicturesAsyncTaskListener.onPostExecute(position, result);
		}
	}
	
	public void setOnMultiPicturesAsyncTaskListener(OnMultiPicturesAsyncTaskListener l) {
		multiPicturesAsyncTaskListener = l;
	}
	
	public OnMultiPicturesAsyncTaskListener getOnMultiPicturesAsyncTaskListener() {
		return multiPicturesAsyncTaskListener;
	}
	
	public static interface OnMultiPicturesAsyncTaskListener {
		
		void onPreExecute(int position);
		
		void onProgressUpdate(int position, Integer... values);
		
		void onPostExecute(int position, Bitmap result);
	}

}
