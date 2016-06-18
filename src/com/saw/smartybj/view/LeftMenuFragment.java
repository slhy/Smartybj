package com.saw.smartybj.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.saw.smartybj.R;
import com.saw.smartybj.domain.NewsCenterData;
import com.saw.smartybj.domain.NewsCenterData.NewsData;
import com.saw.smartybj.utils.DensityUtil;

/**
 * @author Administrator
 * @创建时间 2016-6-14 下午11:37:27
 * @描述 左侧菜单的fragment
 */
public class LeftMenuFragment extends BaseFragment {
	public interface OnSwitchPageListener {
		void switchPage(int selectionIndex);
	}
	private OnSwitchPageListener switchListener;
	/**
	 * 设置监听回调接口
	 * @param listener
	 */
	public void setOnSwitchPageListener(OnSwitchPageListener listener) {
		this.switchListener = listener;
	}
	private List<NewsData> data = new ArrayList<NewsCenterData.NewsData>();//新闻中心左侧菜单的数据
	private ListView lv_leftData;
	private MyAdapter adapter;
	private int selectPosition;//选中的位置
	
	@Override
	public void initEvent() {
		lv_leftData.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//保存选中的位置
				selectPosition = position;
				
				//更新界面
				adapter.notifyDataSetChanged();
				
				//控制新闻中心，四个新闻页面的显示(两种方法，如下1，2)
				//方法1
				//mainActivity.getMainContentFragment().leftMenuClickSwitchPage(selectPosition);
				//方法2(接口回调方式)
				if (switchListener != null) {
					switchListener.switchPage(selectPosition);
				} else {
					mainActivity.getMainContentFragment().leftMenuClickSwitchPage(selectPosition);
				}
				
				//切换SlidingMenu的开关
				mainActivity.getSlidingMenu().toggle();
			}
		});
		super.initEvent();
	}
	@Override
	public View initView() {
		//listView显示左侧菜单
		lv_leftData = new ListView(mainActivity);
		//背景是黑色
		lv_leftData.setBackgroundColor(Color.BLACK);
		
		//选中拖动的背景色 设置成透明
		lv_leftData.setCacheColorHint(Color.TRANSPARENT);
		//设置选中时为透明背景
		lv_leftData.setSelector(new ColorDrawable(Color.TRANSPARENT));
		//设置没有分割线
		lv_leftData.setDividerHeight(0);
		//设置距离顶部的距离45px
		int dp = 45;
		lv_leftData.setPadding(0, DensityUtil.dip2px(mainActivity, dp), 0, 0);
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
			//判断是否被选中
			tv_currentView.setEnabled(position == selectPosition);
			return tv_currentView;
		}
		
	}

}
