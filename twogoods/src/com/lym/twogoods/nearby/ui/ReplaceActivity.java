package com.lym.twogoods.nearby.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.lym.twogoods.R;
import com.lym.twogoods.ui.base.BackFragmentActivity;

/**
 * <p>
 * 选择城市Activity
 * </p>
 * 
 * @author 龙宇文
 * */
public class ReplaceActivity extends BackFragmentActivity {

	// 定义控件
	private SelectCityClearEditText et_nearby_select_city_input;
	private Button btn_nearby_select_city_input_cancel;
	private ListView lv_nearby_select_city_search_result;
	private LinearLayout ll_nearby_select_city_dingwei;
	private GridView gv_nearby_select_city_hot_city;
	private Button btn_nearby_select_city_replace_add_more;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_select_city_activity);
		init();
	}

	private void init() {
		et_nearby_select_city_input = (SelectCityClearEditText) findViewById(R.id.et_nearby_select_city_replace_input);
		btn_nearby_select_city_input_cancel = (Button) findViewById(R.id.btn_nearby_select_city_replace_input_cancel);
		lv_nearby_select_city_search_result = (ListView) findViewById(R.id.lv_nearby_select_city_replace_search_result);
		ll_nearby_select_city_dingwei = (LinearLayout) findViewById(R.id.ll_nearby_select_city_replace_dingwei);
		gv_nearby_select_city_hot_city = (GridView) findViewById(R.id.gv_nearby_select_city_replace_hot_city);
		btn_nearby_select_city_replace_add_more = (Button) findViewById(R.id.btn_nearby_select_city_replace_add_more);
	}

}
