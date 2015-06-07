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
		
	}

	private void init() {
		back = (ImageButton) findViewById(R.id.more_back);
		firstUse = (LinearLayout) findViewById(R.id.more_firstuse);
	}

	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.more_back:
			finish();
			break;
		case R.id.more_firstuse:
			SharedPreferences mySharedPreferences= getSharedPreferences("test", Activity.MODE_PRIVATE); 
			SharedPreferences.Editor editor = mySharedPreferences.edit(); 
			editor.remove("first");
			editor.commit();
			finish();
			break;
		default:
			break;
		}
		
	}

}
