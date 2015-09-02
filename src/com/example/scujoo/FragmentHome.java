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

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.scujoo.MainActivity.MyTouchListener;
import com.scujoo.adapter.AdapterDemand;
import com.scujoo.adapter.AdapterInternship;
import com.scujoo.adapter.AdapterRecruit;
import com.scujoo.datas.DatasDemand;
import com.scujoo.datas.DatasInternship;
import com.scujoo.datas.DatasRecruit;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.Md5;
import com.squareup.picasso.Picasso;

public class FragmentHome extends Fragment {

	private float startX;
	private ListView recruit;
	private ListView demand;
	private ListView internship;
	private ImageView content1;
	private ImageView content2;
	private ImageView content3;
	private ImageView content4;
	private ViewFlipper flipper;
	private View rootView;
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
	private FragmentManager fm;
	private FragmentTransaction ft;
	private int index = 1;
	private String URL = StaticDatas.URL + "scujoo/hot_home.php";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_home, container, false);
		init();// ��ʼ�����
		final ImageView iv1 = (ImageView) rootView
				.findViewById(R.id.home_flipper_iv1);
		final ImageView iv2 = (ImageView) rootView
				.findViewById(R.id.home_flipper_iv2);
		final ImageView iv3 = (ImageView) rootView
				.findViewById(R.id.home_flipper_iv3);
		final ImageView iv4 = (ImageView) rootView
				.findViewById(R.id.home_flipper_iv4);
		final ImageView[] arr = { iv1, iv2, iv3, iv4 };
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

		// ��ʾ���ڼ���
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("���ڼ���...");
		dialog.setIndeterminate(false);
		dialog.setCancelable(true);
		dialog.show();

		Context context = getActivity().getApplicationContext();
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
		if (mNetworkInfo != null) {
			// ������������ӣ�ִ���첽����
			Yibu yibu = new Yibu();
			yibu.execute();
		} else {
			Toast.makeText(getActivity(), "����������", 1).show();
			getActivity().finish();
		}

		// ����listView�����¼�
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
		// ����listView�����¼�
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

		/**
		 * Fragment�У�ע�� ����MainActivity��Touch�ص��Ķ���
		 * ��д���е�onTouchEvent�����������и�Fragment���߼�����
		 */
		MainActivity.MyTouchListener mTouchListener = new MyTouchListener() {
			@Override
			public void onTouchEvent(MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					startX = event.getX();
					break;
				}

				case MotionEvent.ACTION_UP: {
					// ���һ���
					if (event.getX() - startX > 50) {
						flipper.setInAnimation(getActivity(), R.anim.left_in);
						flipper.setOutAnimation(getActivity(), R.anim.left_out);
						flipper.showPrevious();
						flipper.setInAnimation(getActivity(), R.anim.right_in);
						flipper.setOutAnimation(getActivity(), R.anim.right_out);

					}
					// ���󻬶�
					if (startX - event.getX() > 50) {
						flipper.setInAnimation(getActivity(), R.anim.right_in);
						flipper.setOutAnimation(getActivity(), R.anim.right_out);
						flipper.showNext();
						flipper.setInAnimation(getActivity(), R.anim.right_in);
						flipper.setOutAnimation(getActivity(), R.anim.right_out);
					}
					break;
				}
				}
			}
		};
		// �ڸ�Fragment�Ĺ��캯����ע��mTouchListener�Ļص�

		/*
		 * ((MainActivity) this.getActivity())
		 * .registerMyTouchListener(mTouchListener);
		 */

		// ������������¼����ı�ָʾ����ʾ
		flipper.getInAnimation().setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				iv1.setImageResource(R.drawable.point_gray);
				iv2.setImageResource(R.drawable.point_gray);
				iv3.setImageResource(R.drawable.point_gray);
				iv4.setImageResource(R.drawable.point_gray);
				arr[index].setImageResource(R.drawable.point_green);
				index = (index + 1) % 4;
			}
		});
		return rootView;
	}

	private void init() {
		content1 = (ImageView) rootView
				.findViewById(R.id.home_flipper_content1);
		content2 = (ImageView) rootView
				.findViewById(R.id.home_flipper_content2);
		content3 = (ImageView) rootView
				.findViewById(R.id.home_flipper_content3);
		content4 = (ImageView) rootView
				.findViewById(R.id.home_flipper_content4);
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
		fm = getActivity().getSupportFragmentManager();
		ft = fm.beginTransaction();
		flipper = (ViewFlipper) rootView.findViewById(R.id.home_flipper_image);

		Picasso.with(getActivity())
				.load("http://120.25.245.241/scujoo/otherImages/home1.jpg")
				.into(content1);
		Picasso.with(getActivity())
				.load("http://120.25.245.241/scujoo/otherImages/home2.jpg")
				.into(content2);
		Picasso.with(getActivity())
				.load("http://120.25.245.241/scujoo/otherImages/home3.jpg")
				.into(content3);
		Picasso.with(getActivity())
				.load("http://120.25.245.241/scujoo/otherImages/home4.jpg")
				.into(content4);

		/*
		 * flipper.addView(getImageView(R.drawable.home1));
		 * flipper.addView(getImageView(R.drawable.home2));
		 * flipper.addView(getImageView(R.drawable.home3));
		 * flipper.addView(getImageView(R.drawable.home4));
		 */
		flipper.setBackgroundColor(Color.BLACK);
		flipper.setFlipInterval(3000);
		flipper.setInAnimation(getActivity(), R.anim.right_in);
		flipper.setOutAnimation(getActivity(), R.anim.right_out);
		flipper.startFlipping();
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
			topCalendar.setVisibility(View.GONE);
			llRecruit.setVisibility(View.VISIBLE);
			llDemand.setVisibility(View.VISIBLE);
			llIntrenship.setVisibility(View.VISIBLE);
			// ȡ���첽���������
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

			// ����listView�ĸ߶�������ΧLinerLayout�ĸ߶�
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

	// ��̬��ȡListView�ĸ߶�
	public static int setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
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

	private ImageView getImageView(int resId) {
		ImageView image = new ImageView(getActivity());
		image.setBackgroundResource(resId);
		return image;
	}

}
