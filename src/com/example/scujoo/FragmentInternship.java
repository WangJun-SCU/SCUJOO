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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.scujoo.adapter.AdapterInternship;
import com.scujoo.datas.DatasDemand;
import com.scujoo.datas.DatasInternship;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.HttpUtils;
import com.scujoo.utils.Md5;

public class FragmentInternship extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {

	private ListView listViewInternship;
	private List<DatasInternship> listInternship;
	private AdapterInternship adapterInternship;
	private ProgressDialog dialog;
	private LinearLayout topCalendar;
	private FragmentInternship fragmentInternship;
	private TextView topTitle;
	private SwipeRefreshLayout swipeRefreshLayout;
	private String URL = StaticDatas.URL + "scujoo/internship.php";
	FragmentManager fm;
	FragmentTransaction ft;

	private Handler handlerInternship = new Handler() {
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
					listInternship.add(new DatasInternship(id, name,
							publishTime, position));
				}
				adapterInternship.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_internship,
				container, false);
		fm = getActivity().getSupportFragmentManager();
		ft = fm.beginTransaction();
		topCalendar = (LinearLayout) getActivity().findViewById(R.id.id_top_calendar);
		
		String callBack = "";
		try {
			callBack = getArguments().getString("callBack", "");
			System.out.println("callBack:" + callBack);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		// ˢ�¿ռ������
		swipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.fragment_internship_refresh);
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
			url = getArguments().getString("url13", "");
			content = getArguments().getString("content", "");
			select = getArguments().getString("select", "");
			if (url != "") {
				URL = url;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("url=" + url);

		SharedPreferences sp = getActivity().getSharedPreferences("datas",
				Activity.MODE_PRIVATE);
		String userName = sp.getString("userName", "");
		String userPass = sp.getString("userPass", "");
		String token;
		token = "userName=" + userName + "&userPass=" + userPass + "token";
		String md5 = Md5.Md5Str(token);

		listViewInternship = (ListView) rootView
				.findViewById(R.id.fragment_internship_listView);
		listInternship = new ArrayList<DatasInternship>();
		adapterInternship = new AdapterInternship(rootView.getContext(),
				listInternship);

		listViewInternship.setAdapter(adapterInternship);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("userPass", userPass));
		params.add(new BasicNameValuePair("md5", md5));

		if ("default".equals(content)) {

		} else if ("internship".equals(content)) {
			params.add(new BasicNameValuePair("content", content));
			params.add(new BasicNameValuePair("select", select));
			URL = "http://120.25.245.241/scujoo/select.php";
		}

		System.out.println("��������ݣ�" + userName + "--" + userPass + "--" + md5);

		// �ж��Ƿ�����������
		Context context = getActivity().getApplicationContext();
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			HttpUtils.getJson(URL, handlerInternship, params);
		} else {
			Toast.makeText(getActivity(), "����������", 1).show();
			getActivity().finish();
		}

		listViewInternship.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DatasInternship datasInternship = listInternship.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ContentInternship.class);
				intent.putExtra("id", datasInternship.getId());
				startActivity(intent);
			}
		});
		if (url != "") {

		} else {
			topTitle = (TextView) getActivity().findViewById(R.id.top_title);
			topTitle.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					listViewInternship.setSelectionAfterHeaderView();
				}
			});
		}
		return rootView;
	}

	//ˢ�½���
	public void onRefresh() {
		fragmentInternship = new FragmentInternship();
		Bundle bundle = new Bundle();
		bundle.putString("callBack", "callBack");
		fragmentInternship.setArguments(bundle);
		ft.replace(R.id.id_content, fragmentInternship);
		ft.commit();
	}

}
