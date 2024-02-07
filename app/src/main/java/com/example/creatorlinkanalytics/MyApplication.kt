package com.example.creatorlinkanalytics

import android.app.Application
import com.example.creatorlinkanalytics.di.ApiModule
import com.example.creatorlinkanalytics.di.AppComponent
import com.example.creatorlinkanalytics.di.DaggerAppComponent

class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .apiModule(ApiModule(this))
            .build()
    }

}