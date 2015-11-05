package com.lym.twogoods.nearby.adapter;

import java.util.List;
import com.lym.twogoods.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * <p>
 *热门城市listview适配器
 * </p>
 * 
 * @author 龙宇文
 * */
public class NearbyHotCityListviewAdapter extends BaseAdapter implements OnClickListener{

	private List<List<String>> mList;
	private LayoutInflater mInflater;
	//获取nearby_select_city_activity.xml文件中的设置位置的textview
	private View other;
	private TextView tv;
	private Button btn;
	
	public NearbyHotCityListviewAdapter (Context context,List<List<String>> list){
		mList = list;
		mInflater = LayoutInflater.from(context);
		init();
	}
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View converView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if(converView==null){
			viewHolder=new ViewHolder();
			converView=mInflater.inflate(R.layout.listviewitem_hot_city, null);
			viewHolder.tv1=(Button) converView.findViewById(R.id.tv_select_city_col1);
			viewHolder.tv2=(Button) converView.findViewById(R.id.tv_select_city_col2);
			viewHolder.tv3=(Button) converView.findViewById(R.id.tv_select_city_col3);
			viewHolder.tv4=(Button) converView.findViewById(R.id.tv_select_city_col4);
			converView.setTag(viewHolder);
		}else {
			viewHolder=(ViewHolder) converView.getTag();
		}
		List<String> list=mList.get(position);
		viewHolder.tv1.setText(list.get(0));
		viewHolder.tv2.setText(list.get(1));
		viewHolder.tv3.setText(list.get(2));
		viewHolder.tv4.setText(list.get(3));
		viewHolder.tv1.setOnClickListener(this);
		viewHolder.tv2.setOnClickListener(this);
		viewHolder.tv3.setOnClickListener(this);
		viewHolder.tv4.setOnClickListener(this);
		return converView;
	}

	class ViewHolder{
		public Button tv1;
		public Button tv2;
		public Button tv3;
		public Button tv4;
	}

	@Override
	public void onClick(View v) {
		Log.v("tag", "点击了吗");
		btn=(Button)v;
		tv.setText(btn.getText().toString());
		Log.v("tag",  btn.getText().toString());
	}

	private void init(){
		other=mInflater.inflate(R.layout.nearby_select_city_activity, null);
		tv=(TextView) other.findViewById(R.id.tv_select_city_position_set);
	}

}
