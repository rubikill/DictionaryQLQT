package com.hcmus.dictionaryqlqt;

import java.io.IOException;
import java.util.ArrayList;


import dao.FavoriteHistoryImp;
import dao.IFavoriteHistory;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class TabFavoritesActivity extends Activity implements OnClickListener, OnItemClickListener{

	private ImageView btnEditFavorites, btnFavDelete, btnFavDeleteAll, btnFavCancel;
	private ListView listFavorite;
	
	private  IFavoriteHistory  favorite; 
	private ArrayList<String> arrfavorite = null;
	private ArrayAdapter<String> adapter = null;
	private MyArrayAdapter myadapter = null;
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
	
		
		favorite = new FavoriteHistoryImp(TabFavoritesActivity.this);
			arrfavorite  = favorite.ReadTable("Favorite");
		
		if(arrfavorite == null){
			Toast.makeText(this,"IS EMPTY", Toast.LENGTH_SHORT).show();
		}
		else {
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrfavorite);
			listFavorite.setAdapter(adapter);
			btnEditFavorites.setVisibility(0);
		}
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
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnEditFavorites:
			myadapter = new MyArrayAdapter( this, R.layout.my_item_layout,arrfavorite);
			listFavorite.setAdapter(myadapter);
			btnEditFavorites.setVisibility(-1);
			btnFavDelete.setVisibility(0);
			btnFavDeleteAll.setVisibility(0);
			btnFavCancel.setVisibility(0);
			break;
		case R.id.btnFavDelete:
			Boolean isDelted = false;
			int size = listFavorite.getChildCount();
			for(int i = size -1 ; i >=0 ; i--){
				View view = listFavorite.getChildAt(i);
				CheckBox checkbox = (CheckBox)view.findViewById(R.id.chkbitem);
				if(checkbox.isChecked()){
					arrfavorite.remove(i);
					isDelted = true;
					String worddelete = arrfavorite.get(i);
					
						favorite.DeleteItem(worddelete, "Favorite");
					
				}
			}
			if(isDelted == true){
				myadapter.notifyDataSetChanged();
			}
			break;
		case R.id.btnFavDeleteAll:
			favorite.DeleteAll("Favorite");
			arrfavorite.removeAll(arrfavorite);
			myadapter.notifyDataSetChanged();
			break;
		case R.id.btnFavCancel:
			listFavorite.setAdapter(adapter);
			btnEditFavorites.setVisibility(0);
			btnFavDelete.setVisibility(-1);
			btnFavDeleteAll.setVisibility(-1);
			btnFavCancel.setVisibility(-1);
			break;
		}		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		wordisdeleted = arrfavorite.get(arg2);
	}
	
}
