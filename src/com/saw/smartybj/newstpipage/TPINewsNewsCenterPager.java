package com.saw.smartybj.newstpipage;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

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
import com.saw.smartybj.activity.NewsDetailActivity;
import com.saw.smartybj.domain.NewsCenterData.NewsData.ViewTagData;
import com.saw.smartybj.domain.TPINewsData;
import com.saw.smartybj.domain.TPINewsData.TPINewsData_Data.TPINewsData_Data_ListNewsData;
import com.saw.smartybj.domain.TPINewsData.TPINewsData_Data.TPINewsData_Data_LunboData;
import com.saw.smartybj.utils.DensityUtil;
import com.saw.smartybj.utils.MyConstants;
import com.saw.smartybj.utils.SpTools;
import com.saw.smartybj.view.RefreshListView.OnRefreshDataListener;

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
	@ViewInject(R.id.lv_tpi_news_listnews)
	//private ListView lv_listnews;//listview新闻列表
	private com.saw.smartybj.view.RefreshListView lv_listnews;//listview新闻列表
	
	
	//数据
	private MainActivity mainActivity;
	private View root;
	
	private ViewTagData viewTagData;//页签对应的数据

	private Gson gson;
	
	private boolean isFresh = false;//记录是否是刷新数据的状态

	private TPINewsData newsData;
	//轮播图的适配器
	private List<TPINewsData_Data_LunboData> lunboDatas  = new ArrayList<TPINewsData.TPINewsData_Data.TPINewsData_Data_LunboData>();
	//轮播图的适配器
	private LunBoAdapter lunboAdapter;
	
	private BitmapUtils bitmapUtils;

	private int picSelectIndex;

	private Handler handler;

	private LunBoTask lunboTask;
	//新闻列表的数据
	private List<TPINewsData_Data_ListNewsData> listNews = new ArrayList<TPINewsData.TPINewsData_Data.TPINewsData_Data_ListNewsData>();

	private ListNewsAdapter listNewsAdapter;
	
	
	private String loadingMoreDatasUrl;//加载更多数据的url
	
	private String loadingDataUrl;//获取数据的url

	public TPINewsNewsCenterPager(MainActivity mainActivity, ViewTagData viewTagData) {
		this.mainActivity = mainActivity;
		this.viewTagData = viewTagData;
		gson = new Gson();
		handler = new Handler();
		lunboTask = new LunBoTask();
		//xutils bitmap组件
		bitmapUtils = new BitmapUtils(mainActivity);
		bitmapUtils.configDefaultBitmapConfig(Config.ARGB_4444);//一个字节
		
		initView();//初始化界面
		initData();//初始化数据
		initEvent();//初始化事件
	}

	private void initEvent() {
		//给新闻列表加单击事件
		lv_listnews.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				//获取点击当前的新闻链接
				TPINewsData_Data_ListNewsData tpiNewsData_Data_ListNewsData = listNews.get(position - 1);
				String newsurl = tpiNewsData_Data_ListNewsData.url;
				
				//修改读过的新闻字体颜色
				//获取新闻的标记id
				String newsid = tpiNewsData_Data_ListNewsData.id;
				//保存id sharedprefrences
				String readIDs = SpTools.getString(mainActivity, MyConstants.READNEWSIDS, null);
				if (TextUtils.isEmpty(readIDs)) {
					//第一次 没有保存过id
					readIDs = newsid;//保存当前新闻的id
				} else if (! readIDs.contains(newsid)) {
					//添加保存新闻id
					readIDs += "," + newsid;
				}
				//重新保存度过的新闻的id
				SpTools.setString(mainActivity, MyConstants.READNEWSIDS, readIDs);
				//修改读过的新闻字体颜色
				//告诉界面更新
				listNewsAdapter.notifyDataSetChanged();
				//跳转到新闻页面显示新闻
				Intent newsActivity = new Intent(mainActivity, NewsDetailActivity.class);
				newsActivity.putExtra("newsurl", MyConstants.REQUEST_HOST+newsurl);
				mainActivity.startActivity(newsActivity);
			}
		});
		
		//设置刷新数据事件
		lv_listnews.setOnRefreshDataListener(new OnRefreshDataListener() {
			/**
			 * 下拉刷新
			 */
			@Override
			public void refreshData() {
				isFresh = true;
				//刷新数据
				getDataFromNet(MyConstants.REQUEST_HOST+viewTagData.url,false);
				//改变listview的状态
			}
			/**
			 * 上拉加载更多
			 */
			@Override
			public void loadingMore() {
				//判断是否有更多的数据
				if (TextUtils.isEmpty(loadingMoreDatasUrl)) {
					Toast.makeText(mainActivity, "没有更多数据", Toast.LENGTH_SHORT).show();
					//关闭刷新数据的状态
					lv_listnews.refreshStateFinish();
				} else {
					//有数据
					getDataFromNet(loadingMoreDatasUrl,true);
				}
				
			}
		});
		//给轮播图添加页面切换事件
		vp_lunbo.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				picSelectIndex = position;
				setPicAndPointSelect(picSelectIndex);
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initData() {
		//轮播图的适配器
		lunboAdapter = new LunBoAdapter();
		//给轮播图
		vp_lunbo.setAdapter(lunboAdapter);
		
		//新闻列表适配器
		listNewsAdapter = new ListNewsAdapter();
		lv_listnews.setAdapter(listNewsAdapter);
		
		
		//轮播图的数据
		//新闻列表的数据
		//从本地获取数据
		String jsonCache = SpTools.getString(mainActivity, loadingDataUrl, "");
		if (! TextUtils.isEmpty(jsonCache)) {
			//解析数据
			newsData = parseJson(jsonCache);
			//处理数据
			processData(newsData);
		}
		loadingDataUrl = MyConstants.REQUEST_HOST+viewTagData.url;
		getDataFromNet(loadingDataUrl,false);//从网络获取数据
		
	}
	/**
	 * 解析json数据
	 * @param jsonData
	 * @return
	 */
	private TPINewsData parseJson(String jsonData) {
		TPINewsData tpiNewsData = gson.fromJson(jsonData, TPINewsData.class);
		if (! TextUtils.isEmpty(tpiNewsData.data.more)) {
			loadingMoreDatasUrl = MyConstants.REQUEST_HOST+tpiNewsData.data.more;
		} else {
			loadingMoreDatasUrl = "";
		}
		return tpiNewsData;
	}
	/**
	 * 处理数据
	 * @param newsData
	 */
	private void processData(TPINewsData newsData) {
		//完成数据的处理
		//1.设置轮播图的数据
		setLunBoData(newsData);
		//2.轮播图对应的点的处理
		initPoints();//初始化轮播图的点
		picSelectIndex = 0;
		//3.设置图片描述和点的选中选过
		setPicAndPointSelect(picSelectIndex);
		//4.开始轮播图
		lunboTask.startLunbo();
		//5.新闻列表的数据
		setListViewNews(newsData);
		
		
	}
	/**
	 * 设置新闻列表的数据
	 * @param newsData
	 */
	private void setListViewNews(TPINewsData newsData) {
		listNews = newsData.data.news;
		//更新界面
		listNewsAdapter.notifyDataSetChanged();
		
	}

	/**
	 * 轮播图的处理
	 */
	private void lunboProcess() {
		if (handler == null) {
		
			handler = new Handler();
		}
		//清空掉原来所有的任务
		handler.removeCallbacksAndMessages(null);
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//任务
				//控制轮播图的显示
				vp_lunbo.setCurrentItem((vp_lunbo.getCurrentItem() + 1) % vp_lunbo.getAdapter().getCount());
				handler.postDelayed(this, 2000);
			}
		}, 2000);
	}
	/**
	 * 轮播图的处理
	 * @author Administrator
	 * @创建时间 2016-6-25 下午10:25:43
	 * @描述 TODO
	 */
	private class LunBoTask extends Handler implements Runnable {
		public void stopLunbo() {
			//清空掉原来所有的任务
			handler.removeCallbacksAndMessages(null);
		}
		public void startLunbo() {
			stopLunbo();
			handler.postDelayed(this, 2000);
		}
		@Override
		public void run() {
			//控制轮播图的显示
			vp_lunbo.setCurrentItem((vp_lunbo.getCurrentItem() + 1 % vp_lunbo.getAdapter().getCount()));
			//handler.postDelayed(this, 2000);
			startLunbo();
		}
	}
	/**
	 * 设置图片描述和点的选中选过
	 * @param picSelectIndex2
	 */
	private void setPicAndPointSelect(int picSelectIndex) {
		//越界判断
		if (picSelectIndex < 0 || picSelectIndex > lunboDatas.size() - 1) {
			return;
		}
		//设置描述信息
		tv_pic_desc.setText(lunboDatas.get(picSelectIndex).title);
		//设置点是否选中的
		for (int i = 0; i < lunboDatas.size(); i++) {
			ll_points.getChildAt(i).setEnabled(i == picSelectIndex);
		}
	}
	/**
	 * 初始化轮播图的点
	 */
	private void initPoints() {
		//先清空以前存在的点
		ll_points.removeAllViews();
		for (int i = 0; i < lunboDatas.size(); i++) {
			View v_point = new View(mainActivity);
			//设置点的背景选择器
			v_point.setBackgroundResource(R.drawable.point_selector);
			v_point.setEnabled(false);//模式是灰色的点
			//设置点的大小
			LayoutParams params = new LayoutParams(DensityUtil.dip2px(mainActivity, 5), DensityUtil.dip2px(mainActivity, 5));
			//设置点与点之间的间距
			params.leftMargin = DensityUtil.dip2px(mainActivity, 10);
			//设置参数
			v_point.setLayoutParams(params);
			ll_points.addView(v_point);
		}
		
	}

	private void setLunBoData(TPINewsData newsData) {
		//获取轮播图数据
		lunboDatas = newsData.data.topnews;
		//通知界面更新
		lunboAdapter.notifyDataSetChanged();
		
	}
	private class ListNewsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listNews.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (holder == null) {
				convertView = View.inflate(mainActivity, R.layout.tpi_news_listview_item, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_tpi_news_listview_item_icon);
				holder.iv_newspic = (ImageView) convertView.findViewById(R.id.iv_tpi_news_listview_item_pic);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_tpi_news_listview_item_time);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_tpi_news_listview_item_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			//设置数据
			TPINewsData_Data_ListNewsData tpiNewsData_Data_ListNewsData = listNews.get(position);
			//判断该新闻是否读取过
			String newsId = tpiNewsData_Data_ListNewsData.id;
			String readnewsIds = SpTools.getString(mainActivity, MyConstants.READNEWSIDS, "");
			if (TextUtils.isEmpty(readnewsIds) || ! readnewsIds.contains(newsId)) {
				//空 没有保存过id
				holder.tv_title.setTextColor(Color.BLACK);
				holder.tv_time.setTextColor(Color.BLACK);
			} else {
				holder.tv_title.setTextColor(Color.GRAY);
				holder.tv_time.setTextColor(Color.GRAY);
			}
			
			//设置标题
			holder.tv_title.setText(tpiNewsData_Data_ListNewsData.title);
			//设置时间
			holder.tv_time.setText(tpiNewsData_Data_ListNewsData.pubdate);
			//设置图片
			bitmapUtils.display(holder.iv_newspic, MyConstants.REQUEST_HOST+tpiNewsData_Data_ListNewsData.listimage);
			
			return convertView;
		}
		
	}
	
	private class ViewHolder {
		ImageView iv_newspic;
		TextView tv_title;
		TextView tv_time;
		ImageView iv_icon;
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
			//设置图片的填充模式
			iv_lunbo_pic.setScaleType(ScaleType.FIT_XY);
			//设置默认的图片,网络缓慢
			iv_lunbo_pic.setImageResource(R.drawable.news_pic_default);
			//给图片添加数据
			TPINewsData_Data_LunboData tpiNewsData_Data_LunboData = lunboDatas.get(position);
			//图片的url
			String topimageUrl = MyConstants.REQUEST_HOST+tpiNewsData_Data_LunboData.topimage;
			//把url的图片给iv_lunbo_pic
			//异步加载图片，并且显示到组件中
			bitmapUtils.display(iv_lunbo_pic, topimageUrl);
			//给图片 添加触摸事件
			iv_lunbo_pic.setOnTouchListener(new OnTouchListener() {
				
				private float downX;
				private float downY;
				private float upX;
				private float upY;
				private long downTime;

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN://按下停止轮播
						downX = event.getX();
						downY = event.getY();
						downTime = System.currentTimeMillis();
						lunboTask.stopLunbo();
						break;
					case MotionEvent.ACTION_CANCEL://事件取消开始轮播图
						lunboTask.startLunbo();
						break;
					case MotionEvent.ACTION_UP://松开开始轮播
						upX = event.getX();
						upY = event.getY();
						if (upX == downX && upY == downY) {
							long upTime = System.currentTimeMillis();
							if  (upTime - downTime < 500) {//单击事件
								lunboPicClick("被单击了");
							}
						}
						lunboTask.startLunbo();
						break;
					default:
						break;
					}
					return true;
				}
				/**
				 * 处理图片的单击事件
				 */
				private void lunboPicClick(Object data) {
					System.out.println(data);
				}
			});
			
			container.addView(iv_lunbo_pic);
			return iv_lunbo_pic;
		}
		
	}
	/**
	 * 获取网络数据
	 */
	private void getDataFromNet(final String url,final boolean isLoadingMore) {
		//httpUtils
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url,new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				//请求成功
				String jsonData = responseInfo.result;
				//保存数据到本地
				SpTools.setString(mainActivity, url, jsonData);
				//解析数据
				newsData = parseJson(jsonData);
				
				//是否是加载更多的数据
				if (isLoadingMore) {
					//原有的数据+新数据
					listNews.addAll(newsData.data.news);
					//更新界面
					listNewsAdapter.notifyDataSetChanged();
					Toast.makeText(mainActivity, "加载数据成功", Toast.LENGTH_SHORT).show();
				} else {
					//第一次取数据或者刷新数据
					//处理数据
					processData(newsData);
					if (isFresh) {
						Toast.makeText(mainActivity, "刷新数据成功", Toast.LENGTH_SHORT).show();
					}
				}
				//设置listview有隐藏
				lv_listnews.refreshStateFinish();
				
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				//请求数据失败
				if (isFresh) {
					lv_listnews.refreshStateFinish();
					Toast.makeText(mainActivity, "刷新数据失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void initView() {
		//获取页签对应的根布局
		root = View.inflate(mainActivity, R.layout.tpi_news_content, null);
		ViewUtils.inject(this, root);
		
		View lunBoPic = View.inflate(mainActivity, R.layout.tpi_news_lunbopic, null);
		ViewUtils.inject(this, lunBoPic);
		
		//启用下拉刷新
		lv_listnews.setIsRefreshHead(true);
		//把轮播图加到ListView中
		lv_listnews.addHeaderView(lunBoPic);
	}
	public View getRoot() {
		return root;
	}
	
	
	
	
}
