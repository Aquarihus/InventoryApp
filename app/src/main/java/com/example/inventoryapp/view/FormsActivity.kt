package com.example.inventoryapp.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inventoryapp.MainActivity
import com.example.inventoryapp.R

class FormsActivity : AppCompatActivity() {

    private var button: Button? = null
    private var editGtin: EditText? = null
    private var editName: EditText? = null
    private var editDate: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms)
        Log.i("DIM", "onCreate Forms")

        intent = getIntent()

        // Initialize Button
        button = findViewById(R.id.updateButton)

    }

    public fun submit(view: View) {


        var intent: Intent = Intent()
        intent.putExtra("test", "test")
        setResult(1, intent)

        // Edit Text
        editGtin = findViewById(R.id.InsertGtin)
        var gtinText = editGtin!!.getText().toString()
        Log.i("DIM", "Insert Gtin, value = $gtinText")

        editName = findViewById(R.id.InsertName)
        var nameText = editName!!.getText().toString()
        Log.i("DIM", "Insert Name, value = $nameText")

        editDate = findViewById(R.id.EditDate)
        var dateText = editDate!!.getText().toString()
        Log.i("DIM", "Insert Text, value = $dateText")

        Log.i("DIM", "Button clicked")
        finish()
    }
}