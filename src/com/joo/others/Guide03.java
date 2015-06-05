package com.joo.others;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.scujoo.MainActivity;
import com.example.scujoo.R;

public class Guide03 extends Fragment {

	private Button enter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.guide03, container,false);
		enter = (Button) rootView.findViewById(R.id.guide03_enter);
		enter.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
		
		return rootView;
	}
}
