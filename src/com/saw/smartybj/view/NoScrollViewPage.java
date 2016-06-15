package com.saw.smartybj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Administrator
 * @创建时间 2016-6-16 上午1:08:08
 * @描述 不能滑动
 */
public class NoScrollViewPage extends ViewPager {

	public NoScrollViewPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoScrollViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 不让自己拦截
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return false;
	}

}
