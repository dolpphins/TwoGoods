package com.lym.twogoods.mine.adapter;

import java.util.List;

import com.lym.twogoods.adapter.base.BaseGoodsListViewAdapter;
import com.lym.twogoods.bean.Goods;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;
import cn.bmob.v3.listener.DeleteListener;

/**
 * 我发布的商品列表适配器
 * 
 * @author 麦灿标
 */
public class MinePublishGoodsListAdapter extends BaseGoodsListViewAdapter {

	private final static String TAG = "MinePublishGoodsListAdapter";
	
	public MinePublishGoodsListAdapter(Activity at, List<Goods> goodsList) {
		super(at, goodsList);
	}

	@Override
	protected void setCustomContent(ItemViewHolder viewHolder, Goods item) {
		viewHolder.base_goods_listview_item_user_layout.setOnTouchListener(null);
		
		final Goods goods = item;
		viewHolder.base_goods_listview_item_operation.setVisibility(View.VISIBLE);
		viewHolder.base_goods_listview_item_operation.setText("删除");
		viewHolder.base_goods_listview_item_operation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i(TAG, "onClick");
				
				AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
				builder.setTitle("你真的要删除该贰货吗");
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确定", new AlertDialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						deleteGoods(goods);
					}
				});
				builder.show();
			}
		});
	}
	
	private void deleteGoods(Goods goods) {
		if(goods == null) {
			return;
		}
		final Goods item = goods;
		final ProgressDialog pb = new ProgressDialog(mActivity);
		pb.setCancelable(false);
		pb.show();
		goods.delete(mActivity, new DeleteListener() {
			
			@Override
			public void onSuccess() {
				Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
				mGoodsList.remove(item);
				notifyDataSetChanged();
				pb.dismiss();
			}
			
			@Override
			public void onFailure(int arg0, String arg1) {
				pb.dismiss();
			}
		});
	}
}
