package com.hcmus.dictionaryqlqt;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class FullscreenActivity extends TabActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_fullscreen);
		
		Resources ressources = getResources();
		TabHost tabHost = getTabHost();
		tabHost.setup();
		
		Intent intentDictionary = new Intent().setClass(this, TabDictionaryActivity.class);
		TabSpec tabSpecDictionary = tabHost
		  .newTabSpec("Dictionary")
		  .setIndicator("", ressources.getDrawable(R.drawable.tab_dictionary))
		  .setContent(intentDictionary);
 
		Intent intentRecent= new Intent().setClass(this, TabRecentActivity.class);
		TabSpec tabSpecRecent = tabHost
		  .newTabSpec("Recent")
		  .setIndicator("", ressources.getDrawable(R.drawable.tab_recent))
		  .setContent(intentRecent);
		
		Intent intentFavorites= new Intent().setClass(this, TabFavoritesActivity.class);
		TabSpec tabSpecFavorites = tabHost
		  .newTabSpec("Favorites")
		  .setIndicator("", ressources.getDrawable(R.drawable.tab_star))
		  .setContent(intentFavorites);
		
		Intent intentDaily= new Intent().setClass(this, TabDailyActivity.class);
		TabSpec tabSpecDaily = tabHost
		  .newTabSpec("Daily")
		  .setIndicator("", ressources.getDrawable(R.drawable.tab_wotd))
		  .setContent(intentDaily);
		
		Intent intentMore= new Intent().setClass(this, TabMoreActivity.class);
		TabSpec tabSpecMore = tabHost
		  .newTabSpec("More")
		  .setIndicator("", ressources.getDrawable(R.drawable.tab_more))
		  .setContent(intentMore);
		
		
		tabHost.addTab(tabSpecDictionary);
		tabHost.addTab(tabSpecRecent);
		tabHost.addTab(tabSpecFavorites);
		tabHost.addTab(tabSpecDaily);
		tabHost.addTab(tabSpecMore);
 
		tabHost.setCurrentTab(0);
	}
}
