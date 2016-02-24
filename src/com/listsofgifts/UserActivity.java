package com.listsofgifts;

import android.util.Log;
import android.util.SparseBooleanArray;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class UserActivity extends Activity {
	
	ListView lv;
	//ArrayList<String> wishArray;
	ArrayAdapter<String> adapter;
	EditText wishField;
	Button addBtn, delBtn;
	ImageButton iconBtn1, iconBtn2, iconBtn3;
	private static final String TAG = UserActivity.class.getSimpleName();
	DBAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		Log.d(TAG, "In the onCreate() User");
		
		wishField = (EditText) findViewById(R.id.user_wishField);
		addBtn = (Button) findViewById(R.id.userAddBtn);
		delBtn = (Button) findViewById(R.id.userDelBtn);
		iconBtn1 = (ImageButton) findViewById(R.id.iconBtn1);
		iconBtn2 = (ImageButton) findViewById(R.id.iconBtn2);
		iconBtn3 = (ImageButton) findViewById(R.id.iconBtn3);
		lv = (ListView) findViewById(R.id.wishList);
		
		updateList();	
				
		addBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String wish = wishField.getText().toString();
				dbAdapter.insertWish("admin", wish);
				wishField.setText("");
				updateList();
				//adapter.notifyDataSetChanged();
				//lv.setAdapter(adapter);
			}
		});
		delBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			
			}
		});
		
		iconBtn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Change view to home/my info
				Intent userInfoScreen = new Intent(UserActivity.this, UserInfoActivity.class);
				startActivity(userInfoScreen);
				adapter.notifyDataSetChanged();
			}
		});
		iconBtn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Show my list of Users
		/*		Intent loginScreen = new Intent(UserActivity.this, MemberListActivity.class);
				startActivity(memberScreen);
		*/
				adapter.notifyDataSetChanged();
			}
		});
		iconBtn3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Logout
				adapter.notifyDataSetChanged();
				Intent loginScreen = new Intent(UserActivity.this, MainActivity.class);
				startActivity(loginScreen);
			}
		});
	}
	
	public void updateList() {
		ArrayList<String> wishArray = new ArrayList<String>();
		dbAdapter = new DBAdapter(UserActivity.this);
		dbAdapter.open();
		wishArray = dbAdapter.showWishes("admin");
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, wishArray); 		
		lv.setAdapter(adapter);
		
		//return wishArray;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
