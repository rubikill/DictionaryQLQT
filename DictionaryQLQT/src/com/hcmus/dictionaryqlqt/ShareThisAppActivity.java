package com.hcmus.dictionaryqlqt;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.ProfilePictureView;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;

public class ShareThisAppActivity extends ListActivity implements
		OnClickListener {

	static final String[] TAB_MORE = new String[] { "Email", "Facebook",
			"Twitter" };
	
	static final int EMAIL = 0;
	static final int FACEBOOK = 1;
	static final int TWITTER = 2;
	private PopupWindow facebookPpopup;
	private UiLifecycleHelper uiHelper;
	LoginButton loginButton;
	Button btnShare;
	Button btn_face_close;
	boolean facebookLogin = false;
	private ProfilePictureView profilePictureView;
	String uid = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_tab_more);
		setListAdapter(new ShareThisAppAdapter(this, TAB_MORE));
		//--------------facebook-------------
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		loginButton = (LoginButton) findViewById(R.id.authButton);
		if (isLoggedIn())
			facebookLogin = true;
		else
			facebookLogin = false;
		//---------------------/facebook--------------
	}
	//------------------------facbook
/***
 * ham callback
 */

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	//kiem tra trang thai login
	public boolean isLoggedIn() {
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			return true;
		} else {
			return false;
		}
	}
	//thay doi trang thai
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			makeMeRequest(session);
			facebookLogin = true;
			try {
				btnShare.setVisibility(View.VISIBLE);
			} catch (Exception ex) {
			}

		} else if (state.isClosed()) {
			facebookLogin = false;
			uid = null;

			btnShare.setVisibility(View.INVISIBLE);
			profilePictureView.setProfileId(uid);
		}

	}
	
	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					@Override
					public void onCompleted(GraphUser user, Response response) {
						// If the response is successful
						if (session == Session.getActiveSession()) {
							if (user != null) {
								// Set the id for the ProfilePictureView
								// view that in turn displays the profile
								// picture.
								uid = user.getId();
								profilePictureView.setProfileId(uid);
								// Set the Textview's text to the user's name.
							}
						}
						if (response.getError() != null) {
							// Handle errors, will do so later.
						}
					}
				});
		request.executeAsync();
	}
	// khoi tao popup share facebook
		private void initFacebookPopup() {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) ShareThisAppActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.popup_loginfacebook,
					(ViewGroup) findViewById(R.id.popup_element));
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = (int) (size.x * 0.8);
			int height = (int) (size.y * 0.7);
			facebookPpopup = new PopupWindow(layout, width, height, true);
			facebookPpopup.showAtLocation(layout, Gravity.CENTER, 0, 0);
			btnShare = (Button) layout.findViewById(R.id.btnShare);
			btn_face_close = (Button) layout.findViewById(R.id.btn_face_close);
			btnShare.setOnClickListener(this);
			btn_face_close.setOnClickListener(this);
			if (!facebookLogin)
				btnShare.setVisibility(View.INVISIBLE);
			else
				btnShare.setVisibility(View.VISIBLE);
			profilePictureView = (ProfilePictureView) layout
					.findViewById(R.id.selection_profile_pic);
			profilePictureView.setCropped(true);
			profilePictureView.setProfileId(uid);
		}
		
		private void shareToFacebook() {
			// TODO Auto-generated method stub
			Bundle params = new Bundle();
			params.putString("name", "Dictionary-QLQPPM-N2");
			params.putString("caption", "Contact us for more details.");
			params.putString("description",
					"Dictionary project of university of Siences.");
			params.putString("picture",
					"http://www.hcmus.edu.vn/images/stories/logo-khtn.png");

			WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this,
					Session.getActiveSession(), params)).setOnCompleteListener(
					new OnCompleteListener() {

						@Override
						public void onComplete(Bundle values,
								FacebookException error) {

						}

					}).build();
			feedDialog.show();
		}
		
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		switch (position) {
		case EMAIL:

			break;
		case FACEBOOK:
			initFacebookPopup();
			break;
		case TWITTER:

			break;
		default:
			break;
		}
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnShare:
			shareToFacebook();
			break;
		case R.id.btn_face_close:
			facebookPpopup.dismiss();
		default:
			break;
		}
	}

	

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}
}
