package com.vanh1200.newsfilter.SLQite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.vanh1200.newsfilter.Model.News;

import java.util.ArrayList;

public class DownloadDao {
    private static final String TAG = "downloadDAO";

//    public static FavoriteDAO instance;

    private Context mContext;
    private DBHelper mHelper;
    private SQLiteDatabase database;
    private String[] allColumn = {
            DBHelper.COLUMN_LINK,
            DBHelper.COLUMN_TITLE,
            DBHelper.COLUMN_DESCRIPTION,
            DBHelper.COLUMN_IMAGE,
            DBHelper.COLUMN_PUBDATE,
            DBHelper.COLUMN_PUBLISHER
    };

    public DownloadDao(Context context) {
        this.mContext = context;
        mHelper = new DBHelper(context);
    }

    public void addNews(News news){
        database = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_LINK, news.getLink());
        values.put(DBHelper.COLUMN_TITLE, news.getTitle());
        values.put(DBHelper.COLUMN_DESCRIPTION, news.getDescription());
        values.put(DBHelper.COLUMN_IMAGE, news.getImage());
        values.put(DBHelper.COLUMN_PUBDATE, news.getPubDate());
        values.put(DBHelper.COLUMN_PUBLISHER, news.getPublisher());
        database.insert(DBHelper.TABLE_DOWNLOAD, null, values);
        database.close();
    }

    public void deleteNews(News news){
        database = mHelper.getWritableDatabase();
        int i = database.delete(mHelper.TABLE_DOWNLOAD, DBHelper.COLUMN_LINK + "=?", new String[]{news.getLink()});
        Log.d(TAG, "deleted " + i + "item");
        database.close();
    }

    public void updateNews(News news){
        database = mHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_LINK, news.getLink());
        values.put(DBHelper.COLUMN_TITLE, news.getTitle());
        values.put(DBHelper.COLUMN_DESCRIPTION, news.getDescription());
        values.put(DBHelper.COLUMN_IMAGE, news.getImage());
        values.put(DBHelper.COLUMN_PUBDATE, news.getPubDate());
        values.put(DBHelper.COLUMN_PUBLISHER, news.getPublisher());
        database.update(DBHelper.TABLE_DOWNLOAD, values, DBHelper.COLUMN_LINK + " =?", new String[]{news.getLink()});
        database.close();
    }

    public ArrayList<News> getAllNews(){
        ArrayList<News> arrNews = new ArrayList<>();
        database = mHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_DOWNLOAD, null);
        if(cursor.moveToFirst()){
            do{
                News news = new News();
                news.setLink(cursor.getString(0));
                news.setTitle(cursor.getString(1));
                news.setDescription(cursor.getString(2));
                news.setImage(cursor.getString(3));
                news.setPubDate(cursor.getString(4));
                news.setPublisher(cursor.getString(5));
                news.setDownloaded(true);
                arrNews.add(news);
            }while(cursor.moveToNext());
        }
        cursor.close();
        database.close();
        return arrNews;
    }

    public boolean isContain(News news){
        ArrayList<News> arrNews = getAllNews();
        for (News news_temp: arrNews) {
            if(news_temp.getLink().equals(news.getLink()))
                return true;
        }
        return false;
    }
}
