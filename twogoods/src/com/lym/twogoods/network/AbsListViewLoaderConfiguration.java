package com.lym.twogoods.network;


/**
 * AbsListViewLoader配置
 * 
 * @author 麦灿标
 *
 */
public class AbsListViewLoaderConfiguration {

	private boolean mReadDiskCache;
	
	private boolean mSaveDiskCache;
	
	private AbsListViewLoaderConfiguration(Builder builder) {
		mReadDiskCache = builder.mReadDiskCache;
		mSaveDiskCache = builder.mSaveDiskCache;
	}
	
	/**
	 * 是否读取磁盘缓存
	 * 
	 * @return 是返回true,不是返回false.
	 */
	public boolean isReadDiskCache() {
		return mReadDiskCache;
	}
	
	/**
	 * 是否保存缓存到磁盘中
	 * 
	 * @return 是返回true,不是返回false.
	 */
	public boolean isSaveDiskCache() {
		return mSaveDiskCache;
	}
	
	
	public static class Builder {
		
		private boolean mReadDiskCache;
		
		private boolean mSaveDiskCache;
		
		public Builder setReadDiskCache(boolean readDiskCache) {
			mReadDiskCache = readDiskCache;
			return this;
		}
		
		public Builder setSaveDiskCache(boolean saveDiskCache) {
			mSaveDiskCache = saveDiskCache;
			return this;
		}
		
		public AbsListViewLoaderConfiguration build() {
			return new AbsListViewLoaderConfiguration(this);
		}
	}
}
