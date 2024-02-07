package com.example.creatorlinkanalytics.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.creatorlinkanalytics.MainActivity
import com.example.creatorlinkanalytics.MyApplication
import com.example.creatorlinkanalytics.adapter.RecentLinksAdapter
import com.example.creatorlinkanalytics.databinding.FragmentRecentLinksBinding
import com.example.creatorlinkanalytics.viewModel.DashBoardViewModel
import com.example.creatorlinkanalytics.viewModel.DashBoardViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject


class RecentLinksFragment : Fragment(), RecentLinksAdapter.WebsiteItemClickListener {


    @Inject
    lateinit var viewModel: DashBoardViewModel

    @Inject
    lateinit var dashBoardViewModelFactory: DashBoardViewModelFactory
    private var _binding: FragmentRecentLinksBinding? = null
    private val binding get() = _binding!!
    private var recentLinksAdapter: RecentLinksAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecentLinksBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity().application as MyApplication).appComponent.inject(this)
        if (::viewModel.isInitialized) {
            viewModel =
                ViewModelProvider(this, dashBoardViewModelFactory)[DashBoardViewModel::class.java]
        }

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recentLinksAdapter = RecentLinksAdapter(requireContext(), this)
        val recyclerView = binding.rvRecentLinks
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = recentLinksAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }


        lifecycleScope.launch {
            val db = viewModel.fetchAllDashBoard()

            recentLinksAdapter?.apply {
                if (db != null) {
                    setData(db.data?.recent_links ?: arrayListOf())
                }
            }

        }
    }

    override fun onWebsiteItemClicked(url: String) {
      openWebsiteInChrome(url)
    }

    // Method to open the website in Chrome
    private fun openWebsiteInChrome(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.android.chrome") // Use Chrome package
        try {
            (activity as MainActivity).startActivity(intent)
        } catch (e: Exception) {
            // Chrome not installed, fallback to other browsers or web view
            intent.setPackage(null)
            (activity as MainActivity).startActivity(intent)
        }


    }
}