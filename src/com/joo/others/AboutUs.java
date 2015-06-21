package com.joo.others;

import com.example.scujoo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AboutUs extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_us);
		
		findViewById(R.id.about_us_back).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}
}
