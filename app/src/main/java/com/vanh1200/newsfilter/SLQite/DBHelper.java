package com.vanh1200.newsfilter.SLQite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{
    private static final String TAG = "DBHelper";

    public static String DATABASE_NAME = "news.db";
    public static String TABLE_FAVORITE = "favorites";
    public static String TABLE_DOWNLOAD = "downloads";
    public static String TABLE_HISTORY = "history";

    //Table favorites
    public static String COLUMN_TITLE = "title";
    public static String COLUMN_IMAGE = "image";
    public static String COLUMN_DESCRIPTION = "description";
    public static String COLUMN_PUBDATE = "pubDate";
    public static String COLUMN_PUBLISHER = "publisher";
    public static String COLUMN_LINK = "link";
    private static String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITE + " ("
            + COLUMN_LINK + " TEXT PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT, "
            + COLUMN_DESCRIPTION + " TEXT, "
            + COLUMN_IMAGE + " TEXT, "
            + COLUMN_PUBDATE + " TEXT, "
            + COLUMN_PUBLISHER + " TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITES_TABLE);
        Log.d(TAG, "onCreate: Create Database successfull!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Frome ver " + oldVersion + "to ver: " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITE);
        onCreate(db);
    }

}
