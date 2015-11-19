package com.example.webviewtest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Date;

import uk.co.senab.photoview.PhotoView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ImageShowActivity extends Activity {
	private View currentItemIndicator;
	private LinearLayout indicatorLayout;
	public static final String IAMGE_URLS = "imageURL";
	public static final String POSITION = "position";
	private String[] imageURLs;
	private int position = 0;
	private DisplayImageOptions options;
	private ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_show);

		Intent intent = getIntent();
		Serializable serializable = intent.getSerializableExtra(IAMGE_URLS);
		if (serializable instanceof String[]) {
			imageURLs = (String[]) serializable;
		}
		position = intent.getIntExtra(POSITION, 0);
		if (imageURLs == null || imageURLs.length == 0) {
			finish();
		}
		options = CommonUtil.getImageBuilder().build();

		initImageLoader();

		initView();
	}

	private void initView() {

		viewPager = (ViewPager) findViewById(R.id.viewPager1);
		PagerAdapter adapter = new MyPagerAdapter();
		viewPager.setAdapter(adapter);

		indicatorLayout = (LinearLayout) findViewById(R.id.indicatorLayout);
		final ImageView[] indicatorImages = new ImageView[3];
		int padding = (int) getResources().getDisplayMetrics().density * 5;
		for (int i = 0; i < indicatorImages.length; i++) {
			indicatorImages[i] = new ImageView(this);
			indicatorImages[i].setPadding(padding, 0, padding, 0);
			// int resId = i == 0 ? R.drawable.guide_circle_red
			// : R.drawable.guide_circle_gray;
			int resId = R.drawable.guide_circle_gray;
			indicatorImages[i].setImageResource(resId);
			indicatorLayout.addView(indicatorImages[i]);
		}

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// Log.d("ViewPager", "position=" + position +
				// "  positionOffset="
				// + positionOffset + "   positionOffsetPixels="
				// + positionOffsetPixels);
				LayoutParams params = (LayoutParams) currentItemIndicator
						.getLayoutParams();
				int width = indicatorLayout.getWidth() / imageURLs.length;
				params.leftMargin = (int) ((position + positionOffset) * width);
				currentItemIndicator.setLayoutParams(params);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		currentItemIndicator = findViewById(R.id.currentItemIndicator);
		viewPager.setCurrentItem(position);

		viewPager.post(new Runnable() {

			@Override
			public void run() {
				LayoutParams params = (LayoutParams) currentItemIndicator
						.getLayoutParams();
				int width = indicatorLayout.getWidth() / imageURLs.length;
				params.leftMargin = (int) (position * width);
				currentItemIndicator.setLayoutParams(params);
			}
		});
	}

	public void saveImage(View view) {
		int currentItem = viewPager.getCurrentItem();
		File file = ImageLoader.getInstance().getDiskCache().get(imageURLs[currentItem]);
		File dir;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			dir = new File(Environment.getExternalStorageDirectory(),"/todayfocus/newsreader");
		}else{
			dir = new File(getFilesDir(),"/todayfocus/newsreader");
		}
		boolean mkdirs = dir.mkdirs();
		Log.i("mytag", "mkdirs:"+mkdirs+"  "+dir.canWrite());
		String fileName = new Date().getTime()+".jpg";
		File outFile = new File(dir,fileName);
		Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		if (bitmap!=null) {
			try {
				outFile.createNewFile();
				bitmap.compress(CompressFormat.JPEG, 80, new FileOutputStream(outFile));
			} catch (Exception e) {
			Log.e("mytag", e.getMessage(), e);
			}
			bitmap.recycle();
		}
		
		
	}

	private void initImageLoader() {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
				this);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		ImageLoader.getInstance().init(config.build());
	}

	private class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageURLs.length;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());
			if (!TextUtils.isEmpty(imageURLs[position])) {
				ImageLoader.getInstance().displayImage(imageURLs[position],
						photoView, options);
			}
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}
}
