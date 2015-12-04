package com.lym.twogoods.publish.util;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class DataMangerUtils {
	/**
	 * <p>
	 * 判断货品描述是否为空
	 * </p>
	 * @param context	上下文
	 * @param et_publish_fragment_description	货品的描述
	 * @param sp_publish_fragment_sort			货品的分类
	 * @param et_publish_fragment_tel			联系方式
	 * @param et_publish_fragment_price			货品价格
	 * @param sp_publish_fragment_date			货品的发布期限
	 * @param tv_publish_fragment_position_set	发布人的即时位置
	 * @return
	 */
	public static boolean judgeGoods(Context context,EditText et_publish_fragment_description,Spinner sp_publish_fragment_sort,EditText et_publish_fragment_tel,EditText et_publish_fragment_price,Spinner sp_publish_fragment_date,TextView tv_publish_fragment_position_set){
		if (((et_publish_fragment_description.getText().toString()).equals(""))
				|| ((sp_publish_fragment_sort.getSelectedItem().toString())
						.equals(""))
				|| ((et_publish_fragment_tel.getText().toString()).equals(""))
				|| ((et_publish_fragment_price.getText().toString()).equals(""))
				|| ((sp_publish_fragment_date.getSelectedItem().toString())
						.equals(""))
				|| ((tv_publish_fragment_position_set.getText().toString())
						.equals(""))) {
			Toast.makeText(context, "你发布的商品信息不全哦", Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		return true;
	}
	
	public static boolean goodsDescriptionIsWrote(Activity activity,EditText et_publish_fragment_description,Spinner sp_publish_fragment_sort,EditText et_publish_fragment_tel,EditText et_publish_fragment_price,Spinner sp_publish_fragment_date,TextView tv_publish_fragment_position_set) {
		if (((et_publish_fragment_description.getText().toString()).equals(""))
				&& ((sp_publish_fragment_sort.getSelectedItem().toString())
						.equals(""))
				&& ((et_publish_fragment_tel.getText().toString()).equals(""))
				&& ((et_publish_fragment_price.getText().toString()).equals(""))
				&& ((sp_publish_fragment_date.getSelectedItem().toString())
						.equals(""))
				&& ((tv_publish_fragment_position_set.getText().toString())
						.equals(""))){
			return false;
		}else {
			return true;
		}
	}
}
