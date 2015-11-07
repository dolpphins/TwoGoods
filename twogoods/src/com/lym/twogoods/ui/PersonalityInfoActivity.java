package com.lym.twogoods.ui;

import com.lym.twogoods.R;
import com.lym.twogoods.bean.User;
import com.lym.twogoods.mine.fragment.PersonalityInfoFragment;
import com.lym.twogoods.ui.base.BackFragmentActivity;

import android.os.Bundle;


/**
 * <p>
 * 	个人更多信息Activity
 * </p>
 * <p>
 * 	必须传递用户数据:<pre>intent.putExtra("user", user);</pre>
 * </p>
 * 
 * @author 麦灿标
 * */
public class PersonalityInfoActivity extends BackFragmentActivity{

	private PersonalityInfoFragment mPersonalityInfoFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCenterTitle(getResources().getString(R.string.more_personality));
		
		User user = (User) getIntent().getSerializableExtra("user");
		mPersonalityInfoFragment = new PersonalityInfoFragment(user);
		showFragment(mPersonalityInfoFragment);
	}
}
