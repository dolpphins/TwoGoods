package com.lym.twogoods.index.interf;


/**
 * 发表评论监听器
 * 
 * @author 麦灿标
 */
public interface OnPublishCommentListener {
	
	/**
	 * 发表评论成功回调接口
	 */
	void onSuccess();
	
	/**
	 * 发表评论失败回调接口
	 */
	void onFail();
}
