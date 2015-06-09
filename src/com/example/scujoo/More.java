package com.example.scujoo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class More extends Activity implements OnClickListener{

	private ImageButton back;
	private LinearLayout firstUse;
	private LinearLayout edit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		init();
		initEvent();

	}

	private void initEvent() {
		back.setOnClickListener(this);
		firstUse.setOnClickListener(this);
		edit.setOnClickListener(this);
		
	}

	private void init() {
		back = (ImageButton) findViewById(R.id.more_back);
		firstUse = (LinearLayout) findViewById(R.id.more_firstuse);
		edit = (LinearLayout) findViewById(R.id.more_edit);
	}

	public void onClick(View v) {
		SharedPreferences mySharedPreferences= getSharedPreferences("datas", Activity.MODE_PRIVATE); 
		SharedPreferences.Editor editor = mySharedPreferences.edit(); 
		switch(v.getId())
		{
		case R.id.more_back:
			finish();
			break;
		case R.id.more_firstuse:
			editor.remove("first");
			editor.commit();
			finish();
			break;
		case R.id.more_edit:
			editor.remove("userName");
			editor.remove("userPass");
			editor.commit();
			startActivity(new Intent().setClass(getApplication(), Login.class));
			finish();
			break;
		default:
			break;
		}
		
	}

}
