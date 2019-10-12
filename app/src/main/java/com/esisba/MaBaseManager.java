package com.esisba;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MaBaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tp6db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE1_NAME = "words";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_WORD = "word";


    private static final String TABLE2_NAME = "user";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SCORE = "score";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql1 = "CREATE TABLE " + TABLE1_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_WORD + " varchar(20) NOT NULL);";
        sqLiteDatabase.execSQL(sql1);

        String sql2 = "CREATE TABLE " + TABLE2_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_SCORE + " INTEGER NOT NULL," +
                COLUMN_NAME + " varchar(50) NOT NULL" +
                ");";
        sqLiteDatabase.execSQL(sql2);
    }

    MaBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql1 = "DROP TABLE IF EXISTS " + TABLE1_NAME + ";";
        sqLiteDatabase.execSQL(sql1);
        onCreate(sqLiteDatabase);

        String sql2 = "DROP TABLE IF EXISTS " + TABLE2_NAME + ";";
        sqLiteDatabase.execSQL(sql2);
        onCreate(sqLiteDatabase);
    }

    boolean addWord(String word) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_WORD, word);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE1_NAME, null, contentValues) != -1;
    }

    void updateUser(int id, int score) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "UPDATE " + TABLE2_NAME + " SET " +
                COLUMN_SCORE + " = " + score + " WHERE " +
                COLUMN_ID + " = " + id ;
        db.execSQL(sql);
    }




    boolean addUser(String name, int score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_SCORE, score);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE2_NAME, null, contentValues) != -1;
    }

    Cursor getAllWords() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE1_NAME, null);
    }
    Cursor getAllUsers() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE2_NAME, null);
    }
}