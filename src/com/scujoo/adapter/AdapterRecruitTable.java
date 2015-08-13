package com.scujoo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.scujoo.R;
import com.scujoo.datas.DatasRecruit;

public class AdapterRecruitTable extends BaseAdapter {
	
	private Context context;
	private DatasRecruit[] arrRecruit;
	GridView.LayoutParams params;
	public AdapterRecruitTable(Context context,DatasRecruit[] arrRecruit,GridView.LayoutParams params)
	{
		this.context = context;
		this.arrRecruit = arrRecruit;
		this.params = params;
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
		return 28;
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return arrRecruit[position];
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.list_table, null);
		}
		TextView name = (TextView) convertView.findViewById(R.id.id_list_table);
		
		convertView.setLayoutParams(params);
		
		name.setText(arrRecruit[position].getName());
		return convertView;
	}

}
