package com.example.scujoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Collection extends FragmentActivity implements OnClickListener {

	private ImageButton back;
	LinearLayout bottomRecruit;
	LinearLayout bottomDemand;
	LinearLayout bottomInternship;
	TextView topTitle;

	Fragment fragmentRecruit;
	Fragment fragmentDemand;
	Fragment fragmentIntership;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.collection);
		init();
		initEvent();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		hideFragment(ft);

		hideBlue();
		showBlue(bottomRecruit);
		topTitle.setText("校园宣讲");
		if (fragmentRecruit == null) {
			fragmentRecruit = new FragmentRecruitCollection();

			Bundle bundle = new Bundle();
			bundle.putString("url11",
					"http://120.25.245.241/scujoo/recruit_collect.php");
			fragmentRecruit.setArguments(bundle);

			ft.add(R.id.collection_content, fragmentRecruit);
		} else {
			ft.show(fragmentRecruit);
		}
		ft.commit();
	}

	private void init() {

		back = (ImageButton) findViewById(R.id.collection_back);
		bottomRecruit = (LinearLayout) findViewById(R.id.bottom_collect_recruit);
		bottomDemand = (LinearLayout) findViewById(R.id.bottom_collect_demand);
		bottomInternship = (LinearLayout) findViewById(R.id.bottom_collect_internship);
		topTitle = (TextView) findViewById(R.id.collection_title);
	}

	private void initEvent() {
		bottomRecruit.setOnClickListener(this);
		bottomDemand.setOnClickListener(this);
		bottomInternship.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	public void onClick(View v) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		hideFragment(ft);
		switch (v.getId()) {
		case R.id.collection_back:
			finish();
			break;
		case R.id.bottom_collect_recruit:
			hideBlue();
			showBlue(bottomRecruit);
			topTitle.setText("校园宣讲");
			if (fragmentRecruit == null) {
				fragmentRecruit = new FragmentRecruitCollection();

				Bundle bundle = new Bundle();
				bundle.putString("url11",
						"http://120.25.245.241/scujoo/recruit_collect.php");
				fragmentRecruit.setArguments(bundle);

				ft.add(R.id.collection_content, fragmentRecruit);
			} else {
				ft.show(fragmentRecruit);
			}
			ft.commit();
			break;
		case R.id.bottom_collect_demand:

			hideBlue();
			showBlue(bottomDemand);
			topTitle.setText("需求");
			if (fragmentDemand == null) {
				fragmentDemand = new FragmentDemandCollection();

				Bundle bundle1 = new Bundle();
				bundle1.putString("url12",
						"http://120.25.245.241/scujoo/demand_collect.php");
				fragmentDemand.setArguments(bundle1);

				ft.add(R.id.collection_content, fragmentDemand);
			} else {
				ft.show(fragmentDemand);
			}
			ft.commit();

			break;
		case R.id.bottom_collect_internship:
			hideBlue();
			showBlue(bottomInternship);
			topTitle.setText("需求");
			if (fragmentIntership == null) {
				fragmentIntership = new FragmentInternshipCollection();

				Bundle bundle2 = new Bundle();
				bundle2.putString("url13",
						"http://120.25.245.241/scujoo/internship_collect.php");
				fragmentIntership.setArguments(bundle2);

				ft.add(R.id.collection_content, fragmentIntership);
			} else {
				ft.show(fragmentIntership);
			}
			ft.commit();
			break;
		default:
			break;
		}
	}

	// 将fragment隐藏起来
		private void hideFragment(FragmentTransaction ft) {
			if (fragmentRecruit != null) {
				ft.hide(fragmentRecruit);
			}
			if (fragmentDemand != null) {
				ft.hide(fragmentDemand);
			}
			if (fragmentIntership != null) {
				ft.hide(fragmentIntership);
			}
		}

	// 点击按钮呈现颜色
	void showBlue(LinearLayout ll) {
		ll.setBackgroundColor(getResources().getColor(R.color.blue));
	}

	// 重置按钮颜色
	private void hideBlue() {
		bottomRecruit.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomDemand.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomInternship.setBackgroundColor(getResources().getColor(
				R.color.gray));
	}
}
