package com.example.Game4.Database;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Coordinates_DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "coordinates.db";
    public static final String DB_TABLE_EASY = "easy";
    public static final String DB_TABLE_HARD = "hard";
    public static final String DB_TABLE_NORMAL = "normal";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "name";
    Context context;
    SQLiteDatabase database;

    public Coordinates_DB(Context context2) {
        super(context2, DATABASE_NAME, null, 1);
        this.context = context2;
    }

    public void creteDataBase() {
        if (!checkDataBase()) {
            getWritableDatabase();
            try {
                copyDataBase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        if (checkDB != null) {
            return true;
        }
        return false;
    }

    private void copyDataBase() throws IOException {
        AssetManager assets = this.context.getAssets();
        String str = DATABASE_NAME;
        InputStream inputStream = assets.open(str);
        OutputStream outputStream = new FileOutputStream(this.context.getDatabasePath(str).getPath());
        byte[] buffer = new byte[1024];
        while (true) {
            int read = inputStream.read(buffer);
            int length = read;
            if (read > 0) {
                outputStream.write(buffer, 0, length);
            } else {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                return;
            }
        }
    }

    public void openDatabase() {
        this.database = SQLiteDatabase.openDatabase(this.context.getDatabasePath(DATABASE_NAME).getPath(), null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void onCreate(SQLiteDatabase db) {
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public float Read(String tempTable, String tempName, String tempColumn) {
        this.database = getWritableDatabase();
        Cursor cursor = this.database.query(tempTable, null, "name LIKE ?", new String[]{tempName}, null, null, null);
        cursor.moveToFirst();
        float coords = cursor.getFloat(cursor.getColumnIndexOrThrow(tempColumn));
        cursor.close();
        return coords;
    }
}