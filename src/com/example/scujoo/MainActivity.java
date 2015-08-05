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
	private LinearLayout topCalendar;
	private Fragment fragmentHome;
	private Fragment fragmentRecruit;
	private Fragment fragmentDemand;
	private Fragment fragmentIntership;
	private Fragment fragmentRecruitUp;
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
	FragmentManager fm;
	FragmentTransaction ft1;
	private String userName;
	private SharedPreferences.Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();// ��ʼ�����
		initEvent();
		topCalendar.setVisibility(View.GONE);
		topSearch.setVisibility(View.INVISIBLE);

		String content = "";
		content = getIntent().getStringExtra("content");
		System.out.println("MainActivity.content:" + content);

		String select = getIntent().getStringExtra("select");

		fm = getSupportFragmentManager();
		ft1 = fm.beginTransaction();
		if (content == null) {
			topTitle.setText("������Ƹ");
			if (fragmentHome == null) {
				fragmentHome = new FragmentHome();
				ft1.replace(R.id.id_content, fragmentHome);
			} else {
				ft1.show(fragmentHome);
			}
			ft1.commit();
		} else if ("recruit".equals(content)) {
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomRecruit);
			topTitle.setText("У԰����");
			topTitleStr = (String) topTitle.getText();
			if (fragmentRecruit == null) {
				fragmentRecruit = new FragmentRecruitCollection();
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
			topTitle.setText("��ҵ����");
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
			topTitle.setText("ʵϰѶϢ");
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
		editor = sp.edit(); 

		nouse1 = (TextView) findViewById(R.id.drawer_left_1);
		nouse2 = (TextView) findViewById(R.id.drawer_left_2);
		nouse3 = (TextView) findViewById(R.id.drawer_left_3);
		nouse4 = (RelativeLayout) findViewById(R.id.drawer_left_4);
		topCalendar = (LinearLayout) findViewById(R.id.id_top_calendar);
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
		userName = sp.getString("userName", "");

		head = (CircleImageView) findViewById(R.id.drawer_left_head);

		Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// ��Sd����ͷ��ת����Bitmap
		if (bt != null) {
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bt);// ת����drawable
			head.setImageDrawable(drawable);
		} else {
			/**
			 * ���SD����û������Ҫ�ӷ�����ȡͷ��ȡ������ͷ���ٱ�����SD��
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
	}

	public void onClick(View v) {
		FragmentTransaction ft1 = fm.beginTransaction();
		System.out.println("3333");
		System.out.println("4444");
		switch (v.getId()) {
		case R.id.bottom_home:
			topSearch.setVisibility(View.INVISIBLE);
			topCalendar.setVisibility(View.INVISIBLE);
			hideBlue();
			showBlue(bottomHome);
			topTitle.setText("������Ƹ");
			fragmentHome = new FragmentHome();
			ft1.replace(R.id.id_content, fragmentHome);
			ft1.commit();
			break;
		case R.id.bottom_recruit:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomRecruit);
			topTitle.setText("У԰����");
			topTitleStr = (String) topTitle.getText();
			fragmentRecruitUp = new FragmentRecruitUp();
			System.out.println("5555");
			ft1.replace(R.id.id_content, fragmentRecruitUp);
			System.out.println("666");
			ft1.commit();
			System.out.println("7777");
			break;
		case R.id.bottom_demand:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomDemand);
			topTitle.setText("��ҵ����");
			topTitleStr = (String) topTitle.getText();
			fragmentDemand = new FragmentDemand();
			ft1.replace(R.id.id_content, fragmentDemand);
			ft1.commit();
			break;
		case R.id.bottom_internship:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomInternship);
			topTitle.setText("ʵϰѶϢ");
			topTitleStr = (String) topTitle.getText();
			System.out.println("222222222");
			fragmentIntership = new FragmentInternship();
			ft1.replace(R.id.id_content, fragmentIntership);
			ft1.commit();
			break;
		case R.id.top_drawer:
			drawerLayout.openDrawer(mainLeft);
			break;
		case R.id.drawer_left_personal:
			if (userName.equals("visitor")) {
				editor.remove("userName");
				editor.remove("userPass");
				editor.commit();
				startActivity(new Intent().setClass(MainActivity.this,Login.class));
				finish();
			} else {
				startActivity(new Intent().setClass(MainActivity.this,PersonalMessage.class));
			}
			break;
		case R.id.drawer_left_more:
			startActivity(new Intent().setClass(MainActivity.this, More.class));
			break;
		case R.id.drawer_left_collection:
			if(userName.equals("visitor"))
			{
				editor.remove("userName");
				editor.remove("userPass");
				editor.commit();
				startActivity(new Intent().setClass(MainActivity.this,Login.class));
				finish();
			}else
			{
				startActivity(new Intent().setClass(MainActivity.this,
						Collection.class));
			}
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

	// �����ť������ɫ
	void showBlue(LinearLayout ll) {
		ll.setBackgroundColor(getResources().getColor(R.color.blue));
	}

	// ���ð�ť��ɫ
	private void hideBlue() {
		bottomHome.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomRecruit.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomDemand.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomInternship.setBackgroundColor(getResources().getColor(
				R.color.gray));
	}

	// ������η��ذ�ťʵ���˳�����
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
			Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
					Toast.LENGTH_SHORT).show();
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

}
