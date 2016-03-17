package com.listsofgifts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/*************************************************
 * 
 * To do:
 * Send username to logged in
 * Send correct username to db with wish
 * Fix delete with correct username
 * Retrieve list with correct username
 * 
 *************************************************/


public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	private ProgressDialog pDialog;
	EditText username, password;
	Button loginBtn, regBtn;
	DBAdapter dbAdapter;
	CheckBox rememberBox;
	private boolean saveLogin;
	private SharedPreferences loginPref;
	private SharedPreferences.Editor loginPrefEditor;
	public static PreparedStatement ps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG, "In the onCreate() Main");
		
		username = (EditText) findViewById(R.id.login_un);
		password = (EditText) findViewById(R.id.login_pw);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		regBtn = (Button) findViewById(R.id.loginRegBtn);
		rememberBox = (CheckBox) findViewById(R.id.loginRememberBox);
		loginPref = getSharedPreferences("loginPrefs", MODE_PRIVATE);
		loginPrefEditor = loginPref.edit();
		
		pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
		
		saveLogin = loginPref.getBoolean("saveLogin", false);
		if (saveLogin == true) {
			username.setText(loginPref.getString("username", ""));
			password.setText(loginPref.getString("password", ""));
			rememberBox.setChecked(true);
		}
		
		regBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent regScreen = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(regScreen);
			}
		});
		loginBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String un = username.getText().toString();
				String pw = password.getText().toString();
				if (rememberBox.isChecked()) {
					loginPrefEditor.putBoolean("saveLogin", true);
					loginPrefEditor.putString("username", un);
					loginPrefEditor.putString("password", pw);
					loginPrefEditor.commit();				
				} else {
					loginPrefEditor.clear();
					loginPrefEditor.commit();
				} 
				if (loginUser(un, pw) > 0) {
					Log.d("LogAc", "Login successfull");
					Toast.makeText(MainActivity.this,
							"Successfully Logged In", Toast.LENGTH_LONG).show();
					// Send login info?
					Intent userScreen = new Intent(MainActivity.this, UserActivity.class);
					startActivity(userScreen);
				} else {
					Toast.makeText(MainActivity.this,
						"Invalid username or password", Toast.LENGTH_LONG).show();
				}
				//login(un, pw);
			}
		});
	}
	public void login(String un, String pw) {
		try {
			dbAdapter = new DBAdapter(MainActivity.this);
			dbAdapter.open();
			Log.d("LogAc", "Trying to login");
			if (un.length() > 0 && pw.length() > 0 && 
					(pw.equals(dbAdapter.checkPassword(un, pw)))) {
				Log.d("LogAc", "Correct length");
				if (dbAdapter.login(un, pw)) {	
					Log.d("LogAc", "Login succeeded");
					Toast.makeText(MainActivity.this,
							"Successfully Logged In", Toast.LENGTH_LONG).show();
					// Send login info?
					Intent userScreen = new Intent(MainActivity.this, UserActivity.class);
					startActivity(userScreen);
				} else {
					Toast.makeText(MainActivity.this,
						"Invalid username or password", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(MainActivity.this,
						"Missing field or incorrect password", Toast.LENGTH_LONG).show();
			}	
		} catch (Exception e) {
			Log.d("LogAc", "Login failed");
			Toast.makeText(MainActivity.this,
					"Username or Password is empty", Toast.LENGTH_LONG).show();
		} 	
	}
	// Add arguments : String host, String port, String un, String pw, String db
	private int loginUser(String un, String pw) {
		List<String> result = new ArrayList<String>();
		String lHost = "ip";
		String username = "username";
		String password = "password";
		try {
			String urlString = "jdbc:mysql://" + lHost + ":" + 3306 + "/test";
			Class.forName("com.mysql.jdbc.Driver"); 
			Connection con = DriverManager.getConnection(urlString, username, password);
		 
			System.out.println("Connected");
			String query = "select username, password, email from login where username =? and password =?";
			ps = con.prepareStatement(query);
			
			ps.setString(1, un);
			ps.setString(2, pw);
				 
			ResultSet rs = ps.executeQuery();	
			if (rs.next()) {
				return 1;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
