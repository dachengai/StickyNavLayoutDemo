package com.dacheng.coordinatorlayout_recyclerview.sticklayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.dacheng.coordinatorlayout_recyclerview.R;

import java.util.ArrayList;
import java.util.List;


public class StickyNavLayoutActivity extends FragmentActivity
{
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private List<Fragment> fragments;
	private ViewPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sticky);

		initViews();
	}

	private void initViews()
	{
		mViewPager = (ViewPager) findViewById(R.id.id_stickynavlayout_content);
		mTabLayout = (TabLayout) findViewById(R.id.id_stickynavlayout_nav);

		fragments = new ArrayList<>();
		fragments.add(new ListFragment());
		fragments.add(new ListFragment());
		fragments.add(new ListFragment());

		List<String> titles = new ArrayList<>();
		titles.add("全部");
		titles.add("买买买");
		titles.add("穿搭");


		mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
		mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
		mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));


		mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments, titles);
		mViewPager.setAdapter(mAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
		mTabLayout.setTabsFromPagerAdapter(mAdapter);
	}


}
