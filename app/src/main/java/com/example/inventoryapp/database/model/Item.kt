package com.example.inventoryapp.database.model

class Item(val id: Int, val gtin: String, val name: String, val expiryDate: String) {

    // One item in the inventory

    override fun toString() : String
    {
        return "id= $id gtin= $gtin name= $name expiryDate= $expiryDate"
    }

}