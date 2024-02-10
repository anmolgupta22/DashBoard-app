package com.example.creatorlinkanalytics.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.creatorlinkanalytics.database.RoomRepository
import com.example.creatorlinkanalytics.di.DashBoardRepository
import com.example.creatorlinkanalytics.model.DashBoardResponse
import com.example.creatorlinkanalytics.model.DashBoardResponseDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashBoardViewModel @Inject constructor(
    private val repository: DashBoardRepository,
    private val roomRepository: RoomRepository,
) :
    ViewModel() {

    private val _dashboard = MutableLiveData<Result<DashBoardResponse?>>()
    val dashboard: LiveData<Result<DashBoardResponse?>> get() = _dashboard


    fun getDashBoardRequest() {
        val job = viewModelScope.async {
            repository.getDashBoard()
        }
        viewModelScope.launch(Dispatchers.IO) {
            _dashboard.postValue(job.await())
        }
    }

    fun insertDashBoardData(dashBoardResponseDb: DashBoardResponseDb) {
        roomRepository.insertDashBoardData(dashBoardResponseDb)
    }


    suspend fun fetchAllDashBoard(): DashBoardResponseDb? {
        val job = viewModelScope.async(Dispatchers.IO) {
            roomRepository.fetchAllDashBoard()
        }
        return job.await()
    }

    suspend fun deleteAllStarWars() {
        val job = viewModelScope.async(Dispatchers.IO) {
            roomRepository.deleteAllDashBoard()
        }
        job.await()
    }


}
