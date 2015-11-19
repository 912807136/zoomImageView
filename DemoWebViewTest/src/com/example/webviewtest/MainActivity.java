package com.example.webviewtest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class MainActivity extends Activity {
	private String content = "<html><head></head><body>"
			+ "<img src=\"http://s1.hao123img.com/res/images/search_logo/webv2.png\" /><br />"
			+ "<img src=\"http://s0.hao123img.com/res/img/logo/logo-wide.png\"/><br />"
			+ "<img src=\"http://img14.360buyimg.com/n1/s130x130_jfs/t1180/236/925849879/82803/784f564d/555aebfbN2625109b.jpg\"/><br />"
			+ "<script src=\"file:///android_asset/web/image_click.js\"></script>"
			+ "<script src=\"file:///android_asset/web/ready.js\"></script>"
			+ "<script src=\"file:///android_asset/web/zepto.min.js\"></script>"
			+ "<script src=\"file:///android_asset/web/zepto.picLazyLoad.min.js\"></script>"
			+"</body></html>";

	// private String aa = "<html><head></head><body>"
	// +
	// "<img src=\"http://s1.hao123img.com/res/images/search_logo/webv2.png\" /><br />"
	// +
	// "<img src=\"http://s0.hao123img.com/res/img/logo/logo-wide.png\"/><br />"
	// +
	// "<img src=\"http://img14.360buyimg.com/n1/s130x130_jfs/t1180/236/925849879/82803/784f564d/555aebfbN2625109b.jpg\"/><br />"
	// + "<script>"
	// + "var objs=document.getElementsByTagName(\"img\");"
	// + "var urls = new Array();"
	// + "for(var i=0;i<objs.length;i++){"
	// + "urls[i] = objs[i].src;"
	// + "objs[i].onclick=function(){listener.showImage(urls,this.src);};}"
	// + "</script></body></html>";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		test1();
		
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void test1() {
		WebView view = (WebView) findViewById(R.id.webView1);
		view.getSettings().setJavaScriptEnabled(true);
		 view.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//		view.loadUrl("file:///android_asset/web/test.html");
		view.addJavascriptInterface(new JavascriptInterface(), "listener");

//		String content = getAssetsContent();
//		Log.i("mytag", "content:" + content);
	}

	private String getAssetsContent() {
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			is = getAssets().open("web/image_click.js");
			baos = new ByteArrayOutputStream();
			int len;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			return baos.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public class JavascriptInterface {
		@android.webkit.JavascriptInterface
		public void showImage(String[] urls, String url) {
			int selectPosition = 0;
			for (int i = 0; i < urls.length; i++) {
				if (urls[i].equals(url)) {
					selectPosition = i;
					Log.i("mytag", "position:" + i);
				}
			}

			Intent intent = new Intent(MainActivity.this,
					ImageShowActivity.class);
			intent.putExtra(ImageShowActivity.IAMGE_URLS, urls);
			intent.putExtra(ImageShowActivity.POSITION, selectPosition);
			startActivity(intent);
			Log.i("mytag", "url:" + url);
		}
	}

}
