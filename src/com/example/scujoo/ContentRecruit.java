package com.example.scujoo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView.FindListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.Md5;

public class ContentRecruit extends Activity {
	
	private ImageButton back;
	private ImageButton collect;
	private TextView name;
	private TextView time;
	private TextView place;
	private TextView position;
	private TextView pay;
	private TextView workPlace;
	private TextView intro;
	private TextView others;
	private TextView hits;
	private String id;
	private String URL = StaticDatas.URL + "scujoo/content_recruit.php";
	private String URLCollect = StaticDatas.URL + "scujoo/collect_recruit.php";
	private String URLCollectDelete = StaticDatas.URL + "scujoo/collect_delete_recruit.php";
	private String URLCollectYny = StaticDatas.URL + "scujoo/collect_yn_recruit.php";
	private String userName;
	private String userPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_recruit);
		
		init();
		
		Yibu yibu = new Yibu();
		String[] result2 = new String[9]; 
		try {
			result2 = yibu.execute().get();
			name.setText(result2[0]);
			time.setText(result2[1]);
			place.setText(result2[2]);
			position.setText(result2[3]);
			pay.setText(result2[4]);
			workPlace.setText(result2[5]);
			intro.setText(result2[6]);
			others.setText(result2[7]);
			hits.setText("浏览("+result2[9]+")");
			if("200".equals(result2[8]))
			{
				collect.setImageResource(R.drawable.collect_full);
				collect.setTag("2");
			}else if("404".equals(result2[8]))
			{
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
				if("1".equals(tagC))
				{
					Yibu2 yibu2 = new Yibu2();
					String result = null;
					try {
						result = yibu2.execute().get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if("200".equals(result))
					{
						Toast.makeText(getApplicationContext(), "收藏成功", 1).show();
						collect.setImageResource(R.drawable.collect_full);
						collect.setTag("2");
					}else
					{
						Toast.makeText(getApplicationContext(), "收藏失败", 1).show();
					}
				}else if("2".equals(tagC))
				{
					Yibu3 yibu3 = new Yibu3();
					String result = null;
					try {
						result = yibu3.execute().get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					if("200".equals(result))
					{
						Toast.makeText(getApplicationContext(), "取消收藏成功", 1).show();
						collect.setImageResource(R.drawable.collect_empty);
						collect.setTag("1");
					}else
					{
						Toast.makeText(getApplicationContext(), "取消收藏失败", 1).show();
					}
				}
			}
		});
	}

	private void init() {
		back = (ImageButton) findViewById(R.id.content_recruit_back);
		name = (TextView) findViewById(R.id.content_recruit_name);
		time = (TextView) findViewById(R.id.content_recruit_time);
		place = (TextView) findViewById(R.id.content_recruit_place);
		position = (TextView) findViewById(R.id.content_recruit_position);
		pay = (TextView) findViewById(R.id.content_recruit_pay);
		workPlace = (TextView) findViewById(R.id.content_recruit_work_place);
		intro = (TextView) findViewById(R.id.content_recruit_intro);
		others = (TextView) findViewById(R.id.content_recruit_others);
		hits = (TextView) findViewById(R.id.content_recruit_hits);
		id = getIntent().getStringExtra("id");
		
		SharedPreferences sp = getSharedPreferences("datas", Activity.MODE_PRIVATE);
		userName = sp.getString("userName", "");
		userPass = sp.getString("userPass", "");
		
		collect = (ImageButton) findViewById(R.id.content_recruit_collect);
	}
	
class Yibu extends AsyncTask<String, String, String[]>{

	@Override
	protected String[] doInBackground(String... params) {
		
		HttpPost httpPost = new HttpPost(URL);
		HttpPost httpPost3 = new HttpPost(URLCollectYny);
		HttpResponse httpResponse = null;
		HttpResponse httpResponse3 = null;
		
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		String token,md5;
		token = "userName=" + userName+ "&userPass="+ userPass + "token";
		md5 = Md5.Md5Str(token);
		param.add(new BasicNameValuePair("userName", userName));
		param.add(new BasicNameValuePair("userPass", userPass));
		param.add(new BasicNameValuePair("md5",md5));
		param.add(new BasicNameValuePair("id",id));
		param.add(new BasicNameValuePair("userId", userName));
		param.add(new BasicNameValuePair("messageId", id));
		
		System.out.println(param);
		try {
			String[] result1 = new String[10]; 
			JSONObject obj = null;
			String collectYn = null;
			
			httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			System.out.println(httpResponse.getStatusLine().getStatusCode());
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("详细信息返回结果："+result);
				try {
					obj = new JSONObject(new JSONObject(result).getString("result"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			httpPost3.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			httpResponse3 = new DefaultHttpClient().execute(httpPost3);
			System.out.println(httpResponse3.getStatusLine().getStatusCode());
			if(httpResponse3.getStatusLine().getStatusCode() == 200)
			{
				String result2 = EntityUtils.toString(httpResponse3.getEntity());
				System.out.println("是否收藏返回结果："+result2);
				try {
					JSONObject obj2 = new JSONObject(result2);
					collectYn = obj2.getString("result");
					System.out.println("collectYn:"+collectYn);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			try {
				result1[0] = obj.getString("name");
				result1[1] = obj.getString("recruitTime");
				result1[2] = obj.getString("recruitPlace");
				result1[3] = obj.getString("position");
				result1[4] = obj.getString("pay");
				result1[5] = obj.getString("workPlace");
				result1[6] = obj.getString("intro");
				result1[7] = obj.getString("others");
				result1[8] = collectYn;
				result1[9] = obj.getString("hits");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			System.out.println("result1:"+result1);
			System.out.println("result1[8]:"+result1[8]);
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
class Yibu2 extends AsyncTask<String, String, String>{

	@Override
	protected String doInBackground(String... params) {
		HttpPost httpPost = new HttpPost(URLCollect);
		HttpResponse httpResponse = null;
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		String token,md5;
		token = "userName=" + userName+ "&userPass="+ userPass + "token";
		md5 = Md5.Md5Str(token);
		param.add(new BasicNameValuePair("userName", userName));
		param.add(new BasicNameValuePair("userPass", userPass));
		param.add(new BasicNameValuePair("md5",md5));
		param.add(new BasicNameValuePair("messageId",id));
		SharedPreferences sp = getSharedPreferences("datas", MODE_PRIVATE);
		param.add(new BasicNameValuePair("userId",sp.getString("userName", "")));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
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
class Yibu3 extends AsyncTask<String, String, String>{

	@Override
	protected String doInBackground(String... params) {
		HttpPost httpPost = new HttpPost(URLCollectDelete);
		HttpResponse httpResponse = null;
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		String token,md5;
		token = "userName=" + userName+ "&userPass="+ userPass + "token";
		md5 = Md5.Md5Str(token);
		param.add(new BasicNameValuePair("userName", userName));
		param.add(new BasicNameValuePair("userPass", userPass));
		param.add(new BasicNameValuePair("md5",md5));
		param.add(new BasicNameValuePair("messageId",id));
		SharedPreferences sp = getSharedPreferences("datas", MODE_PRIVATE);
		param.add(new BasicNameValuePair("userId",sp.getString("userName", "")));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
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
