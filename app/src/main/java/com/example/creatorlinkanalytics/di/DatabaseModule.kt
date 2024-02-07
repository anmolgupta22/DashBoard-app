package com.example.creatorlinkanalytics.di

import android.app.Application
import com.example.creatorlinkanalytics.database.DBHelper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(application: Application) = DBHelper.getInstance(application)

    @Provides
    @Singleton
    fun providesLocationDao(database: DBHelper) = database.characterDao()

}
