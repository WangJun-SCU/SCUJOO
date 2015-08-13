package com.scujoo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.scujoo.R;
import com.scujoo.datas.DatasDemand;
import com.scujoo.datas.DatasInternship;

public class AdapterInternship extends BaseAdapter {
	
	private Context context;
	private List<DatasInternship> listInternship;
	private DatasInternship datasInternship;
	
	public AdapterInternship(Context context,List<DatasInternship> listInternship)
	{
		this.context = context;
		this.listInternship = listInternship;
	}

	public int getCount() {
		return listInternship.size();
	}

	public Object getItem(int position) {
		return listInternship.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_internship, null);
		}
		
		TextView name = (TextView) convertView.findViewById(R.id.list_internship_name);
		TextView publishTime = (TextView) convertView.findViewById(R.id.list_internship_publishTime);
		TextView positionDemand = (TextView) convertView.findViewById(R.id.list_internship_position);

		datasInternship = listInternship.get(position);
		
		name.setText(datasInternship.getName().trim());
		publishTime.setText(datasInternship.getPublishTime().trim());
		positionDemand.setText(datasInternship.getPosition().trim());
		
		return convertView;
	}

}
