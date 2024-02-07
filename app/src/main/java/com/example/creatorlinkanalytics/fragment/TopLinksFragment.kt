package com.example.creatorlinkanalytics.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.creatorlinkanalytics.MainActivity
import com.example.creatorlinkanalytics.MyApplication
import com.example.creatorlinkanalytics.adapter.TopLinksAdapter
import com.example.creatorlinkanalytics.databinding.FragmentTopLinksBinding
import com.example.creatorlinkanalytics.viewModel.DashBoardViewModel
import com.example.creatorlinkanalytics.viewModel.DashBoardViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopLinksFragment : Fragment(), TopLinksAdapter.WebsiteItemClickListener {

    @Inject
    lateinit var viewModel: DashBoardViewModel

    @Inject
    lateinit var dashBoardViewModelFactory: DashBoardViewModelFactory
    private var _binding: FragmentTopLinksBinding? = null
    private val binding get() = _binding!!
    private var topLinksAdapter: TopLinksAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTopLinksBinding.inflate(inflater)
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
        topLinksAdapter = TopLinksAdapter(requireContext(), this)
        val recyclerView = binding.rvTopLinks
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = topLinksAdapter
            itemAnimator = null
            setHasFixedSize(true)
        }

        lifecycleScope.launch {
            Log.d("TAG", "updated: check the d")
            lifecycleScope.launch {
                val db = viewModel.fetchAllDashBoard()
                topLinksAdapter?.apply {
                    if (db != null) {
                        setData(db.data?.top_links ?: arrayListOf())
                    } else {
                        Handler(Looper.getMainLooper()).postDelayed({
                            // Your Code
                            fetchDbData()
                        }, 300)
                    }
                }
            }

        }

    }

    private fun fetchDbData() {
        lifecycleScope.launch {
            val db = viewModel.fetchAllDashBoard()
            topLinksAdapter?.apply {
                if (db != null) {
                    setData(db.data?.top_links ?: arrayListOf())
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