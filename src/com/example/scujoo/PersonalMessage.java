package com.example.scujoo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class PersonalMessage extends Activity {
	
	private ImageButton back;
	private TextView name;
	private TextView college;
	private TextView major;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_message);
		
		init();
		initMessage();
		
		back.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	private void init()
	{
		back = (ImageButton) findViewById(R.id.personal_message_back);
		name = (TextView) findViewById(R.id.personal_message_name);
		college = (TextView) findViewById(R.id.personal_message_college);
		major = (TextView) findViewById(R.id.personal_message_major);
	}
	
	//设置个人信息
	private void initMessage()
	{
		SharedPreferences sp = getSharedPreferences("datas", Activity.MODE_PRIVATE);
		System.out.println("Datas-name"+sp.getString("name", ""));
		name.setText(sp.getString("name", ""));
		college.setText(sp.getString("college", ""));
		major.setText(sp.getString("major", ""));
	}

}
