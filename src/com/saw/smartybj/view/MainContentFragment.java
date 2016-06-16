package com.saw.smartybj.view;

import java.util.ArrayList;
import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.saw.smartybj.R;
import com.saw.smartybj.basepage.BaseTagPage;
import com.saw.smartybj.basepage.GovAffairsBaseTagPager;
import com.saw.smartybj.basepage.HomeBaseTagPager;
import com.saw.smartybj.basepage.NewsCenterBaseTagPager;
import com.saw.smartybj.basepage.SettingCenterBaseTagPager;
import com.saw.smartybj.basepage.SmartServiceBaseTagPager;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * @author Administrator
 * @创建时间 2016-6-14 下午11:38:21
 * @描述 主界面的fragment
 */
public class MainContentFragment extends BaseFragment {
	
	@ViewInject(R.id.vp_main_content_pages)
	private ViewPager viewPager;
	
	@ViewInject(R.id.rg_content_radios)
	private RadioGroup rg_radios;
	
	private int selectIndex; //设置当前选中的页面编号
	
	private List<BaseTagPage> pages = new ArrayList<BaseTagPage>();
	
	
	@Override
	public void initEvent() {
		// TODO Auto-generated method stub
		//添加自己的事件
		//单选按钮的切换事件
		rg_radios.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				
				//五个单选按钮
				switch (checkedId) {
					case R.id.rb_main_content_home://主界面
						selectIndex = 0;
						break;
					case R.id.rb_main_content_newscenter://新闻中心
						selectIndex = 1;
						break;
					case R.id.rb_main_content_smartservice://智慧服务
						selectIndex = 2;	
						break;
					case R.id.rb_main_content_govaffairs://政务
						selectIndex = 3;
						break;
					case R.id.rb_main_content_settingcenter://设置中心
						selectIndex = 4;
						break;
	
					default:
						break;
				}//end
				
				switchPage(selectIndex);
				
			}

			
		});
		super.initEvent();
	}
	/**
	 * 设置选中的页面
	 * @param selectIndex
	 */
	protected void switchPage(int selectIndex) {
		// TODO Auto-generated method stub
//		BaseTagPage currentPage = pages.get(selectIndex);
		viewPager.setCurrentItem(selectIndex);//设置ViewPage当前显示页
		
		//如果是第一个或者是最后一个不让左侧菜单活动出来（首页和设置中心）
		if (selectIndex == 0 || selectIndex == pages.size() - 1) {
			//不让左侧菜单滑动出来
			mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);//不处理滑动
		} else {
			//可以左侧菜单滑动出来
			mainActivity.getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);//全屏滑动
		}
	}
	
	@Override
	public View initView() {
		View root = View.inflate(mainActivity, R.layout.fragment_content_view, null);
		
		//xutils动态注入view
		ViewUtils.inject(this, root);
		return root;
	}
	
	@Override
	public void initData() {
		//添加首页
		pages.add(new HomeBaseTagPager(mainActivity));
		//添加新闻中心
		pages.add(new NewsCenterBaseTagPager(mainActivity));
		//添加政务
		pages.add(new GovAffairsBaseTagPager(mainActivity));
		//添加智慧服务
		pages.add(new SmartServiceBaseTagPager(mainActivity));
		//添加设置中心
		pages.add(new SettingCenterBaseTagPager(mainActivity));
		
		//设置适配器
		MyAdapter adapter = new MyAdapter();
		viewPager.setAdapter(adapter);
		
		//设置默认选择首页
		switchPage(selectIndex);
		//设置第一个按钮被选中
		rg_radios.check(R.id.rb_main_content_home);
	}
	
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return pages.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			BaseTagPage baseTagPage = pages.get(position);
			View root = baseTagPage.getRoot();
			container.addView(root);
			return root;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}
		
	}

}
