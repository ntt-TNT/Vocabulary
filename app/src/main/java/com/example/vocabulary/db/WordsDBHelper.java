package com.example.vocabulary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.vocabulary.Bean.Word;

public class WordsDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "wordsdb";//数据库名字
    private final static int DATABASE_VERSION = 1;//数据库版本

    private SQLiteDatabase sqLiteDatabase;

    //建表SQL
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " + Word.TABLE_NAME + " (" + Word.COLUMN_NAME_WORD + " text PRIMARY KEY ," +
              Word.COLUMN_NAME_MEANING + " TEXT" + ","+ Word.COLUMN_NAME_SAMPLE + " TEXT" + " )";
    //删表SQL
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + Word.TABLE_NAME;

    public WordsDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);
        onCreate(sqLiteDatabase);
    }
}
