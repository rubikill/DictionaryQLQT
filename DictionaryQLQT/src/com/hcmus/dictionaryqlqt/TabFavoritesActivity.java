package com.hcmus.dictionaryqlqt;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class TabFavoritesActivity extends Activity implements OnClickListener{

	private ImageView btnEditFavorites, btnFavDelete, btnFavDeleteAll, btnFavCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_favorites);
		
		btnEditFavorites = (ImageView)findViewById(R.id.btnEditFavorites);
		btnFavDelete = (ImageView)findViewById(R.id.btnFavDelete);
		btnFavDeleteAll = (ImageView)findViewById(R.id.btnFavDeleteAll);
		btnFavCancel = (ImageView)findViewById(R.id.btnFavCancel);
		
		btnEditFavorites.setVisibility(0);
		btnFavDelete.setVisibility(-1);
		btnFavDeleteAll.setVisibility(-1);
		btnFavCancel.setVisibility(-1);
		
		btnEditFavorites.setOnClickListener(this);
		btnFavDelete.setOnClickListener(this);
		btnFavDeleteAll.setOnClickListener(this);
		btnFavCancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnEditFavorites:
			btnEditFavorites.setVisibility(-1);
			btnFavDelete.setVisibility(0);
			btnFavDeleteAll.setVisibility(0);
			btnFavCancel.setVisibility(0);
			break;
		case R.id.btnFavDelete:
			
			break;
		case R.id.btnFavDeleteAll:
	
			break;
		case R.id.btnFavCancel:
			btnEditFavorites.setVisibility(0);
			btnFavDelete.setVisibility(-1);
			btnFavDeleteAll.setVisibility(-1);
			btnFavCancel.setVisibility(-1);
			break;
		}		
	}

}
