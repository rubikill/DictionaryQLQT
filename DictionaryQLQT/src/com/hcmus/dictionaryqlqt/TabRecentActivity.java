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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class TabRecentActivity extends Activity implements OnClickListener, OnItemClickListener{

	private ImageView btnEditrecent, btnRecentDelete, btnRecentDeleteAll, btnRecentCancel;
	private ListView listHistory;
	
	private  IFavoriteHistory  recent ;
	private ArrayList<String> arrHistory = null;
	private ArrayAdapter<String> adapter = null;
	private MyArrayAdapter myadapter = null;
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_recent);
		
		btnEditrecent = (ImageView)findViewById(R.id.btnEditrecent);
		btnRecentDelete = (ImageView)findViewById(R.id.btnRecentDelete);
		btnRecentDeleteAll = (ImageView)findViewById(R.id.btnRecentDeleteAll);
		btnRecentCancel = (ImageView)findViewById(R.id.btnRecentCancel);
		listHistory = (ListView)findViewById(R.id.listRecentSearch);
		
		recent = new FavoriteHistoryImp(TabRecentActivity.this); 
		
		btnRecentDelete.setVisibility(-1);
		btnRecentDeleteAll.setVisibility(-1);
		btnRecentCancel.setVisibility(-1);
		
		btnEditrecent.setOnClickListener(this);
		btnRecentDelete.setOnClickListener(this);
		btnRecentDeleteAll.setOnClickListener(this);
		btnRecentCancel.setOnClickListener(this);
		
		listHistory.setOnItemClickListener(this);
		
	}
	
	@Override
	protected void onResume() {		
		super.onResume();
		arrHistory  = recent.ReadTable("Recent");
		if(arrHistory == null){
			Toast.makeText(this,"IS EMPTY", Toast.LENGTH_SHORT).show();
		}
		else {
			adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrHistory);
			listHistory.setAdapter(adapter);
			btnEditrecent.setVisibility(0);
		}
	}
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btnEditrecent:
			myadapter = new MyArrayAdapter( this, R.layout.my_item_layout,arrHistory);
			listHistory.setAdapter(myadapter);
			btnEditrecent.setVisibility(-1);
			btnRecentDelete.setVisibility(0);
			btnRecentDeleteAll.setVisibility(0);
			btnRecentCancel.setVisibility(0);
			break;
		case R.id.btnRecentDelete:
			Boolean isDelted = false;
			int size = listHistory.getChildCount();
			for(int i = size -1 ; i >=0 ; i--){
				View view = listHistory.getChildAt(i);
				CheckBox checkbox = (CheckBox)view.findViewById(R.id.chkbitem);
				if(checkbox.isChecked()){
					arrHistory.remove(i);
					isDelted = true;
					String worddelete = arrHistory.get(i);
					recent.DeleteItem(worddelete, "Recent");
				}
			}
			if(isDelted == true){
				myadapter.notifyDataSetChanged();
			}
			break;
		case R.id.btnRecentDeleteAll:
			recent.DeleteAll("Recent");
			arrHistory.removeAll(arrHistory);
			adapter.notifyDataSetChanged();
			break;
		case R.id.btnRecentCancel:
			listHistory.setAdapter(adapter);
			btnEditrecent.setVisibility(0);
			btnRecentDelete.setVisibility(-1);
			btnRecentDeleteAll.setVisibility(-1);
			btnRecentCancel.setVisibility(-1);
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		String word = arrHistory.get(arg2);
		FullscreenActivity.getInstance().setDictionaryTab(word);
	}

}
