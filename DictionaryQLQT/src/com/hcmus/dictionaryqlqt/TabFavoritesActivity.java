package com.hcmus.dictionaryqlqt;

import java.io.IOException;
import java.util.ArrayList;

import dao.FavoriteHistoryDAO;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class TabFavoritesActivity extends Activity implements OnClickListener, OnItemClickListener {

	private ImageView btnEditFavorites, btnFavDelete, btnFavDeleteAll, btnFavCancel;
	private ListView listFavorite;
	
	private FavoriteHistoryDAO  favorite = new FavoriteHistoryDAO(); 
	private ArrayList<String> arrfavorite = null;
	private ArrayAdapter<String> adapter = null;
	private String wordisdeleted = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_favorites);
		
		btnEditFavorites = (ImageView)findViewById(R.id.btnEditFavorites);
		btnFavDelete = (ImageView)findViewById(R.id.btnFavDelete);
		btnFavDeleteAll = (ImageView)findViewById(R.id.btnFavDeleteAll);
		btnFavCancel = (ImageView)findViewById(R.id.btnFavCancel);
		listFavorite = (ListView)findViewById(R.id.listFavorites);
	
		
		btnFavDelete.setVisibility(-1);
		btnFavDeleteAll.setVisibility(-1);
		btnFavCancel.setVisibility(-1);
		
		btnEditFavorites.setOnClickListener(this);
		btnFavDelete.setOnClickListener(this);
		btnFavDeleteAll.setOnClickListener(this);
		btnFavCancel.setOnClickListener(this);
		
		listFavorite.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		loadFavorite();
	}
	
	private void loadFavorite(){
		try {
			arrfavorite  = favorite.ReadFile(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(arrfavorite == null){
			Toast.makeText(this,"IS EMPTY", Toast.LENGTH_SHORT).show();
		}
		else {
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrfavorite);
			listFavorite.setAdapter(adapter);
			btnEditFavorites.setVisibility(0);
		}
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
			if(wordisdeleted != ""){
				try {
					favorite.DeleteItem(wordisdeleted, 2);
					arrfavorite.remove(wordisdeleted);
					adapter.notifyDataSetChanged();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnFavDeleteAll:
			favorite.DeleteAll(2);
			arrfavorite.removeAll(arrfavorite);
			adapter.notifyDataSetChanged();
			break;
		case R.id.btnFavCancel:
			btnEditFavorites.setVisibility(0);
			btnFavDelete.setVisibility(-1);
			btnFavDeleteAll.setVisibility(-1);
			btnFavCancel.setVisibility(-1);
			break;
		}		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String word = arrfavorite.get(arg2);
		FullscreenActivity.getInstance().setDictionaryTab(word);		
	}
}
