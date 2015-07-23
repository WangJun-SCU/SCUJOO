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

import com.scujoo.adapter.AdapterDemand;
import com.scujoo.datas.DatasDemand;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.HttpUtils;
import com.scujoo.utils.Md5;

public class FragmentDemand extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {

	private ListView listViewDemand;
	private List<DatasDemand> listDemand;
	private AdapterDemand adapterDemand;
	private ProgressDialog dialog;

	private TextView topTitle;

	private SwipeRefreshLayout swipeRefreshLayout;

	private String URL = StaticDatas.URL + "scujoo/demand.php";

	private Handler handlerDemand = new Handler() {
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
					String publishTime = obj.getString("publishTime");
					String position = obj.getString("position");
					listDemand.add(new DatasDemand(id, name, publishTime,
							position));
				}
				adapterDemand.notifyDataSetChanged();
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
		View rootView = inflater.inflate(R.layout.fragment_demand, container,
				false);
		// 显示正在加载
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("正在加载...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.show();

		// 刷新空间的声明
		swipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.fragment_demand_refresh);
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

		try {
			url = getArguments().getString("url12", "");
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
		//核心代码
		listViewDemand = (ListView) rootView.findViewById(R.id.fragment_demand_listView);
		listDemand = new ArrayList<DatasDemand>();
		adapterDemand = new AdapterDemand(rootView.getContext(), listDemand);
		listViewDemand.setAdapter(adapterDemand);
		
		//封装传送到服务器的数据
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("userPass", userPass));
		params.add(new BasicNameValuePair("md5", md5));

		//判断是否是从搜索界面回调
		if ("default".equals(content)) {

		} else if ("demand".equals(content)) {
			params.add(new BasicNameValuePair("content", content));
			params.add(new BasicNameValuePair("select", select));
			URL = "http://120.25.245.241/scujoo/select.php";
		}

		// 判断是否有网络连接
		Context context = getActivity().getApplicationContext();
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			//如果有网络连接，执行异步传输
			HttpUtils.getJson(URL, handlerDemand, params);
		} else {
			Toast.makeText(getActivity(), "无网络连接", 1).show();
			getActivity().finish();
		}

		//设置listView监听事件
		listViewDemand.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DatasDemand datasDemand = listDemand.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ContentDemand.class);
				intent.putExtra("id", datasDemand.getId());
				startActivity(intent);
			}
		});
		if (url != "") {

		} else {
			//点击标题title返回顶部
			topTitle = (TextView) getActivity().findViewById(R.id.top_title);
			topTitle.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					listViewDemand.setSelectionAfterHeaderView();
				}
			});
		}
		return rootView;
	}

	public void onRefresh() {
		myHandler.sendEmptyMessageDelayed(0x1234, 2000);
	}

}
