package com.example.creatorlinkanalytics.database


import com.example.creatorlinkanalytics.database.dao.DashBoardDao
import com.example.creatorlinkanalytics.model.DashBoardResponseDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


open class RoomDatabase @Inject constructor(
    private val dashBoardDao: DashBoardDao,
) {

    fun insertDashBoardData(dashBoard: DashBoardResponseDb) {
        dashBoardDao.insert(dashBoard)
    }

    suspend fun fetchAllDashBoard(): DashBoardResponseDb? {
        return withContext(Dispatchers.IO) {
            dashBoardDao.fetchAllDashBoard()
        }
    }

    suspend fun deleteAllDashBoard() {
        return withContext(Dispatchers.IO) {
            dashBoardDao.deleteAllDashBoard()
        }
    }
}