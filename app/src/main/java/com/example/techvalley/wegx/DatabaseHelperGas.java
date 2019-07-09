package com.example.techvalley.wegx;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelperGas extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "GAS_data.db";
    public static final String TABLE_NAME = "DAILY_usage";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "DATE";
    public static final String COL_3 = "USAGE";
    public static final String COL_4 = "MONTH";


    public DatabaseHelperGas(Context context) {
        super(context, DATABASE_NAME, null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,USAGE DOUBLE, MONTH INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String DATE,Double USAGE, Integer MONTH) {
        SQLiteDatabase dbG = this.getWritableDatabase();
        ContentValues contentValuesG = new ContentValues();
        contentValuesG.put(COL_2,DATE);
        contentValuesG.put(COL_3,USAGE);
        contentValuesG.put(COL_4,MONTH);

        long result = dbG.insert(TABLE_NAME,null ,contentValuesG);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase dbG = this.getWritableDatabase();
        Cursor resG = dbG.rawQuery("select * from "+TABLE_NAME,null);
        return resG;
    }



    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
