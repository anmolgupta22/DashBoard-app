package com.example.creatorlinkanalytics.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.creatorlinkanalytics.database.RoomRepository
import com.example.creatorlinkanalytics.di.DashBoardRepository
import javax.inject.Inject

class DashBoardViewModelFactory @Inject constructor(
    private val instance: DashBoardRepository,
    private val roomRepository: RoomRepository,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(DashBoardRepository::class.java, RoomRepository::class.java)
            .newInstance(instance, roomRepository)
    }
}