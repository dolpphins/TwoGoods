package com.lym.twogoods.publish.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

public class PublishSpinnerDialog extends AlertDialog {

	public PublishSpinnerDialog(Context context, int theme) {  
        super(context, theme);  
    }  
  
    public PublishSpinnerDialog(Context context) {  
        super(context);  
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    }
}
