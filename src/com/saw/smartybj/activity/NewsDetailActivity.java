package com.saw.smartybj.activity;

import com.saw.smartybj.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * @author Administrator
 * @创建时间 2016-7-3 下午10:16:44
 * @描述 新闻详情页面
 */
public class NewsDetailActivity extends Activity {

	private ImageButton ib_back;
	private ImageButton ib_setTextSize;
	private ImageButton ib_share;
	private WebView wv_news;
	private ProgressBar pb_loadingnews;
	private WebSettings wv_setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//去掉标题，初始化界面
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//初始化界面
		initView();
		//初始化数据
		initData();
		//初始化事件
		initEvent();
	}

	private void initEvent() {
		//创建三个按钮公用的监听器
		OnClickListener listener = new OnClickListener() {
			int textSizeIndex = 0;//0 超大号字体 1 大号 2正常 3小号 4超小号
			private AlertDialog dialog;
			@Override
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.ib_base_content_back://返回按钮
					//关闭当前新闻页面
					finish();
					break;
				case R.id.ib_base_content_textSize://字体
					//通过对话框来修改字体大小 五种字体大小
					ShowChangeTextSizeDialog();
					//设置字体大小 
					setTextSize();
					break;

				default:
					break;
				}
			}

			private void ShowChangeTextSizeDialog() {
				AlertDialog.Builder ab = new AlertDialog.Builder(NewsDetailActivity.this);
				ab.setTitle("改变字体大小");
				String[] textSize = new String[]{"超大号","大号","正常","小号","超小号"}; 
				ab.setSingleChoiceItems(textSize, textSizeIndex, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int witch) {
						// TODO Auto-generated method stub
						textSizeIndex = witch;
						setTextSize();
						
						
					}
				});
				dialog = ab.create();
				dialog.show();
			}

			private void setTextSize() {
				// TODO Auto-generated method stub
				switch (textSizeIndex) {
				case 0://超大号
					wv_setting.setTextSize(TextSize.LARGEST);
					break;
				case 1://大号
					wv_setting.setTextSize(TextSize.LARGER);
					break;
				case 2://正常
					wv_setting.setTextSize(TextSize.NORMAL);
					break;
				case 3://小号
					wv_setting.setTextSize(TextSize.SMALLER);
					break;
				case 4://超小号
					wv_setting.setTextSize(TextSize.SMALLEST);
					break;
				default:
					break;
				}
				dialog.dismiss();
			}
		};
		//给返回按钮添加事件
		ib_back.setOnClickListener(listener);
		//给字体大小添加事件
		ib_setTextSize.setOnClickListener(listener);
		//给分享按钮添加事件
		ib_share.setOnClickListener(listener);
		
		//给webview加个加载完成事件
		wv_news.setWebViewClient(new WebViewClient(){
			/**
			 * 页面加载完成的事件
			 */
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				//隐藏进度条
				pb_loadingnews.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}
			
		});
	}

	private void initData() {
		//获取数据
		String newsurl = getIntent().getStringExtra("newsurl");
		if (TextUtils.isEmpty(newsurl)) {
			Toast.makeText(getApplicationContext(), "连接错误", Toast.LENGTH_SHORT).show();
		} else {
			//有新闻
			//加载新闻
			wv_news.loadUrl(newsurl);
		}
	}

	private void initView() {
		setContentView(R.layout.newscenter_newsdetail);
		//设置菜单按钮隐藏
		findViewById(R.id.ib_base_content_menu).setVisibility(View.GONE);
		//隐藏标题
		findViewById(R.id.tv_base_content_title).setVisibility(View.GONE);
		//返回的按钮
		ib_back = (ImageButton) findViewById(R.id.ib_base_content_back);
		ib_back.setVisibility(View.VISIBLE);
		//修改新闻的字体按钮
		ib_setTextSize = (ImageButton) findViewById(R.id.ib_base_content_textSize);
		ib_setTextSize.setVisibility(View.VISIBLE);
		//分享
		ib_share = (ImageButton) findViewById(R.id.ib_base_content_share);
		ib_share.setVisibility(View.VISIBLE);
		//显示新闻的webview
		wv_news = (WebView) findViewById(R.id.wb_newscenter_newsdetail);
		//控制webview的显示设置
		
		wv_setting = wv_news.getSettings();
		//设置放大缩小
		wv_setting.setBuiltInZoomControls(true);
		//可以编译javascript脚本
		wv_setting.setJavaScriptEnabled(true);
		
		//设置双击放大或缩小
		wv_setting.setUseWideViewPort(true);
		
		//加载新闻的进度
		pb_loadingnews = (ProgressBar) findViewById(R.id.pb_newscenter_newsdetail_loading);
		
	}
}
