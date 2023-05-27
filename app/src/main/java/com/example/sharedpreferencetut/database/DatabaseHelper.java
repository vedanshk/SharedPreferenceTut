package com.example.sharedpreferencetut.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.sharedpreferencetut.entity.Contact;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int  DB_VERSION = 1;
    public static final String DB_NAME  =" contact_db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null , DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Contact.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME);
            onCreate(db);
    }

    // Insert Data into Database

    public long insertContact(String name , String email){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();

        values.put(Contact.COLUMN_NAME , name);
        values.put(Contact.COLUMN_EMAIL , email);

        return db.insert(Contact.TABLE_NAME ,  null , values);

    }

    @SuppressLint("Range")
    public Contact getContact(long id){

        Contact contact = null;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.query(Contact.TABLE_NAME , new String[]{Contact.COLUMN_ID , Contact.COLUMN_NAME , Contact.COLUMN_EMAIL}, Contact.COLUMN_ID + "=?" , new String[]{String.valueOf(id)} , null , null , null );
        if( cursor != null){
            cursor.moveToFirst();
            contact = new Contact(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)) ,
                    cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)) ,
                    cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID))
                    );

            cursor.close();
            db.close();

        }


        return  contact;

    }

    public ArrayList<Contact> getAllContact(){

        ArrayList<Contact> contacts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM " +  Contact.TABLE_NAME +  " ORDER BY " + Contact.COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(selectQuery , null);

        if(cursor.moveToFirst()){
            do{
              @SuppressLint("Range") Contact  contact = new Contact(cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)) ,
                        cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)) ,
                        cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID))
                );

              contacts.add(contact);

            }while(cursor.moveToNext());

            cursor.close();
            db.close();
        }
        return contacts;
    }

    public long updateContact(Contact contact){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Contact.COLUMN_NAME , contact.getName());
        values.put(Contact.COLUMN_EMAIL , contact.getEmail());

        return db.update(Contact.TABLE_NAME , values , Contact.COLUMN_ID+ "=?" , new String[]{ String.valueOf(contact.getId())});

    }

    public void deleteContact(Contact contact){

        SQLiteDatabase db = getWritableDatabase();


        db.delete(Contact.TABLE_NAME , Contact.COLUMN_ID + "=?" , new String[]{ String.valueOf(contact.getId())});

    }


}
