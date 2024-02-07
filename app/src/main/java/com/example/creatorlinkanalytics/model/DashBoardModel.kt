package com.example.creatorlinkanalytics.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonObject

@Entity(tableName = "tbl_dash_board")
data class DashBoardResponseDb(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo
    var support_whatsapp_number: String? = null,

    @ColumnInfo
    var extra_income: Double? = null,

    @ColumnInfo
    var total_links: Int? = null,

    @ColumnInfo
    var total_clicks: Int? = null,

    @ColumnInfo
    var today_clicks: Int? = null,

    @ColumnInfo
    var top_source: String? = null,

    @ColumnInfo
    var top_location: String? = null,

    @ColumnInfo
    var startTime: String? = null,

    @ColumnInfo
    var links_created_today: Int? = null,

    @ColumnInfo
    var applied_campaign: Int? = null,

    @ColumnInfo
    var data: DataList? = DataList(),

    )

data class DashBoardResponse(

    var status: Boolean? = null,
    var statusCode: Int? = null,
    var message: String? = null,
    var support_whatsapp_number: String? = null,
    var extra_income: Double? = null,
    var total_links: Int? = null,
    var total_clicks: Int? = null,
    var today_clicks: Int? = null,
    var top_source: String? = null,
    var top_location: String? = null,
    var startTime: String? = null,
    var links_created_today: Int? = null,
    var applied_campaign: Int? = null,
    var data: Data? = Data(),
)

data class RecentLinks(

    var url_id: Int? = null,
    var web_link: String? = null,
    var smart_link: String? = null,
    var title: String? = null,
    var total_clicks: Int? = null,
    var original_image: String? = null,
    var thumbnail: String? = null,
    var times_ago: String? = null,
    var created_at: String? = null,
    var domain_id: String? = null,
    var url_prefix: String? = null,
    var url_suffix: String? = null,
    var app: String? = null,
    var is_favourite: Boolean? = null,

    )


data class TopLinks(

    var url_id: Int? = null,
    var web_link: String? = null,
    var smart_link: String? = null,
    var title: String? = null,
    var total_clicks: Int? = null,
    var original_image: String? = null,
    var thumbnail: String? = null,
    var times_ago: String? = null,
    var created_at: String? = null,
    var domain_id: String? = null,
    var url_prefix: String? = null,
    var url_suffix: String? = null,
    var app: String? = null,
    var is_favourite: Boolean? = null,

    )


data class OverallUrlData(
    val date: String? = null,
    val value: Int? = null
)

data class Data(

    var recent_links: ArrayList<RecentLinks> = arrayListOf(),
    var top_links: ArrayList<TopLinks> = arrayListOf(),
    var favourite_links: ArrayList<String> = arrayListOf(),
    var overall_url_chart: JsonObject? = null,
)

data class DataList(

    var recent_links: ArrayList<RecentLinks> = arrayListOf(),
    var top_links: ArrayList<TopLinks> = arrayListOf(),
    var favourite_links: ArrayList<String> = arrayListOf(),
    var overall_url_chart: ArrayList<OverallUrlData> = arrayListOf(),
)

