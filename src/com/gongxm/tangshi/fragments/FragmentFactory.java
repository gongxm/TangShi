package com.gongxm.tangshi.fragments;

import java.util.HashMap;
import java.util.Map;

public class FragmentFactory {
	private static Map<Integer,BaseFragment> map=new HashMap<Integer, BaseFragment>();

	public static BaseFragment createFragment(int position) {
		BaseFragment fragment = map.get(position);
		if (fragment == null) {
			switch (position) {
			case 0:
				fragment = new TsFragment();
				break;
			case 1:
				fragment = new CiFragment();
				break;
			case 2:
				fragment = new QuFragment();
				break;
			}
			map.put(position, fragment);
		}
		return fragment;
	}
}
