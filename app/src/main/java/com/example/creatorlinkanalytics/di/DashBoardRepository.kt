package com.example.creatorlinkanalytics.di

import com.example.creatorlinkanalytics.model.DashBoardResponse

interface DashBoardRepository {
    suspend fun getDashBoard(): Result<DashBoardResponse?>
}


