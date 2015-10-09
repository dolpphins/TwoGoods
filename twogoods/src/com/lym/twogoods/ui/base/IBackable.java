package com.lym.twogoods.ui.base;

/**
 * <p>可返回接口,该接口为内部接口,只有{@link #BackActivity}和{@link BackFragmentActivity}
 * 实现了该接口</p>
 * 
 * @author 麦灿标
 * */
interface IBackable {

	/**
	 * <p>当点击ActionBar的左边返回图标时调用该回调函数</p>
	 * */
	void onActionBarBack();
}
