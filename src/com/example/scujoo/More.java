package com.example.scujoo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class More extends Activity {

	private ImageButton back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		init();

		back.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

	}

	private void init() {
		back = (ImageButton) findViewById(R.id.more_back);
	}

}
