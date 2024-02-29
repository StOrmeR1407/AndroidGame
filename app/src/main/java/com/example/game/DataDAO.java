package com.example.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.Map;

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
    public long insertData(String iduser, String name,String cookie) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDUSER, iduser);
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_COOKIE, cookie);
        return database.insert(DatabaseHelper.TABLE_NAME, null, values);
    }

    // Truy vấn dữ liệu
    public Map<String, String> getAllDataAsMap() {
        Map<String, String> resultMap = new HashMap<>();

        String[] columns = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_IDUSER, DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_COOKIE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);

        // Di chuyển con trỏ đến vị trí đầu tiên của Cursor
        if (cursor.moveToFirst()) {
            do {
                // Lấy dữ liệu từ Cursor cho mỗi cột
                long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String iduser = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_IDUSER));
                String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
                String cookie = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_COOKIE));
                // Lưu trữ dữ liệu vào Map
                resultMap.put("ID", String.valueOf(id));
                resultMap.put("ID User", iduser);
                resultMap.put("Name", name);
                resultMap.put("Cookie", cookie);
            } while (cursor.moveToNext());
        }

        // Đóng Cursor sau khi sử dụng xong
        cursor.close();

        return resultMap;
    }


    // Cập nhật dữ liệu
    public int updateData(long id, String iduser, String name,String cookie) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_IDUSER, iduser);
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_COOKIE, cookie);
        return database.update(DatabaseHelper.TABLE_NAME, values, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    // Xoá dữ liệu
    public int deleteData(long id) {
        return database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public int getDataCount() {
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_NAME;
        Cursor cursor = database.rawQuery(query, null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }

        // Đóng Cursor sau khi sử dụng xong
        cursor.close();

        return count;
    }
}

