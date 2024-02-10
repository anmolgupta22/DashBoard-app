package com.example.creatorlinkanalytics.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Update


@Dao
interface BaseDao<T> {

    @Insert(onConflict = REPLACE)
    fun insertAll(dashBoardResponseDb: List<T>)

    @Insert(onConflict = REPLACE)
    fun insert(dashBoardResponseDb: T): Long

    @Update(onConflict = REPLACE)
    fun update(dashBoardResponseDb: T): Int

    @Delete
    fun delete(dashBoardResponseDb: T): Int
}