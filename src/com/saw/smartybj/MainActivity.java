package com.saw.smartybj;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		initView();
	}

	private void initView() {
		//设置主界面
		setContentView(R.layout.fragment_content);
		
		//设置左侧菜单界面
		setBehindContentView(R.layout.fragment_left);
		
		//设置滑动模式
		SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);//设置左侧可以滑动
		//设置滑动位置
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏
		//设置主界面左侧滑动后剩余的空间位置
		sm.setBehindOffset(250);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
