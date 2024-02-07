package com.example.creatorlinkanalytics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.creatorlinkanalytics.Constant.EXTRA_INCOME
import com.example.creatorlinkanalytics.Constant.TODAY_CLICKS
import com.example.creatorlinkanalytics.Constant.TOP_LOCATION
import com.example.creatorlinkanalytics.Constant.TOP_SOURCE
import com.example.creatorlinkanalytics.Constant.TOTAL_CLICKS
import com.example.creatorlinkanalytics.Constant.TOTAL_LINKS
import com.example.creatorlinkanalytics.databinding.DashboardInfoItemBinding
import com.example.creatorlinkanalytics.model.DashBoardResponseDb

class DashBoardAdapter :
    RecyclerView.Adapter<DashBoardAdapter.ViewHolder>() {

    private var items: ArrayList<DashBoardResponseDb> = arrayListOf()

    fun setData(itemsData: DashBoardResponseDb?) {
        items.clear()
        if (itemsData != null) {
            items.add(itemsData)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        DashboardInfoItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    class ViewHolder(
        private val binding: DashboardInfoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DashBoardResponseDb) {
            with(binding) {
                todayClickNo.text = item.today_clicks.toString()
                todayClickTv.text = TODAY_CLICKS

                topLocationName.text = item.top_location
                topLocation.text = TOP_LOCATION

                topSourceName.text = item.top_source
                topSource.text = TOP_SOURCE

                totalClickName.text = item.total_clicks.toString()
                totalClickTv.text = TOTAL_CLICKS

                extraIncomeName.text = item.extra_income.toString()
                extraIncomeTv.text = EXTRA_INCOME

                totalLinkName.text = item.total_links.toString()
                totalLinkTv.text = TOTAL_LINKS
            }
        }


    }
}