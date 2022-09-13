package com.example.demolisting.models

data class RecyclerList(val products: ArrayList<RecyclerData>)
data class RecyclerData(val title: String, val description: String, val thumbnail: String)
