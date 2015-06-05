package com.example.scujoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity implements OnClickListener{
	
	private LinearLayout bottomHome;
	private LinearLayout bottomRecruit;
	private LinearLayout bottomDemand;
	private LinearLayout bottomInternship;
	
	private Fragment fragmentHome;
	private Fragment fragmentRecruit;
	private Fragment fragmentDemand;
	private Fragment fragmentIntership;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();//初始化组件
		initEvent();
	}
	
	
	
	private void init()
	{
		bottomHome = (LinearLayout) findViewById(R.id.bottom_home);
		bottomRecruit = (LinearLayout) findViewById(R.id.bottom_recruit);
		bottomDemand = (LinearLayout) findViewById(R.id.bottom_demand);
		bottomInternship = (LinearLayout) findViewById(R.id.bottom_internship);
	}
	
	private void initEvent()
	{
		bottomHome.setOnClickListener(this);
		bottomRecruit.setOnClickListener(this);
		bottomDemand.setOnClickListener(this);
		bottomInternship.setOnClickListener(this);
	}
	

	public void onClick(View v) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		hideFragment(ft);
		hideBlue();
		switch(v.getId())
		{
		case R.id.bottom_home:
			showBlue(bottomHome);
			if(fragmentHome==null)
			{
				fragmentHome = new FragmentHome();
				ft.add(R.id.id_content, fragmentHome);
			}else{
				ft.show(fragmentHome);
			}
			break;
		case R.id.bottom_recruit:
			showBlue(bottomRecruit);
			if(fragmentRecruit==null)
			{
				fragmentRecruit = new FragmentRecruit();
				ft.add(R.id.id_content, fragmentRecruit);
			}else{
				ft.show(fragmentRecruit);
			} 
			break;
		case R.id.bottom_demand:
			showBlue(bottomDemand);
			if(fragmentDemand==null)
			{
				fragmentDemand = new FragmentDemand();
				ft.add(R.id.id_content, fragmentDemand);
			}else{
				ft.show(fragmentDemand);
			}
			break;
		case R.id.bottom_internship:
			showBlue(bottomInternship);
			if(fragmentIntership==null)
			{
				fragmentIntership = new FragmentInternship();
				ft.add(R.id.id_content, fragmentIntership);
			}else{
				ft.show(fragmentIntership);
			}
			break;
		default:
			break;
		}
		ft.commit();
	}
	
	//将fragment隐藏起来
		private void hideFragment(FragmentTransaction ft)
		{
			if(fragmentHome!=null)
			{
				ft.hide(fragmentHome);
			}
			if(fragmentRecruit!=null)
			{
				ft.hide(fragmentRecruit);
			}
			if(fragmentDemand!=null)
			{
				ft.hide(fragmentDemand);
			}
			if(fragmentIntership!=null)
			{
				ft.hide(fragmentIntership);
			}
		}
		
		//点击按钮呈现颜色
		private void showBlue(LinearLayout ll)
		{
			ll.setBackgroundColor(getResources().getColor(R.color.blue));
		}
		//重置按钮颜色
		private void hideBlue()
		{
			bottomHome.setBackgroundColor(getResources().getColor(R.color.gray));
			bottomRecruit.setBackgroundColor(getResources().getColor(R.color.gray));
			bottomDemand.setBackgroundColor(getResources().getColor(R.color.gray));
			bottomInternship.setBackgroundColor(getResources().getColor(R.color.gray));
		}
}
