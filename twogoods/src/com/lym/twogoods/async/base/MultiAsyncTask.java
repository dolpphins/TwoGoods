package com.lym.twogoods.async.base;

import android.content.Context;

/**
 * <p>
 * 	多任务异步任务类 ,该类可同时执行多个类型相同的任务.
 * </p>
 * 
 * @author 麦灿标
 * */
public abstract class MultiAsyncTask<Params, Progress, Result> extends BaseAsyncTask<Params, Progress, Result>{

	/** 用于记录当前任务号 */
	private int mPosition;
	
	public MultiAsyncTask(int position, Context context) {
		super(context);
		mPosition = position;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		onPreExecute(mPosition);
	}
	
	@Override
	protected void onProgressUpdate(Progress... values) {
		super.onProgressUpdate(values);
		onProgressUpdate(mPosition, values);
	}
	
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		onPostExecute(mPosition, result);
	}
	
	public abstract void onPreExecute(int position);
	
	public abstract void onProgressUpdate(int position, Progress... values);
	
	public abstract void onPostExecute(int position, Result result);
	
}
