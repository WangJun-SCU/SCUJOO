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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.scujoo.adapter.AdapterRecruit;
import com.scujoo.datas.DatasRecruit;
import com.scujoo.datas.StaticDatas;
import com.scujoo.utils.HttpUtils;
import com.scujoo.utils.Md5;

public class FragmentRecruit extends Fragment {

	private ListView listViewRecruit;
	private List<DatasRecruit> listRecruit;
	private AdapterRecruit adapterRecruit;

	private String URL = StaticDatas.URL + "scujoo/recruit.php";

	private Handler handlerRecruit = new Handler() {
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
					String recruitPlace = obj.getString("recruitPlace");
					String recruitTime = obj.getString("recruitTime");
					listRecruit.add(new DatasRecruit(id, name, recruitTime,
							recruitPlace));
				}
				adapterRecruit.notifyDataSetChanged();
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_recruit, container,
				false);
		SharedPreferences sp = getActivity().getSharedPreferences("test",
				Activity.MODE_PRIVATE);
		String userName = sp.getString("userName", "");
		String userPass = sp.getString("userPass", "");
		String token;
		token = "userName=" + userName + "&userPass=" + userPass + "token";
		String md5 = Md5.Md5Str(token);

		listViewRecruit = (ListView) rootView
				.findViewById(R.id.fragment_recruit_listView);
		listRecruit = new ArrayList<DatasRecruit>();
		adapterRecruit = new AdapterRecruit(rootView.getContext(), listRecruit);

		listViewRecruit.setAdapter(adapterRecruit);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("userName", userName));
		params.add(new BasicNameValuePair("userPass", userPass));
		params.add(new BasicNameValuePair("md5", md5));
		HttpUtils.getJson(URL, handlerRecruit, params);

		listViewRecruit.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DatasRecruit datasRecruit = listRecruit.get(position);
				Intent intent = new Intent();
				intent.setClass(getActivity(), ContentRecruit.class);
				intent.putExtra("id", datasRecruit.getId());
				startActivity(intent);
			}
		});

		return rootView;
	}

}
