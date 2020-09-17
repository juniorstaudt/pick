package com.br.jrstaudt.pick.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.content.contentValuesOf

private val DB_NAME = "musicClasses_db"

class DBHelper (context:Context): SQLiteOpenHelper(context, DB_NAME, null, 1)  {

    val classItems: List<MusicClass>

        get() {
            val listClasses = ArrayList<MusicClass>()
            val selectQuery = "SELECT * FROM " + MusicClass.CLASSES_TABLE_NAME + " ORDER BY " + MusicClass.CLASSES_DATE + " DESC"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)

            if(cursor.moveToFirst()) {
                do {
                    val mClass = MusicClass()
                    mClass.id = cursor.getInt(cursor.getColumnIndex(MusicClass.CLASSES_ID))
                    mClass.description = cursor.getString(cursor.getColumnIndex(MusicClass.CLASSES_DESC))
                    mClass.date = cursor.getString(cursor.getColumnIndex(MusicClass.CLASSES_DATE))

                    listClasses.add(mClass)
                } while (cursor.moveToNext())
            }

            db.close()
            return listClasses
        }


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(MusicClass.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + MusicClass.CLASSES_TABLE_NAME)
        onCreate(db)
    }

    fun insertMusicClass(item: String): Long {

        val db = this.writableDatabase
        val values = ContentValues()

        values.put(MusicClass.CLASSES_DESC, item)
        val musicClasses = db.insert(MusicClass.CLASSES_TABLE_NAME, null, values)

        db.close()
        return musicClasses
    }

    fun getItem(item: Long): MusicClass {
        val db = this.readableDatabase

        val cursor = db.query(MusicClass.CLASSES_TABLE_NAME,
            arrayOf(MusicClass.CLASSES_ID, MusicClass.CLASSES_DESC, MusicClass.CLASSES_DATE),
            MusicClass.CLASSES_ID + " =?", arrayOf(item.toString()),
            null, null, null, null)

        cursor?.moveToFirst()

        val listClass = MusicClass(
            cursor!!.getInt(cursor.getColumnIndex(MusicClass.CLASSES_ID)),
            cursor!!.getString(cursor.getColumnIndex(MusicClass.CLASSES_DESC)),
            cursor!!.getString(cursor.getColumnIndex(MusicClass.CLASSES_DATE)),
            )
        cursor.close()
        return listClass
    }

    fun deleteClass(musicClass: MusicClass) {
        val db = this.writableDatabase
        db.delete(MusicClass.CLASSES_TABLE_NAME, MusicClass.CLASSES_ID + " = ?",
        arrayOf(musicClass.id.toString()))
        db.close()
    }

    fun deleteAllClasses() {
        val db = this.writableDatabase
        db.delete(MusicClass.CLASSES_TABLE_NAME, MusicClass.CLASSES_ID + " > ?",
        arrayOf("0"))
        db.close()
    }

    fun updateClass(musicClass: MusicClass): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(MusicClass.CLASSES_DESC, musicClass.description)
        return db.update(MusicClass.CLASSES_TABLE_NAME, values, MusicClass.CLASSES_ID + " = ?",
            arrayOf(musicClass.id.toString()))
    }
}