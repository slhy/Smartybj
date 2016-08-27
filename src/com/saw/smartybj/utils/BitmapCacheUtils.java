package com.saw.smartybj.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.saw.smartybj.MainActivity;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

/**
 * @author Administrator
 * @创建时间 2016-8-27 上午8:48:15
 * @描述 TODO
 */
public class BitmapCacheUtils {
	//动态获取jvm内存
	private int maxSize = (int) (Runtime.getRuntime().freeMemory() / 3);
	//图片的缓存容器
	private LruCache<String, Bitmap> memCache = new LruCache<String, Bitmap>(maxSize) {

		@Override
		protected int sizeOf(String key, Bitmap value) {
			//计算图片占用多大
			return value.getRowBytes() * value.getHeight();
		}
		
	};
	//保留最后一次访问url的信息
	private Map<ImageView, String> urlImageViewDatas = new HashMap<ImageView, String>();
	private File cacheDir;
	private MainActivity mainActivity;
	private ExecutorService threadPool;
	public BitmapCacheUtils(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		//获取当前app的cache目录
		cacheDir = mainActivity.getCacheDir();
		//线程池
		threadPool = Executors.newFixedThreadPool(6);//这里的数字和你手机屏幕一屏显示的图片个数（listview是3个，gridview是6个）
		
		
	}
	public void display(ImageView iv,String ivUrl) {
		//1.先从内存中取
		Bitmap bitmap = memCache.get(ivUrl);
		if (bitmap != null) {
			System.out.println("从内存获取数据");
			//缓存中有图片
			iv.setImageBitmap(bitmap);
			return;
		}
		
		//2.再从本地文件取,app(/data/data/包名/cache)
		bitmap = getCacheFile(ivUrl);
		if (bitmap != null) {
			System.out.println("从文件获取数据");
			//本地文件中有图片
			iv.setImageBitmap(bitmap);
			return;
		}
		//3.从网络取
		urlImageViewDatas.put(iv, ivUrl);//保留最后一次访问的url
		getBitmapFromNet(iv,ivUrl);
	}
	private void getBitmapFromNet(ImageView iv, String ivUrl) {
		//访问网络
		//new Thread(new DownloadUrl(iv, ivUrl)).start();
		//使用线程池
		threadPool.submit(new DownloadUrl(iv, ivUrl));
		
	}
	private class DownloadUrl implements Runnable {
		private String ivUrl;
		private ImageView iv;
		public DownloadUrl(ImageView iv,String ivUrl) {
			this.iv = iv;
			this.ivUrl = ivUrl;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(ivUrl);
				HttpURLConnection con = (HttpURLConnection) url.openConnection();
				con.setConnectTimeout(6000);//设置超时时间
				con.setRequestMethod("GET");
				int code = con.getResponseCode();
				if (code == 200) {
					//请求成功
					InputStream inputStream = con.getInputStream();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					//1.往内存中添加
					memCache.put(ivUrl, bitmap);
					//2.往本地文件中添加
					saveBitmapToCacheFile(bitmap, ivUrl);
					//3.显示数据
					mainActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							System.out.println("从网络获取数据");
							//显示图片
							//判断URL是不是最新的
							//是最新的 是自己的数据
							if (ivUrl.equals(urlImageViewDatas.get(iv))) {
								//自己的数据  url一致
								iv.setImageBitmap(bitmap);
							}
							
						}
					});
					
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * 保存bitmap到cache目录的文件中
	 * @param bitmap
	 * @param ivUrl
	 */
	public void saveBitmapToCacheFile(Bitmap bitmap,String ivUrl) {
		//把ivUrl转化成MD5值，再把MD5作为文件名
		File file = new File(cacheDir, Md5Utils.md5(ivUrl));
		try {
			bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @param ivUrl
	 * 当做缓存文件的名字
	 * @return
	 */
	public Bitmap getCacheFile(String ivUrl) {
		//把ivUrl转化成MD5值，再把MD5作为文件名
		File file = new File(cacheDir, Md5Utils.md5(ivUrl));
		if (file != null && file.exists()) {
			//文件存在
			//文件转化成bitmap
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			//再往内存中写一份
			memCache.put(ivUrl, bitmap);
			return bitmap;
		} else {
			//不存在
			return null;
		}
		
	}
}
