package com.gongxm.tangshi.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gongxm.tangshi.bean.Bean;

public class UrlUtils {
	public static String getShiList(int index) {
		String url = Constants.BASE_URL + Constants.TYPE + index + Constants.SHI;
		return url;
	}

	public static String getCiList(int index) {
		String url = Constants.BASE_URL + Constants.TYPE + index + Constants.CI;
		return url;
	}

	public static String getQuList(int index) {
		String url = Constants.BASE_URL + Constants.TYPE + index + Constants.QU;
		return url;
	}

	public static List<Bean> getItems(String path) {
		List<Bean> list = new ArrayList<Bean>();
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			InputStream inputStream = conn.getInputStream();
			String result = StreamUtils.readStream(inputStream);
			String[] results = result.split("<div class=\"sons\">");
			for (int i = 1; i < results.length; i++) {
				String titleText = results[i].substring(results[i].indexOf("<p><a style=\" font-size:14px;"), results[i].indexOf("</a></p>"));
				String title = titleText.substring(titleText.lastIndexOf(">") + 1);
				String author = results[i].substring(results[i].indexOf("color:#676767;") + 16, results[i].indexOf("&nbsp;"));
				String desc = results[i].substring(results[i].indexOf("margin-bottom:0px;") + 20, results[i].indexOf("<a title="));
				String textUrl = results[i].substring(results[i].indexOf("font-size:14px"),
						results[i].indexOf("\" target", results[i].indexOf("font-size:14px"))).replace("font-size:14px;\" href=\"", "/");
				list.add(new Bean(title, author, desc, textUrl));
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
