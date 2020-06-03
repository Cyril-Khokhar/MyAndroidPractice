package com.example.userpermissiondemo.database

import android.app.Application
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.userpermissiondemo.app.Config
import com.example.userpermissiondemo.app.MyApplication
import java.sql.Blob

class dbHelper : SQLiteOpenHelper(MyApplication.instance, Config.DB_NAME, null, Config.DB_VERSION){
    private var database : SQLiteDatabase = writableDatabase
    companion object{
        const val TABLE_NAME = "UserImages"
        const val COLUMN_ID = "Id"
        const val COLUMN_IMAGE = "Image"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val IMAGE_TABLE = "CREATE TABLE ${TABLE_NAME}(${COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT, ${COLUMN_IMAGE} BLOB)"
        db?.execSQL(IMAGE_TABLE)
    }

    fun addImages(image : ByteArray){
        var contentValues = ContentValues()
        contentValues.put(COLUMN_IMAGE, image)
        database.insert(TABLE_NAME, null, contentValues)
    }

    fun getImages() : ArrayList<ByteArray>{
        var mlist = ArrayList<ByteArray>()
        var columns = arrayOf(COLUMN_ID, COLUMN_IMAGE)
        var cursor = database.query(TABLE_NAME, columns, null,null,null,null,null)
        if(cursor!=null && cursor.moveToFirst()){
            do{
                var imageName = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE))
                mlist.add(imageName)
            }
            while(cursor.moveToNext())
        }
        cursor.close()
        return mlist
    }

    fun deleteImages(){
        database.delete(TABLE_NAME, null,null)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

}