package com.saw.smartybj.basepage;

import com.saw.smartybj.MainActivity;
import com.saw.smartybj.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-15 下午10:15:55
 * @描述 TODO
 */
public class BaseTagPage {
	protected MainActivity mainActivity;
	protected View root;
	protected ImageButton ib_menu;//按钮ImageButton
	protected TextView tv_title;//标题TextView
	protected FrameLayout fl_content;//内容  FrameLayout
	public BaseTagPage(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		initView();//初始化布局
		initData();//初始化数据
		initEvent();//初始化事件
	}


	public void initView() {
		//界面的根布局
		root = View.inflate(mainActivity, R.layout.fragment_content_base_content, null);
		
		ib_menu = (ImageButton) root.findViewById(R.id.ib_base_content_menu);
		
		tv_title = (TextView) root.findViewById(R.id.tv_base_content_title);
		
		fl_content = (FrameLayout) root.findViewById(R.id.fl_base_content_tag);
	}
	public void initData() {
		
	}
	public void initEvent() {
		//给头部的菜单按钮添加点击事件
		ib_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//打开或关闭左侧菜单
				mainActivity.getSlidingMenu().toggle();//左侧菜单的开关
				
			}
		});
	}
	public View getRoot() {
		return root;
	}
}
