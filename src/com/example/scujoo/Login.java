package com.example.scujoo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.Md5;

public class Login extends Activity {
	
	private Button login;
	private Button visitor;
	private String URL = StaticDatas.URL + "scujoo/login.php";
	private String URLVisitor = StaticDatas.URL + "scujoo/login_visitor.php";
	private EditText userName;
	private EditText userPass;
	private ProgressDialog pDialog;

	private String md5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		SharedPreferences sp = getSharedPreferences("datas", Activity.MODE_PRIVATE);
		
		String username = sp.getString("userName", "");
		String name = sp.getString("name", "");
		System.out.println("userName"+username);
		System.out.println("name"+name);
		if(!"".equals(username))
		{ 
			startActivity(new Intent().setClass(Login.this,MainActivity.class));
			finish();
		}
		
		init();
		//登录事件
		login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Boolean isE = isEmpty();
				
				if(isE)
				{
					Yibu yb = new Yibu();
					yb.execute();
				}
			}
		});
		//访客进入事件
		visitor.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Yibu2 yb = new Yibu2();
				yb.execute();
			}
		});
	}

	//初始化组件
	private void init() {
		login = (Button) findViewById(R.id.login_id);
		visitor = (Button) findViewById(R.id.login_visitor);
		userName = (EditText) findViewById(R.id.login_username);
		userPass = (EditText) findViewById(R.id.login_userpass);
	}
	
	//判断用户名和密码是否填写完成
	private boolean isEmpty()
	{
		if("".equals(userName.getText().toString()))
		{
			Toast.makeText(getApplicationContext(), "请填写学号", 1).show();
			return false;
		}
		if("".equals(userPass.getText().toString()))
		{
			Toast.makeText(getApplicationContext(), "请填写密码", 1).show();
			return false;
		}
		
		return true;
	}
	
	//异步传输，实现教务处登录
	class Yibu extends AsyncTask<String, String, String> {
		
		@Override
		 protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Login.this);
	            pDialog.setMessage("正在登录...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	        }

		@Override
		protected String doInBackground(String... params) {
			HttpPost httpPost = new HttpPost(URL);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			String token;
			token = "userName=" + userName.getText().toString() + "&userPass="+ userPass.getText().toString() + "token";
			md5 = Md5.Md5Str(token);
			param.add(new BasicNameValuePair("userName", userName.getText().toString()));
			param.add(new BasicNameValuePair("userPass", userPass.getText().toString()));
			param.add(new BasicNameValuePair("md5",md5));
			HttpResponse httpResponse = null;
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse.getEntity());
					System.out.println("result"+result);
					return result;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return "11";
			} catch (IOException e) {
				e.printStackTrace();
				return "22";
			}
			return "33";
		}
		protected void onPostExecute(String message) {
			pDialog.dismiss();
			if ("404".equals(message)) {
				Toast.makeText(getApplicationContext(), "用户名或密码错误", 1).show();
			}else if ("203".equals(message)) {
				Toast.makeText(getApplicationContext(), "MD5不一致", 1).show();
			}else{
				String name = "";
				String college = "";
				String major = "";
				String mail = "";
				String intro = "";
				try {
					JSONObject jsonObject = new JSONObject(message);
					name = jsonObject.getString("name");
					college = jsonObject.getString("college");
					major = jsonObject.getString("major");
					mail = jsonObject.getString("mail");
					intro = jsonObject.getString("intro");
					System.out.println(mail+"111"+intro);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(college!="")
				{
					//保存用户名密码
					SharedPreferences sp = getSharedPreferences("datas", Activity.MODE_PRIVATE);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("userName", userName.getText().toString());
					editor.putString("userPass", userPass.getText().toString());
					editor.putString("name", name);
					editor.putString("college", college);
					editor.putString("major", major);
					editor.putString("mail", mail);
					editor.putString("intro", intro);
					editor.commit();
					Toast.makeText(getApplicationContext(), "登录成功", 1).show();
					startActivity(new Intent().setClass(Login.this,MainActivity.class));
					finish();
				}else{
					Toast.makeText(getApplicationContext(), "没有连接网络", 1).show();
					finish();
				}
				
			}
        }
	}
	class Yibu2 extends AsyncTask<String, String, String>
	{
		@Override
		 protected void onPreExecute() {
	            super.onPreExecute();
	            pDialog = new ProgressDialog(Login.this);
	            pDialog.setMessage("正在进入...");
	            pDialog.setIndeterminate(false);
	            pDialog.setCancelable(true);
	            pDialog.show();
	            System.out.println("888");
	        }

		@Override
		protected String doInBackground(String... params) {
			String userName = "visitor";
			String userPass = "visitor";
			String token;
			token = "userName=" + userName+ "&userPass="+ userPass + "token";
			md5 = Md5.Md5Str(token);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			param.add(new BasicNameValuePair("userName", userName));
			param.add(new BasicNameValuePair("userPass", userPass));
			param.add(new BasicNameValuePair("md5", md5));
			HttpPost httpPost = new HttpPost(URLVisitor);
			HttpResponse httpResponse = null;
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param,HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if(httpResponse.getStatusLine().getStatusCode()==200)
				{
					String result = EntityUtils.toString(httpResponse.getEntity());
					System.out.println("Login-result:"+result);
					return result;
				}else{
					return null;
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			System.out.println("777");
			if(result.equals("200"))
			{
				System.out.println("111");
				SharedPreferences sp = getSharedPreferences("datas", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("userName", "visitor");
				editor.putString("userPass", "visitor");
				editor.putString("name", "访客");
				System.out.println("222");
				editor.commit();
				System.out.println("333");
				Toast.makeText(getApplicationContext(), "访问成功", 1).show();
				System.out.println("444");
				startActivity(new Intent().setClass(Login.this,MainActivity.class));
				System.out.println("555");
				finish();
				System.out.println("666");
			}else{
				Toast.makeText(Login.this, "访问异常", 1).show();
			}
		}
		
		
	}

}
