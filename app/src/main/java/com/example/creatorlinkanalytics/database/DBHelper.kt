package com.example.creatorlinkanalytics.database

import android.content.Context
import androidx.room.*
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.creatorlinkanalytics.model.DashBoardResponse
import com.example.creatorlinkanalytics.database.dao.DashBoardDao
import com.example.creatorlinkanalytics.model.DashBoardResponseDb

@Database(
    entities = [DashBoardResponseDb::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class DBHelper : RoomDatabase() {

    abstract fun characterDao(): DashBoardDao

    companion object {

        private var appDataBaseInstance: DBHelper? = null

        @Synchronized
        fun getInstance(context: Context): DBHelper {
            if (appDataBaseInstance == null) {
                appDataBaseInstance = Room.databaseBuilder(
                    context.applicationContext,
                    DBHelper::class.java,
                    "star_war_database"
                )
                    .allowMainThreadQueries()
                    .build()
            }
            return appDataBaseInstance!!
        }
    }
}