package com.hcmus.dictionaryqlqt;


import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class FullscreenActivity extends TabActivity {

	private static FullscreenActivity Instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		Instance = this;
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public static FullscreenActivity getInstance(){
		return Instance;
	}
	

}
