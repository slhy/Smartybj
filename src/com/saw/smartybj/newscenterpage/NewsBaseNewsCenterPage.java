package com.saw.smartybj.newscenterpage;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.saw.smartybj.MainActivity;

/**
 * @author Administrator
 * @创建时间 2016-6-18 下午9:12:19
 * @描述 新闻
 */
public class NewsBaseNewsCenterPage extends BaseNewsCenterPage {

	public NewsBaseNewsCenterPage(MainActivity mainActivity) {
		super(mainActivity);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View initView() {
		//要显示的内容
		TextView tv = new TextView(mainActivity);
		tv.setText("新闻的内容");
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

}
