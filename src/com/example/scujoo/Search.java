package com.example.scujoo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

public class Search extends Activity {

	private ImageButton back;
	private SearchView search;
	private String content;
	private TextView contentt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		// �������뷨Ĭ�ϴ�
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		String contentStr = getIntent().getStringExtra("content");
		contentt = (TextView) findViewById(R.id.search_content_str);
		contentt.setText(contentStr);
		
		if("У԰����".equals(contentStr))
		{
			content = "recruit";
		}else if("��ҵ����".equals(contentStr))
		{
			content = "demand";
		}else if("ʵϰѶϢ".equals(contentStr))
		{
			content = "internship";
		}

		search = (SearchView) findViewById(R.id.search_id);
		
		//�Ƿ���ʾȷ��������ť
		//search.setSubmitButtonEnabled(true);
		
		int id = search.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
		TextView textView = (TextView) search.findViewById(id);
		textView.setTextColor(Color.WHITE);
		
		
		search.setOnQueryTextListener(new OnQueryTextListener() {
			public boolean onQueryTextSubmit(String query) {
				Intent intent = new Intent();
				intent.setClass(Search.this, MainActivity.class);
				String select = search.getQuery().toString();
				System.out.println("����Ĳ�ѯΪ��" + select);
				intent.putExtra("content", content);
				intent.putExtra("select", select);
				System.out.println("content="+content);
				System.out.println("select="+select);
				startActivity(intent);
				finish();
				return false;
			}
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
 
		back = (ImageButton) findViewById(R.id.search_back);
		back.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				startActivity(new Intent().setClass(Search.this, MainActivity.class));
				finish();
			}
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			startActivity(new Intent().setClass(Search.this, MainActivity.class));
			finish();
		}
		return false;
	}
}
