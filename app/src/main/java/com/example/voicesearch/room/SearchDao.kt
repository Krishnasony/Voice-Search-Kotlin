package com.example.voicesearch.room

import androidx.room.Dao
import androidx.room.Insert
import com.example.voicesearch.room.entity.RecentSearch

@Dao
interface SearchDao {
    @Insert
    fun insertSearchData(vararg recentSearch: RecentSearch)
}