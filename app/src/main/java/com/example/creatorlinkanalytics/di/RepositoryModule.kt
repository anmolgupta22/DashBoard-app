package com.example.creatorlinkanalytics.di

import com.example.creatorlinkanalytics.database.RoomDatabase
import com.example.creatorlinkanalytics.viewModel.DashBoardViewModel
import dagger.Module
import dagger.Provides


@Module
class RepositoryModule {
    @Provides
    fun provideStarWarsRepository(apiService: ApiService): DashBoardRepository {
        return DashBoardRepositoryImpl(apiService)
    }

    @Provides
    fun provideMyViewModel(dashBoardRepository: DashBoardRepository, roomDatabase: RoomDatabase): DashBoardViewModel {
        return DashBoardViewModel(dashBoardRepository, roomDatabase)
    }

}
