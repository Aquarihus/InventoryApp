package com.example.inventoryapp

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    private var helper: DbHelper? = null

    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("DIM", "onCreate")

        // Initialize db
        helper = DbHelper(this)
        database = helper!!.getWritableDatabase()

        // Initialize Button
        button = findViewById(R.id.Add)
        button!!.setOnClickListener {
            Log.i("DIM", "Button is clicked")
        }

        // insertItem("12345678912345", "Danette", 11022020)
        // getItem("12345678912345")
        // updateTable("12345678912345", 12032020)
        // getAllItems()
    }

    override fun onRestart() {
        setContentView(R.layout.activity_main)
        Log.i("DIM", "onRestart")
        super.onRestart()
    }

    override fun onResume() {
        setContentView(R.layout.activity_main)
        Log.i("DIM", "onResume")
        super.onResume()
    }

    override fun onPause() {
        Log.i("DIM", "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.i("DIM", "onStop")
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("DIM", "onSaveInstanceState")
        setContentView(R.layout.activity_main)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i("DIM", "onRestoreInstanceState")
    }

    private fun getItem(gtin: String) : Item? {

        var column = arrayOf(helper!!.COLUMN_ID, helper!!.COLUMN_GTIN, helper!!.COLUMN_NAME, helper!!.COLUMN_EXPIRYDATE)
        var where = helper!!.COLUMN_GTIN + "=?"
        var orderBy = helper!!.COLUMN_EXPIRYDATE + " DESC"
        var args = arrayOf(gtin)

        var cursor: Cursor? = database!!.query(helper!!.TABLE_NAME, column, where, args, null, null, orderBy)

        if (cursor == null)
        {
            Log.i("DIM", "Failed to retrieve item")

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

        Log.i("DIM", "Get item$item")

        // close the db connection
        cursor.close()

        return item
    }

    private fun getAllItems(): List<Item>? {
        var items = ArrayList<Item>()

        var column = arrayOf(helper!!.COLUMN_ID, helper!!.COLUMN_GTIN, helper!!.COLUMN_NAME, helper!!.COLUMN_EXPIRYDATE)

        var cursor: Cursor? = database!!.query(helper!!.TABLE_NAME, column, null, null, null, null, null)

        if (cursor == null)
        {
            Log.i("DIM", "Failed to retrieve items")

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

        items.add(item);

        while (cursor.moveToNext()){

            // prepare item object
            var item = Item(
                cursor.getInt(cursor.getColumnIndex(helper!!.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_GTIN)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_EXPIRYDATE))
            )

            items.add(item);
        }

        Log.i("DIM", "Get all items : $items")

        // close db connection
        cursor.close()

        return items;
    }

    private fun insertItem(gtin: String, name: String, expiryDate: Int)
    {
        var value = ContentValues()

        value.put(helper!!.COLUMN_GTIN, gtin)
        value.put(helper!!.COLUMN_NAME, name);
        value.put(helper!!.COLUMN_EXPIRYDATE, expiryDate)

        database!!.insert(helper!!.TABLE_NAME, null, value)

        Log.i("DIM", "Insert succeed")

    }

    private fun updateTable(gtin: String, expiryDate: Int)
    {
        var value = ContentValues()
        value.put(helper!!.COLUMN_EXPIRYDATE, expiryDate)

        var where = helper!!.COLUMN_GTIN + "=?"
        var args = arrayOf(gtin)

        // updating row
        database!!.update(helper!!.TABLE_NAME, value, where, args)

        Log.i("DIM", "Update succeed, " + getItem(gtin).toString())

    }

    override fun onDestroy() {
        // call to the superclass constructor
        super.onDestroy()
        Log.i("DIM", "onDestroy")
        database?.delete(helper!!.TABLE_NAME, null, null)

        // close db connection
        database!!.close()
    }

    public fun openForms(view: View) {
        Log.i("DIM", "Forms is open")

        var intent: Intent = Intent(this, FormsActivity::class.java)
        intent.putExtra("key", "test");
        startActivity(intent)
    }

}