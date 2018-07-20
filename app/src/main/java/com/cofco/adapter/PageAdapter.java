package com.cofco.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class PageAdapter extends  FragmentPagerAdapter{
	private ArrayList<Fragment> fragmentsList;
	
	
	public PageAdapter(FragmentManager fm) {
		super(fm);
	}
	 public PageAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
	        super(fm);
	     this.fragmentsList = fragments;
	    }

	  @Override
	    public int getCount() {
	        return fragmentsList.size();
	    }

	    @Override
	    public Fragment getItem(int arg0) {
	        return fragmentsList.get(arg0);
	    }

	    @Override
	    public int getItemPosition(Object object) {
	        return super.getItemPosition(object);
	    }


}
