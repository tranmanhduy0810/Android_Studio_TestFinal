package com.webmientay.baitaptonghop.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "PhoneManager";
    public static int DB_VER = 1;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS phones(id VARCHAR(255) PRIMARY KEY, name VARCHAR(255) NOT NULL, price INTERGER NOT NULL)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS phones";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

}
