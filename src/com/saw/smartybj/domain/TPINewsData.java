package com.saw.smartybj.domain;

import java.util.List;

/**
 * @author Administrator
 * @创建时间 2016-6-19 下午10:10:23
 * @描述 页签对应的新闻数据
 */
public class TPINewsData {
	public int retcode;
	public TPINewsData_Data data;
	
	
	public class TPINewsData_Data {
		public String countcommenturl;
		public String more;
		public String title;
		public List<TPINewsData_Data_ListNewsData> news;
		public List<TPINewsData_Data_TopicData> topic;
		public List<TPINewsData_Data_LunboData> topnews;
		
		public class TPINewsData_Data_ListNewsData {
			public String comment;
			public String commentlist;
			public String commenturl;
			public String id;
			public String listimage;
			public String pubdate;
			public String title;
			public String type;
			public String url;
		}
		public class TPINewsData_Data_TopicData {
			public String description;
			public String id;
			public String listimage;
			public String sort;
			public String title;
			public String url;
		}
		public class TPINewsData_Data_LunboData {
			public String comment;
			public String commentlist;
			public String commenturl;
			public String id;
			public String pubdate;
			public String title;
			public String topimage;
			public String type;
			public String url;
		}
	}
}
