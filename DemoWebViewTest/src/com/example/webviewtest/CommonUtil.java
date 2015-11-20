package com.example.webviewtest;

import java.io.File;

import android.content.Context;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class CommonUtil {

	public static String getDirectoty(Context context) {
		File file = new File(context.getCacheDir(), "image");
		return file.getAbsolutePath();
	}    

	public static Builder getImageBuilder() {
		Builder builder = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.item_default_image)
				.showImageForEmptyUri(R.drawable.item_default_image)
				.showImageOnFail(R.drawable.item_default_image)
				.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(1000, true, true, false));
		return builder;
	}




}
