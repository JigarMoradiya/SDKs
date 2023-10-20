package com.connectedminds.sdks.ui.view.home.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.connectedminds.sdks.R
import com.connectedminds.sdks.databinding.FragmentHomeBinding
import com.connectedminds.sdks.ui.view.base.BaseFragment
import com.connectedminds.sdks.ui.view.home.adapters.SdkTypesRecycleAdapter
import com.sdk.data.SdkChild
import com.sdk.data.SdkTypes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment(), SdkTypesRecycleAdapter.ItemClickListener {
    private lateinit var binding : FragmentHomeBinding
    private lateinit var navController : NavController
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setNavigationGraph()
        initViews()
        return binding.root
    }
    private fun setNavigationGraph() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

    private fun initViews() {
        val listing : ArrayList<SdkTypes> = arrayListOf()
        val listingChild1 : ArrayList<SdkChild> = arrayListOf()
        listingChild1.add(SdkChild("login","Configure User"))
        listing.add(SdkTypes("Authentication Functions",listingChild1))

        val listingChild0 : ArrayList<SdkChild> = arrayListOf()
        listingChild0.add(SdkChild("getTrendingFeed","Trending Feed"))
        listing.add(SdkTypes("Feeds",listingChild0))

        val listingChild2 : ArrayList<SdkChild> = arrayListOf()
        listingChild2.add(SdkChild("getAllPodcast","Get All Podcasts"))
        listingChild2.add(SdkChild("getPodcastById","Get Podcasts By Id"))
        listingChild2.add(SdkChild("getPodcastByEmotionId","Get Podcasts By Categories"))
        listingChild2.add(SdkChild("getPodcastByEmotionId_view","Horizontal Podcast View"))
        listing.add(SdkTypes("Podcast Functions",listingChild2))

        val listingChild3 : ArrayList<SdkChild> = arrayListOf()
        listingChild3.add(SdkChild("getAllArticle","Get All Articles"))
        listingChild3.add(SdkChild("getArticleById","Get Articles By Id"))
        listingChild3.add(SdkChild("getArticleByEmotionId","Get Articles By Categories"))
        listingChild3.add(SdkChild("getArticleByEmotionId_view","Horizontal Article View"))
        listing.add(SdkTypes("Article Functions",listingChild3))

        val listingChild4 : ArrayList<SdkChild> = arrayListOf()
        listingChild4.add(SdkChild("getAllClip","Get All Clips"))
        listingChild4.add(SdkChild("getClipById","Get Clips By Id"))
        listingChild4.add(SdkChild("getClipByEmotionId","Get Clips By Categories"))
        listingChild4.add(SdkChild("getClipByEmotionId_view","Horizontal Clip View"))
        listing.add(SdkTypes("Clip Functions",listingChild4))

        val adapter = SdkTypesRecycleAdapter(listing,this)
        binding.rv.adapter = adapter

    }

    override fun onChildItemClicked(id: String) {
        val bundle = Bundle()
        bundle.putString("id",id)
        navController.navigate(R.id.toResultFragment, bundle)
    }

}