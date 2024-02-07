package com.example.creatorlinkanalytics.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.creatorlinkanalytics.Constant.RECENT_LINKS
import com.example.creatorlinkanalytics.Constant.TOP_LINKS
import com.example.creatorlinkanalytics.MyApplication
import com.example.creatorlinkanalytics.R
import com.example.creatorlinkanalytics.adapter.DashBoardAdapter
import com.example.creatorlinkanalytics.adapter.TabLayoutAdapter
import com.example.creatorlinkanalytics.databinding.FragmentDashBoardBinding
import com.example.creatorlinkanalytics.model.DashBoardResponse
import com.example.creatorlinkanalytics.model.DashBoardResponseDb
import com.example.creatorlinkanalytics.model.DataList
import com.example.creatorlinkanalytics.model.OverallUrlData
import com.example.creatorlinkanalytics.viewModel.DashBoardViewModel
import com.example.creatorlinkanalytics.viewModel.DashBoardViewModelFactory
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class DashBoardFragment : Fragment(), TabConfigurationStrategy {

    @Inject
    lateinit var viewModel: DashBoardViewModel

    @Inject
    lateinit var dashBoardViewModelFactory: DashBoardViewModelFactory

    private var _binding: FragmentDashBoardBinding? = null
    private val binding get() = _binding!!
    private var dashBoardResponse: DashBoardResponse? = null
    private var dashBoardAdapter: DashBoardAdapter? = null
    private val titles: ArrayList<String> = arrayListOf()
    private val overallUrlDataList = mutableListOf<OverallUrlData>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDashBoardBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)
        if (::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this, dashBoardViewModelFactory)[DashBoardViewModel::class.java]
        }

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        dashBoardAdapter = DashBoardAdapter()
        val recyclerView = binding.rvDashboardInfo
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = dashBoardAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        binding.getting.text = getGreetingMessage()
        setupTabTitles()
        setupViewPager()
        TabLayoutMediator(binding.liveChatTabs, binding.liveChatViewPager, this).attach()

        // request the dashboard api
        viewModel.getDashBoardRequest()

        initializeStatusObserver()

        with(binding) {
            chart.description.isEnabled = false
            chart.setTouchEnabled(false)
            chart.setDrawGridBackground(false)
            chart.isDragEnabled = false
            chart.setScaleEnabled(false)
            chart.legend.isEnabled = false
            chart.axisRight.setDrawLabels(false)
        }
    }

    private fun initializeStatusObserver() {
        viewModel.dashboard.observe(viewLifecycleOwner) { response ->
            Log.d("TAG", "initializeStatusObserver: check the response $response")
            dashBoardResponse = response.getOrNull()
            if (dashBoardResponse != null) {
                val jsonObject =
                    Gson().fromJson(
                        dashBoardResponse?.data?.overall_url_chart,
                        JsonObject::class.java
                    )

                // Iterate over the entries of the JsonObject
                jsonObject?.entrySet()?.forEach { (date, value) ->
                    val valueInt = value?.asInt
                    val overallUrlData = OverallUrlData(date, valueInt)
                    overallUrlDataList.add(overallUrlData)
                }

                var dataList: DataList? = null
                dashBoardResponse?.data?.run {
                    dataList = DataList(
                        recent_links, top_links, favourite_links,
                        overallUrlDataList as ArrayList<OverallUrlData>
                    )
                }

                dashBoardResponse?.run {
                    val job = lifecycleScope.async {
                        viewModel.deleteAllStarWars()
                    }
                    val insertJob = lifecycleScope.async {
                        job.await()
                        viewModel.insertDashBoardData(
                            DashBoardResponseDb(
                                support_whatsapp_number = support_whatsapp_number,
                                extra_income = extra_income,
                                total_links = total_links,
                                total_clicks = total_clicks,
                                today_clicks = today_clicks,
                                top_source = top_source,
                                top_location = top_location,
                                startTime = startTime,
                                links_created_today = links_created_today,
                                applied_campaign = applied_campaign,
                                data = dataList
                            )
                        )
                    }
                    setDataAdapter(insertJob = insertJob)
                }
            } else {
                lifecycleScope.launch {
                    dashBoardAdapter?.apply {
                        val fetchJob = lifecycleScope.async {
                            viewModel.fetchAllDashBoard()
                        }
                        setData(fetchJob.await())
                        chart()
                    }
                }
            }
        }
    }

    private fun setDataAdapter(insertJob: Deferred<Unit>) {
        lifecycleScope.launch {
            insertJob.await()
            dashBoardAdapter?.apply {
                val fetchJob = lifecycleScope.async {
                    viewModel.fetchAllDashBoard()
                }
                setData(fetchJob.await())
                chart()
            }
        }

    }

    private fun chart() {

        // list of the months
        val monthsArray: Array<String> = resources.getStringArray(R.array.months_array)

        val xAxis = binding.chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(monthsArray)
        xAxis.labelCount = 12
        xAxis.granularity = 1f
        xAxis.spaceMin = 0.01f
        xAxis.spaceMax = 0.01f

        val entries: MutableList<Entry> = ArrayList()

        val job = lifecycleScope.async {
            val dataList = viewModel.fetchAllDashBoard()?.data?.overall_url_chart
            dataList?.forEach { (date, value) ->
                if (value != null) {
                    if (date != null) {
                        entries.add(Entry(getMonth(date).toFloat(), value.toFloat()))
                    }
                }
            }
        }
        lifecycleScope.launch {
            job.await()
            val dataSet = LineDataSet(entries, "")
            with(dataSet) {
                color = resources.getColor(R.color.blue, activity?.theme)
                lineWidth = 2.5f
                valueTextSize = 0f
                setDrawCircles(false)
                setDrawFilled(true)
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_layout)
                fillDrawable = drawable
            }

            val lineData = LineData(dataSet)
            binding.chart.data = lineData
            binding.chart.invalidate()

        }
    }


    private fun setupTabTitles() {
        titles.add(TOP_LINKS)
        titles.add(RECENT_LINKS)
    }

    private fun setupViewPager() {
        try {
            val tabLayoutAdapter = TabLayoutAdapter(requireActivity())
            val fragmentList: MutableList<Fragment> = ArrayList()
            fragmentList.add(TopLinksFragment())
            fragmentList.add(RecentLinksFragment())
            tabLayoutAdapter.setData(fragmentList)
            binding.liveChatViewPager.isUserInputEnabled = false
            binding.liveChatViewPager.adapter = tabLayoutAdapter
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        tab.text = titles[position]
    }

    private fun getMonth(dateString: String): Int {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(dateString, formatter)
        return date.monthValue - 1
    }

    private fun getGreetingMessage(): String {
        val calendar = Calendar.getInstance()

        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 4..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }

}
