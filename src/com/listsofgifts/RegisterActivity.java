package com.listsofgifts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	private static final String TAG = RegisterActivity.class.getSimpleName();
	private ProgressDialog pDialog;
	public static PreparedStatement ps;
	public static Connection con;
	EditText tUn, tPw, tPwc, tEmail;
	Button cancelBtn, regBtn;
	DBAdapter dbAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		Log.d(TAG, "In the onCreate() " + this);
		String lHost = "52.34.67.20";
		String username = "";
		String password = "";
		try {
			String urlString = "jdbc:mysql://" + lHost + ":" + 3306 + "/test";
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(urlString, username, password);
		} catch (Exception e) {
			Log.d(TAG, "Could not connect");
			e.printStackTrace();
		}
		
		tUn = (EditText) findViewById(R.id.reg_un);
		tPw = (EditText) findViewById(R.id.reg_pw);
		tPwc = (EditText) findViewById(R.id.reg_pwc);
		tEmail = (EditText) findViewById(R.id.reg_email);
		cancelBtn = (Button) findViewById(R.id.regCancelBtn);
		regBtn = (Button) findViewById(R.id.regRegBtn);
		
		// Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
				
		regBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String un = tUn.getText().toString();
				String pw = tPw.getText().toString();
				String pwc = tPwc.getText().toString();
				String email = tEmail.getText().toString();	
				//register(un, pw, pwc, email);
				if (regUser(un, pw, pwc, email) > 0) {
					Intent regScreen = new Intent(RegisterActivity.this, UserActivity.class);
					startActivity(regScreen);
				} else {
					Toast.makeText(RegisterActivity.this, "Failed to register",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		
		cancelBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent userScreen = new Intent(RegisterActivity.this, MainActivity.class);
				startActivity(userScreen);
			}
		});
	}
	public void register(String un, String pw, String pwc, String email) {
		dbAdapter = new DBAdapter(RegisterActivity.this);
		dbAdapter.open();
		if (un.length() > 0 && pw.length() > 0 && pwc.length() > 0) {
			if (pw.equals(pwc)) {
				Log.d(TAG, "Registering user");
				if (dbAdapter.registerUser(un, pw, email) != -1) {
					Log.d(TAG, "Registering successful");
					// Send registration info
					Intent regScreen = new Intent(RegisterActivity.this, UserActivity.class);
					startActivity(regScreen);
				} else {
					Toast.makeText(RegisterActivity.this, "Failed to register",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(RegisterActivity.this, "Passwords do not match",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(RegisterActivity.this, "Username, password and confirmation of password must be"
					+ "filled in", Toast.LENGTH_LONG).show();
		}
	}
	public int regUser(String un, String pw, String pwc, String email) {
		String lHost = "52.34.67.20";
		String username = "";
		String password = "";
		int result = 0;
		if (un.length() > 0 && pw.length() > 0 && pwc.length() > 0) {
			if (pw.equals(pwc)) {
				Log.d(TAG, "Registering user");
				try {
					String urlString = "jdbc:mysql://" + lHost + ":" + 3306 + "/test";
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(urlString, username, password);
				
					System.out.println("Connected");
					String query = "insert into login (username, password, email) values (?, ?, ?)";
					ps = con.prepareStatement(query);
					
					//ps.setNull(1, java.sql.Types.INTEGER);
					ps.setString(1, un);
					ps.setString(2, pw);
					ps.setString(3, email);
					 
					result = ps.executeUpdate();			
					System.out.println("Registered");
					return result;
					} catch (Exception e) {
						Toast.makeText(RegisterActivity.this, "Failed to register",
								Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				} else {
					Toast.makeText(RegisterActivity.this, "Passwords do not match",
							Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(RegisterActivity.this, "Username, password and confirmation of password must be"
						+ "filled in", Toast.LENGTH_LONG).show();
			}
		return result;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
