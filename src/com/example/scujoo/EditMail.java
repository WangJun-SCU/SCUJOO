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

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.Md5;

public class EditMail extends Activity implements OnClickListener {

	private ImageButton back;
	private Button save;
	private EditText content;
	private String mailContnet;

	private String URL = StaticDatas.URL + "scujoo/edit_mail.php";
	private String md5 = "";
	private String userName;
	private String userPass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_mail);

		SharedPreferences sp = getSharedPreferences("datas",
				Activity.MODE_PRIVATE);

		userName = sp.getString("userName", "");
		userPass = sp.getString("userPass", "");

		// 设置输入法默认打开
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		init();
		initListener();
	}

	private void initListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(this);
		save.setOnClickListener(this);
	}

	private void init() {
		// TODO Auto-generated method stub
		back = (ImageButton) findViewById(R.id.edit_mail_back);
		save = (Button) findViewById(R.id.edit_mail_save);
		content = (EditText) findViewById(R.id.edit_mail_content);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.edit_mail_back:
			finish();
			break;
		case R.id.edit_mail_save:
			mailContnet = content.getText().toString();
			System.out.println(mailContnet);
			Yibu yibu = new Yibu();
			yibu.execute();
			break;
		}
	}

	class Yibu extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			HttpPost httpPost = new HttpPost(URL);
			HttpResponse httpResponse = null;
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			String token;
			token = "userName=" + userName+ "&userPass="
					+ userPass + "token";
			md5 = Md5.Md5Str(token);
			param.add(new BasicNameValuePair("userName", userName));
			param.add(new BasicNameValuePair("userPass", userPass));
			param.add(new BasicNameValuePair("md5", md5));
			param.add(new BasicNameValuePair("mail", content.getText().toString()));
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
				httpResponse = new DefaultHttpClient().execute(httpPost);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					String result = EntityUtils.toString(httpResponse.getEntity());
					return result;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "11";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "22";
			}
			return "33";
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result.equals("200"))
			{
				Toast.makeText(getApplicationContext(), "修改成功", 1).show();
				SharedPreferences sp = getSharedPreferences("datas", Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("mail", content.getText().toString());
				editor.commit();
				finish();
			}
		}

	}
}
