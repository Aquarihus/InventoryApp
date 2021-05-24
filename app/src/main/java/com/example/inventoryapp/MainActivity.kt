package com.example.inventoryapp

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    private var helper: DbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize db
        helper = DbHelper(this)
        database = helper!!.getWritableDatabase()

        insertItem("12345678912345", "Danette", 11022020)
        getItem("12345678912345")

    }

    private fun getItem(gtin: String) : Item? {

        var column = arrayOf(helper!!.COLUMN_ID, helper!!.COLUMN_GTIN, helper!!.COLUMN_NAME, helper!!.COLUMN_EXPIRYDATE)
        var where = helper!!.COLUMN_GTIN + "=?"
        var orderBy = helper!!.COLUMN_EXPIRYDATE + " DESC"
        var args = arrayOf(gtin)

        var cursor: Cursor? = database!!.query(helper!!.TABLE_NAME, column, where, args, null, null, orderBy)

        if (cursor == null)
        {
            Log.i("GET ITEM", "Failed to retrieve item")

            return null
        }

        cursor.moveToFirst()

        // prepare item object
        var item = Item(
                cursor.getInt(cursor.getColumnIndex(helper!!.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_GTIN)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_EXPIRYDATE))
        )

        Log.i("GET ITEM", item.toString())

        // close the db connection
        cursor.close()

        return item
    }

    private fun insertItem(gtin: String, name: String, expiryDate: Int)
    {
        var value = ContentValues()

        value.put(helper!!.COLUMN_GTIN, gtin)
        value.put(helper!!.COLUMN_NAME, name);
        value.put(helper!!.COLUMN_EXPIRYDATE, expiryDate)

        database!!.insert(helper!!.TABLE_NAME, null, value)

        Log.i("INSERT", "Insert succeed")

    }

    override fun onDestroy() {
        // call to the superclass constructor
        super.onDestroy()
        database?.delete(helper!!.TABLE_NAME, null, null)
    }

}