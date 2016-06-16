package com.saw.smartybj.domain;

import java.util.List;

/**
 * @author Administrator
 * @创建时间 2016-6-16 下午10:21:26
 * @描述 新闻中心数据的封装
 */
public class NewsCenterData {
	public int retcode;
	
	public List<NewsData> data;
	
	public class NewsData {
		public List<ViewTagData> children;
		
		/**
		 * 新闻中心页签的数据
		 * @author Administrator
		 * @创建时间 2016-6-16 下午10:28:34
		 * @描述 TODO
		 */
		public class ViewTagData {
			public String id;
			public String title;
			public int type;
			public String url;
		}
		
		public String id;
		public String title;
		public int type;
		public String url;
		public String url1;
		
		public String excurl;
		public String dayurl;
		public String weekurl;
		
		
               
	}
	
	public List<String> extend;
}
