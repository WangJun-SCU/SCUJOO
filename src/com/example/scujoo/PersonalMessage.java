package com.example.scujoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class PersonalMessage extends Activity {
	
	private ImageButton back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_message);
		
		System.out.print("3333333");
		
		init();
		System.out.print("4444");
		
		back.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
		System.out.print("55555");
	}
	
	private void init()
	{
		back = (ImageButton) findViewById(R.id.personal_message_back);
	}

}
