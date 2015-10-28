package com.lym.twogoods.async.base;

import android.content.Context;
import android.os.AsyncTask;

/**
 * <p>
 * 	基本异步任务类
 * </p>
 * 
 * @param <Params> 参数类型
 * @param <Progress> 进度类型
 * @param <Result> 结果类型
 * 
 * @author 麦灿标
 * */
public abstract class BaseAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{
	
	protected Context mContext;
	
	/** 异步任务处理监听器 */
	protected OnAsyncTaskListener<Progress, Result> onAsyncTaskListener;
	
	public BaseAsyncTask(Context context) {
		mContext = context;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(onAsyncTaskListener != null) {
			onAsyncTaskListener.onPreExecute();
		}
	}
	
	@Override
	protected Result doInBackground(Params... params) {
		return null;
	}
	
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if(onAsyncTaskListener != null) {
			onAsyncTaskListener.onPostExecute(result);
		}
	}
	
	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
		if(onAsyncTaskListener != null) {
			onAsyncTaskListener.onProgressUpdate(values);
		}
		
	}

	public void setOnAsyncTaskListener(OnAsyncTaskListener<Progress, Result> l) {
		onAsyncTaskListener = l;
	}
	
	public OnAsyncTaskListener<Progress, Result> getOnAsyncTaskListener() {
		return onAsyncTaskListener;
	}
	
	/**
	 * <p>
	 * 	DisplayPicturesAsyncTask执行过程监听器接口
	 * </p>
	 * 
	 * @author 麦灿标
	 * */
	public static interface OnAsyncTaskListener<Progress, Result> {
		
		/**
		 * 任务执行前回调函数
		 * */
		void onPreExecute();
		
		/**
		 * 任务执行进度更新回调函数
		 * 
		 * @param values 进度信息
		 * */
		void onProgressUpdate(Progress... values);
		
		/**
		 * 任务执行完回调函数
		 * 
		 * @param result doInBackground返回值
		 * */
		void onPostExecute(Result result);
	}
	
}
