package com.tdx.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyViewPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;// ��Ҫ��ӵ������Fragment
	private final String[] TITLES = { "他们最幸福", "乖，摸摸头", "更多内容", };
	public MyViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/**
	 * �Զ���Ĺ��캯��
	 * @param fm
	 * @param fragments
	 *            ArrayList<Fragment>
	 */
	public MyViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}
	@Override
	public CharSequence getPageTitle(int position) {
		return TITLES[position];
	}
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);// ����Fragment����
	}

	@Override
	public int getCount() {
		return fragments.size();// ����Fragment�ĸ���
	}
}