package com.saw.smartybj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Administrator
 * @创建时间 2016-6-23 下午10:48:23
 * @描述 父控件不拦截的ViewPager(自己来处理touch事件)
 */
public class InterceptScrollViewPager extends ViewPager {

	private float downX;
	private float downY;
	private float moveX;
	private float moveY;

	public InterceptScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public InterceptScrollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//true 申请父控件不拦截我的touch事件，false默认父控件拦截事件
		//getParent().requestDisallowInterceptTouchEvent(true);
		//事件完全由自己处理
		//如果在第一个页面，并且是从左往右滑动，让父控件拦截我
		//如果在最后一个页面，并且是从右往左滑动，父控件拦截
		//否则都不让父控件拦截
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN://按下
			getParent().requestDisallowInterceptTouchEvent(true);
			//记录按钮的位置坐标
			downX = ev.getX();
			downY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE://按下
			//记录移动的位置
			moveX = ev.getX();
			moveY = ev.getY();
			float dx = moveX - downX;
			float dy = moveY - downY;
			//横向移动
			if (Math.abs(dx) > Math.abs(dy)) {
				//如果在第一个页面，并且是从左往右滑动，让父控件拦截我
				if (getCurrentItem() == 0 && dx > 0) {
					getParent().requestDisallowInterceptTouchEvent(false);
				} else if (getCurrentItem() == getAdapter().getCount() - 1 && dx < 0) {
					//如果在最后一个页面，并且是从右往左滑动，父控件拦截
					getParent().requestDisallowInterceptTouchEvent(false);
				} else {
					getParent().requestDisallowInterceptTouchEvent(true);
				}
			} else {
				//让父控件拦截
				getParent().requestDisallowInterceptTouchEvent(false);
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
