package com.example.scujoo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.scujoo.utils.CircleImageView;

public class MainActivity extends FragmentActivity implements OnClickListener {

	private android.support.v4.widget.DrawerLayout drawerLayout;
	private LinearLayout mainLeft;
	private LinearLayout bottomHome;
	private LinearLayout bottomRecruit;
	private LinearLayout bottomDemand;
	private LinearLayout bottomInternship;
	private LinearLayout personalMessage;
	private LinearLayout more;
	private LinearLayout collection;

	private Fragment fragmentHome;
	private Fragment fragmentRecruit;
	private Fragment fragmentDemand;
	private Fragment fragmentIntership;

	private ImageButton topDrawer;
	private ImageButton topSearch;

	private TextView nouse1;
	private TextView nouse2;
	private TextView nouse3;
	private RelativeLayout nouse4;

	private TextView topTitle;
	private TextView name;
	private static String topTitleStr = "nima";

	private CircleImageView head;
	private Bitmap headBitmap;
	private static String path = "/sdcard/myHead/";

	private SwipeRefreshLayout swipeRefreshLayout;

	/*private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			removeFragment(ft);
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomRecruit);
			topTitle.setText("校园宣讲");
			topTitleStr = (String) topTitle.getText();
			fragmentRecruit = new FragmentRecruit();
			ft.add(R.id.id_content, fragmentRecruit);
			ft.commit();
		}
	};
*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();// 初始化组件
		initEvent();

		topSearch.setVisibility(View.INVISIBLE);

		String content = "";
		content = getIntent().getStringExtra("content");
		System.out.println("MainActivity.content:" + content);

		String select = getIntent().getStringExtra("select");

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft1 = fm.beginTransaction();
		if (content == null) {
			topTitle.setText("热门招聘");
			if (fragmentHome == null) {
				fragmentHome = new FragmentHome();
				ft1.add(R.id.id_content, fragmentHome);
			} else {
				ft1.show(fragmentHome);
			}
			ft1.commit();
		} else if ("recruit".equals(content)) {
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomRecruit);
			topTitle.setText("校园宣讲");
			topTitleStr = (String) topTitle.getText();
			if (fragmentRecruit == null) {
				fragmentRecruit = new FragmentRecruit();
				Bundle bundle = new Bundle();
				bundle.putString("content", content);
				bundle.putString("select", select);
				fragmentRecruit.setArguments(bundle);
				System.out.println("MainActivity.content=" + content);
				ft1.add(R.id.id_content, fragmentRecruit);
			} else {
				ft1.show(fragmentRecruit);
			}
			ft1.commit();
		} else if ("demand".equals(content)) {
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomDemand);
			topTitle.setText("就业需求");
			topTitleStr = (String) topTitle.getText();
			if (fragmentDemand == null) {
				fragmentDemand = new FragmentDemand();
				Bundle bundle = new Bundle();
				bundle.putString("content", content);
				bundle.putString("select", select);
				fragmentDemand.setArguments(bundle);
				System.out.println("MainActivity.content=" + content);
				ft1.add(R.id.id_content, fragmentDemand);
			} else {
				ft1.show(fragmentDemand);
			}
			ft1.commit();
		} else if ("internship".equals(content)) {
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomInternship);
			topTitle.setText("实习讯息");
			topTitleStr = (String) topTitle.getText();
			if (fragmentIntership == null) {
				fragmentIntership = new FragmentInternship();
				Bundle bundle = new Bundle();
				bundle.putString("content", content);
				bundle.putString("select", select);
				fragmentIntership.setArguments(bundle);
				System.out.println("MainActivity.content=" + content);
				ft1.add(R.id.id_content, fragmentIntership);
			} else {
				ft1.show(fragmentIntership);
			}
			ft1.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	@Override
	protected void onPause() {
		super.onPause();
		drawerLayout.closeDrawer(mainLeft);
	}

	private void init() {
		SharedPreferences sp = getSharedPreferences("datas",
				Activity.MODE_PRIVATE);

		//swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.fragment_recruit_refresh);

		nouse1 = (TextView) findViewById(R.id.drawer_left_1);
		nouse2 = (TextView) findViewById(R.id.drawer_left_2);
		nouse3 = (TextView) findViewById(R.id.drawer_left_3);
		nouse4 = (RelativeLayout) findViewById(R.id.drawer_left_4);

		bottomHome = (LinearLayout) findViewById(R.id.bottom_home);
		bottomRecruit = (LinearLayout) findViewById(R.id.bottom_recruit);
		bottomDemand = (LinearLayout) findViewById(R.id.bottom_demand);
		bottomInternship = (LinearLayout) findViewById(R.id.bottom_internship);
		personalMessage = (LinearLayout) findViewById(R.id.drawer_left_personal);
		more = (LinearLayout) findViewById(R.id.drawer_left_more);
		collection = (LinearLayout) findViewById(R.id.drawer_left_collection);
		topSearch = (ImageButton) findViewById(R.id.top_search);

		drawerLayout = (DrawerLayout) findViewById(R.id.main_drawer);
		mainLeft = (LinearLayout) findViewById(R.id.main_left);

		topDrawer = (ImageButton) findViewById(R.id.top_drawer);

		topTitle = (TextView) findViewById(R.id.top_title);
		name = (TextView) findViewById(R.id.drawer_left_name);

		name.setText(sp.getString("name", ""));

		head = (CircleImageView) findViewById(R.id.drawer_left_head);

		Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从Sd中找头像，转换成Bitmap
		if (bt != null) {
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
			head.setImageDrawable(drawable);
		} else {
			/**
			 * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
			 * 
			 */
		}
	}

	private void initEvent() {
		bottomHome.setOnClickListener(this);
		bottomRecruit.setOnClickListener(this);
		bottomDemand.setOnClickListener(this);
		bottomInternship.setOnClickListener(this);
		topDrawer.setOnClickListener(this);
		personalMessage.setOnClickListener(this);
		more.setOnClickListener(this);
		collection.setOnClickListener(this);
		topSearch.setOnClickListener(this);
		nouse1.setOnClickListener(this);
		nouse2.setOnClickListener(this);
		nouse3.setOnClickListener(this);
		nouse4.setOnClickListener(this);
		name.setOnClickListener(this);
		//swipeRefreshLayout.setOnRefreshListener((OnRefreshListener) this);
	}

	public void onClick(View v) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		removeFragment(ft);
		// hideFragment(ft);
		switch (v.getId()) {
		case R.id.bottom_home:
			topSearch.setVisibility(View.INVISIBLE);
			hideBlue();
			showBlue(bottomHome);
			topTitle.setText("热门招聘");
			fragmentHome = new FragmentHome();
			ft.add(R.id.id_content, fragmentHome);
			ft.commit();
			break;
		case R.id.bottom_recruit:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomRecruit);
			topTitle.setText("校园宣讲");
			topTitleStr = (String) topTitle.getText();
			fragmentRecruit = new FragmentRecruit();
			ft.add(R.id.id_content, fragmentRecruit);
			ft.commit();
			break;
		case R.id.bottom_demand:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomDemand);
			topTitle.setText("就业需求");
			topTitleStr = (String) topTitle.getText();
			fragmentDemand = new FragmentDemand();
			ft.add(R.id.id_content, fragmentDemand);
			ft.commit();
			break;
		case R.id.bottom_internship:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomInternship);
			topTitle.setText("实习讯息");
			topTitleStr = (String) topTitle.getText();
			System.out.println("222222222");
			fragmentIntership = new FragmentInternship();
			ft.add(R.id.id_content, fragmentIntership);
			ft.commit();
			break;
		case R.id.top_drawer:
			drawerLayout.openDrawer(mainLeft);
			break;
		case R.id.drawer_left_personal:
			startActivity(new Intent().setClass(MainActivity.this,
					PersonalMessage.class));
			break;
		case R.id.drawer_left_more:
			startActivity(new Intent().setClass(MainActivity.this, More.class));
			break;
		case R.id.drawer_left_collection:
			startActivity(new Intent().setClass(MainActivity.this,
					Collection.class));
			break;
		case R.id.top_search:
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, Search.class);
			intent.putExtra("content", topTitleStr);
			startActivity(intent);
			finish();
			break;
		case R.id.drawer_left_1:
			break;
		case R.id.drawer_left_2:
			break;
		case R.id.drawer_left_3:
			break;
		case R.id.drawer_left_4:
			break;
		case R.id.drawer_left_name:
			break;
		default:
			break;
		}
	}

	// 将fragment隐藏起来
	private void hideFragment(FragmentTransaction ft) {
		if (fragmentHome != null) {
			ft.hide(fragmentHome);
			// fragmentHome = null;
		}
		if (fragmentRecruit != null) {
			ft.hide(fragmentRecruit);
			// fragmentRecruit = null;
		}
		if (fragmentDemand != null) {
			ft.hide(fragmentDemand);
			// fragmentDemand = null;
		}
		if (fragmentIntership != null) {
			ft.hide(fragmentIntership);
			// fragmentIntership = null;
		}
	}

	// 将fragment移除
	private void removeFragment(FragmentTransaction ft) {
		if (fragmentHome != null) {
			ft.remove(fragmentHome);
			System.out.println("111111");
			// ft.commit();
		}
		if (fragmentRecruit != null) {
			ft.remove(fragmentRecruit);
			System.out.println("22222");
			// ft.commit();
		}
		if (fragmentDemand != null) {
			ft.remove(fragmentDemand);
			System.out.println("333333");
			// ft.commit();
		}
		if (fragmentIntership != null) {
			ft.remove(fragmentIntership);
			System.out.println("444444");
			// ft.commit();
		}
	}

	// 点击按钮呈现颜色
	void showBlue(LinearLayout ll) {
		ll.setBackgroundColor(getResources().getColor(R.color.blue));
	}

	// 重置按钮颜色
	private void hideBlue() {
		bottomHome.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomRecruit.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomDemand.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomInternship.setBackgroundColor(getResources().getColor(
				R.color.gray));
	}

	// 点击两次返回按钮实现退出程序
	private static boolean isExit = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

}
