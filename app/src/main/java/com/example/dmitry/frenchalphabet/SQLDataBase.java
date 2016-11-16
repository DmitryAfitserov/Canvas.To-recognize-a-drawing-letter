package com.example.dmitry.frenchalphabet;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Dmitry on 27.10.2016.
 */

public class SQLDataBase extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "DATA_SETTINGS";

    public static final String TABLE = "ALPHABETTABLE";
    public static final String KEY_ID = "_id";
    public static final String KEY_LETTER = "letter";
    public static final String KEY_TRANSCRIPTION = "transcription";
    public static final String KEY_COEF = "coef";
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_LETTER + " TEXT, "
            + KEY_TRANSCRIPTION + " TEXT, " + KEY_COEF + " INTEGER)";



    public SQLDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        insert(db, "Aa", "aah", 0);
        insert(db, "Bb", "beh", 0);
        insert(db, "Cc", "seh", 0);
        insert(db, "Dd", "deh", 0);
        insert(db, "Ee", "eh", 0);
        insert(db, "Ff", "eff", 0);
        insert(db, "Gg", "jeh", 0);
        insert(db, "Hh", "ahsh", 0);
        insert(db, "Ii", "ee", 0);
        insert(db, "Jj", "jee", 0);
        insert(db, "Kk", "kah", 0);
        insert(db, "Ll", "ell", 0);
        insert(db, "Mm", "em", 0);
        insert(db, "Nn", "en", 0);
        insert(db, "Oo", "oh", 0);
        insert(db, "Pp", "peh", 0);
        insert(db, "Qq", "koo", 0);
        insert(db, "Rr", "air", 0);
        insert(db, "Ss", "ess", 0);
        insert(db, "Tt", "teh", 0);
        insert(db, "Uu", "ooh", 0);
        insert(db, "Vv", "veh", 0);
        insert(db, "Ww", "doo-blan-veh", 0);
        insert(db, "Xx", "eeks", 0);
        insert(db, "Yy", "ee-grek", 0);
        insert(db, "Zz", "zed", 0);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    private void insert(SQLiteDatabase db, String letter, String transcription, int coef){
        ContentValues contentValue = new ContentValues();

        contentValue.put(KEY_LETTER, letter);
        contentValue.put(KEY_TRANSCRIPTION, transcription);
        contentValue.put(KEY_COEF, coef);


         long d = db.insert(TABLE, null, contentValue);
        Log.d("EEE", "вставка " + d);

    }
}
