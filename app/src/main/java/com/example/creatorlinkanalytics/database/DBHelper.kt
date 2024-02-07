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
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return appDataBaseInstance!!
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE tbl_star_wars (`id` INT PRIMARY KEY, `support_whatsapp_number` TEXT, `extra_income` DOUBLE, `total_links` INT, `total_Clicks` INT, `today_clicks` INT, `top_source` TEXT, `top_location` TEXT, `start_time` TEXT, `links_created_today`, INT, `applied_campaign` INT, `data` TEXT)")
                db.execSQL("INSERT INTO tbl_star+wars (id, support_whatsapp_number, extra_income, previous, `total_links`, `total_Clicks`, `today_clicks`, `top_source`, `top_location`, `start_time`, `links_created_today`, `applied_campaign`, `data`)")
            }
        }
    }
}