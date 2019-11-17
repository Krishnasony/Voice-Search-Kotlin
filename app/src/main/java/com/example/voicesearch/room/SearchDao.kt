package com.example.voicesearch.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.voicesearch.room.entity.RecentSearch

@Dao
interface SearchDao {
    @Insert
    fun insertSearchData(vararg recentSearch: RecentSearch)

    @Query("SELECT * FROM recent_search WHERE search LIKE :searchText")
    fun getRecentSearchData(searchText: String):LiveData<List<RecentSearch>>
}