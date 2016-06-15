package com.saw.smartybj;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.saw.smartybj.view.LeftMenuFragment;
import com.saw.smartybj.view.MainContentFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.Window;

public class MainActivity extends SlidingFragmentActivity {

	private static final String LEFT_MENU_TAG = "LEFT_MENU_TAG";
	private static final String MAIN_MENU_TAG = "MAIN_MENU_TAG";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
		initView();//初始化界面
		initData();//初始化数据
	}

	private void initData() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		//获取事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		//完成左侧界面菜单的替换
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), LEFT_MENU_TAG);
		//完成主界面的替换
		transaction.replace(R.id.fl_main_menu, new MainContentFragment(), MAIN_MENU_TAG);
		//提交事务
		transaction.commit();
	}

	private void initView() {
		//设置主界面
		setContentView(R.layout.fragment_content_tag);
		
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
