package com.saw.smartybj.view;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-14 下午11:38:21
 * @描述 主界面的fragment
 */
public class MainContentFragment extends BaseFragment {

	@Override
	public View initView() {
		TextView tv = new TextView(mainActivity);
		tv.setText("主界面菜单");
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

}
