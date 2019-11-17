package com.example.voicesearch.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.voicesearch.repo.RecentSearchRepo
import com.example.voicesearch.room.database.AppDataBase
import com.example.voicesearch.room.entity.RecentSearch

class RecentSearchViewModel(application: Application):AndroidViewModel(application) {
    private val repo = RecentSearchRepo(AppDataBase.getDatabase(application).searchDao)
    suspend fun addRecentSearch(recentSearch: RecentSearch){
        repo.insertSearch(recentSearch)
    }
}