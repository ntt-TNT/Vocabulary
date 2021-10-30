package com.example.vocabulary.db;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAction {

    private static final String TAG = "dbAction";
    private static WordsDBHelper mDbHelper;

    //使用Sql语句删除单词
    public static void DeleteUserSql(String strWord){
        String sql="delete from Vocabulary where WordName='"+strWord+"'";
        //Gets the data repository in write mode*/

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Log.d(TAG, "DeleteUserSql: ");
        db.execSQL(sql);
    }
}
