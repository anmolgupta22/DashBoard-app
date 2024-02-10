package com.example.creatorlinkanalytics.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.creatorlinkanalytics.model.DashBoardResponseDb


@Dao
interface DashBoardDao : BaseDao<DashBoardResponseDb> {

    @Query("Select * from tbl_dash_board")
    suspend fun fetchAllDashBoard(): DashBoardResponseDb?

    @Query("Delete from tbl_dash_board")
    suspend fun deleteAllDashBoard()
}