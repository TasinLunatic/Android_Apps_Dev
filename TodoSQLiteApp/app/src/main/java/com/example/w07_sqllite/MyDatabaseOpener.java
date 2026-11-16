package com.example.w07_sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDatabaseOpener extends SQLiteOpenHelper {
    public MyDatabaseOpener(@Nullable Context context) {
        super(context, "todo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table todo (id INTEGER PRIMARY KEY AUTOINCREMENT, task TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
