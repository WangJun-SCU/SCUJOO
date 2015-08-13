package com.scujoo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.scujoo.R;
import com.scujoo.datas.DatasRecruit;

public class AdapterRecruit extends BaseAdapter {
	
	private Context context;
	private List<DatasRecruit> listRecruit;
	private DatasRecruit datasRecruit;
	
	public AdapterRecruit(Context context,List<DatasRecruit> listRecruit)
	{
		this.context = context;
		this.listRecruit = listRecruit;
	}

	public int getCount() {
		return listRecruit.size();
	}

	public Object getItem(int position) {
		return listRecruit.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_recruit, null);
		}
		
		TextView name = (TextView) convertView.findViewById(R.id.list_recruit_name);
		TextView recruitTime = (TextView) convertView.findViewById(R.id.list_recruit_time);
		TextView recruitPlace = (TextView) convertView.findViewById(R.id.list_recruit_place);

		datasRecruit = listRecruit.get(position);
		
		name.setText(datasRecruit.getName().trim());
		recruitPlace.setText(datasRecruit.getRecruitPlace().trim());
		recruitTime.setText(datasRecruit.getRecruitTime().trim());
		
		return convertView;
	}

}
