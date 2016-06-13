package com.saw.smartybj.activity;

import java.util.ArrayList;

import com.saw.smartybj.MainActivity;
import com.saw.smartybj.R;
import com.saw.smartybj.utils.DensityUtil;
import com.saw.smartybj.utils.MyConstants;
import com.saw.smartybj.utils.SpTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

/**
 * @author Administrator
 * @创建时间 2016-6-11 下午9:44:45
 * @描述 设置向导界面，采用Viewpager界面切换
 */
public class GuideActivity extends Activity {
	private ViewPager vp_guide;
	private LinearLayout ll_points;
	private View v_redpoint;
	private Button bt_startExt;
	private ArrayList<ImageView> guides;
	private MyAdapter adapter;
	private int disPoints;//两个指示点之间的距离

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题头
		initView();//初始化界面
		initData();//初始化数据
		initEvent();//初始化事件
	}

	private void initEvent() {
		//监听布局完成，触发结果
		v_redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			

			@Override
			public void onGlobalLayout() {
				//取消注册 界面变化而发生的回调结果
				v_redpoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				disPoints = (ll_points.getChildAt(1).getLeft() - ll_points.getChildAt(0).getLeft());
			}
		});
		//给按钮添加点击事件
		bt_startExt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				//保存设置的状态
				SpTools.setBoolean(getApplicationContext(), MyConstants.ISSETUP, true);
				//进入主界面
				Intent main = new Intent(GuideActivity.this, MainActivity.class);
				startActivity(main);//启动主界面
				//关闭自己
				finish();
			}
		});
		//给ViewPage添加页码改变事件
		vp_guide.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				//当前ViewPage显示的页码
				//如果ViewPage滑动到第三个页码(最后一个页面)，显示button
				if (position == guides.size() - 1) {
					bt_startExt.setVisibility(View.VISIBLE);
				} else {
					bt_startExt.setVisibility(View.GONE);//隐藏该Button按钮
				}
			}
			/**
			 * 页面滑动过程触发的事件
			 * @param position当前ViewPage停留的位置
			 * @param positionOffset偏移的比例值
			 * @param positionOffsetPixels偏移的像素
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
					//计算红点的左边距
					float leftMargin = disPoints * (position + positionOffset);
					//设置红点的左边距
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v_redpoint.getLayoutParams();
					layoutParams.leftMargin = Math.round(leftMargin);
					//重新设置布局
					v_redpoint.setLayoutParams(layoutParams);
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initData() {
		//ViewPage adapter list 
		//图片的数组
		int[] pics = new int[] {R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
		//定义ViewPage使用的容器
		guides = new ArrayList<ImageView>();
		//初始化容器中的数据
		for (int i = 0; i < pics.length; i++) {
			ImageView iv_temp = new ImageView(getApplicationContext());
			iv_temp.setBackgroundResource(pics[i]);
			//添加界面的数据
			guides.add(iv_temp);
			
			//给点的容器LiearLayout初始化添加灰色点
			View v_point = new View(getApplicationContext());
			v_point.setBackgroundResource(R.drawable.gray_point);
			int dip = 10;
			//设置灰色点的大小 LinearLayout.LayoutParams
			LayoutParams params = new LayoutParams(DensityUtil.dip2px(getApplicationContext(), dip),DensityUtil.dip2px(getApplicationContext(), dip));//注意，单位是px,不是dp
			if (i != 0) {
				params.leftMargin = DensityUtil.dip2px(getApplicationContext(), 5);//像素px
			}
			v_point.setLayoutParams(params);
			//添加灰色点到线性布局中
			ll_points.addView(v_point);
		}
		//创建ViewPage的适配器
		adapter = new MyAdapter();
		vp_guide.setAdapter(adapter);
		
		
	}
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return guides.size();//返回数据的个数
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);//从viewpage中移除
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//container -> viewPage
			//获取view
			View child = guides.get(position);
			//添加view
			container.addView(child);
			return child;
		}
		
	}

	private void initView() {
		setContentView(R.layout.activity_guide);
		//ViewPage组件
		vp_guide = (ViewPager) findViewById(R.id.vp_guide_pages);
		//动态加点容器
		ll_points = (LinearLayout) findViewById(R.id.ll_guide_points);
		//红点
		v_redpoint = findViewById(R.id.v_guide_redpoint);
		//开始体验按钮
		bt_startExt = (Button) findViewById(R.id.bt_guide_startext);
	}
}
