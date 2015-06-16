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
	private String userName;
	private String userPass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_demand);
		
		init();
		
		Yibu yibu = new Yibu();
		String[] result2 = new String[8]; 
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
		
		SharedPreferences sp = getSharedPreferences("datas", Activity.MODE_PRIVATE);
		userName = sp.getString("userName", "");
		userPass = sp.getString("userPass", "");
	}
	
class Yibu extends AsyncTask<String, String, String[]>{

	
	@Override
	protected String[] doInBackground(String... params) {
		HttpPost httpPost = new HttpPost(URL);
		HttpResponse httpResponse = null;
		
		List<NameValuePair> param = new ArrayList<NameValuePair>();
		String token,md5;
		token = "userName=" + userName+ "&userPass="+ userPass + "token";
		md5 = Md5.Md5Str(token);
		param.add(new BasicNameValuePair("userName", userName));
		param.add(new BasicNameValuePair("userPass", userPass));
		param.add(new BasicNameValuePair("md5",md5));
		param.add(new BasicNameValuePair("id",id));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
			httpResponse = new DefaultHttpClient().execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				String[] result1 = new String[8];
				System.out.println("result------"+result);
				try {
					
					JSONObject obj = new JSONObject(new JSONObject(result).getString("result"));
					
					result1[0] = obj.getString("name");
					result1[1] = obj.getString("publishTime");
					result1[2] = obj.getString("deadline");
					result1[3] = obj.getString("position");
					result1[4] = obj.getString("pay");
					result1[5] = obj.getString("workPlace");
					result1[6] = obj.getString("intro");
					result1[7] = obj.getString("others");
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
