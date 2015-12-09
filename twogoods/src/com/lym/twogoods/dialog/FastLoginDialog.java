package com.lym.twogoods.dialog;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.Login;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.ui.RegisterActivity;
import com.lym.twogoods.user.Loginer;
import com.lym.twogoods.user.listener.DefaultLoginListener;
import com.lym.twogoods.utils.EncryptHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 快速登录对话框
 * 
 * @author 麦灿标
 *
 */
public class FastLoginDialog {

	private Context mContext;
	
	private View mView;
	private AlertDialog mDialog;
	
	private EditText app_fast_login_username;
	private EditText app_fast_login_password;
	private TextView app_fast_login_tip;
	private TextView app_fast_login_button;
	private TextView app_fast_login_register;
	
	private OnFastLoginListener mFastLoginListener;
	
	public FastLoginDialog(Context context) {
		mContext = context;
		init();
	}
	
	private void init() {
		mView = LayoutInflater.from(mContext).inflate(R.layout.app_fast_login_dialog, null);
		mDialog = new AlertDialog.Builder(mContext)
					.setView(mView)
					.create();
		
		app_fast_login_username = (EditText) mView.findViewById(R.id.app_fast_login_username);
		app_fast_login_password = (EditText) mView.findViewById(R.id.app_fast_login_password);
		app_fast_login_tip = (TextView) mView.findViewById(R.id.app_fast_login_tip);
		app_fast_login_button = (TextView) mView.findViewById(R.id.app_fast_login_button);
		app_fast_login_register = (TextView) mView.findViewById(R.id.app_fast_login_register);
		
		//登录按钮
		app_fast_login_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				app_fast_login_tip.setText("");
				String username = app_fast_login_username.getText().toString();
				String password = app_fast_login_password.getText().toString();
				Login item = new Login();
				item.setUsername(username);
				item.setPhone(username);
				item.setPassword(EncryptHelper.getMD5(password));
				Loginer.getInstance().login(mContext, item, new FastLoginListener(mContext));
			}
		});
		//注册按钮
		app_fast_login_register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hide();
				Intent intent = new Intent(mContext, RegisterActivity.class);
				mContext.startActivity(intent);
			}
		});
	}
	
	/**
	 * 显示对话框
	 */
	public void show() {
		if(mDialog != null && !mDialog.isShowing()) {
			mDialog.show();
		}
	}
	
	/**
	 * 隐藏对话框
	 */
	public void hide() {
		if(mDialog != null && mDialog.isShowing()) {
			mDialog.hide();
		}
	}
	
	public void setOnFastLoginListener(OnFastLoginListener listener) {
		mFastLoginListener = listener;
	}
	
	public OnFastLoginListener getOnFastLoginListener() {
		return mFastLoginListener;
	}
	
	private class FastLoginListener extends DefaultLoginListener {
		
		public FastLoginListener(Context context) {
			super(context);
		}

		@Override
		public void onStart() {
			super.onStart();
		}
		
		@Override
		public void onError(int errorCode) {
			super.onError(errorCode);
			String message = Loginer.getErrorMessage(mContext, errorCode);
			app_fast_login_tip.setText(message);
			if(mFastLoginListener != null) {
				mFastLoginListener.onError(errorCode);
			}
		}
		
		@Override
		public void onSuccess(User user) {
			super.onSuccess(user);
			hide();
			if(mFastLoginListener != null) {
				mFastLoginListener.onSuccess(user);
			}
		}
	}
	
	public interface OnFastLoginListener {
		
		void onError(int errorCode);
		
		void onSuccess(User user);
	}
}
