package com.example.voicesearch.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.voicesearch.repo.RecentSearchRepo
import com.example.voicesearch.room.database.AppDataBase
import com.example.voicesearch.room.entity.RecentSearch

class RecentSearchViewModel(application: Application):AndroidViewModel(application) {
    private val repo = RecentSearchRepo(AppDataBase.getDatabase(application).searchDao)
    var recentSearchLiveData:LiveData<List<RecentSearch>> ? = null
    suspend fun addRecentSearch(recentSearch: RecentSearch){
        repo.insertSearch(recentSearch)
    }
    suspend fun getRecentSearchData(searchString: String){
        recentSearchLiveData = repo.getRecentSearchData(searchString)
    }
}