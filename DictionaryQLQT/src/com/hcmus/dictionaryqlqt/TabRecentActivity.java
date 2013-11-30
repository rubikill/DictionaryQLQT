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

public class TabRecentActivity extends Activity implements OnClickListener,
		OnItemClickListener {

	enum ScreenState {
		View, Empty, Edit,
	}

	private ImageView btnEditrecent, btnRecentDelete, btnRecentDeleteAll,
			btnRecentCancel;
	private ListView listHistory;
	private View empty;
	private View deleteBar;
	private View toolBar;

	private IFavoriteHistory recentDAO;
	private ArrayList<String> arrHistory = null;
	private RecentArrayAdapter recentAdapter = null;
	private String tableName = "Recent";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_recent);

		btnEditrecent = (ImageView) findViewById(R.id.btnEditrecent);
		btnRecentDelete = (ImageView) findViewById(R.id.btnRecentDelete);
		btnRecentDeleteAll = (ImageView) findViewById(R.id.btnRecentDeleteAll);
		btnRecentCancel = (ImageView) findViewById(R.id.btnRecentCancel);
		listHistory = (ListView) findViewById(R.id.listWord);
		empty = findViewById(R.id.tvempty);
		deleteBar = findViewById(R.id.delete_bar);
		toolBar = findViewById(R.id.tool_bar);

		btnEditrecent.setOnClickListener(this);
		btnRecentDelete.setOnClickListener(this);
		btnRecentDeleteAll.setOnClickListener(this);
		btnRecentCancel.setOnClickListener(this);
		listHistory.setOnItemClickListener(this);

		recentDAO = new FavoriteHistoryImp(TabRecentActivity.this);	
	}

	@Override
	protected void onResume() {
		super.onResume();
		boolean isEmpty = recentDAO.IsEmpty(tableName);
		ScreenState state = isEmpty ? ScreenState.Empty : ScreenState.View;
		changeState(state);
	}

	private void loadListWord(boolean isEdit) {
		arrHistory = recentDAO.ReadTable(tableName);
		recentAdapter = new RecentArrayAdapter(this,
				R.layout.item_recent_favorite, arrHistory, isEdit);
		listHistory.setAdapter(recentAdapter);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnEditrecent:
			changeState(ScreenState.Edit);
			break;
		case R.id.btnRecentDelete:
			delete();
			break;
		case R.id.btnRecentDeleteAll:
			deleteAll();
			break;
		case R.id.btnRecentCancel:
			changeState(ScreenState.View);
			break;
		}
	}
	
	private void delete(){
		Boolean isDelted = false;
		int size = listHistory.getChildCount();
		for (int i = size - 1; i >= 0; i--) {
			View view = listHistory.getChildAt(i);
			CheckBox checkbox = (CheckBox) view
					.findViewById(R.id.recent_item_chkbox);
			if (checkbox.isChecked()) {
				isDelted = true;
				String worddelete = arrHistory.get(i);
				recentDAO.DeleteItem(worddelete, tableName);
				arrHistory.remove(i);
			}
		}
		if (isDelted == true) {
			if (recentDAO.IsEmpty(tableName)){
				changeState(ScreenState.Empty);
			}
			else {
				recentAdapter.notifyDataSetChanged();
			}
		}
	}
	
	private void deleteAll(){
		recentDAO.DeleteAll(tableName);
		changeState(ScreenState.Empty);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String word = arrHistory.get(arg2);
		HomeActivity.getInstance().setDictionaryTab(word);
	}

	private void changeState(ScreenState newState) {
		switch (newState) {
		case View:
			empty.setVisibility(View.GONE);
			listHistory.setVisibility(View.VISIBLE);	
			toolBar.setVisibility(View.VISIBLE);
			deleteBar.setVisibility(View.GONE);
			btnEditrecent.setVisibility(View.VISIBLE);			
			loadListWord(false);
			break;
		case Empty:
			empty.setVisibility(View.VISIBLE);
			listHistory.setVisibility(View.GONE);
			toolBar.setVisibility(View.GONE);
			break;
		case Edit:
			toolBar.setVisibility(View.VISIBLE);
			deleteBar.setVisibility(View.VISIBLE);
			btnEditrecent.setVisibility(View.GONE);
			loadListWord(true);
			break;
		}
	}

}
