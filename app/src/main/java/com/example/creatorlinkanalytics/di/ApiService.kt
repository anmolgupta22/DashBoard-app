package com.example.creatorlinkanalytics.di

import com.example.creatorlinkanalytics.model.DashBoardResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/api/v1/dashboardNew")
    suspend fun getDashBoard(): Response<DashBoardResponse>
}