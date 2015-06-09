package com.example.scujoo;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.scujoo.adapter.AdapterDemand;
import com.scujoo.adapter.AdapterInternship;
import com.scujoo.datas.DatasDemand;
import com.scujoo.datas.DatasInternship;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.HttpUtils;
import com.scujoo.utils.Md5;

public class FragmentDemand extends Fragment {
	
	private ListView listViewDemand;
	private List<DatasDemand> listDemand;
	private AdapterDemand adapterDemand;
	
	private String URL = StaticDatas.URL + "scujoo/demand.php";
	
	private Handler handlerDemand= new Handler() {
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
					listDemand.add(new DatasDemand(id,name,publishTime, position));
				}
				adapterDemand.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_demand, container, false);
		
		SharedPreferences sp = getActivity().getSharedPreferences("test", Activity.MODE_PRIVATE); 
		String userName = sp.getString("userName", "");
		String userPass = sp.getString("userPass", "");
		String token;
		token = "userName=" + userName + "&userPass="+ userPass + "token";
		String md5 = Md5.Md5Str(token);
		
		listViewDemand = (ListView) rootView.findViewById(R.id.fragment_demand_listView);
		listDemand = new ArrayList<DatasDemand>();
		adapterDemand = new AdapterDemand(rootView.getContext(), listDemand);

		listViewDemand.setAdapter(adapterDemand);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("userPass", userPass));
		params.add(new BasicNameValuePair("md5", md5));
		System.out.println("传入的数据："+userName+"--"+userPass+"--"+md5);
		HttpUtils.getJson(URL, handlerDemand, params); 
		
		return rootView;
	}

}
