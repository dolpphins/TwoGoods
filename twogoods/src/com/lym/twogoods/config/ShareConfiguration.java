package com.lym.twogoods.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lym.twogoods.R;

/**
 * <p>
 * 	分享相关配置
 * </p>
 * 
 * @author 麦灿标
 * */
public class ShareConfiguration {

	/** 分享集合 */
	private final static List<ShareItem> sShareList = new ArrayList<ShareItem>();
	
	static {
		ShareItem item1 = new ShareItem("QQ", R.drawable.img_new_share_qq);
		ShareItem item2 = new ShareItem("微信", R.drawable.img_new_share_weixin);
		ShareItem item3 = new ShareItem("新浪微博", R.drawable.img_new_share_sina);
		
		sShareList.add(item1);
		sShareList.add(item2);
		sShareList.add(item3);
	}
	
	/**
	 * <p>
	 * 	获取分享列表
	 * </p>
	 * 
	 * @return 返回分享列表
	 * */
	public static List<ShareItem> getShareList() {
		return sShareList;
	}
	
	public static class ShareItem {
		
		public ShareItem(String shareName, int shareDrawableID) {
			this.shareName = shareName;
			this.shareDrawableID = shareDrawableID;
		}
		
		public String shareName;
		
		public int shareDrawableID;
	}
}
