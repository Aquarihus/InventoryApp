package com.example.inventoryapp

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.database.DbHelper
import com.example.inventoryapp.database.model.Item
import com.example.inventoryapp.view.FormsActivity
import com.example.inventoryapp.view.ItemsAdapter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private var database: SQLiteDatabase? = null
    private var helper: DbHelper? = null

    private var itemAdapter: ItemsAdapter? = null
    private var itemsList: ArrayList<Item>? = ArrayList<Item>()

    private var button: Button? = null

    private var recyclerView: RecyclerView? = null
    private var noItemsView:TextView? = null

    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i("DIM", "onCreate")

        // Initialize db
        helper = DbHelper(this)
        database = helper!!.writableDatabase

        // Initialize Button
        button = findViewById(R.id.Add)

        recyclerView = findViewById(R.id.recycler_view)
        noItemsView = findViewById(R.id.empty_items_view)

        itemAdapter = ItemsAdapter(this, itemsList)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.itemAnimator = DefaultItemAnimator()
        //recyclerView!!.addItemDecoration(My)
        recyclerView!!.adapter = itemAdapter

        toggleEmptyItems()

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

    public fun getAllItems(): ArrayList<Item>? {
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

    public fun insertItem(gtin: String, name: String, expiryDate: String)
    {
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

    override fun onDestroy() {
        // call to the superclass constructor
        super.onDestroy()
        Log.i("DIM", "onDestroy")
        database?.delete(helper!!.TABLE_NAME, null, null)

        // close db connection
        database!!.close()
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

    private fun toggleEmptyItems() {

        Log.i("DIM", "toggleEmptyItems " + getItemCount())

        if (getItemCount() > 0) {
                noItemsView!!.visibility = View.GONE;
        } else {
            noItemsView!!.visibility = View.VISIBLE;
        }
    }

    /**
     * Inserting new item in db
     * and refreshing the list
     */
    private fun createItem(gtin: String, name: String, expiryDate: String) {

        Log.i("DIM", "createItem")

        // inserting item in db
        insertItem(gtin, name, expiryDate)

        // get the newly inserted note from db
        val newItem: Item? = getItem(gtin)
        if (newItem != null) {
            // adding new note to array list at 0 position
            itemsList!!.add(0, newItem)
        }
        else
        {
            updateTable(gtin, expiryDate)
        }

        // refreshing the list
        itemAdapter!!.notifyDataSetChanged()

        toggleEmptyItems()
    }

    public fun openForms(view: View) {
        Log.i("DIM", "Open forms")
        intent = Intent(baseContext, FormsActivity::class.java)
        item= intent.getSerializableExtra("item") as Item?

        intent.putExtra("item", -1)
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        Log.i("DIM", "onActivityResult ")

        if (requestCode == 0)
        {
            Log.i("DIM", "Request 0")

            var itemContent: java.util.ArrayList<String>? = data!!.getStringArrayListExtra("itemContent")
            Log.i("DIM", "itemContent $itemContent")

            createItem(itemContent!![0], itemContent[1], itemContent[2])
            Toast.makeText(this, "Data submitted", Toast.LENGTH_SHORT).show();

            // refreshing the list
            itemAdapter!!.notifyDataSetChanged()
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

}