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
import com.scujoo.datas.DatasRecruit;

public class AdapterDemand extends BaseAdapter {
	
	private Context context;
	private List<DatasDemand> listDemand;
	private DatasDemand datasDemand;
	
	public AdapterDemand(Context context,List<DatasDemand> listDemand)
	{
		this.context = context;
		this.listDemand = listDemand;
	}

	public int getCount() {
		return listDemand.size();
	}

	public Object getItem(int position) {
		return listDemand.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_demand, null);
		}
		
		TextView name = (TextView) convertView.findViewById(R.id.list_demand_name);
		TextView publishTime = (TextView) convertView.findViewById(R.id.list_demand_publishTime);
		TextView positionDemand = (TextView) convertView.findViewById(R.id.list_demand_position);

		datasDemand = listDemand.get(position);
		
		name.setText(datasDemand.getName());
		publishTime.setText(datasDemand.getPublishTime());
		positionDemand.setText(datasDemand.getPosition());
		
		return convertView;
	}

}
