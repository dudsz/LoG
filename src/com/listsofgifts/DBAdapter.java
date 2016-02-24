package com.listsofgifts;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter {

	private SQLiteDatabase db;
	private Context context;
	private DBhelper helper;
	private static final String TAG = DBAdapter.class.getSimpleName();
		
	public DBAdapter(Context c) {
		 context = c;
	}
	
	public DBAdapter open() throws SQLException {
		helper = new DBhelper(context);
		db = helper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		helper.close();
	}
	public long registerUser(String username, String password, String email) {
		ContentValues values = new ContentValues();
		
		values.put(DBhelper.lUn, username);
		values.put(DBhelper.lPw, password);
		values.put(DBhelper.lEmail, email);
		
		long id = db.insert(DBhelper.loginTable, null, values);
		Log.d("RegUser", "New user inserted into sqlite: " + id);
		return id;
	}
	public boolean login(String username, String password) {
		String checkQuery = "SELECT * FROM " + DBhelper.loginTable + " WHERE Username = ? AND"
				+ " Password = ?";
		Cursor c = db.rawQuery(checkQuery, new String[] {username, password});
		if (c != null) {
			if (c.getCount() > 0) {
				Log.d("Login", "User login successful");
				c.close();
				return true;
			}
		}
		c.close();
		return false;		
	}
	public String checkPassword(String username, String password) {
		String checkQuery = "SELECT * FROM " + DBhelper.loginTable + " WHERE Username = ? "
				+ "AND Password = ?";
		Cursor c = db.rawQuery(checkQuery, new String[] {username, password});
		if (c.getCount() < 0) {
			Log.d("PwCheck", "User does not exist");
			c.close();
			return "No match";
		} else {
			c.moveToFirst();
			String pwConf = c.getString(c.getColumnIndex("Password"));
			Log.d("PwCheck", "Password does not match");
			c.close();
			return pwConf;
		}
	}
	public int deleteUser(String username) {
		String query = "Username = ?";
		int deleted = db.delete(DBhelper.loginTable, query, new String[] {username});
		
		return deleted;
	}
	public int updateUser(String un, String pw, String email) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBhelper.lUn, un);
		values.put(DBhelper.lPw, pw);
		values.put(DBhelper.lEmail, email);
		
		int i = db.update(DBhelper.loginTable, values, DBhelper.lUn + " = ?", new String[] {un});
		db.close();
		return i;
	}
	public HashMap<String, String> getUserData() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT * FROM " + DBhelper.loginTable;
		
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		
		c.moveToFirst();
		if (c.getCount() > 0) {
			user.put(DBhelper.lUn, c.getString(1));
			user.put(DBhelper.lPw, c.getString(2));
			user.put(DBhelper.lEmail, c.getString(3));
		}
		c.close();
		db.close();
		Log.d(TAG, "Fetching user from db: " + user.toString());
		return user;
	}	
	/**********************************************
	 * 
	 *    		Wish handling
	 * 
	 **********************************************/
	
	public void insertWish(String username, String wish) {
		ContentValues values = new ContentValues();
		
		values.put(DBhelper.wUn, username);
		values.put(DBhelper.wWish, wish);
		
		db.insert(DBhelper.wishTable, null, values);
		Log.d(TAG, "New wish inserted into sqlite");
	}
	public int deleteWish(String username, String wish) {
		String query = "Username = ? AND Wish = ? ";
		int deleted = db.delete(DBhelper.wishTable, query, new String[] {username, wish});
		
		return deleted;
	}
	public ArrayList<String> showWishes(String username) {
		ArrayList<String> wishes = new ArrayList<String>();
		String query = "SELECT * FROM " + DBhelper.wishTable + " WHERE Username = ?";
		Cursor c = db.rawQuery(query, new String[] {username});
		
		if (c.moveToFirst()) {
			do {
				String wish = c.getString(c.getColumnIndex("Wish"));
				wishes.add(wish);
			} while (c.moveToNext());
		}
		c.close();
		return wishes;		
	}
}
