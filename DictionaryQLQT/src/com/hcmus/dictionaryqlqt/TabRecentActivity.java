package com.hcmus.dictionaryqlqt;

import dao.FavoriteHistoryDAO;
import android.os.Bundle;
import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class TabRecentActivity extends Activity implements OnClickListener{

	private ImageView btnEditrecent, btnRecentDelete, btnRecentDeleteAll, btnRecentCancel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_recent);
		
		btnEditrecent = (ImageView)findViewById(R.id.btnEditrecent);
		btnRecentDelete = (ImageView)findViewById(R.id.btnRecentDelete);
		btnRecentDeleteAll = (ImageView)findViewById(R.id.btnRecentDeleteAll);
		btnRecentCancel = (ImageView)findViewById(R.id.btnRecentCancel);
	
		btnEditrecent.setVisibility(0);
		btnRecentDelete.setVisibility(-1);
		btnRecentDeleteAll.setVisibility(-1);
		btnRecentCancel.setVisibility(-1);
		
		btnEditrecent.setOnClickListener(this);
		btnRecentDelete.setOnClickListener(this);
		btnRecentDeleteAll.setOnClickListener(this);
		btnRecentCancel.setOnClickListener(this);
		
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
			//lay tu can xoa roi them vao thay cho keyword
			//favoriteHistory.DeleteItem(Keyword, 1);
			break;
		case R.id.btnRecentDeleteAll:
			favoriteHistory.DeleteAll(1);
			break;
		case R.id.btnRecentCancel:
			btnEditrecent.setVisibility(0);
			btnRecentDelete.setVisibility(-1);
			btnRecentDeleteAll.setVisibility(-1);
			btnRecentCancel.setVisibility(-1);
			break;
		}
	}

}
