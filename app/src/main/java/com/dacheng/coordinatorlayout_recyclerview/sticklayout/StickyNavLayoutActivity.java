package com.dacheng.coordinatorlayout_recyclerview.sticklayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.dacheng.coordinatorlayout_recyclerview.R;
import com.dacheng.coordinatorlayout_recyclerview.sticklayout.views.AutoScrollBanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class StickyNavLayoutActivity extends FragmentActivity
{
	private ViewPager mViewPager;
	private TabLayout mTabLayout;
	private List<Fragment> fragments;
	private ViewPagerAdapter mAdapter;
	private AutoScrollBanner mAutoScrollBanner;

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
		mAutoScrollBanner = (AutoScrollBanner) findViewById(R.id.autoscrollbanner);

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

		List<Integer> listRes = new ArrayList<>();
		listRes.add(R.drawable.one);
		listRes.add(R.drawable.two);
		listRes.add(R.drawable.one);
		listRes.add(R.drawable.two);
		mAutoScrollBanner.setViewRes(listRes);
		mAutoScrollBanner.setOffscreenPageLimit(4);
		mAutoScrollBanner.setPagerMargin(10);
		//mAutoScrollBanner.setClipChildren(false);
		mAutoScrollBanner.setPageTransformer(false,new CustomPageTransformer());
	}


	/**
	 * 自定义的Transformer（page切换过程中有旋转效果）
	 */
	public class CustomPageTransformer implements ViewPager.PageTransformer {
		private static final float MIN_SCALE = 0.9f;
		private static final float MIN_ALPHA = 0.5f;

		private  float defaultScale = 0.9f;

		public void transformPage(View view, float position) {
			int pageWidth = view.getWidth();
			int pageHeight = view.getHeight();

			if (position < -1) { // [-Infinity,-1)
				// This page is way off-screen to the left.
				view.setAlpha(0);
				view.setScaleX(defaultScale);
				view.setScaleY(defaultScale);
			} else if (position <= 1) { // [-1,1]
				// Modify the default slide transition to shrink the page as well
				float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
				float vertMargin = pageHeight * (1 - scaleFactor) / 2;
				float horzMargin = pageWidth * (1 - scaleFactor) / 2;
				if (position < 0) {
					view.setTranslationX(horzMargin - vertMargin / 2);
				} else {
					view.setTranslationX(-horzMargin + vertMargin / 2);
				}

				// Scale the page down (between MIN_SCALE and 1)
				view.setScaleX(scaleFactor);
				view.setScaleY(scaleFactor);

				// Fade the page relative to its size.
				view.setAlpha(MIN_ALPHA +
						(scaleFactor - MIN_SCALE) /
								(1 - MIN_SCALE) * (1 - MIN_ALPHA));

			} else { // (1,+Infinity]
				// This page is way off-screen to the right.
				view.setAlpha(0);
				view.setScaleX(defaultScale);
				view.setScaleY(defaultScale);
			}
		}
	}


}
