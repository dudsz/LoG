package com.listsofgifts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		
		EditText username = (EditText) findViewById(R.id.info_un);
		EditText pw = (EditText) findViewById(R.id.info_pw);
		EditText pwc = (EditText) findViewById(R.id.info_pwc);
		EditText email = (EditText) findViewById(R.id.info_email);
		Button cancelBtn = (Button) findViewById(R.id.infoCancelBtn);
		Button saveBtn = (Button) findViewById(R.id.infoSaveBtn);
		
		username.setText("my username", TextView.BufferType.EDITABLE);
		pw.setText("my pw", TextView.BufferType.EDITABLE);
		pwc.setText("my pwc", TextView.BufferType.EDITABLE);
		email.setText("my email", TextView.BufferType.EDITABLE);
		
		saveBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent userScreen = new Intent(UserInfoActivity.this, UserActivity.class);
				startActivity(userScreen);
			}
		});
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent mainScreen = new Intent(UserInfoActivity.this, UserActivity.class);
				startActivity(mainScreen);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
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
