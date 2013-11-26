package com.hcmus.dictionaryqlqt;

import java.io.IOException;
import java.util.ArrayList;

import dao.FavoriteHistoryDAO;
import android.os.Bundle;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class TabRecentActivity extends Activity implements OnClickListener, OnItemClickListener{

	private ImageView btnEditrecent, btnRecentDelete, btnRecentDeleteAll, btnRecentCancel;
	private ListView listHistory;
	
	private  FavoriteHistoryDAO  history = new FavoriteHistoryDAO(); 
	private ArrayList<String> arrHistory = null;
	private ArrayAdapter<String> adapter = null;
	private String wordisdeleted = "";
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_recent);
		
		btnEditrecent = (ImageView)findViewById(R.id.btnEditrecent);
		btnRecentDelete = (ImageView)findViewById(R.id.btnRecentDelete);
		btnRecentDeleteAll = (ImageView)findViewById(R.id.btnRecentDeleteAll);
		btnRecentCancel = (ImageView)findViewById(R.id.btnRecentCancel);
		listHistory = (ListView)findViewById(R.id.listRecentSearch);
		
		try {
			arrHistory  = history.ReadFile(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(arrHistory == null){
			Toast.makeText(this,"IS EMPTY", Toast.LENGTH_SHORT).show();
		}
		else {
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrHistory);
			listHistory.setAdapter(adapter);
			btnEditrecent.setVisibility(0);
		}
		
		btnRecentDelete.setVisibility(-1);
		btnRecentDeleteAll.setVisibility(-1);
		btnRecentCancel.setVisibility(-1);
		
		btnEditrecent.setOnClickListener(this);
		btnRecentDelete.setOnClickListener(this);
		btnRecentDeleteAll.setOnClickListener(this);
		btnRecentCancel.setOnClickListener(this);
		
		listHistory.setOnItemClickListener(this);
		
	}
	FavoriteHistoryDAO favoriteHistory = new FavoriteHistoryDAO();
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnEditrecent:
			btnEditrecent.setVisibility(-1);
			btnRecentDelete.setVisibility(0);
			btnRecentDeleteAll.setVisibility(0);
			btnRecentCancel.setVisibility(0);
			break;
		case R.id.btnRecentDelete:
			if(wordisdeleted != ""){
				try {
					favoriteHistory.DeleteItem(wordisdeleted, 1);
					arrHistory.remove(wordisdeleted);
					adapter.notifyDataSetChanged();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(this,"Deleted", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btnRecentDeleteAll:
			favoriteHistory.DeleteAll(1);
			arrHistory.removeAll(arrHistory);
			adapter.notifyDataSetChanged();
			break;
		case R.id.btnRecentCancel:
			btnEditrecent.setVisibility(0);
			btnRecentDelete.setVisibility(-1);
			btnRecentDeleteAll.setVisibility(-1);
			btnRecentCancel.setVisibility(-1);
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		wordisdeleted = arrHistory.get(arg2);
	}

}
