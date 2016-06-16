package com.saw.smartybj.view;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.saw.smartybj.R;
import com.saw.smartybj.domain.NewsCenterData;
import com.saw.smartybj.domain.NewsCenterData.NewsData;

/**
 * @author Administrator
 * @创建时间 2016-6-14 下午11:37:27
 * @描述 左侧菜单的fragment
 */
public class LeftMenuFragment extends BaseFragment {
	private List<NewsData> data = new ArrayList<NewsCenterData.NewsData>();//新闻中心左侧菜单的数据
	private ListView lv_leftData;
	private MyAdapter adapter;
	@Override
	public View initView() {
		//listView显示左侧菜单
		lv_leftData = new ListView(mainActivity);
		return lv_leftData;
	}
	public void setLeftMenuData(List<NewsData> data) {
		this.data = data;
		adapter.notifyDataSetChanged();//设置好数据后，通知界面刷新数据，进行显示
	}
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		//组织数据
		adapter = new MyAdapter();
		lv_leftData.setAdapter(adapter);
		super.initData();
	}
	
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			//显示数据
			TextView tv_currentView;
			if (convertView == null) {
				tv_currentView = (TextView) View.inflate(mainActivity, R.layout.leftmenu_list_item, null);
			} else {
				tv_currentView = (TextView) convertView;
			}
			//设置数据
			tv_currentView.setText(data.get(position).title);
			return tv_currentView;
		}
		
	}

}
