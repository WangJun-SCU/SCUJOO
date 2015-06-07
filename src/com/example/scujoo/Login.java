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

import android.app.Activity;
import android.content.Intent;
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
	private String URL = StaticDatas.URL + "scujoo/login.php";

	private EditText userName;
	private EditText userPass;

	private String md5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		init();
		
		login.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				Boolean isE = isEmpty();
				
				if(isE)
				{
					Yibu yb = new Yibu();
					String result = "default";
					try {
						result = yb.execute().get();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					if ("200".equals(result)) {
						Toast.makeText(getApplicationContext(), "µ«¬º≥…π¶", 1).show();
						startActivity(new Intent().setClass(Login.this,MainActivity.class));
						finish();
					} else if ("404".equals(result)) {
						Toast.makeText(getApplicationContext(), "”√ªß√˚ªÚ√‹¬Î¥ÌŒÛ", 1).show();
					}else if ("203".equals(result)) {
						Toast.makeText(getApplicationContext(), "MD5≤ª“ª÷¬", 1).show();
					}else{
						Toast.makeText(getApplicationContext(), result, 1).show();
					}
				}
			}
		});
	}

	private void init() {
		login = (Button) findViewById(R.id.login_id);
		userName = (EditText) findViewById(R.id.login_username);
		userPass = (EditText) findViewById(R.id.login_userpass);
	}
	
	private boolean isEmpty()
	{
		if("".equals(userName.getText().toString()))
		{
			Toast.makeText(getApplicationContext(), "«ÎÃÓ–¥—ß∫≈", 1).show();
			return false;
		}
		if("".equals(userPass.getText().toString()))
		{
			Toast.makeText(getApplicationContext(), "«ÎÃÓ–¥√‹¬Î", 1).show();
			return false;
		}
		
		return true;
	}
	
	
	class Yibu extends AsyncTask<String, String, String> {

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
			System.out.println(md5);
			System.out.println(userName.getText().toString());
			System.out.println(userPass.getText().toString());
			HttpResponse httpResponse = null;
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse.getEntity());
					System.out.println(result);
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
        }
	}

}
