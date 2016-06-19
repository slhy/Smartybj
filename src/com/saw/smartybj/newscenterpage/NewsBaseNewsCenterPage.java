package com.saw.smartybj.newscenterpage;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.saw.smartybj.MainActivity;
import com.saw.smartybj.R;
import com.saw.smartybj.domain.NewsCenterData;
import com.saw.smartybj.domain.NewsCenterData.NewsData.ViewTagData;
import com.viewpagerindicator.TabPageIndicator;

/**
 * @author Administrator
 * @创建时间 2016-6-18 下午9:12:19
 * @描述 新闻
 */
public class NewsBaseNewsCenterPage extends BaseNewsCenterPage {
	@ViewInject(R.id.newscenter_vp)
	private ViewPager vp_newscenter;
	
	
	@ViewInject(R.id.newscenter_tpi)
	private TabPageIndicator tpi_newscenter;
	
	@OnClick(R.id.newscenter_ib_nextpage)
	public void next(View v) {
		//切换到下一页面
		vp_newscenter.setCurrentItem(vp_newscenter.getCurrentItem() + 1);
	}
	
	private List<ViewTagData> viewTagDatas = new ArrayList<NewsCenterData.NewsData.ViewTagData>();//新闻中心头部页签的数据
	public NewsBaseNewsCenterPage(MainActivity mainActivity, List<ViewTagData> children) {
		super(mainActivity);
		this.viewTagDatas = children;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initEvent() {
		//添加自己的事件
		//给ViewPager添加页面切换的监听事件，当页面位于第一个可以滑动出左侧菜单，否则不滑动
		tpi_newscenter.setOnPageChangeListener(new OnPageChangeListener() {
			
			/**
			 * 监听页面停留的位置
			 */
			@Override
			public void onPageSelected(int position) {
				//当页面位于第一个可以滑动出左侧菜单
				if (position == 0) {
					mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
				} else {
					mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				}
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		super.initEvent();
	}
	@Override
	public View initView() {
		//要显示的内容
		View newsCenterRoot = View.inflate(mainActivity, R.layout.newscenterpage_content, null);
		
		//xutil注入组件
		ViewUtils.inject(this, newsCenterRoot);
		return newsCenterRoot;
	}
	
	@Override
	public void initData() {
		//设置数据
		MyAdapter adapter = new MyAdapter();
		//设置ViewPager的适配器
		vp_newscenter.setAdapter(adapter);
		//把ViewPager和Tabpagerindicator关联
		tpi_newscenter.setViewPager(vp_newscenter);
		super.initData();
	}
	/**
	 * 页签对应ViewPage的适配器
	 * @author Administrator
	 * @创建时间 2016-6-19 上午10:46:51
	 * @描述 TODO
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return viewTagDatas.size();
		}
		
		
		/**
		 * 页签显示数据调用该方法
		 */
		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			//获取页签的数据
			return viewTagDatas.get(position).title;
		}



		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			TextView tv = new TextView(mainActivity);
			tv.setText(viewTagDatas.get(position).title);
			tv.setTextSize(25);
			tv.setGravity(Gravity.CENTER);
			container.addView(tv);
			return tv;
			
		}
		
	}

}
