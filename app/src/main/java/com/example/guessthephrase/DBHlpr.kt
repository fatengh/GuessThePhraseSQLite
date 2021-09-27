package com.example.guessthephrase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHlpr(context: Context): SQLiteOpenHelper(context,"GuessPhrases.db",null,1) {

    override fun onCreate(p0: SQLiteDatabase?) {
        if (p0!=null)
        {
            p0.execSQL("create table Phrases(Id integer primary key ,Pharse text)")
        }

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    fun addPha(text :String): Long {
        val cv= ContentValues()
        cv.put("Pharse",text)
        var s = this.writableDatabase.insert("Phrases",null,cv)
        return s
    }

    fun getPha():ArrayList<String>{
        val phs=ArrayList<String>()
        val cursor: Cursor =this.readableDatabase.query("Phrases",null,null,null,null,null,null)

        if(cursor.moveToFirst()){
            var ph=cursor.getString(cursor.getColumnIndex("Pharse"))
            phs.add(ph)
            while (cursor.moveToNext()){
                ph=cursor.getString(cursor.getColumnIndex("Pharse"))
                phs.add(ph)
            }
        }

        cursor.close()
        return phs

    }
}