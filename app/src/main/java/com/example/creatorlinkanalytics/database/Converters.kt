package com.example.creatorlinkanalytics.database

import androidx.room.TypeConverter
import com.example.creatorlinkanalytics.model.DataList
import com.google.gson.Gson

class Converters {

    @TypeConverter
    fun fromString(value: String): DataList {
        return Gson().fromJson(value, DataList::class.java)
    }

    @TypeConverter
    fun fromData(data: DataList): String {
        return Gson().toJson(data)
    }
}