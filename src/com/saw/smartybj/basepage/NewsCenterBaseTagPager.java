package com.saw.smartybj.basepage;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.saw.smartybj.MainActivity;
import com.saw.smartybj.domain.NewsCenterData;
import com.saw.smartybj.utils.MyConstants;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

/**
 * @author Administrator
 * @创建时间 2016-6-15 下午10:26:38
 * @描述 TODO
 */
public class NewsCenterBaseTagPager extends BaseTagPage {

	public NewsCenterBaseTagPager(MainActivity mainActivity) {
		super(mainActivity);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void initData() {
		//获取网络数据
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, MyConstants.REQUEST_URL+"getCategory", new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				// TODO Auto-generated method stub
				//访问数据成功
				String jsonData = responseInfo.result;
//				System.out.println(jsonData);
				//解析数据
				parseData(jsonData);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				// TODO Auto-generated method stub
				//访问数据失败
				System.out.println("网络请求错误失败："+error);
			}
		});
		//设置本page的标题
		tv_title.setText("新闻中心");
		//要展示的内容，替换掉白纸 fl_content;//内容  FrameLayout
		TextView tv = new TextView(mainActivity);
		tv.setText("新闻中心的内容");
		tv.setTextSize(25);
		tv.setGravity(Gravity.CENTER);
		//添加到白纸中
		fl_content.addView(tv);
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
		
		NewsCenterData newsCenterData = gson.fromJson(jsonData, NewsCenterData.class);
		mainActivity.getLeftMenuFragment().setLeftMenuData(newsCenterData.data);
		//System.out.println(newsCenterData.data.get(0).children.get(0).title);
	}

}
