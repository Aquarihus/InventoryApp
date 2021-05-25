package com.example.inventoryapp.database.model

import java.io.Serializable

class Item(val id: Int, val gtin: String, val name: String, val expiryDate: String): Serializable {

    // One item in the inventory

    override fun toString() : String
    {
        return "id= $id gtin= $gtin name= $name expiryDate= $expiryDate"
    }

}