package com.example.scujoo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.scujoo.adapter.AdapterDemand;
import com.scujoo.adapter.AdapterInternship;
import com.scujoo.adapter.AdapterRecruit;
import com.scujoo.datas.DatasDemand;
import com.scujoo.datas.DatasInternship;
import com.scujoo.datas.DatasRecruit;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.HttpUtils;
import com.scujoo.utils.Md5;

public class FragmentHome extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {

	private ListView recruit;
	private ListView demand;
	private ListView internship;
	View rootView;
	private List<DatasRecruit> listDatasRecruit;
	private List<DatasDemand> listDatasDemand;
	private List<DatasInternship> listDatasInternship;
	private AdapterRecruit adapterRecruit;
	private AdapterDemand adapterDemand;
	private AdapterInternship adapterInternship;
	private ProgressDialog dialog;
	private LinearLayout llRecruit;
	private LinearLayout llDemand;
	private LinearLayout llIntrenship;
	private LinearLayout llOutRecruit;
	private LinearLayout llOutDemand;
	private LinearLayout llOutInternship;
	private LinearLayout topCalendar;
	private SwipeRefreshLayout swipeRefreshLayout;
	private FragmentHome fragmentHome;
	FragmentManager fm;
	FragmentTransaction ft;

	private String URL = StaticDatas.URL + "scujoo/hot_home.php";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		init();// 初始化组件
		llRecruit.setVisibility(View.INVISIBLE);
		llDemand.setVisibility(View.INVISIBLE);
		llIntrenship.setVisibility(View.INVISIBLE);
		
		String callBack = "";
		try {
			callBack = getArguments().getString("callBack", "");
			System.out.println("callBack:" + callBack);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// 刷新监听器
		swipeRefreshLayout.setOnRefreshListener(this);
		// 设置刷新效果的颜色
		swipeRefreshLayout
				.setColorScheme(android.R.color.holo_blue_dark,
						android.R.color.holo_green_dark,
						android.R.color.holo_orange_dark,
						android.R.color.holo_red_dark);

		if (callBack.equals("callBack")) {
			dialog = new ProgressDialog(getActivity());
			swipeRefreshLayout.setRefreshing(true);
		} else {
			// 显示正在加载
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("正在加载...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);
			dialog.show();
		}
		Context context = getActivity().getApplicationContext();
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			// 如果有网络连接，执行异步传输
			Yibu yibu = new Yibu();
			yibu.execute();
		} else {
			Toast.makeText(getActivity(), "无网络连接", 1).show();
			getActivity().finish();
		}


		// 设置listView监听事件
		recruit.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DatasRecruit datasRecruit = listDatasRecruit.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ContentRecruit.class);
				intent.putExtra("id", datasRecruit.getId());
				startActivity(intent);
			}
		});
		// 设置listView监听事件
		demand.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DatasDemand datasDemand = listDatasDemand.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ContentDemand.class);
				intent.putExtra("id", datasDemand.getId());
				startActivity(intent);
			}
		});
		internship.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DatasInternship datasInternship = listDatasInternship
						.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ContentInternship.class);
				intent.putExtra("id", datasInternship.getId());
				startActivity(intent);
			}
		});

		return rootView;
	}

	private void init() {
		recruit = (ListView) rootView
				.findViewById(R.id.fragment_home_list_recruit);
		demand = (ListView) rootView
				.findViewById(R.id.fragment_home_list_demand);
		internship = (ListView) rootView
				.findViewById(R.id.fragment_home_list_internship);
		listDatasRecruit = new ArrayList<DatasRecruit>();
		listDatasDemand = new ArrayList<DatasDemand>();
		listDatasInternship = new ArrayList<DatasInternship>();
		llRecruit = (LinearLayout) rootView
				.findViewById(R.id.fragment_home_ll_recruit);
		llDemand = (LinearLayout) rootView
				.findViewById(R.id.fragment_home_ll_demand);
		llIntrenship = (LinearLayout) rootView
				.findViewById(R.id.fragment_home_ll_internship);
		llOutRecruit = (LinearLayout) rootView
				.findViewById(R.id.fragment_home_out_recruit);
		llOutDemand = (LinearLayout) rootView
				.findViewById(R.id.fragment_home_out_demand);
		llOutInternship = (LinearLayout) rootView
				.findViewById(R.id.fragment_home_out_intrernship);
		topCalendar = (LinearLayout) getActivity().findViewById(
				R.id.id_top_calendar);
		swipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.fragment_home_refresh);
		fm = getActivity().getSupportFragmentManager();
		ft = fm.beginTransaction();
	}

	class Yibu extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			
			HttpPost httpPost = new HttpPost(URL);
			HttpResponse httpResponse = null;
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			SharedPreferences sp = getActivity().getSharedPreferences("datas",
					Activity.MODE_PRIVATE);
			String userName = sp.getString("userName", "");
			String userPass = sp.getString("userPass", "");
			String token;
			token = "userName=" + userName + "&userPass=" + userPass + "token";
			String md5 = Md5.Md5Str(token);
			param.add(new BasicNameValuePair("userName", userName));
			param.add(new BasicNameValuePair("userPass", userPass));
			param.add(new BasicNameValuePair("md5", md5));
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					System.out.println("HomeResult:" + result);
					return result;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			swipeRefreshLayout.setRefreshing(false);
			topCalendar.setVisibility(View.GONE);
			llRecruit.setVisibility(View.VISIBLE);
			llDemand.setVisibility(View.VISIBLE);
			llIntrenship.setVisibility(View.VISIBLE);
			// 取得异步传输的数据
			try {
				JSONObject obj = new JSONObject(result);
				JSONObject obj2 = new JSONObject(obj.getString("result"));
				JSONArray arrRecruit = new JSONArray(obj2.getString("recruit"));
				JSONArray arrDemand = new JSONArray(obj2.getString("demand"));
				JSONArray arrInternship = new JSONArray(
						obj2.getString("internship"));
				System.out.println("Recruit" + arrRecruit);
				System.out.println("demand" + arrDemand);
				System.out.println("internship" + arrInternship);
				for (int i = 0; i < arrRecruit.length(); i++) {
					JSONObject obj3 = arrRecruit.getJSONObject(i);
					listDatasRecruit.add(new DatasRecruit(obj3.getString("id"),
							obj3.getString("name"), obj3
									.getString("recruitTime"), obj3
									.getString("recruitPlace")));
				}
				for (int i = 0; i < arrDemand.length(); i++) {
					JSONObject obj3 = arrDemand.getJSONObject(i);
					listDatasDemand.add(new DatasDemand(obj3.getString("id"),
							obj3.getString("name"), obj3
									.getString("publishTime"), obj3
									.getString("position")));
				}
				for (int i = 0; i < arrInternship.length(); i++) {
					JSONObject obj3 = arrInternship.getJSONObject(i);
					listDatasInternship.add(new DatasInternship(obj3
							.getString("id"), obj3.getString("name"), obj3
							.getString("publishTime"), obj3
							.getString("position")));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			adapterRecruit = new AdapterRecruit(rootView.getContext(),
					listDatasRecruit);
			adapterDemand = new AdapterDemand(rootView.getContext(),
					listDatasDemand);
			adapterInternship = new AdapterInternship(rootView.getContext(),
					listDatasInternship);
			recruit.setAdapter(adapterRecruit);
			demand.setAdapter(adapterDemand);
			internship.setAdapter(adapterInternship);

			// 根据listView的高度设置外围LinerLayout的高度
			ViewGroup.LayoutParams params = llOutRecruit.getLayoutParams();
			params.height = setListViewHeightBasedOnChildren(recruit) + 60;
			llOutRecruit.setLayoutParams(params);

			ViewGroup.LayoutParams params1 = llOutDemand.getLayoutParams();
			params1.height = setListViewHeightBasedOnChildren(demand) + 60;
			llOutDemand.setLayoutParams(params1);

			ViewGroup.LayoutParams params2 = llOutInternship.getLayoutParams();
			params2.height = setListViewHeightBasedOnChildren(internship) + 60;
			llOutInternship.setLayoutParams(params2);
		}
	}

	// 动态获取ListView的高度
	public static int setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			// listItem.measure(0, 0);
			listItem.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		return params.height;
		// listView.setLayoutParams(params);
	}

	public void onRefresh() {
		fragmentHome = new FragmentHome();
		Bundle bundle = new Bundle();
		bundle.putString("callBack", "callBack");
		fragmentHome.setArguments(bundle);
		ft.replace(R.id.id_content, fragmentHome);
		ft.commit();
	}

}
