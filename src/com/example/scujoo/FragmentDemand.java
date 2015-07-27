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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
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
	private LinearLayout topCalendar;
	private Fragment fragmentDemand;

	private TextView topTitle;

	private SwipeRefreshLayout swipeRefreshLayout;

	private String URL = StaticDatas.URL + "scujoo/demand.php";
	FragmentManager fm;
	FragmentTransaction ft;

	private Handler handlerDemand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String jsonData = (String) msg.obj;
			dialog.dismiss();
			swipeRefreshLayout.setRefreshing(false);
			topCalendar.setVisibility(View.VISIBLE);
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_demand, container,
				false);
		fm = getActivity().getSupportFragmentManager();
		ft = fm.beginTransaction();
		topCalendar = (LinearLayout) getActivity().findViewById(R.id.id_top_calendar);

		System.out.println("callBack:-------");
		String callBack = "";
		try {
			callBack = getArguments().getString("callBack", "");
			System.out.println("callBack:" + callBack);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// ˢ�¿ռ������
		swipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.fragment_demand_refresh);
		// ˢ�¼�����
		swipeRefreshLayout.setOnRefreshListener(this);
		// ����ˢ��Ч������ɫ
		swipeRefreshLayout
				.setColorScheme(android.R.color.holo_blue_dark,
						android.R.color.holo_green_dark,
						android.R.color.holo_orange_dark,
						android.R.color.holo_red_dark);
		if (callBack.equals("callBack")) {
			dialog = new ProgressDialog(getActivity());
			swipeRefreshLayout.setRefreshing(true);
		} else {
			// ��ʾ���ڼ���
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("���ڼ���...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);
			dialog.show();
		}

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
		// ���Ĵ���
		listViewDemand = (ListView) rootView
				.findViewById(R.id.fragment_demand_listView);
		listDemand = new ArrayList<DatasDemand>();
		adapterDemand = new AdapterDemand(rootView.getContext(), listDemand);
		listViewDemand.setAdapter(adapterDemand);

		// ��װ���͵�������������
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("userPass", userPass));
		params.add(new BasicNameValuePair("md5", md5));

		// �ж��Ƿ��Ǵ���������ص�
		if ("default".equals(content)) {

		} else if ("demand".equals(content)) {
			params.add(new BasicNameValuePair("content", content));
			params.add(new BasicNameValuePair("select", select));
			URL = "http://120.25.245.241/scujoo/select.php";
		}

		// �ж��Ƿ�����������
		Context context = getActivity().getApplicationContext();
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			// ������������ӣ�ִ���첽����
			HttpUtils.getJson(URL, handlerDemand, params);
		} else {
			Toast.makeText(getActivity(), "����������", 1).show();
			getActivity().finish();
		}

		// ����listView�����¼�
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
			// �������title���ض���
			topTitle = (TextView) getActivity().findViewById(R.id.top_title);
			topTitle.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					listViewDemand.setSelectionAfterHeaderView();
				}
			});
		}
		return rootView;
	}

	public void onRefresh() {
		fragmentDemand = new FragmentDemand();
		Bundle bundle = new Bundle();
		bundle.putString("callBack", "callBack");
		fragmentDemand.setArguments(bundle);
		ft.replace(R.id.id_content, fragmentDemand);
		ft.commit();
	}
}
