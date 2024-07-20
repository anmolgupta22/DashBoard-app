package com.example.creatorlinkanalytics.di

import com.example.creatorlinkanalytics.model.DashBoardResponse
import javax.inject.Inject

class DashBoardRepositoryImpl @Inject constructor(private val apiService: ApiService): DashBoardRepository {

    override suspend fun getDashBoard(): Result<DashBoardResponse?> {

        return try {
            val response = apiService.getDashBoard()
            if (response.isSuccessful && response.body()?.status == true) {
                Result.success(response.body())
            } else {
                // Handle other API response errors
                Result.failure(Exception("Failed to fetch characters. Response code: ${response.code()}"))
            }
        } catch (e: NoConnectivityException) {
            // Handle network-related errors
            Result.failure(Exception("No internet connection"))
        } catch (e: Exception) {
            Result.failure(Exception("Exception: ${e.message}"))
        }
    }
}
