package com.example.dmitry.frenchalphabet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry on 27.10.2016.
 */

public class ContollerDataBase {
    private SQLDataBase mydatabase;
    private Context mycontext;
    private SQLiteDatabase database;

    public ContollerDataBase(Context c) {
        mycontext = c;
    }

    public ContollerDataBase open() {
        mydatabase = new SQLDataBase(mycontext);
        database = mydatabase.getWritableDatabase();
        return this;

    }

    public void close() {
        mydatabase.close();
    }



    public Cursor query() {
        Cursor cursor = database.query(mydatabase.TABLE, null, null,
                null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public long insert(SQLiteDatabase db, String letter, String transcription, int coef){
        ContentValues contentValue = new ContentValues();

        contentValue.put(mydatabase.KEY_LETTER, letter);
        contentValue.put(mydatabase.KEY_TRANSCRIPTION, transcription);
        contentValue.put(mydatabase.KEY_COEF, coef);

        long d = db.insert(mydatabase.TABLE, null, contentValue);
        return d;
    }

    public void clearTable(){
        database.execSQL("delete from "+ mydatabase.TABLE);
    }

    public List getList(){
        Cursor cursor = query();
        List<ModelforAlphabet> list = new ArrayList<>();
        if(cursor.moveToFirst()){

            do{
                ModelforAlphabet modelforAlphabet = new ModelforAlphabet();
                modelforAlphabet.setLetter(cursor.getString(cursor.getColumnIndex(mydatabase.KEY_LETTER)));
                modelforAlphabet.setTranscription(cursor.getString(cursor.getColumnIndex(mydatabase.KEY_TRANSCRIPTION)));
                modelforAlphabet.setCoef(cursor.getInt(cursor.getColumnIndex(mydatabase.KEY_COEF)));
                list.add(modelforAlphabet);

            } while (cursor.moveToNext());

        }
        return list;
    }

    public void setList(ArrayList<ModelforAlphabet> list){
        clearTable();
        for(ModelforAlphabet model: list){
            long d = insert(database, model.getLetter(), model.getTranscription(), model.getCoef());
        }



    }


}
