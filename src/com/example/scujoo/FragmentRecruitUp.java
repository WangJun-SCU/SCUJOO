package com.example.scujoo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
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
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
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
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.scujoo.adapter.AdapterRecruit;
import com.scujoo.datas.DatasRecruit;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.Md5;

public class FragmentRecruitUp extends Fragment implements
		SwipeRefreshLayout.OnRefreshListener {
	private ListView listView;
	private LinearLayout llOutRecruit;
	private LinearLayout topCalendar;
	private LinearLayout datePicker;
	private GridView grid;
	private ProgressDialog dialog;
	private View rootView;
	private String[] s28 = new String[28];
	private SharedPreferences preferences;
	private String userName;
	private String userPass;
	private String md5;
	private String URL = StaticDatas.URL + "scujoo/recruit_date.php";
	private Date d;
	private SimpleDateFormat sdf;
	private String date;
	private AdapterRecruit adapterRecruit;
	private List<DatasRecruit> listRecruit;
	private SwipeRefreshLayout swipe;
	private LinearLayout before;
	private LinearLayout after;
	private TextView currentDate;
	private FragmentRecruitUp fragmentRecruit;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private String sDate;
	private String sWeek;
	private LinearLayout left;
	private TextView z01;
	private TextView z02;
	private TextView z03;
	private TextView z04;
	private TextView z05;
	private TextView z06;
	private TextView z07;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_recruit_table, container,
				false);
		init();

		/*
		 * int height1 = left.getHeight() / 7; LinearLayout.LayoutParams params1
		 * = (LinearLayout.LayoutParams) z01.getLayoutParams();
		 * LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams)
		 * z02.getLayoutParams(); LinearLayout.LayoutParams params3 =
		 * (LinearLayout.LayoutParams) z03.getLayoutParams();
		 * LinearLayout.LayoutParams params4 = (LinearLayout.LayoutParams)
		 * z04.getLayoutParams(); LinearLayout.LayoutParams params5 =
		 * (LinearLayout.LayoutParams) z05.getLayoutParams();
		 * LinearLayout.LayoutParams params6 = (LinearLayout.LayoutParams)
		 * z06.getLayoutParams(); LinearLayout.LayoutParams params7 =
		 * (LinearLayout.LayoutParams) z07.getLayoutParams(); params1.height =
		 * height1; params2.height = height1; params3.height = height1;
		 * params4.height = height1; params5.height = height1; params6.height =
		 * height1; params7.height = height1; z01.setLayoutParams(params1);
		 * z02.setLayoutParams(params2); z03.setLayoutParams(params3);
		 * z04.setLayoutParams(params4); z05.setLayoutParams(params5);
		 * z06.setLayoutParams(params6); z07.setLayoutParams(params7);
		 */

		currentDate.setText(sDate + "   " + sWeek);// 设置当前日期

		Yibu yibu = new Yibu();
		yibu.execute();
		topCalendar.setVisibility(View.VISIBLE);

		// 判断是否是刷新回调---------------------------------------------------------
		String callBack = "";
		try {
			callBack = getArguments().getString("callBack", "");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		// 刷新监听器
		swipe.setOnRefreshListener(this);
		// 设置刷新效果的颜色
		swipe.setColorScheme(android.R.color.holo_blue_dark,
				android.R.color.holo_green_dark,
				android.R.color.holo_orange_dark, android.R.color.holo_red_dark);
		if (callBack.equals("callBack")) {
			dialog = new ProgressDialog(getActivity());
			swipe.setRefreshing(true);
		} else {
			// 显示正在加载
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage("正在加载...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);
			dialog.show();
		}
		datePicker.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Calendar cc = Calendar.getInstance();
				System.out.println("1111111");
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
								System.out.println(tDate);
								fragmentRecruit = new FragmentRecruitUp();
								Bundle bundle = new Bundle();
								bundle.putString("cDate", tDate);
								fragmentRecruit.setArguments(bundle);
								System.out.println("1111111");
								ft.replace(R.id.id_content, fragmentRecruit);
								System.out.println("22222222222");
								ft.commit();
								System.out.println("33333333333");
							}
						}, cc.get(cc.YEAR), cc.get(cc.MONTH), cc
								.get(cc.DAY_OF_MONTH)).show();
			}
		});

		// 添加前一天点击事件
		before.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String[] ss = currentDate.getText().toString().split("  ");
				String tDate = ss[0];// 获取标题栏上的日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dt = new Date();
				try {
					dt = sdf.parse(tDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(dt);
				rightNow.add(Calendar.DAY_OF_YEAR, -1);// 日期减1天
				Date dt1 = rightNow.getTime();
				String reStr = sdf.format(dt1);// 处理过的日期
				fragmentRecruit = new FragmentRecruitUp();
				Bundle bundle = new Bundle();
				bundle.putString("cDate", reStr);// 将处理过的日期回调传给自身
				fragmentRecruit.setArguments(bundle);
				ft.replace(R.id.id_content, fragmentRecruit);
				ft.commit();
			}
		});
		// 添加后一天点击事件
		after.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String[] ss = currentDate.getText().toString().split("  ");
				String tDate = ss[0];// 获取标题栏上的日期
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date dt = new Date();
				try {
					dt = sdf.parse(tDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(dt);
				rightNow.add(Calendar.DAY_OF_YEAR, +1);// 日期加10天
				Date dt1 = rightNow.getTime();
				String reStr = sdf.format(dt1);// 标题栏其日加一天
				fragmentRecruit = new FragmentRecruitUp();
				Bundle bundle = new Bundle();
				bundle.putString("cDate", reStr);
				fragmentRecruit.setArguments(bundle);
				ft.replace(R.id.id_content, fragmentRecruit);
				ft.commit();
			}
		});

		return rootView;
	}

	private void init() {
		fm = getActivity().getSupportFragmentManager();
		ft = fm.beginTransaction();
		for (int i = 0; i < s28.length; i++) {
			s28[i] = "";
		}
		topCalendar = (LinearLayout) getActivity().findViewById(
				R.id.id_top_calendar);
		listView = (ListView) rootView
				.findViewById(R.id.fragment_recruit_table_listview);
		grid = (GridView) rootView
				.findViewById(R.id.fragment_recruit_table_grid);
		swipe = (SwipeRefreshLayout) rootView
				.findViewById(R.id.fragment_recruit_table_refresh);
		before = (LinearLayout) getActivity().findViewById(
				R.id.top_calendar_before);
		after = (LinearLayout) getActivity().findViewById(
				R.id.top_calendar_after);
		currentDate = (TextView) getActivity().findViewById(
				R.id.top_calendar_date);
		datePicker = (LinearLayout) getActivity().findViewById(
				R.id.top_calendar_calendar);
		left = (LinearLayout) rootView
				.findViewById(R.id.fragment_recruit_table_left);
		z01 = (TextView) rootView.findViewById(R.id.fragment_recruit_table_201);
		z02 = (TextView) rootView.findViewById(R.id.fragment_recruit_table_209);
		z03 = (TextView) rootView.findViewById(R.id.fragment_recruit_table_01);
		z04 = (TextView) rootView.findViewById(R.id.fragment_recruit_table_d5);
		z05 = (TextView) rootView.findViewById(R.id.fragment_recruit_table_h3);
		z06 = (TextView) rootView.findViewById(R.id.fragment_recruit_table_w1);
		z07 = (TextView) rootView.findViewById(R.id.fragment_recruit_table_w2);

		d = new Date();
		sdf = new SimpleDateFormat("yyyy-MM-dd");
		date = sdf.format(d);
		listRecruit = new ArrayList<DatasRecruit>();
		SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE");
		sDate = sdf.format(d);
		sWeek = sdf2.format(d);

		preferences = getActivity().getSharedPreferences("datas",
				Activity.MODE_PRIVATE);
		userName = preferences.getString("userName", "");
		userPass = preferences.getString("userPass", "");
		String token = "userName=" + userName + "&userPass=" + userPass
				+ "token";
		md5 = Md5.Md5Str(token);
	}

	// 动态获取ListView的高度
	public static int setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += listItem.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		return params.height;
	}

	class Yibu extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String result = "";
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("userName", userName));
			param.add(new BasicNameValuePair("userPass", userPass));
			param.add(new BasicNameValuePair("md5", md5));
			// 判断是否点击了前一天后者后一天或选择了日期回调
			String cDate = "";
			try {
				cDate = getArguments().getString("cDate", "");
				SimpleDateFormat s1 = new SimpleDateFormat("EEEE");
				SimpleDateFormat s2 = new SimpleDateFormat("yyyy-MM-dd");
				Date d = s2.parse(cDate);
				String ss = s1.format(d);
				currentDate.setText(cDate + "  " + ss);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if (cDate != "") {
				param.add(new BasicNameValuePair("date", cDate));
			} else {
				param.add(new BasicNameValuePair("date", date));
			}

			System.out.println(param);
			HttpPost httpPost = new HttpPost(URL);
			HttpResponse httpResponse = null;
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					result = EntityUtils.toString(httpResponse.getEntity());
					System.out.println(result);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return result;

		}

		@Override
		protected void onPostExecute(String result) {
			dialog.dismiss();
			swipe.setRefreshing(false);
			try {
				JSONObject obj = new JSONObject(result);
				JSONArray array = new JSONArray(obj.getString("result"));
				for (int i = 0; i < array.length(); i++) {
					JSONObject obj2 = array.getJSONObject(i);
					String[] s = obj2.getString("recruitTime").split(" ");
					String time = s[1];
					String recruitPlace = obj2.getString("recruitPlace");
					String name = obj2.getString("name");
					System.out.print("time:" + time + "place:" + recruitPlace
							+ "name:" + name + "\n");
					if (!"09:30:00".equals(time)) {
						System.out.println(time);
						listRecruit.add(new DatasRecruit(obj2.getString("id"),
								obj2.getString("name"), obj2
										.getString("recruitTime"), obj2
										.getString("recruitPlace")));
					} else if ((recruitPlace.equals("就业指导中心201报告厅"))
							&& (time.equals("09:30:00"))) {
						s28[0] = name;
					} else if ((recruitPlace.equals("就业指导中心201报告厅"))
							&& (time.equals("12:30:00"))) {
						s28[1] = name;
					} else if ((recruitPlace.equals("就业指导中心201报告厅"))
							&& (time.equals("15:00:00"))) {
						s28[2] = name;
					} else if ((recruitPlace.equals("就业指导中心201报告厅"))
							&& (time.equals("18:30:00"))) {
						s28[3] = name;
					} else if ((recruitPlace.equals("就业指导中心209教室"))
							&& (time.equals("09:30:00"))) {
						s28[4] = name;
					} else if ((recruitPlace.equals("就业指导中心209教室"))
							&& (time.equals("12:30:00"))) {
						s28[5] = name;
					} else if ((recruitPlace.equals("就业指导中心209教室"))
							&& (time.equals("15:00:00"))) {
						s28[6] = name;
					} else if ((recruitPlace.equals("就业指导中心209教室"))
							&& (time.equals("18:30:00"))) {
						s28[7] = name;
					} else if ((recruitPlace.equals("就业指导中心附楼01教室"))
							&& (time.equals("09:30:00"))) {
						s28[8] = name;
					} else if ((recruitPlace.equals("就业指导中心附楼01教室"))
							&& (time.equals("12:30:00"))) {
						s28[9] = name;
					} else if ((recruitPlace.equals("就业指导中心附楼01教室"))
							&& (time.equals("15:00:00"))) {
						s28[10] = name;
					} else if ((recruitPlace.equals("就业指导中心附楼01教室"))
							&& (time.equals("18:30:00"))) {
						s28[11] = name;
					} else if ((recruitPlace.equals("西五教演播厅"))
							&& (time.equals("09:30:00"))) {
						s28[12] = name;
					} else if ((recruitPlace.equals("西五教演播厅"))
							&& (time.equals("12:30:00"))) {
						s28[13] = name;
					} else if ((recruitPlace.equals("西五教演播厅"))
							&& (time.equals("15:00:00"))) {
						s28[14] = name;
					} else if ((recruitPlace.equals("西五教演播厅"))
							&& (time.equals("18:30:00"))) {
						s28[15] = name;
					} else if ((recruitPlace.equals("西区活动中心三楼大厅"))
							&& (time.equals("09:30:00"))) {
						s28[16] = name;
					} else if ((recruitPlace.equals("西区活动中心三楼大厅"))
							&& (time.equals("12:30:00"))) {
						s28[17] = name;
					} else if ((recruitPlace.equals("西区活动中心三楼大厅"))
							&& (time.equals("15:00:00"))) {
						s28[18] = name;
					} else if ((recruitPlace.equals("西区活动中心三楼大厅"))
							&& (time.equals("18:30:00"))) {
						s28[19] = name;
					} else if ((recruitPlace.equals("文华活动中心一楼放映厅"))
							&& (time.equals("09:30:00"))) {
						s28[20] = name;
					} else if ((recruitPlace.equals("文华活动中心一楼放映厅"))
							&& (time.equals("12:30:00"))) {
						s28[21] = name;
					} else if ((recruitPlace.equals("文华活动中心一楼放映厅"))
							&& (time.equals("15:00:00"))) {
						s28[22] = name;
					} else if ((recruitPlace.equals("文华活动中心一楼放映厅"))
							&& (time.equals("18:30:00"))) {
						s28[23] = name;
					} else if ((recruitPlace.equals("文华活动中心二楼报告厅"))
							&& (time.equals("09:30:00"))) {
						s28[24] = name;
					} else if ((recruitPlace.equals("文华活动中心二楼报告厅"))
							&& (time.equals("12:30:00"))) {
						s28[25] = name;
					} else if ((recruitPlace.equals("文华活动中心二楼报告厅"))
							&& (time.equals("15:00:00"))) {
						s28[26] = name;
					} else if ((recruitPlace.equals("文华活动中心二楼报告厅"))
							&& (time.equals("18:30:00"))) {
						s28[27] = name;
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			adapterRecruit = new AdapterRecruit(rootView.getContext(),
					listRecruit);
			listView.setAdapter(adapterRecruit);
			// 根据listView的高度设置外围LinerLayout的高度
			llOutRecruit = (LinearLayout) rootView
					.findViewById(R.id.fragment_recruit_table_ll);
			ViewGroup.LayoutParams params = llOutRecruit.getLayoutParams();
			params.height = setListViewHeightBasedOnChildren(listView) + 60;
			llOutRecruit.setLayoutParams(params);

			// 自定义适配器
			BaseAdapter adapter2 = new BaseAdapter() {
				public View getView(int position, View convertView,
						ViewGroup parent) {
					convertView = LayoutInflater.from(rootView.getContext())
							.inflate(R.layout.list_table, null);

					int height = grid.getHeight();
					int width = grid.getWidth();
					GridView.LayoutParams params = new GridView.LayoutParams(
							width / 4, height / 7);
					TextView tv = (TextView) convertView
							.findViewById(R.id.id_list_table);
					tv.setText(s28[position]);
					convertView.setLayoutParams(params);
					return convertView;
				}

				public long getItemId(int position) {
					return position;
				}

				public Object getItem(int position) {
					return null;
				}

				public int getCount() {
					return 28;
				}
			};
			grid.setAdapter(adapter2);
		}

	}

	public void onRefresh() {
		fragmentRecruit = new FragmentRecruitUp();
		Bundle bundle = new Bundle();
		bundle.putString("callBack", "callBack");
		fragmentRecruit.setArguments(bundle);
		ft.replace(R.id.id_content, fragmentRecruit);
		ft.commit();
	}
}
