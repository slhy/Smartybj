package com.saw.smartybj.view;

import com.saw.smartybj.MainActivity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Administrator
 * @创建时间 2016-6-14 下午11:35:03
 * @描述 TODO
 */
public abstract class BaseFragment extends Fragment {
	protected MainActivity mainActivity;//上下文

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mainActivity = (MainActivity) getActivity();//获取fragment所在的Activity
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View root = initView();//初始化界面
		return root;
	}
	/**
	 * 子类必须覆盖此方法来完成界面的显示
	 * @return
	 */
	public abstract View initView();
	
	/**
	 * 子类覆盖此方法来完成数据的初始化
	 */
	public void initData() {
		
	}
	/**
	 * 子类覆盖此方法来完成事件的初始化
	 */
	public void initEvent() {
		
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		//初始化事件和数据
		super.onActivityCreated(savedInstanceState);
		//初始化数据
		initData();
		//初始化事件
		initEvent();
	}
}
