package com.saw.smartybj.basepage;

import com.saw.smartybj.R;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-15 下午10:15:55
 * @描述 TODO
 */
public class BaseTagPage {
	protected Context context;
	protected View root;
	protected ImageButton ib_menu;//按钮ImageButton
	protected TextView tv_title;//标题TextView
	protected FrameLayout fl_content;//内容  FrameLayout
	public BaseTagPage(Context context) {
		this.context = context;
		initView();//初始化布局
		initData();//初始化数据
		initEvent();//初始化事件
	}


	public void initView() {
		//界面的根布局
		root = View.inflate(context, R.layout.fragment_content_base_content, null);
		
		ib_menu = (ImageButton) root.findViewById(R.id.ib_base_content_menu);
		
		tv_title = (TextView) root.findViewById(R.id.tv_base_content_title);
		
		fl_content = (FrameLayout) root.findViewById(R.id.fl_base_content_tag);
	}
	public void initData() {
		
	}
	public void initEvent() {
		
	}
	public View getRoot() {
		return root;
	}
}
