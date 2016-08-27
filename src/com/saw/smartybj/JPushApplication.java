package com.saw.smartybj;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

/**
 * @author Administrator
 * @创建时间 2016-8-27 下午4:32:10
 * @描述 TODO
 */
public class JPushApplication extends Application {
	@Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
