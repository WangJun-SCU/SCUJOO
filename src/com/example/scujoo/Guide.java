package com.example.scujoo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.joo.others.Guide01;
import com.joo.others.Guide02;
import com.joo.others.Guide03;

public class Guide extends FragmentActivity {
	
	private ViewPager vp;
	private FragmentPagerAdapter adapter;
	private List<Fragment> mDatas;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.guide);
		
		init();
		
	}
	
	private void init()
	{
		vp = (ViewPager) findViewById(R.id.id_guide);
		mDatas = new ArrayList<Fragment>();
		Guide01 guide01 = new Guide01();
		Guide02 guide02 = new Guide02();
		Guide03 guide03 = new Guide03();
		
		mDatas.add(guide01);
		mDatas.add(guide02);
		mDatas.add(guide03);
		
		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return mDatas.size();
			}
			
			@Override
			public Fragment getItem(int arg0) {
				return mDatas.get(arg0);
			}
		};
		vp.setAdapter(adapter);
	}

}
