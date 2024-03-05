package com.example.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DataDAO {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DataDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // Thêm dữ liệu
    public long insertData(String iduser, String username, String cookie, String level, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDUSER, iduser);
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_COOKIE, cookie);
        values.put(DatabaseHelper.COLUMN_LEVEL, level);
        values.put(DatabaseHelper.COLUMN_TIME, time);
        return database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    // Truy vấn dữ liệu
    public Cursor getAllData() {
        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_IDUSER, DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_COOKIE};
        return database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
    }

    // Cập nhật dữ liệu
    public int updateData(long id, String level, String time) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LEVEL, level);
        values.put(DatabaseHelper.COLUMN_TIME, time);
        return database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    // Xoá dữ liệu
    public int deleteData(long id) {
        return database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public int getDataCount() {
        String countQuery = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(countQuery, null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public Map<String, String> getAllDataAsMap() {
        Map<String, String> resultMap = new HashMap<>();

        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_IDUSER, DatabaseHelper.COLUMN_USERNAME, DatabaseHelper.COLUMN_COOKIE, DatabaseHelper.COLUMN_LEVEL, DatabaseHelper.COLUMN_TIME};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

        // Di chuyển con trỏ đến vị trí đầu tiên của Cursor
        if (cursor.moveToFirst()) {
            do {
                // Lấy dữ liệu từ Cursor cho mỗi cột
                long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String iduser = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IDUSER));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USERNAME));
                String cookie = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COOKIE));
                String level = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LEVEL));
                String time = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TIME));
                // Lưu trữ dữ liệu vào Map
                resultMap.put("ID", String.valueOf(id));
                resultMap.put("ID User", iduser);
                resultMap.put("Name", name);
                resultMap.put("Cookie", cookie);
                resultMap.put("Level", level);
                resultMap.put("Time", time);
            } while (cursor.moveToNext());
        }

        // Đóng Cursor sau khi sử dụng xong
        cursor.close();

        return resultMap;
    }

}

