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
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

class DashBoardFragment : Fragment(), TabLayoutMediator.TabConfigurationStrategy {

    @Inject
    lateinit var viewModel: DashBoardViewModel

    @Inject
    lateinit var dashBoardViewModelFactory: DashBoardViewModelFactory

    private var _binding: FragmentDashBoardBinding? = null
    private val binding get() = _binding!!
    private var dashBoardResponse: DashBoardResponse? = null
    private var dashBoardAdapter: DashBoardAdapter? = null
    private val titles = arrayListOf(TOP_LINKS, RECENT_LINKS)
    private val overallUrlDataList = mutableListOf<OverallUrlData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashBoardBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity().application as MyApplication).appComponent.inject(this)
        if (!::viewModel.isInitialized) {
            viewModel = ViewModelProvider(this, dashBoardViewModelFactory)[DashBoardViewModel::class.java]
        }

        setupRecyclerView()
        binding.getting.text = getGreetingMessage()
        setupViewPager()
        TabLayoutMediator(binding.liveChatTabs, binding.liveChatViewPager, this).attach()
        setupChart()
        viewModel.getDashBoardRequest()
        initializeStatusObserver()
    }

    private fun setupRecyclerView() {
        dashBoardAdapter = DashBoardAdapter()
        binding.rvDashboardInfo.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = dashBoardAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }
    }

    private fun setupViewPager() {
        val tabLayoutAdapter = TabLayoutAdapter(requireActivity())
        val fragmentList = listOf(TopLinksFragment(), RecentLinksFragment())
        tabLayoutAdapter.setData(fragmentList)
        binding.liveChatViewPager.apply {
            isUserInputEnabled = false
            adapter = tabLayoutAdapter
        }
    }

    private fun setupChart() {
        binding.chart.apply {
            description.isEnabled = false
            setTouchEnabled(false)
            setDrawGridBackground(false)
            isDragEnabled = false
            setScaleEnabled(false)
            legend.isEnabled = false
            axisRight.setDrawLabels(false)
        }
    }

    private fun initializeStatusObserver() {
        viewModel.dashboard.observe(viewLifecycleOwner) { response ->
            Log.d("DashBoardFragment", "Response: $response")
            dashBoardResponse = response.getOrNull()
            if (dashBoardResponse != null) {
                processDashboardResponse(dashBoardResponse!!)
            } else {
                fetchDashboardFromDb()
            }
        }
    }

    private fun processDashboardResponse(response: DashBoardResponse) {
        val jsonObject = Gson().fromJson(response.data?.overall_url_chart, JsonObject::class.java)
        jsonObject?.entrySet()?.forEach { (date, value) ->
            value?.asInt?.let {
                overallUrlDataList.add(OverallUrlData(date, it))
            }
        }

        val dataList = DataList(
            response.data?.recent_links?: arrayListOf(), response.data?.top_links?: arrayListOf(),
            response.data?.favourite_links?: arrayListOf(), overallUrlDataList as ArrayList<OverallUrlData>
        )

        val dashBoardResponseDb = DashBoardResponseDb(
            support_whatsapp_number = response.support_whatsapp_number,
            extra_income = response.extra_income,
            total_links = response.total_links,
            total_clicks = response.total_clicks,
            today_clicks = response.today_clicks,
            top_source = response.top_source,
            top_location = response.top_location,
            startTime = response.startTime,
            links_created_today = response.links_created_today,
            applied_campaign = response.applied_campaign,
            data = dataList
        )

        lifecycleScope.launch {
            viewModel.deleteAllStarWars()
            viewModel.insertDashBoardData(dashBoardResponseDb)
            fetchDashboardFromDb()
        }
    }

    private fun fetchDashboardFromDb() {
        lifecycleScope.launch {
            dashBoardAdapter?.apply {
                val dashboardData = viewModel.fetchAllDashBoard()
                setData(dashboardData)
                updateChart(dashboardData?.data?.overall_url_chart)
            }
        }
    }

    private fun updateChart(dataList: ArrayList<OverallUrlData>?) {
        val monthsArray: Array<String> = resources.getStringArray(R.array.months_array)
        val xAxis = binding.chart.xAxis
        xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            valueFormatter = IndexAxisValueFormatter(monthsArray)
            labelCount = 12
            granularity = 1f
            spaceMin = 0.01f
            spaceMax = 0.01f
        }

        val entries = mutableListOf<Entry>()
        dataList?.forEach { overallUrlData ->
            overallUrlData.value?.let { value ->
                Log.d("TAG", "updateChart: check the data ${overallUrlData.date}")
                entries.add(Entry(getMonth(overallUrlData.date?:"").toFloat(), value.toFloat()))
            }
        }

        val dataSet = LineDataSet(entries, "").apply {
            color = ContextCompat.getColor(requireContext(), R.color.blue)
            lineWidth = 2.5f
            valueTextSize = 0f
            setDrawCircles(false)
            setDrawFilled(true)
            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.fade_layout)
        }

        binding.chart.data = LineData(dataSet)
        binding.chart.invalidate()
    }

    private fun getMonth(dateString: String): Int {
        return try {
            val cleanedDateString = dateString.split("T").firstOrNull() ?: return -1 // Use `-1` or any default value to indicate an error
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = LocalDate.parse(cleanedDateString, formatter)
            date.monthValue - 1
        } catch (e: Exception) {
            Log.e("DashBoardFragment", "Unexpected error: ${e.message}", e)
            -1 // Return an error indicator for any other unexpected errors
        }
    }



    private fun getGreetingMessage(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 4..11 -> getString(R.string.good_morning)
            in 12..16 -> getString(R.string.good_afternoon)
            in 17..20 -> getString(R.string.good_evening)
            else -> getString(R.string.good_night)
        }
    }

    override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
        tab.text = titles[position]
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
