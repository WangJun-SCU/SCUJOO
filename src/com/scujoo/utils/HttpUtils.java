package com.scujoo.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class HttpUtils {

	public static void getJson(final String url, final Handler handler, final List<NameValuePair> params) {

		new Thread(new Runnable() {
			public void run() {
				InputStream is = null;
			    JSONObject jObj = null;
			    String json = "";
				 try {
		                DefaultHttpClient httpClient = new DefaultHttpClient();
		                HttpPost httpPost = new HttpPost(url);
		                httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		                HttpResponse httpResponse = httpClient.execute(httpPost);
		                HttpEntity httpEntity = httpResponse.getEntity();
		                is = httpEntity.getContent();                
		            
		        } catch (UnsupportedEncodingException e) {
		            e.printStackTrace();
		        } catch (ClientProtocolException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		        try {
		            BufferedReader reader = new BufferedReader(new InputStreamReader(
		                    is, "UTF-8"));
		            StringBuilder sb = new StringBuilder();
		            String line = null;
		            while ((line = reader.readLine()) != null) {
		                sb.append(line + "\n");
		            }
		            is.close();
		            json = sb.toString();
		        } catch (Exception e) {
		            Log.e("Buffer Error", "Error converting result " + e.toString());
		        }
		        try {
		            jObj = new JSONObject(json);
		        } catch (JSONException e) {
		            Log.e("JSON Parser", "Error parsing data " + e.toString());
		        }
		        Message msg = new Message();
				msg.obj = jObj.toString();
		        handler.sendMessage(msg);
			}
		}).start();
	}
}
