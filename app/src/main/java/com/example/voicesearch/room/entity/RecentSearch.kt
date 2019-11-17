package com.example.voicesearch.room.entity

import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recent_search")
data class RecentSearch(
    @ColumnInfo(name = "search") @SerializedName("search") val search: String,
    @ColumnInfo(name = "search_date") @SerializedName("search_date") val searchDate: Long

):ViewModel(){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "primaryKey")
    var primaryKey: Int = 0
}