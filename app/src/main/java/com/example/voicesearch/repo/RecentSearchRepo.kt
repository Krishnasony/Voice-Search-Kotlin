package com.example.voicesearch.repo

import com.example.voicesearch.room.SearchDao
import com.example.voicesearch.room.entity.RecentSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecentSearchRepo(var dao: SearchDao) {
    suspend fun insertSearch(recentSearch: RecentSearch) = withContext(Dispatchers.IO){
        dao.insertSearchData(recentSearch)
    }
}