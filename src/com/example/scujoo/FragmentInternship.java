package com.example.scujoo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.scujoo.adapter.AdapterInternship;
import com.scujoo.datas.DatasDemand;
import com.scujoo.datas.DatasInternship;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.HttpUtils;
import com.scujoo.utils.Md5;

public class FragmentInternship extends Fragment {
	
	private ListView listViewInternship;
	private List<DatasInternship> listInternship;
	private AdapterInternship adapterInternship;
	
	private String URL = StaticDatas.URL + "scujoo/internship.php";
	
	private Handler handlerInternship = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String jsonData = (String) msg.obj;
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
					listInternship.add(new DatasInternship(id,name,publishTime, position));
				}
				adapterInternship.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_internship, container, false);
		
		SharedPreferences sp = getActivity().getSharedPreferences("test", Activity.MODE_PRIVATE); 
		String userName = sp.getString("userName", "");
		String userPass = sp.getString("userPass", "");
		String token;
		token = "userName=" + userName + "&userPass="+ userPass + "token";
		String md5 = Md5.Md5Str(token);
		
		listViewInternship = (ListView) rootView.findViewById(R.id.fragment_internship_listView);
		listInternship = new ArrayList<DatasInternship>();
		adapterInternship = new AdapterInternship(rootView.getContext(), listInternship);

		listViewInternship.setAdapter(adapterInternship);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("userPass", userPass));
		params.add(new BasicNameValuePair("md5", md5));
		System.out.println("传入的数据："+userName+"--"+userPass+"--"+md5);
		HttpUtils.getJson(URL, handlerInternship, params); 
		
		listViewInternship.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DatasInternship datasInternship = listInternship.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ContentInternship.class);
				intent.putExtra("id", datasInternship.getId());
				startActivity(intent);
			}
		});
		
		return rootView;
	}

}
