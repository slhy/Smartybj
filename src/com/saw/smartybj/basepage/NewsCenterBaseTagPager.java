package com.saw.smartybj.basepage;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.saw.smartybj.MainActivity;
import com.saw.smartybj.domain.NewsCenterData;
import com.saw.smartybj.newscenterpage.BaseNewsCenterPage;
import com.saw.smartybj.newscenterpage.InteractBaseNewsCenterPage;
import com.saw.smartybj.newscenterpage.NewsBaseNewsCenterPage;
import com.saw.smartybj.newscenterpage.PhotosBaseNewsCenterPage;
import com.saw.smartybj.newscenterpage.TopicBaseNewsCenterPage;
import com.saw.smartybj.utils.MyConstants;
import com.saw.smartybj.view.LeftMenuFragment.OnSwitchPageListener;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-15 下午10:26:38
 * @描述 TODO
 */
public class NewsCenterBaseTagPager extends BaseTagPage {
	
	private List<BaseNewsCenterPage> newsCenterPages = new  ArrayList<BaseNewsCenterPage>();
	private NewsCenterData newsCenterData;

	public NewsCenterBaseTagPager(MainActivity mainActivity) {
		super(mainActivity);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initData() {
		//1.获取网络数据
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, MyConstants.REQUEST_URL+"getCategory", new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				//访问数据成功
				String jsonData = responseInfo.result;
				//2.解析数据
				parseData(jsonData);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				//访问数据失败
				System.out.println("网络请求错误失败："+error);
			}
		});
		
		super.initData();
	}
	/**
	 * 解析json数据
	 * @param jsonData 从网络获取到的json数据
	 */
	protected void parseData(String jsonData) {
		// TODO Auto-generated method stub
		//google提供的json解析器
		Gson gson = new Gson();
		
		newsCenterData = gson.fromJson(jsonData, NewsCenterData.class);
//		System.out.println(jsonData);
		
		//3.数据的处理
		mainActivity.getLeftMenuFragment().setLeftMenuData(newsCenterData.data);
		//设置左侧菜单的监听回调
		mainActivity.getLeftMenuFragment().setOnSwitchPageListener(new OnSwitchPageListener() {
			
			@Override
			public void switchPage(int selectionIndex) {
				// TODO Auto-generated method stub
				//System.out.println("直接调自己来实现……");
				NewsCenterBaseTagPager.this.switchPage(selectionIndex);
			}
		});
		//读取的数据封装到容器界面中，通过左侧菜单点击，显示不同的界面
		//根据服务器的数据创建四个页面（按顺序）
		for (NewsCenterData.NewsData newsData:newsCenterData.data) {
			BaseNewsCenterPage newsPage = null;
			//遍历四个新闻中心界面
			switch (newsData.type) {
				case 1://新闻页面
					newsPage = new NewsBaseNewsCenterPage(mainActivity);
					break;
				case 10://专题页面
					newsPage = new TopicBaseNewsCenterPage(mainActivity);
					break;
				case 2://组图页面
					newsPage = new PhotosBaseNewsCenterPage(mainActivity);
					break;
				case 3://互动页面
					newsPage = new InteractBaseNewsCenterPage(mainActivity);
					break;
				default:
					break;
			}
			//添加页面到新闻中心中
			newsCenterPages.add(newsPage);
		}
		//控制四个页面显示,默认显示第一个新闻界面
		switchPage(0);
		
		
	}
	/**
	 * 根据位置动态显示不同的新闻中心页面
	 * @param position
	 */
	public void switchPage(int position) {
		BaseNewsCenterPage baseNewsCenterPage = newsCenterPages.get(position);
		//显示数据
		//设置本page的标题
		tv_title.setText(newsCenterData.data.get(position).title);
		//要展示的内容，替换掉白纸 fl_content;//内容  FrameLayout
		//移除掉原来的画的内容
		fl_content.removeAllViews();
		//添加到白纸中
		fl_content.addView(baseNewsCenterPage.getRoot());
	}

}
