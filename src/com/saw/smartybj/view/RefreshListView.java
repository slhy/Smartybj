package com.saw.smartybj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.saw.smartybj.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
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
	private OnRefreshDataListener listener;//刷新数据的监听回调
	private boolean isEnablePullRefresh;//下拉刷新是否可用
	private boolean isLoadingMore;//是否是加载更多数据的操作
	public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
		initAnimation();
		initEvent();
	}

	private void initEvent() {
		//添加当前listview的滑动事件
		setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				//状态停止，如果listview显示最后一条数据，加载更多数据的显示
				//是否最后一条数据显示
				if (getLastVisiblePosition() == getAdapter().getCount() - 1 && ! isLoadingMore) {
					//最后一条数据，显示加载更多的组件
					foot.setPadding(0, 0, 0, 0);//显示加载更多数据
					setSelection(getAdapter().getCount());
					//加载更多数据
					isLoadingMore = true;
					if (listener != null) {
						listener.loadingMore();//实现该接口的组件去完成数据的加载
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
			//没有启用下拉刷新，后面的代码没必要执行
			if (! isEnablePullRefresh) {
				break;
			}
			//现在是否处于正在刷新数据的状态
			if (currentState == REFRESHING) {
				break;
			}
			
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
			//如果是PULL_DOWN状态，松开恢复原状
			if (currentState == PULL_DOWN) {
				ll_refresh_head_root.setPadding(0, -ll_refresh_head_root_Height, 0, 0);
			} else if(currentState == REFRESH_STATE) {
				//刷新数据
				ll_refresh_head_root.setPadding(0, 0, 0, 0);
				currentState = REFRESHING;//改变状态为正在刷新数据的状态
				refreshState();//刷新界面
				//真的刷新数据
				if (listener != null) {
					listener.refreshData();
				}
			}
			
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}
	public void setOnRefreshDataListener(OnRefreshDataListener listener) {
		this.listener = listener;
	}
	public interface OnRefreshDataListener {
		void refreshData();
		void loadingMore();
	}
	/**
	 * 初始化动画
	 */
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
	/**
	 * 刷新界面
	 */
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
		case REFRESHING://正在刷新状态
			iv_arrow.clearAnimation();//清除所有动画
			iv_arrow.setVisibility(View.GONE);//隐藏箭头
			pb_loading.setVisibility(View.VISIBLE);//显示进度条
			tv_state.setText("正在刷新数据……");
			break;
		default:
			break;
		}
	}
	/**
	 * 刷新数据成功，处理结果
	 */
	public void refreshStateFinish() {
		
		if (isLoadingMore) {
			//上拉加载更多
			isLoadingMore = false;
			//隐藏加载更多数据的组件
			foot.setPadding(0, -ll_refresh_foot_root_Height, 0, 0);
			
		} else {
			//下拉刷新
			//改变文字
			tv_state.setText("下拉刷新");
			iv_arrow.setVisibility(View.VISIBLE);//显示箭头
			pb_loading.setVisibility(View.GONE);//隐藏进度条
			//设置刷新时间为当前时间
			tv_time.setText(getCurrentFormatDate());
			//隐藏刷新的头布局
			ll_refresh_head_root.setPadding(0, -ll_refresh_head_root_Height, 0, 0);
			currentState = PULL_DOWN;//初始化为下拉刷新的状态
		}
	}
	private String getCurrentFormatDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
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
	 * 用户自己选择是否启用下拉刷新头的功能
	 * @param isPullrefresh true启用下拉刷新 false不启用 默认false
	 */
	public void setIsRefreshHead(boolean isPullrefresh) {
		isEnablePullRefresh = isPullrefresh;
	}
	/**
	 * 轮播图view
	 * @param view
	 */
	public void addHeaderView(View view) {
		//判断 如果你使用下拉刷新，把头布局加下拉刷新的容器中，否则加载原生Listview中
		if (isEnablePullRefresh) {
			//启用下拉刷新
			//轮播图的组件
			lunbotu = view;
			head.addView(view);
		} else {
			//使用原生的Listview
			super.addHeaderView(view);
		}
		
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
		tv_time.setText(getCurrentFormatDate());
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
