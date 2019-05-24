package com.example.android_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabasePerson extends SQLiteOpenHelper {

    static final String DB_NAME ="person.db";
    public DatabasePerson( Context context)
    {
        super(context, DB_NAME, null, 1);
    }

    public void QueryData(String sql)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String queryCreateTable ="create table Person(id integer primary key autoincrement, name text, image int);";
//        db.execSQL(queryCreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertPerson(String newName,int newImage)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",newName);
        values.put("image",newImage);

        db.insert("Person",null,values);
    }

    public void updatePerson(int id,String newName,int newImage)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",newName);
        values.put("image",newImage);

        db.update("Person",values,"id =?",new String[]{String.valueOf(id)});
    }

    public void deletePerson(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Person","id=?",new String[]{String.valueOf(id)});
    }

    public void getDataBase(ArrayList<Person> personArrayList)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor= db.query(false,"Person",null,null,null,null,null,null,null);

        while (cursor.moveToNext())
        {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int img= cursor.getInt(cursor.getColumnIndex("image"));

            personArrayList.add(new Person(name,img));
        }
    }

    public void setDataBase(ArrayList<Person> personArrayList)
    {
        int i;
        int id,newImage;
        String newName;
        for(i=0;i<10;i++)
        {
            id= i+1;
            newName= personArrayList.get(i).getName();
            newImage=personArrayList.get(i).getImage();

            updatePerson(id,newName,newImage);
        }
    }

    public int isEmpty()
    {
        int check=1;
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT count('id') FROM Person",null);
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0)
        {
            check =0;
        }


        return check;
    }

}
