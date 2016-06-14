package com.saw.smartybj.view;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-14 下午11:37:27
 * @描述 左侧菜单的fragment
 */
public class LeftMenuFragment extends BaseFragment {

	@Override
	public View initView() {
		TextView tv = new TextView(mainActivity);
		tv.setText("左侧菜单");
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

}
