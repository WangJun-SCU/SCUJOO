package com.example.scujoo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
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
	private String URL = StaticDatas.URL + "scujoo/demand_date.php";
	private FragmentManager fm;
	private FragmentTransaction ft;
	private View rootView;
	private LinearLayout before;
	private LinearLayout after;
	private TextView currentDate;
	private String sDate;
	private String sWeek;
	private SharedPreferences sp;
	private String userName;
	private String userPass;
	private String token;
	private String md5;
	private LinearLayout datePicker;

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
		rootView = inflater.inflate(R.layout.fragment_demand, container, false);
		init();
		currentDate.setText(sDate+"   "+sWeek);//设置当前日期
		
		//判断是否是刷新回调---------------------------------------------------------
		String callBack = "";
		try {
			callBack = getArguments().getString("callBack", "");
			System.out.println("callBack:" + callBack);
		} catch (Exception e1) {
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
		
		String url = "default";//是否是搜索回调
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

		listViewDemand.setAdapter(adapterDemand);

		// 封装传送到服务器的数据
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("userPass", userPass));
		params.add(new BasicNameValuePair("md5", md5));
		String cDate = "";
		//判断是否点击了前一天后者后一天回调
		try {
			cDate = getArguments().getString("cDate", "");
			SimpleDateFormat s1 = new SimpleDateFormat("EEEE");
			SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
			Date d = s2.parse(cDate);
			String ss = s1.format(d);
			currentDate.setText(cDate+"  "+ss);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		if(cDate!="")
		{
			params.add(new BasicNameValuePair("date", cDate));
		}else{
			params.add(new BasicNameValuePair("date", sDate));
		}

		// 判断是否是从搜索界面回调
		if ("default".equals(content)) {

		} else if ("demand".equals(content)) {
			params.add(new BasicNameValuePair("content", content));
			params.add(new BasicNameValuePair("select", select));
			URL = "http://120.25.245.241/scujoo/select.php";
		}

		// 判断是否有网络连接
		Context context = getActivity().getApplicationContext();
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			// 如果有网络连接，执行异步传输
			System.out.println("params:"+params);
			HttpUtils.getJson(URL, handlerDemand, params);
		} else {
			Toast.makeText(getActivity(), "无网络连接", 1).show();
			getActivity().finish();
		}

		// 设置listView监听事件
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
		// 点击标题title返回顶部
		topTitle.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				listViewDemand.setSelectionAfterHeaderView();
			}
		});
		datePicker.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Calendar cc = Calendar.getInstance();
				new DatePickerDialog(getActivity(),
						new DatePickerDialog.OnDateSetListener() {
							boolean mFired = false;
							public void onDateSet(DatePicker view, int year,
									int monthOfYear, int dayOfMonth) {
								if (mFired == true) {
						            return;
						        } else {
						            //first time mFired
						            mFired = true;
						        }
								
								String tDate = "" + year + "-"
										+ (monthOfYear + 1) + "-" + dayOfMonth;// 获取选择的日期
								fragmentDemand = new FragmentDemand();
								Bundle bundle = new Bundle();
								bundle.putString("cDate", tDate);
								fragmentDemand.setArguments(bundle);
								ft.replace(R.id.id_content, fragmentDemand);
								ft.commit();
							}
						}, cc.get(cc.YEAR), cc.get(cc.MONTH), cc
								.get(cc.DAY_OF_MONTH)).show();
			}
		});
		// 添加前一天点击事件
		before.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String[] ss = currentDate.getText().toString().split("  ");
				String tDate = ss[0];//获取标题栏上的日期
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		        Date dt=new Date();
				try {
					dt = sdf.parse(tDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        Calendar rightNow = Calendar.getInstance();
		        rightNow.setTime(dt);
		        rightNow.add(Calendar.DAY_OF_YEAR,-1);//日期加10天
		        Date dt1=rightNow.getTime();
		        String reStr = sdf.format(dt1);//标题栏其日期减一天
				fragmentDemand = new FragmentDemand();
				Bundle bundle = new Bundle();
				bundle.putString("cDate", reStr);
				fragmentDemand.setArguments(bundle);
				ft.replace(R.id.id_content, fragmentDemand);
				ft.commit();
			}
		});
		// 添加后一天点击事件
		after.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String[] ss = currentDate.getText().toString().split("  ");
				String tDate = ss[0];//获取标题栏上的日期
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		        Date dt=new Date();
				try {
					dt = sdf.parse(tDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
		        Calendar rightNow = Calendar.getInstance();
		        rightNow.setTime(dt);
		        rightNow.add(Calendar.DAY_OF_YEAR,+1);//日期加10天
		        Date dt1=rightNow.getTime();
		        String reStr = sdf.format(dt1);//标题栏其日加一天
				fragmentDemand = new FragmentDemand();
				Bundle bundle = new Bundle();
				bundle.putString("cDate", reStr);
				fragmentDemand.setArguments(bundle);
				ft.replace(R.id.id_content, fragmentDemand);
				ft.commit();
			}
		});
		return rootView;
	}

	//初始化
	private void init() {
		fm = getActivity().getSupportFragmentManager();
		ft = fm.beginTransaction();
		topCalendar = (LinearLayout) getActivity().findViewById(
				R.id.id_top_calendar);
		swipeRefreshLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.fragment_demand_refresh);
		topTitle = (TextView) getActivity().findViewById(R.id.top_title);
		before = (LinearLayout) getActivity().findViewById(
				R.id.top_calendar_before);
		after = (LinearLayout) getActivity().findViewById(
				R.id.top_calendar_after);
		datePicker = (LinearLayout) getActivity().findViewById(
				R.id.top_calendar_calendar);
		currentDate = (TextView) getActivity().findViewById(R.id.top_calendar_date);
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
		sDate = sdf.format(d);
		sWeek = sdf2.format(d);
		sp = getActivity().getSharedPreferences("datas",Activity.MODE_PRIVATE);
		userName = sp.getString("userName", "");
		userPass = sp.getString("userPass", "");
		token = "userName=" + userName + "&userPass=" + userPass + "token";
		md5 = Md5.Md5Str(token);
		listViewDemand = (ListView) rootView.findViewById(R.id.fragment_demand_listView);
		listDemand = new ArrayList<DatasDemand>();
		adapterDemand = new AdapterDemand(rootView.getContext(), listDemand);
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
