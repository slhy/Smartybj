package com.saw.smartybj.basepage;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-15 下午10:26:38
 * @描述 TODO
 */
public class HomeBaseTagPager extends BaseTagPage {

	public HomeBaseTagPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void initData() {
		tv_title.setText("首页");
		TextView tv = new TextView(context);
		tv.setText("首页的内容");
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		super.initData();
	}

}
