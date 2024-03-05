package com.example.game;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "YourDatabaseName";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "Cookie";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_IDUSER = "iduser";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_COOKIE = "cookie";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_TIME = "time";

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_IDUSER + " TEXT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_COOKIE + " TEXT," +
                    COLUMN_LEVEL + " TEXT," +
                    COLUMN_TIME + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

