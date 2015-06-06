package com.example.scujoo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Collection extends Activity {
	
	private ImageButton back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collection);
		
		init();
		
		back.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void init() {
		
		back = (ImageButton) findViewById(R.id.collection_back);
	}
}
