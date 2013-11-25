package com.hcmus.dictionaryqlqt;

import android.media.AudioManager;
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

@SuppressWarnings("deprecation")
public class FullscreenActivity extends TabActivity {

	private LinearLayout lnTabs;
	private static FullscreenActivity Instance;
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		Instance = this;
		lnTabs = (LinearLayout) findViewById(R.id.tab_widget);

		tabHost = getTabHost();
		tabHost.setup();

		Intent intentDictionary = new Intent().setClass(this,
				TabDictionaryActivity.class);
		TabSpec tabSpecDictionary = tabHost
				.newTabSpec("Dictionary")
				.setIndicator(
						new IndicatorLayout(this, "Dictionary",
								R.drawable.tab_dictionary))
				.setContent(intentDictionary);

		Intent intentRecent = new Intent().setClass(this,
				TabRecentActivity.class);
		TabSpec tabSpecRecent = tabHost
				.newTabSpec("Recent")
				.setIndicator(
						new IndicatorLayout(this, "Recent",
								R.drawable.tab_recent))
				.setContent(intentRecent);

		Intent intentFavorites = new Intent().setClass(this,
				TabFavoritesActivity.class);
		TabSpec tabSpecFavorites = tabHost
				.newTabSpec("Favorites")
				.setIndicator(
						new IndicatorLayout(this, "Favorites",
								R.drawable.tab_star))
				.setContent(intentFavorites);

		Intent intentDaily = new Intent()
				.setClass(this, TabDailyActivity.class);
		TabSpec tabSpecDaily = tabHost
				.newTabSpec("Daily")
				.setIndicator(
						new IndicatorLayout(this, "Daily", R.drawable.tab_wotd))
				.setContent(intentDaily);

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

	public void hideTabs() {
		Animation slideOut = AnimationUtils.loadAnimation(this,
				R.anim.slide_down_out);
		slideOut.setAnimationListener(tabsSlideOut);
		lnTabs.startAnimation(slideOut);
	}

	public void showTabs() {
		
		LayoutParams params = (LayoutParams) lnTabs.getLayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;

		lnTabs.setLayoutParams(params);
		

		Animation slideIn = AnimationUtils.loadAnimation(this,
				R.anim.slide_down_in);
		lnTabs.startAnimation(slideIn);
	}

	public static FullscreenActivity getInstance() {
		return Instance;
	}

	private AnimationListener tabsSlideOut = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			LayoutParams params = (LayoutParams) lnTabs.getLayoutParams();
			params.height = 0;

			lnTabs.setLayoutParams(params);
		}
	};
}
