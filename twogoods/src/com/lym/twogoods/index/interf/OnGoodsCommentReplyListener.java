package com.lym.twogoods.index.interf;

import com.lym.twogoods.bean.GoodsComment;

/**
 * 商品回复监听器
 * 
 * @author 麦灿标
 *
 */
public interface OnGoodsCommentReplyListener {

	/**
	 * 回复
	 * 
	 * @param goodsComment 要回复的评论信息
	 */
	void onReply(GoodsComment goodsComment);
}
