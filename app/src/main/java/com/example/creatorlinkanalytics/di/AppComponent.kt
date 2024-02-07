package com.example.creatorlinkanalytics.di

import com.example.creatorlinkanalytics.fragment.DashBoardFragment
import com.example.creatorlinkanalytics.fragment.RecentLinksFragment
import com.example.creatorlinkanalytics.fragment.TopLinksFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, RepositoryModule::class, DatabaseModule::class])
interface AppComponent {
    fun inject(fragment: DashBoardFragment)
    fun inject(fragment: TopLinksFragment)
    fun inject(fragment: RecentLinksFragment)
}