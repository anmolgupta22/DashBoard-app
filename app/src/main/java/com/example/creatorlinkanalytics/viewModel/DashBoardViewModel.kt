package com.example.creatorlinkanalytics.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creatorlinkanalytics.database.RoomDatabase
import com.example.creatorlinkanalytics.di.DashBoardRepository
import com.example.creatorlinkanalytics.model.DashBoardResponse
import com.example.creatorlinkanalytics.model.DashBoardResponseDb
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashBoardViewModel @Inject constructor(
    private val repository: DashBoardRepository,
    private val roomDatabase: RoomDatabase,
) :
    ViewModel() {

    private val _dashboard = MutableLiveData<Result<DashBoardResponse?>>()
    val dashboard: LiveData<Result<DashBoardResponse?>> get() = _dashboard


    fun getDashBoardRequest() {
        val job = viewModelScope.async {
            repository.getDashBoard()
        }
        viewModelScope.launch {
            val finishJob = job.await()
            _dashboard.value = finishJob
        }
    }

    fun insertDashBoardData(dashBoardResponseDb: DashBoardResponseDb) {
        roomDatabase.insertDashBoardData(dashBoardResponseDb)
    }


    suspend fun fetchAllDashBoard(): DashBoardResponseDb? {
        val job = viewModelScope.async {
            roomDatabase.fetchAllDashBoard()
        }
        return job.await()
    }

    suspend fun deleteAllStarWars() {
        val job = viewModelScope.async {
            roomDatabase.deleteAllDashBoard()
        }
        job.await()
    }


}
