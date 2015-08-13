package com.example.scujoo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.scujoo.adapter.AdapterRecruit;
import com.scujoo.datas.DatasRecruit;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.HttpUtils;
import com.scujoo.utils.Md5;

public class FragmentRecruit extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {

	private ListView listViewRecruit;
	private List<DatasRecruit> listRecruit;
	private AdapterRecruit adapterRecruit;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ProgressDialog dialog;
	private TextView topTitle;

	private String URL = StaticDatas.URL + "scujoo/recruit.php";

	private Handler handlerRecruit = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String jsonData = (String) msg.obj;
			dialog.dismiss();
			try {
				JSONObject jsonObject = new JSONObject(jsonData);
				String object = jsonObject.getString("result");
				JSONArray jsonArray = new JSONArray(object);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj = jsonArray.getJSONObject(i);
					String id = obj.getString("id");
					String name = obj.getString("name");
					String recruitPlace = obj.getString("recruitPlace");
					String recruitTime = obj.getString("recruitTime");
					listRecruit.add(new DatasRecruit(id, name, recruitTime,
							recruitPlace));
				}
				adapterRecruit.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0x1234) {
				swipeRefreshLayout.setRefreshing(false);
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_recruit, container,
				false);
		// 显示正在加载
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("正在加载...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.show();

		// 刷新空间的声明
		swipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.fragment_recruit_refresh);
		// 刷新监听器
		swipeRefreshLayout.setOnRefreshListener(this);
		// 设置刷新效果的颜色
		swipeRefreshLayout
				.setColorScheme(android.R.color.holo_blue_dark,
						android.R.color.holo_green_dark,
						android.R.color.holo_orange_dark,
						android.R.color.holo_red_dark);

		String url = "default";
		String content = "default";
		String select = "default";

		// 判断是否是从其他界面过来的
		try {
			url = getArguments().getString("url11", "");
			content = getArguments().getString("content", "");
			select = getArguments().getString("select", "");
			if (url != "") {
				URL = url;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SharedPreferences sp = getActivity().getSharedPreferences("datas",
				Activity.MODE_PRIVATE);
		String userName = sp.getString("userName", "");
		String userPass = sp.getString("userPass", "");
		String token;
		token = "userName=" + userName + "&userPass=" + userPass + "token";
		String md5 = Md5.Md5Str(token);

		listViewRecruit = (ListView) rootView
				.findViewById(R.id.fragment_recruit_listView);
		listRecruit = new ArrayList<DatasRecruit>();
		adapterRecruit = new AdapterRecruit(rootView.getContext(), listRecruit);

		listViewRecruit.setAdapter(adapterRecruit);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("userPass", userPass));
		params.add(new BasicNameValuePair("md5", md5));
		System.out.println("FragmentRecruit.content=" + content);
		if ("default".equals(content)) {

		} else if ("recruit".equals(content)) {
			params.add(new BasicNameValuePair("content", content));
			params.add(new BasicNameValuePair("select", select));
			URL = "http://120.25.245.241/scujoo/select.php";
		}
		System.out.println("传入的数据：" + userName + "--" + userPass + "--" + md5);

		// 判断是否有网络连接
		Context context = getActivity().getApplicationContext();
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			HttpUtils.getJson(URL, handlerRecruit, params);
		} else {
			Toast.makeText(getActivity(), "无网络连接", 1).show();
			getActivity().finish();
		}

		listViewRecruit.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DatasRecruit datasRecruit = listRecruit.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ContentRecruit.class);
				System.out.println("datasRecruit.getId()="
						+ datasRecruit.getId());
				intent.putExtra("id", datasRecruit.getId());
				startActivity(intent);
			}
		});
		if (url != "") {

		} else {
			topTitle = (TextView) getActivity().findViewById(R.id.top_title);
			topTitle.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					listViewRecruit.setSelectionAfterHeaderView();
				}
			});
		}
		return rootView;
	}

	public void onRefresh() {
		myHandler.sendEmptyMessageDelayed(0x1234, 2000);
	}
}
