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
import com.example.inventoryapp.database.model.Item

class FormsActivity : AppCompatActivity() {

    private var button: Button? = null

    private var item: Long = 0
    private var itemContent: ArrayList<String>? = ArrayList()

    var editGtin: EditText? = null
    var editName: EditText? = null
    var editDate: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forms)
        Log.i("DIM", "onCreate Forms")

        intent = getIntent()
        item = intent.getLongExtra("item", -1)
        itemContent = intent.getStringArrayListExtra("itemContent")

        // Edit Text
        editGtin = findViewById(R.id.InsertGtin)

        editName = findViewById(R.id.InsertName)

        editDate = findViewById(R.id.EditDate)

        if (item < 0)
        {
            Log.i("DIM", "First enter")
            editGtin!!.setText("");
            editName!!.setText("");
            editDate!!.setText("");

        }
        else
        {
            editGtin!!.setText(itemContent!![0]);
            editName!!.setText(itemContent!![1]);
            editDate!!.setText(itemContent!![2]);
        }

        // Initialize Button
        button = findViewById(R.id.updateButton)

    }

    public fun submit(view: View) {
        Log.i("DIM", "Open forms")
        itemContent = ArrayList<String>()

        var gtinText = editGtin!!.text.toString()
        Log.i("DIM", "Insert Gtin, value = $gtinText")

        var nameText = editName!!.text.toString()
        Log.i("DIM", "Insert Name, value = $nameText")

        var dateText = editDate!!.text.toString()
        Log.i("DIM", "Insert Text, value = $dateText")

        itemContent!!.add(gtinText)
        itemContent!!.add(nameText)
        itemContent!!.add(dateText)

        Log.i("DIM", "submit $itemContent")

        var result = Intent(Intent.ACTION_VIEW)
        result.putStringArrayListExtra("itemContent", itemContent)
        result.putExtra("item", item)
        setResult(RESULT_OK, result)

        finish()
    }

    fun cancel(view: View) {
        Log.i("DIM", "Cancel forms")

        finish()
    }
}