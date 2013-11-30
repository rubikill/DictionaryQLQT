package com.hcmus.dictionaryqlqt;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

/**
 * 
 * @author Minh Khanh
 * Man hinh chinh cua ung dung
 *
 */

@SuppressWarnings("deprecation")
public class HomeActivity extends TabActivity {

	/*
	 * tabwiget
	 */
	private LinearLayout lnTabs;
	
	/*
	 * the hien cua class
	 */
	private static HomeActivity Instance;
	
	/*
	 * tabhost 
	 */
	private TabHost tabHost;

	/**
	 * khoi tao cac tab
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		Instance = this;
		lnTabs = (LinearLayout) findViewById(R.id.tab_widget);

		tabHost = getTabHost();
		tabHost.setup();

		// khoi tao tab Dictionary
		Intent intentDictionary = new Intent().setClass(this,
				TabDictionaryActivity.class);
		TabSpec tabSpecDictionary = tabHost
				.newTabSpec("Dictionary")
				.setIndicator(
						new IndicatorLayout(this, "Dictionary",
								R.drawable.tab_dictionary))
				.setContent(intentDictionary);

		// khoi tao tab Recent
		Intent intentRecent = new Intent().setClass(this,
				TabRecentActivity.class);
		TabSpec tabSpecRecent = tabHost
				.newTabSpec("Recent")
				.setIndicator(
						new IndicatorLayout(this, "Recent",
								R.drawable.tab_recent))
				.setContent(intentRecent);

		// khoi tao tab Favorite
		Intent intentFavorites = new Intent().setClass(this,
				TabFavoritesActivity.class);
		TabSpec tabSpecFavorites = tabHost
				.newTabSpec("Favorites")
				.setIndicator(
						new IndicatorLayout(this, "Favorites",
								R.drawable.tab_star))
				.setContent(intentFavorites);

		// khoi tao tab Daily
		Intent intentDaily = new Intent()
				.setClass(this, TabDailyActivity.class);
		TabSpec tabSpecDaily = tabHost
				.newTabSpec("Daily")
				.setIndicator(
						new IndicatorLayout(this, "Daily", R.drawable.tab_wotd))
				.setContent(intentDaily);

		// khoi tao tab More
		Intent intentMore = new Intent().setClass(this, TabMoreActivity.class);
		TabSpec tabSpecMore = tabHost
				.newTabSpec("More")
				.setIndicator(
						new IndicatorLayout(this, "More", R.drawable.tab_more))
				.setContent(intentMore);

		tabHost.addTab(tabSpecDictionary);
		tabHost.addTab(tabSpecRecent);
		tabHost.addTab(tabSpecFavorites);
		tabHost.addTab(tabSpecDaily);
		tabHost.addTab(tabSpecMore);

		tabHost.setCurrentTab(0);
	}
	
	/**
	 * chuyen qua tab Dictionary
	 * @param word tu can search
	 */
	public void setDictionaryTab(String word){
		tabHost.setCurrentTab(0);
		TabDictionaryActivity instance = TabDictionaryActivity.getInstance();
		if (instance != null){
			instance.Search(word);
		}
	}

	/**
	 * an tabwiget
	 */
	public void hideTabs() {
		Animation slideOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_down_out);
		slideOut.setAnimationListener(tabsSlideOut);
		lnTabs.startAnimation(slideOut);
	}

	/**
	 * hien thi tabwiget
	 */
	public void showTabs() {
		
		LayoutParams params = (LayoutParams) lnTabs.getLayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;

		lnTabs.setLayoutParams(params);
		

		Animation slideIn = AnimationUtils.loadAnimation(this,
				R.anim.slide_down_in);
		lnTabs.startAnimation(slideIn);
	}

	/**
	 * lay the hien cua class
	 * @return instance hien tai
	 */
	public static HomeActivity getInstance() {
		return Instance;
	}

	/**
	 * AnimationListener lang nghe su kien animation an tabwiget
	 */
	private AnimationListener tabsSlideOut = new AnimationListener() {

		/**
		 * thay doi height khi animation ket thuc
		 */
		@Override
		public void onAnimationEnd(Animation animation) {
			LayoutParams params = (LayoutParams) lnTabs.getLayoutParams();
			params.height = 0;

			lnTabs.setLayoutParams(params);
		}

		@Override
		public void onAnimationStart(Animation animation) {}

		@Override
		public void onAnimationRepeat(Animation animation) {}
	};
}
