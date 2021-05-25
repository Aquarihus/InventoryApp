package com.example.inventoryapp.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.inventoryapp.R
import com.example.inventoryapp.database.model.Item


public class ItemsAdapter(context: Context?, itemsList: ArrayList<Item>?): RecyclerView.Adapter<ItemsAdapter.MyViewHolder>() {

    private var context: Context? = context
    private var itemsList: ArrayList<Item>? = itemsList

    public class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var gtin: TextView = view.findViewById(R.id.gtin)
        var name: TextView = view.findViewById(R.id.name)
        var expiryDate: TextView = view.findViewById(R.id.expirydate)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        Log.i("DIM", "onCreateViewHolder")

        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item: Item = itemsList!![position]

        Log.i("DIM", "onBindViewHolder")

        holder.gtin.text = item.gtin
        holder.name.text = item.name
        holder.expiryDate.text = item.expiryDate
    }

    override fun getItemCount(): Int {
        return itemsList!!.size
    }
}