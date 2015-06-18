package com.example.scujoo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.scujoo.ContentDemand.Yibu;
import com.example.scujoo.ContentDemand.Yibu2;
import com.example.scujoo.ContentDemand.Yibu3;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.Md5;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ContentInternship extends Activity {

	private ImageButton back;
	private ImageButton collect;
	private TextView name;
	private TextView publishTime;
	private TextView deadline;
	private TextView position;
	private TextView pay;
	private TextView workPlace;
	private TextView intro;
	private TextView others;
	private String id;
	private String URL = StaticDatas.URL + "scujoo/content_internship.php";
	private String URLCollect = StaticDatas.URL + "scujoo/collect_internship.php";
	private String URLCollectDelete = StaticDatas.URL
			+ "scujoo/collect_delete_internship.php";
	private String URLCollectYny = StaticDatas.URL
			+ "scujoo/collect_yn_internship.php";
	private String userName;
	private String userPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_demand);

		init();

		Yibu yibu = new Yibu();
		String[] result2 = new String[9];
		try {
			result2 = yibu.execute().get();
			name.setText(result2[0]);
			publishTime.setText(result2[1]);
			deadline.setText(result2[2]);
			position.setText(result2[3]);
			pay.setText(result2[4]);
			workPlace.setText(result2[5]);
			intro.setText(result2[6]);
			others.setText(result2[7]);
			if ("200".equals(result2[8])) {
				collect.setImageResource(R.drawable.collect_full);
				collect.setTag("2");
			} else if ("404".equals(result2[8])) {
				collect.setImageResource(R.drawable.collect_empty);
				collect.setTag("1");
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		back.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});
		
		collect.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				String tagC = (String) collect.getTag();
				if ("1".equals(tagC)) {
					Yibu2 yibu2 = new Yibu2();
					String result = null;
					try {
						result = yibu2.execute().get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					if ("200".equals(result)) {
						Toast.makeText(getApplicationContext(), "收藏成功", 1)
								.show();
						collect.setImageResource(R.drawable.collect_full);
						collect.setTag("2");
					} else {
						Toast.makeText(getApplicationContext(), "收藏失败", 1)
								.show();
					}
				} else if ("2".equals(tagC)) {
					Yibu3 yibu3 = new Yibu3();
					String result = null;
					try {
						result = yibu3.execute().get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					if ("200".equals(result)) {
						Toast.makeText(getApplicationContext(), "取消收藏成功", 1)
								.show();
						collect.setImageResource(R.drawable.collect_empty);
						collect.setTag("1");
					} else {
						Toast.makeText(getApplicationContext(), "取消收藏失败", 1)
								.show();
					}
				}
			}
		});

	}

	private void init() {
		back = (ImageButton) findViewById(R.id.content_demand_back);
		collect = (ImageButton) findViewById(R.id.content_demand_collect);
		name = (TextView) findViewById(R.id.content_demand_name);
		publishTime = (TextView) findViewById(R.id.content_demand_publish_time);
		deadline = (TextView) findViewById(R.id.content_demand_deadline);
		position = (TextView) findViewById(R.id.content_demand_position);
		pay = (TextView) findViewById(R.id.content_demand_pay);
		workPlace = (TextView) findViewById(R.id.content_demand_work_place);
		intro = (TextView) findViewById(R.id.content_demand_intro);
		others = (TextView) findViewById(R.id.content_demand_others);
		id = getIntent().getStringExtra("id");

		SharedPreferences sp = getSharedPreferences("datas",
				Activity.MODE_PRIVATE);
		userName = sp.getString("userName", "");
		userPass = sp.getString("userPass", "");
	}

	class Yibu extends AsyncTask<String, String, String[]> {

		@Override
		protected String[] doInBackground(String... params) {
			HttpPost httpPost = new HttpPost(URL);
			HttpPost httpPost1 = new HttpPost(URLCollectYny);
			HttpResponse httpResponse = null;
			HttpResponse httpResponse1 = null;

			List<NameValuePair> param = new ArrayList<NameValuePair>();
			String token, md5;
			token = "userName=" + userName + "&userPass=" + userPass + "token";
			md5 = Md5.Md5Str(token);
			param.add(new BasicNameValuePair("userName", userName));
			param.add(new BasicNameValuePair("userPass", userPass));
			param.add(new BasicNameValuePair("md5", md5));
			param.add(new BasicNameValuePair("id", id));
			param.add(new BasicNameValuePair("userId", userName));
			param.add(new BasicNameValuePair("messageId", id));
			System.out.println(param);

			try {
				String[] result1 = new String[9];
				JSONObject obj = null;
				String collectYn = null;

				httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					System.out.println("ContentInternship返回信息:"+result);
					try {
						obj = new JSONObject(
								new JSONObject(result).getString("result"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				httpPost1
						.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse1 = new DefaultHttpClient().execute(httpPost1);
				System.out.println(httpResponse1.getStatusLine()
						.getStatusCode());
				if (httpResponse1.getStatusLine().getStatusCode() == 200) {
					String result2 = EntityUtils.toString(httpResponse1
							.getEntity());
					System.out.println("返回结果：" + result2);
					try {
						JSONObject obj2 = new JSONObject(result2);
						collectYn = obj2.getString("result");
						System.out.println("collectYn:" + collectYn);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				try {
					result1[0] = obj.getString("name");
					result1[1] = obj.getString("publishTime");
					result1[2] = obj.getString("deadline");
					result1[3] = obj.getString("position");
					result1[4] = obj.getString("pay");
					result1[5] = obj.getString("workPlace");
					result1[6] = obj.getString("intro");
					result1[7] = obj.getString("others");
					result1[8] = collectYn;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				System.out.println("result1:" + result1);
				System.out.println("result1[8]:" + result1[8]);
				return result1;

			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	class Yibu2 extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpPost httpPost = new HttpPost(URLCollect);
			HttpResponse httpResponse = null;
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			String token, md5;
			token = "userName=" + userName + "&userPass=" + userPass + "token";
			md5 = Md5.Md5Str(token);
			param.add(new BasicNameValuePair("userName", userName));
			param.add(new BasicNameValuePair("userPass", userPass));
			param.add(new BasicNameValuePair("md5", md5));
			param.add(new BasicNameValuePair("messageId", id));
			SharedPreferences sp = getSharedPreferences("datas", MODE_PRIVATE);
			param.add(new BasicNameValuePair("userId", sp.getString("userName",
					"")));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					String result1 = null;
					try {
						JSONObject json = new JSONObject(result);
						result1 = json.getString("result");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return result1;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
	}

	class Yibu3 extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpPost httpPost = new HttpPost(URLCollectDelete);
			HttpResponse httpResponse = null;
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			String token, md5;
			token = "userName=" + userName + "&userPass=" + userPass + "token";
			md5 = Md5.Md5Str(token);
			param.add(new BasicNameValuePair("userName", userName));
			param.add(new BasicNameValuePair("userPass", userPass));
			param.add(new BasicNameValuePair("md5", md5));
			param.add(new BasicNameValuePair("messageId", id));
			SharedPreferences sp = getSharedPreferences("datas", MODE_PRIVATE);
			param.add(new BasicNameValuePair("userId", sp.getString("userName",
					"")));

			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse
							.getEntity());
					String result1 = null;
					try {
						JSONObject json = new JSONObject(result);
						result1 = json.getString("result");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return result1;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
			return null;
		}
	}
}
