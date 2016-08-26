package com.saw.smartybj.utils;



/**
 * @author Administrator
 * @创建时间 2016-6-11 下午9:20:57
 * @描述 TODO
 */
public interface MyConstants {
	String HOST = "http://192.168.1.103";
	String REQUEST_HOST = HOST + "/zhbj";
	String REQUEST_URL = HOST + "/index.php/m/";
	String PHOTOSURL = REQUEST_HOST + "/photos/photos_1.json";
	String CONFIGFILE = "cachevalue";//sp的文件名
	String ISSETUP = "issetup";//是否设置过向导界面数据
	String READNEWSIDS = "readnewsids";//保存读过的新闻id
	
	
}
