package com.lym.twogoods.publish.ui;

import java.util.ArrayList;

import com.lym.twogoods.R;
import com.lym.twogoods.publish.adapter.PublishSpinnerListViewAdapter;
import com.lym.twogoods.screen.DisplayUtils;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;

public class PublishSpinner extends Spinner implements OnItemClickListener {

	private PublishSpinnerDialog publishSpinnerDialog;
	private ArrayList<String> list;
	private String text;

	public PublishSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean performClick() {
		Context context = getContext();
		LayoutInflater minInflater = LayoutInflater.from(context);
		View view = minInflater.inflate( R.layout.publish_spinner_layout, null);
		ListView listView = (ListView) view
				.findViewById(R.id.lv_publish_spinner_list);
		PublishSpinnerListViewAdapter publishSpinnerListViewAdapter = new PublishSpinnerListViewAdapter(
				context, getList());
		listView.setAdapter(publishSpinnerListViewAdapter);
		listView.setOnItemClickListener(this);
		publishSpinnerDialog = new PublishSpinnerDialog(context,
				R.style.PublishSpinnerDialog);
		@SuppressWarnings("deprecation")
		LayoutParams params = new LayoutParams(
				DisplayUtils.getScreenWidthPixels((Activity) context)/2,
				DisplayUtils.getScreenHeightPixels((Activity)context)/2);
		publishSpinnerDialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
		publishSpinnerDialog.show();
		publishSpinnerDialog.addContentView(view, params);
		return true;
	}

	public ArrayList<String> getList() {
		return list;
	}

	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		setSelection(position);
		setText(list.get(position));
		if (publishSpinnerDialog != null) {
			publishSpinnerDialog.dismiss();
			publishSpinnerDialog = null;
		}
	}
}
