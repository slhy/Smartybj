package com.saw.smartybj.newstpipage;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap.Config;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.saw.smartybj.MainActivity;
import com.saw.smartybj.R;
import com.saw.smartybj.domain.NewsCenterData.NewsData.ViewTagData;
import com.saw.smartybj.domain.TPINewsData;
import com.saw.smartybj.domain.TPINewsData.TPINewsData_Data.TPINewsData_Data_LunboData;
import com.saw.smartybj.utils.MyConstants;
import com.saw.smartybj.utils.SpTools;

/**
 * @author Administrator
 * @创建时间 2016-6-19 下午9:31:14
 * @描述 新闻中心 页签对应的页面
 */
public class TPINewsNewsCenterPager {
	
	//所有组件
	@ViewInject(R.id.vp_tpi_news_lunbopic)
	private ViewPager vp_lunbo;//轮播图显示组件
	
	@ViewInject(R.id.tv_tpi_news_desc)
	private TextView tv_pic_desc;//图片的描述信息
	
	@ViewInject(R.id.ll_tpi_news_pic_points)
	private LinearLayout ll_points;//轮播图的每张图片对应的点
	
	//数据
	private MainActivity mainActivity;
	private View root;
	
	private ViewTagData viewTagData;//页签对应的数据

	private Gson gson;

	private TPINewsData newsData;
	//轮播图的适配器
	private List<TPINewsData_Data_LunboData> lunboDatas  = new ArrayList<TPINewsData.TPINewsData_Data.TPINewsData_Data_LunboData>();
	//轮播图的适配器
	private LunBoAdapter lunboAdapter;
	
	private BitmapUtils bitmapUtils;

	public TPINewsNewsCenterPager(MainActivity mainActivity, ViewTagData viewTagData) {
		this.mainActivity = mainActivity;
		this.viewTagData = viewTagData;
		//xutils bitmap组件
		bitmapUtils = new BitmapUtils(mainActivity);
		bitmapUtils.configDefaultBitmapConfig(Config.ARGB_4444);//一个字节
		
		initView();//初始化界面
		initData();//初始化数据
		initEvent();//初始化事件
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		
	}

	private void initData() {
		//轮播图的适配器
		lunboAdapter = new LunBoAdapter();
		//给轮播图
		vp_lunbo.setAdapter(lunboAdapter);
		
		//轮播图的数据
		//新闻列表的数据
		//从本地获取数据
		String jsonCache = SpTools.getString(mainActivity, MyConstants.REQUEST_HOST+viewTagData.url, "");
		if (! TextUtils.isEmpty(jsonCache)) {
			//解析数据
			newsData = parseJson(jsonCache);
			//处理数据
			processData(newsData);
		}
		
		getDataFromNet();//从网络获取数据
		
	}
	
	private TPINewsData parseJson(String jsonData) {
		gson = new Gson();
		TPINewsData tpiNewsData = gson.fromJson(jsonData, TPINewsData.class);
		return tpiNewsData;
	}
	
	private void processData(TPINewsData newsData) {
		//完成数据的处理
		//1.设置轮播图的数据
		setLunBoData(newsData);
		
		
	}
	private void setLunBoData(TPINewsData newsData) {
		//获取轮播图数据
		lunboDatas = newsData.data.topnews;
		//通知界面更新
		lunboAdapter.notifyDataSetChanged();
		
	}
	/**
	 * @author Administrator
	 * @创建时间 2016-6-19 下午10:36:16
	 * @描述 轮播图的适配器
	 */
	public class LunBoAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return lunboDatas.size();
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
			ImageView iv_lunbo_pic = new ImageView(mainActivity);
			//设置默认的图片,网络缓慢
			iv_lunbo_pic.setImageResource(R.drawable.news_pic_default);
			//给图片添加数据
			TPINewsData_Data_LunboData tpiNewsData_Data_LunboData = lunboDatas.get(position);
			//图片的url
			String topimageUrl = MyConstants.REQUEST_HOST+tpiNewsData_Data_LunboData.topimage;
			//把url的图片给iv_lunbo_pic
			//异步加载图片，并且显示到组件中
			bitmapUtils.display(iv_lunbo_pic, topimageUrl);
			container.addView(iv_lunbo_pic);
			return iv_lunbo_pic;
		}
		
	}

	private void getDataFromNet() {
		//httpUtils
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, MyConstants.REQUEST_HOST+viewTagData.url,new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//请求成功
				String jsonData = responseInfo.result;
				//保存数据到本地
				SpTools.setString(mainActivity, viewTagData.url, jsonData);
				//解析数据
				newsData = parseJson(jsonData);
				//处理数据
				processData(newsData);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				
			}
		});
	}

	private void initView() {
		//获取页签对应的根布局
		root = View.inflate(mainActivity, R.layout.tpi_news_content, null);
		
		ViewUtils.inject(this, root);
	}
	public View getRoot() {
		return root;
	}
	
	
	
	
}
