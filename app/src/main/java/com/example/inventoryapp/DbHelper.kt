package com.example.inventoryapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase

import android.database.sqlite.SQLiteDatabase.CursorFactory

import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DbHelper : SQLiteOpenHelper {

    public var TABLE_NAME: String = "inventory"

    public var COLUMN_ID: String = "id"
    public var COLUMN_GTIN: String = "gtin"
    public var COLUMN_NAME: String = "name"
    public var COLUMN_EXPIRYDATE: String = "expirydate"

    constructor(context: Context?) :
        super(context, "inventory.db", null, 1) {
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
        Log.i("CREATE", "Database created")
    }

    override fun onUpgrade(db: SQLiteDatabase, version_old: Int, version_new: Int) {
        db.execSQL(DROP_TABLE)
        Log.i("DROP", "Database dropped")
        db.execSQL(CREATE_TABLE)
    }

    // Create table SQL query
    private val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_GTIN + " TEXT,"
            + COLUMN_NAME + " TEXT, "
            + COLUMN_EXPIRYDATE + " DATETIME DEFAULT CURRENT_TIMESTAMP"
            + ")")

    private val DROP_TABLE = "drop table $TABLE_NAME";
}
