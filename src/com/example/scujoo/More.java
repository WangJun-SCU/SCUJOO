package com.example.scujoo;

import com.joo.others.AboutUs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class More extends Activity implements OnClickListener{

	private ImageButton back;
	private LinearLayout firstUse;
	private LinearLayout edit;
	private LinearLayout aboutUs;
	private LinearLayout optionBack;
	private LinearLayout update;

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
		aboutUs.setOnClickListener(this);
		optionBack.setOnClickListener(this);
		update.setOnClickListener(this);
		
	}

	private void init() {
		back = (ImageButton) findViewById(R.id.more_back);
		firstUse = (LinearLayout) findViewById(R.id.more_firstuse);
		edit = (LinearLayout) findViewById(R.id.more_edit);
		aboutUs = (LinearLayout) findViewById(R.id.more_about_us);
		optionBack = (LinearLayout) findViewById(R.id.more_option_back);
		update = (LinearLayout) findViewById(R.id.more_update);
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
		case R.id.more_about_us:
			startActivity(new Intent().setClass(More.this, AboutUs.class));
			break;
		case R.id.more_update:
			Toast.makeText(getApplicationContext(), "已是最新版本！", 1).show();
			break;
		case R.id.more_option_back:
			Toast.makeText(getApplicationContext(), "暂不开通！", 1).show();
			break;
		default:
			break;
		}
		
	}

}
