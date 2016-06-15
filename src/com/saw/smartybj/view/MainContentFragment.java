package com.saw.smartybj.view;

import java.util.ArrayList;
import java.util.List;

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
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-14 下午11:38:21
 * @描述 主界面的fragment
 */
public class MainContentFragment extends BaseFragment {
	
	@ViewInject(R.id.vp_main_content_pages)
	private ViewPager viewPager;
	
	@ViewInject(R.id.rg_content_radios)
	private RadioButton rb_radio;
	
	private List<BaseTagPage> pages = new ArrayList<BaseTagPage>();
	
	
	
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
