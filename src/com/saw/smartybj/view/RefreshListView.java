package com.saw.smartybj.view;

import com.saw.smartybj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * @author Administrator
 * @创建时间 2016-6-27 下午9:51:56
 * @描述 自定义刷新头和加载数据尾的ListView
 */
public class RefreshListView extends ListView {

	private View foot;//listview尾部
	private LinearLayout head;//listview头部
	private LinearLayout ll_refresh_head_root;
	private int ll_refresh_head_root_Height;
	private int ll_refresh_foot_root_Height;
	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public RefreshListView(Context context) {
		this(context,null);
	}
	private void initView() {
		initHead();
		initFoot();
	}
	/**
	 * 轮播图view
	 * @param view
	 */
	public void addLunBoView(View view) {
		head.addView(view);
	}

	/**
	 * 初始化头部组件
	 */
	private void initHead() {
		//listview头部
		head = (LinearLayout) View.inflate(getContext(), R.layout.listview_head_container, null);
		//listview刷新头的根布局
		ll_refresh_head_root = (LinearLayout) head.findViewById(R.id.ll_listview_head_root);
		//隐藏刷新头的根布局,轮播图还要显示
		//获取刷新头组件的高度
		ll_refresh_head_root.measure(0, 0);
		//获取测量的高度
		ll_refresh_head_root_Height = ll_refresh_head_root.getMeasuredHeight();
		head.setPadding(0, -ll_refresh_head_root_Height, 0, 0);
		//加载到头部
		addHeaderView(head);
	}
	/**
	 * 初始化尾部组件
	 */
	private void initFoot() {
		//listview尾部
		foot = View.inflate(getContext(), R.layout.listview_refresh_foot, null);
		//测量尾部组件的高度
		foot.measure(0, 0);
		//尾部组件的高度
		ll_refresh_foot_root_Height = foot.getMeasuredHeight();
		foot.setPadding(0, -ll_refresh_foot_root_Height, 0, 0);
		
		//加载到尾部
		addFooterView(foot);
		
	}

}
