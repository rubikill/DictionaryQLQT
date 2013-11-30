package com.hcmus.dictionaryqlqt;

import java.util.ArrayList;

import dao.FavoriteHistoryImp;
import dao.IFavoriteHistory;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

public class TabFavoritesActivity extends Activity implements OnClickListener, OnItemClickListener {

	enum ScreenState {
		View, Empty, Edit,
	}

	private ImageView btnFavEdit, btnFavDelete, btnFavDeleteAll,
			btnFavCancel;
	private ListView listFavorite;
	private View empty;
	private View deleteBar;
	private View toolBar;

	private IFavoriteHistory favoriteDAO;
	private ArrayList<String> arrFavorite = null;
	private RecentArrayAdapter favoriteAdapter = null;
	private String tableName = "Favorite";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_favorites);

		btnFavEdit = (ImageView) findViewById(R.id.btnFavEdit);
		btnFavDelete = (ImageView) findViewById(R.id.btnFavDelete);
		btnFavDeleteAll = (ImageView) findViewById(R.id.btnFavDeleteAll);
		btnFavCancel = (ImageView) findViewById(R.id.btnFavCancel);
		listFavorite = (ListView) findViewById(R.id.listWord);
		empty = findViewById(R.id.tvempty);
		deleteBar = findViewById(R.id.delete_bar);
		toolBar = findViewById(R.id.tool_bar);

		btnFavEdit.setOnClickListener(this);
		btnFavDelete.setOnClickListener(this);
		btnFavDeleteAll.setOnClickListener(this);
		btnFavCancel.setOnClickListener(this);
		listFavorite.setOnItemClickListener(this);

		favoriteDAO = new FavoriteHistoryImp(this);	
	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean isEmpty = favoriteDAO.IsEmpty(tableName);
		ScreenState state = isEmpty ? ScreenState.Empty : ScreenState.View;
		changeState(state);
	}

	private void loadListWord(boolean isEdit) {
		arrFavorite = favoriteDAO.ReadTable(tableName);
		favoriteAdapter = new RecentArrayAdapter(this,
				R.layout.item_recent_favorite, arrFavorite, isEdit);
		listFavorite.setAdapter(favoriteAdapter);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnFavEdit:
			changeState(ScreenState.Edit);
			break;
		case R.id.btnFavDelete:
			delete();
			break;
		case R.id.btnFavDeleteAll:
			deleteAll();
			break;
		case R.id.btnFavCancel:
			changeState(ScreenState.View);
			break;
		}
	}
	
	private void delete(){
		Boolean isDelted = false;
		int size = listFavorite.getChildCount();
		for (int i = size - 1; i >= 0; i--) {
			View view = listFavorite.getChildAt(i);
			CheckBox checkbox = (CheckBox) view
					.findViewById(R.id.recent_item_chkbox);
			if (checkbox.isChecked()) {
				isDelted = true;
				String worddelete = arrFavorite.get(i);
				favoriteDAO.DeleteItem(worddelete, tableName);
				arrFavorite.remove(i);
			}
		}
		if (isDelted == true) {
			if (favoriteDAO.IsEmpty(tableName)){
				changeState(ScreenState.Empty);
			}
			else {
				favoriteAdapter.notifyDataSetChanged();
			}
		}
	}
	
	private void deleteAll(){
		favoriteDAO.DeleteAll(tableName);
		changeState(ScreenState.Empty);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String word = arrFavorite.get(arg2);
		FullscreenActivity.getInstance().setDictionaryTab(word);
	}

	private void changeState(ScreenState newState) {
		switch (newState) {
		case View:
			empty.setVisibility(View.GONE);
			listFavorite.setVisibility(View.VISIBLE);	
			toolBar.setVisibility(View.VISIBLE);
			deleteBar.setVisibility(View.GONE);
			btnFavEdit.setVisibility(View.VISIBLE);			
			loadListWord(false);
			break;
		case Empty:
			empty.setVisibility(View.VISIBLE);
			listFavorite.setVisibility(View.GONE);
			toolBar.setVisibility(View.GONE);
			break;
		case Edit:
			toolBar.setVisibility(View.VISIBLE);
			deleteBar.setVisibility(View.VISIBLE);
			btnFavEdit.setVisibility(View.GONE);
			loadListWord(true);
			break;
		}
	}

}
