package com.example.inventoryapp

import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Typeface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.inventoryapp.database.DbHelper
import com.example.inventoryapp.database.model.Item
import com.example.inventoryapp.view.FormsActivity
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    private var helper: DbHelper? = null

    private var appLayout: LinearLayout? = null
    var height = 0
    var width = 0


    private var itemsList: ArrayList<Item>? = ArrayList<Item>()

    private var button: Button? = null


    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("DIM", "onCreate")

        // Initialize db
        helper = DbHelper(this)
        database = helper!!.writableDatabase

        // Initialize Button | layout
        button = findViewById(R.id.Add)
        appLayout = findViewById(R.id.layouuut)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        findViewById<View>(R.id.layouuut).layoutParams.height = height - 300


        refresh()

    }

    public fun getItem(gtin: String) : Item? {

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

        if (cursor.count == 0)
        {
            Log.i("DIM", "Count null")
            return null
        }

        // prepare item object
        var items = Item(
                cursor.getInt(cursor.getColumnIndex(helper!!.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_GTIN)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(helper!!.COLUMN_EXPIRYDATE))
        )

        Log.i("DIM", "Get item $items")

        // close the db connection
        cursor.close()

        return items
    }

    public fun getAllItems(): ArrayList<Item> {
        var items = ArrayList<Item>()

        var column = arrayOf(helper!!.COLUMN_ID, helper!!.COLUMN_GTIN, helper!!.COLUMN_NAME, helper!!.COLUMN_EXPIRYDATE)

        var cursor: Cursor? = database!!.query(helper!!.TABLE_NAME, column, null, null, null, null, null)

        if (cursor == null)
        {
            Log.i("DIM", "Failed to retrieve items")
            return items
        }

        cursor.moveToFirst()

        if (cursor.count == 0)
        {
            Log.i("DIM", "Count null")
            return items
        }
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

    public fun insertItem(gtin: String, name: String, expiryDate: String)
    {
        var item = getItem(gtin)

        if (item != null && item.gtin == gtin)
        {
            updateTable(gtin, expiryDate)
            return
        }

        var value = ContentValues()

        value.put(helper!!.COLUMN_GTIN, gtin)
        value.put(helper!!.COLUMN_NAME, name);
        value.put(helper!!.COLUMN_EXPIRYDATE, expiryDate)

        database!!.insert(helper!!.TABLE_NAME, null, value)

        Log.i("DIM", "Insert succeed")

    }

    public fun updateTable(gtin: String, expiryDate: String)
    {
        var value = ContentValues()
        value.put(helper!!.COLUMN_EXPIRYDATE, expiryDate)

        var where = helper!!.COLUMN_GTIN + "=?"
        var args = arrayOf(gtin)

        // updating row
        database!!.update(helper!!.TABLE_NAME, value, where, args)

        Log.i("DIM", "Update succeed, " + getItem(gtin).toString())

    }

    public fun getItemCount(): Int
    {
        var column = arrayOf(helper!!.COLUMN_ID, helper!!.COLUMN_GTIN, helper!!.COLUMN_NAME, helper!!.COLUMN_EXPIRYDATE)

        var cursor: Cursor? = database!!.query(helper!!.TABLE_NAME, column, null, null, null, null, null)

        if (cursor == null)
        {
            Log.i("DIM", "Failed to retrieve items")
            return 0
        }

        var count = cursor.count
        cursor.close()
        return count
    }

    private fun createItem(gtin: String, name: String, expiryDate: String) {

        Log.i("DIM", "createItem")

        val linLay = LinearLayout(this@MainActivity)
        linLay.orientation = LinearLayout.VERTICAL

        val newName = TextView(this@MainActivity)
        newName.text = "\n$name"
        newName.textSize = 17f
        newName.setTextColor(-0x1000000)
        newName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))

        val newGtin = TextView(this@MainActivity)
        newGtin.text = "$gtin"
        newGtin.textSize = 17f
        newGtin.setTextColor(-0x1000000)

        val newDate = TextView(this@MainActivity)
        newDate.text = "$expiryDate\n"
        newDate.textSize = 17f
        newDate.setTextColor(-0x1000000)

        linLay.addView(newName)
        linLay.addView(newGtin)
        linLay.addView(newDate)
        linLay.gravity = Gravity.START

        appLayout!!.addView(linLay)

        val line = View(this@MainActivity)
        appLayout!!.addView(line)
        line.layoutParams.width = width
        line.layoutParams.height = 1
        line.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.black))

    }

    private fun refresh()
    {
        Log.i("DIM", "refresh")

        appLayout!!.removeAllViews()
        var list = getAllItems()

        for (elem in list)
        {
            createItem(elem.gtin, elem.name, elem.expiryDate)
        }
    }

    public fun openForms(view: View) {
        Log.i("DIM", "Open forms")
        intent = Intent(baseContext, FormsActivity::class.java)
        item= intent.getSerializableExtra("item") as Item?

        intent.putExtra("item", -1)

        var array = ArrayList<String>()

        for (i in 0..2)
            array.add("")

        intent.putStringArrayListExtra("itemContent", array)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.i("DIM", "onActivityResult ")

        if (resultCode == RESULT_OK && requestCode == 0)
        {
            Log.i("DIM", "Request 0")

            var itemContent: java.util.ArrayList<String>? = data!!.getStringArrayListExtra("itemContent")
            Log.i("DIM", "itemContent $itemContent")

            insertItem(itemContent!![0], itemContent[1], itemContent[2])
            Toast.makeText(this, "Data submitted", Toast.LENGTH_SHORT).show();
        }

        // refreshing the list
        refresh()

        super.onActivityResult(requestCode, resultCode, data)

    }

}