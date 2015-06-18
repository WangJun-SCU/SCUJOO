package com.example.scujoo;

import com.scujoo.utils.CircleImageView;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

	private TextView topTitle;
	private TextView name;
	private static String topTitleStr = "nima";

	private CircleImageView head;
	private Bitmap headBitmap;
	private static String path = "/sdcard/myHead/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		init();// 初始化组件
		initEvent();
		
		topSearch.setVisibility(View.INVISIBLE);

		String content = "";
		content = getIntent().getStringExtra("content");
		String select = getIntent().getStringExtra("select");

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if("".equals(content))
		{
			topTitle.setText("首页");
			if (fragmentHome == null) {
				fragmentHome = new FragmentHome();
				ft.add(R.id.id_content, fragmentHome);
			} else {
				ft.show(fragmentHome);
			}
			ft.commit();
		}else if("recruit".equals(content))
		{
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
				System.out.println("MainActivity.content="+content);
				ft.add(R.id.id_content, fragmentRecruit);
			} else {
				ft.show(fragmentRecruit);
			}
			ft.commit();
		}else if("demand".equals(content))
		{
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
				System.out.println("MainActivity.content="+content);
				ft.add(R.id.id_content, fragmentDemand);
			} else {
				ft.show(fragmentDemand);
			}
			ft.commit();
		}else if("internship".equals(content))
		{
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
				System.out.println("MainActivity.content="+content);
				ft.add(R.id.id_content, fragmentIntership);
			} else {
				ft.show(fragmentIntership);
			}
			ft.commit();
		}
	}

	private void init() {
		SharedPreferences sp = getSharedPreferences("datas",
				Activity.MODE_PRIVATE);

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
	}

	public void onClick(View v) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		hideFragment(ft);
		switch (v.getId()) {
		case R.id.bottom_home:
			topSearch.setVisibility(View.INVISIBLE);
			hideBlue();
			showBlue(bottomHome);
			topTitle.setText("首页");
			if (fragmentHome == null) {
				fragmentHome = new FragmentHome();
				ft.add(R.id.id_content, fragmentHome);
			} else {
				ft.show(fragmentHome);
			}
			ft.commit();
			break;
		case R.id.bottom_recruit:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomRecruit);
			topTitle.setText("校园宣讲");
			topTitleStr = (String) topTitle.getText();
			if (fragmentRecruit == null) {
				fragmentRecruit = new FragmentRecruit();
				ft.add(R.id.id_content, fragmentRecruit);
			} else {
				ft.show(fragmentRecruit);
			}
			ft.commit();
			break;
		case R.id.bottom_demand:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomDemand);
			topTitle.setText("就业需求");
			topTitleStr = (String) topTitle.getText();
			if (fragmentDemand == null) {
				fragmentDemand = new FragmentDemand();
				ft.add(R.id.id_content, fragmentDemand);
			} else {
				ft.show(fragmentDemand);
			}
			ft.commit();
			break;
		case R.id.bottom_internship:
			topSearch.setVisibility(View.VISIBLE);
			hideBlue();
			showBlue(bottomInternship);
			topTitle.setText("实习讯息");
			topTitleStr = (String) topTitle.getText();
			System.out.println("222222222");
			if (fragmentIntership == null) {
				fragmentIntership = new FragmentInternship();
				ft.add(R.id.id_content, fragmentIntership);
			} else {
				ft.show(fragmentIntership);
			}
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
			intent.setClass(MainActivity.this,Search.class);
			intent.putExtra("content", topTitleStr);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}

	// 将fragment隐藏起来
	private void hideFragment(FragmentTransaction ft) {
		if (fragmentHome != null) {
			ft.hide(fragmentHome);
		}
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
		bottomHome.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomRecruit.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomDemand.setBackgroundColor(getResources().getColor(R.color.gray));
		bottomInternship.setBackgroundColor(getResources().getColor(
				R.color.gray));
	}
	
	//点击两次返回按钮实现退出程序
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
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
}
