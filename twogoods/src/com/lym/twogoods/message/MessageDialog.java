package com.lym.twogoods.message;

import java.util.List;

import com.lym.twogoods.R;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 自定义dialog
 * 
 * @author yjf
 * 
 */
public class MessageDialog extends Dialog {

	private Context mContext;
	private List<String> list; 
	private ListView mListView;

	public MessageDialog(Context context, List<String> list) {
		super(context, R.style.messageDialog); 
		this.mContext = context;
		this.list = list;

		initComponent();

	}

	private void initComponent() {
		View view = LayoutInflater.from(mContext).inflate(
				R.layout.message_dialog, null);
		
		mListView = (ListView) view.findViewById(R.id.item_dialog_list_lv);
		mListView.setAdapter(new MyAdapter());
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				itemOnClickListener.itemOnClick(position);
			}

		});
		this.setContentView(view);
	}

	/**
	 * mListView适配器
	 * 
	 * @author TY
	 * 
	 */
	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			return list.size();
		}

		@Override
		public Object getItem(int position) {

			return list.get(position);
		}

		@Override
		public long getItemId(int position) {

			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = View.inflate(mContext,
						R.layout.message_dialog_item, null);
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.item);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.textView.setText(list.get(position));

			return convertView;
		}

		private class ViewHolder {
			private TextView textView;
		}

	}

	private MyItemOnClickListener itemOnClickListener;

	
	public interface MyItemOnClickListener {
		public void itemOnClick(int itemPosition);
	}
	
	public void setItemOnClickListener(MyItemOnClickListener myItemOnClickListener){
		this.itemOnClickListener = myItemOnClickListener;
	}
	
	public class DialogItemOnClickListener implements MyItemOnClickListener
	{

		@Override
		public void itemOnClick(int itemPosition) {
			// TODO 自动生成的方法存根
			if(itemPosition==0)
			{
				System.out.println("调用了Dialog的ItemOnClick方法");
				
			}
		}
		
	}
	
	
	
	

}
