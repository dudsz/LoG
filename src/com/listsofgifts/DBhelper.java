package com.listsofgifts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBhelper extends SQLiteOpenHelper {
	
	private static final String TAG = DBhelper.class.getSimpleName();
	private static final String dbName = "DB1";
	private static final int dbVersion = 1;
	
	public static final String loginTable = "login";
	public static final String wishTable = "wishTable";
	public static final String membersListTable = "mlTable";

	public static final String lUId = "UniqueId";
	public static final String lUn = "Username";
	public static final String lPw = "Password";
	public static final String lEmail = "Email";

	public static final String wUId = "UniqueId";
	public static final String wUn = "Username";
	public static final String wWish = "Wish";
	
	public static final String mlUId = "UniqueId";
	public static final String mlMemb = "Member";
	public static final String mlEmail = "MemberEmail";
	
	private static final String createLTable = "create table " + loginTable + "(" +
			lUId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + lUn + " TEXT UNIQUE, " + lPw + " TEXT NOT NULL, " + 
			lEmail + " TEXT UNIQUE)";
	private static final String createWTable = "create table " + wishTable + "(" +
			wUId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + wUn + " TEXT NOT NULL, " + wWish + " TEXT UNIQUE)";
	private static final String createMLTable = "create table " + membersListTable + "(" +
			mlUId + " INTEGER PRIMARY KEY AUTOINCREMENT, " + mlMemb + " TEXT UNIQUE, " + mlEmail + " TEXT UNIQUE)";
	
	SQLiteDatabase db;
		
	public DBhelper(Context context) {
		super(context, dbName, null, dbVersion);
	}
	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createLTable);
		db.execSQL(createWTable);
		db.execSQL(createMLTable);
		
		Log.d(TAG, "Database tables created");
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("TaskDBAdapter", "Upgrading from version " + oldVersion + 
				" to " + newVersion + ", which will destroy all old data");
		db.execSQL("drop table if exists loginTable");
		db.execSQL("drop table if exists wishTable");
		db.execSQL("drop table if exists membersListTable");
		
		onCreate(db);
	}
}
