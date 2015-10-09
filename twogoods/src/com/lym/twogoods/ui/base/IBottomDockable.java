package com.lym.twogoods.ui.base;

import android.view.View;

/**
 * <p>
 * 	底部可停靠接口,只能由Activity实现,如果实现了该接口,那么界面将出现一个停靠View,
 *  该View由{@link IBottomDockable#onCreateBottomView()}.
 *  该接口为内部接口.
 * </p>
 * 
 * 
 * */
interface IBottomDockable {

	/**
	 * 创建底部停靠布局
	 * 
	 * @return 要显示的布局View对象
	 * */
	View onCreateBottomView();
}
