package com.saw.smartybj.basepage;

import com.saw.smartybj.MainActivity;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-15 下午10:26:38
 * @描述 TODO
 */
public class SettingCenterBaseTagPager extends BaseTagPage {

	public SettingCenterBaseTagPager(MainActivity mainActivity) {
		super(mainActivity);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initData() {
		//屏蔽菜单按钮
		ib_menu.setVisibility(View.GONE);
		//设置本page的标题
		tv_title.setText("设置中心");
		//要展示的内容，替换掉白纸 fl_content;//内容  FrameLayout
		TextView tv = new TextView(mainActivity);
		tv.setText("设置中心的内容");
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		//添加到白纸中
		fl_content.addView(tv);
		super.initData();
	}

}
