package com.saw.smartybj.view;

import com.saw.smartybj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
	private float downY = -1;
	private final int PULL_DOWN = 1;//下拉刷新状态
	private final int REFRESH_STATE = 2;//松开刷新状态
	private final int REFRESHING = 3;//正在刷新
	private int currentState = PULL_DOWN;//当前的状态
	private View lunbotu;//轮播图的组件
	private int listViewOnScreenY;//listview在屏幕中y轴坐标位置
	private TextView tv_state;//刷新状态的文字描述
	private TextView tv_time;//最新的刷新时间
	private ImageView iv_arrow;//下拉刷新的箭头
	private ProgressBar pb_loading;//下拉刷新的进度条
	private RotateAnimation up_ra;////向上动画
	private RotateAnimation down_ra;//向下动画
	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
		initAnimation();
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
	 * 覆盖此方法，完成自己事件的处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//需要我的功能，屏蔽掉父类的touch事件（return true）
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN://按下
			downY = ev.getY();//按下时y轴的坐标
			break;
		case MotionEvent.ACTION_MOVE://移动
			//判断轮播图是否完全显示
			if (! isLunboFullShow()) {
				break;
			}
			if (downY == -1) {
				downY = ev.getY();
			}
			//获取移动位置的坐标
			float moveY = ev.getY();
			//移动位置的间距
			float dy = moveY - downY;
			//下拉拖动（当listview显示第一条数据）处理自己的事件，不让listview原生的拖动事件生效
			if (dy > 0 && getFirstVisiblePosition() == 0) {
				//当前padding top的参数值
				float scrollYDis = - ll_refresh_head_root_Height + dy; 
				if (scrollYDis < 0 && currentState != PULL_DOWN) {
					//刷新头没有完全显示
					//下拉刷新状态
					currentState = PULL_DOWN;//目的只执行一次
					refreshState();
				} else if (scrollYDis >= 0 && currentState != REFRESH_STATE) {
					//松开刷新的状态
					currentState = REFRESH_STATE;//记录松开刷新的状态，只进一次
					refreshState();
				}
				ll_refresh_head_root.setPadding(0, (int) scrollYDis, 0, 0);
				return true;
			}
			break;
		case MotionEvent.ACTION_UP://松开
			downY = -1;
			ll_refresh_head_root.setPadding(0, -ll_refresh_head_root_Height, 0, 0);
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	private void initAnimation() {
		//向上动画
		up_ra = new RotateAnimation(0, -180, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		up_ra.setDuration(500);
		up_ra.setFillAfter(true);//停留在动画结束的状态
		
		//向下动画
		down_ra = new RotateAnimation(-180, -360, 
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		down_ra.setDuration(500);
		down_ra.setFillAfter(true);//停留在动画结束的状态
		
	}
	private void refreshState() {
		switch (currentState) {
		case PULL_DOWN://下拉刷新
			//改变文字
			tv_state.setText("下拉刷新");
			iv_arrow.startAnimation(down_ra);
			break;
		case REFRESH_STATE://松开刷新
			tv_state.setText("松开刷新");
			iv_arrow.startAnimation(up_ra);
			break;

		default:
			break;
		}
	}

	/**
	 * 判断轮播图是否完全显示
	 * @return
	 */
	private boolean isLunboFullShow() {
		int[] location = new int[2];
		//如果轮播图没有完全显示，响应的是ListView的事件
		//判断轮播图是否完全显示
		//取出listview在屏幕中的坐标和轮播图在屏幕中的坐标  进行判断
		//先取listview在屏幕中的坐标
		if (listViewOnScreenY == 0) {
			this.getLocationOnScreen(location);
			//获取listview在屏幕中的y轴坐标
			listViewOnScreenY = location[1];
		}
		////轮播图在屏幕中的坐标
		lunbotu.getLocationOnScreen(location);
		//判断
		if (location[1] < listViewOnScreenY) {
			//轮播图没有完全显示
			//继续响应listview的事件
			return false;
		}
		return true;
	}

	/**
	 * 轮播图view
	 * @param view
	 */
	public void addLunBoView(View view) {
		//轮播图的组件
		lunbotu = view;
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
		//刷新状态的文字描述
		tv_state = (TextView) head.findViewById(R.id.tv_listview_head_state_desc);
		//最新的刷新时间
		tv_time = (TextView) head.findViewById(R.id.tv_listview_head_refresh_time);
		//下拉刷新的箭头
		iv_arrow = (ImageView) head.findViewById(R.id.iv_listview_head_arrow);
		//下拉刷新的进度条
		pb_loading = (ProgressBar) head.findViewById(R.id.pb_listview_head_loading);
		//隐藏刷新头的根布局,轮播图还要显示
		//获取刷新头组件的高度
		ll_refresh_head_root.measure(0, 0);
		//获取测量的高度
		ll_refresh_head_root_Height = ll_refresh_head_root.getMeasuredHeight();
		ll_refresh_head_root.setPadding(0, -ll_refresh_head_root_Height, 0, 0);
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
