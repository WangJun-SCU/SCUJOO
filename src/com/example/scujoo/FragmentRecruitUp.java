package com.example.scujoo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

public class FragmentRecruitUp extends Fragment {
	private ListView listView;
	private LinearLayout llOutRecruit;
	private LinearLayout topCalendar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_recruit_table,
				container, false);
		topCalendar = (LinearLayout) getActivity().findViewById(R.id.id_top_calendar);
		String[] s = { "阿里巴巴", "百度", "腾讯" };
		listView = (ListView) rootView
				.findViewById(R.id.fragment_recruit_table_listview);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, s);
		listView.setAdapter(adapter);
		
		// 根据listView的高度设置外围LinerLayout的高度
		llOutRecruit = (LinearLayout) rootView.findViewById(R.id.fragment_recruit_table_ll);
		ViewGroup.LayoutParams params = llOutRecruit.getLayoutParams();
		params.height = setListViewHeightBasedOnChildren(listView);
		llOutRecruit.setLayoutParams(params);
		
		return rootView;
	}

	// 动态获取ListView的高度
	public static int setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return 0;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			// listItem.measure(0, 0);
			listItem.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		return params.height;
		// listView.setLayoutParams(params);
	}
}
