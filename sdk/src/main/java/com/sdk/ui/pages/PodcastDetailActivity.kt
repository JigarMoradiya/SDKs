package com.sdk.ui.pages

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sdk.R
import com.sdk.data.Article
import com.sdk.data.Chip
import com.sdk.data.Podcast
import com.sdk.data.toChip
import com.sdk.databinding.ActivityArticleDetailsBinding
import com.sdk.databinding.ActivityPodcastDetailsBinding
import com.sdk.network.ApiResponsesListener
import com.sdk.network.CallApi
import com.sdk.ui.interfaces.RemoveTagClickListner
import com.sdk.utils.Constants
import com.sdk.utils.CustomFunctions
import com.sdk.utils.CustomFunctions.getColorWithAlpha
import com.sdk.utils.DateFunctions
import com.sdk.utils.extensions.hide
import com.sdk.utils.extensions.onClick
import com.sdk.utils.extensions.show
import com.sdk.utils.extensions.toastS


class PodcastDetailActivity : AppCompatActivity(), ApiResponsesListener {
    private lateinit var binding: ActivityPodcastDetailsBinding
    private var podcastId : String? = null
    private var podcastDetails: Podcast? = null
    companion object {
        fun getInstance(context: Context?, podcastId : String) {
            Intent(context, PodcastDetailActivity::class.java).apply {
                putExtra(Constants.PODCAST_ID,podcastId)
                context?.startActivity(this)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPodcastDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initListener()
    }

    private fun initListener() {
        with(binding){
            relBack.onClick {
                finish()
            }
            nestedScrollView.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                val scrollBounds = Rect()
                nestedScrollView.getHitRect(scrollBounds)

                val alpha: Float = scrollY.toFloat() / relHeader.height
                relHeader.setBackgroundColor(getColorWithAlpha(alpha,ContextCompat.getColor(this@PodcastDetailActivity,R.color.color_primary_dark)))
                tvTitle.setTextColor(getColorWithAlpha(alpha,ContextCompat.getColor(this@PodcastDetailActivity,R.color.white)))

                if (alpha == 0.0F) {
                    // View is within the visible window
                    imgBack.setBackgroundResource(R.drawable.bg_back_alpha)
                } else {
                    // View is not within the visible window
                    imgBack.setBackgroundColor(ContextCompat.getColor(this@PodcastDetailActivity,R.color.transparent))
                }
            }
        }

    }

    private fun initView() {
        podcastId = intent.getStringExtra(Constants.PODCAST_ID)
        podcastId?.let { CallApi.getPodcastById(it,this, "getPodcastById") }
    }
    private fun setPodcastData() {
        with(binding){
            nestedScrollView.show()
            relHeader.show()
            podcastDetails?.let {
                data = it
                layoutAuthorDetails.hide()
                if (it.connectedminds_feed == 1){
                    cardview.imgByConnectedMind.show()
                    cardview.imgConnectedMindLogo.show()
                }
                it.sentiments?.let {
                    val list = ArrayList<Chip>()
                    for (element in it) {
                        list.add(element.toChip())
                    }
                    CustomFunctions.setCategoryChips(this@PodcastDetailActivity,list,binding.chipGroup)
                }

//                DateFunctions.covertTimeAgoForPodcast(it.created_at).also {
//                    binding.tvDate.text = it
//                }
            }
        }

    }
    override fun success(message: String?, apiName: String?, content: JsonObject?, view: View?) {
        binding.progressBar.hide()
        if (content?.has("data") == true) {
            podcastDetails = Gson().fromJson(content.get("data").asJsonObject,Podcast::class.java)
            podcastDetails?.fetched_at = System.currentTimeMillis()
            setPodcastData()
        }
    }

    override fun error(message: String?, apiName: String?) {
        binding.progressBar.hide()
        message?.let { toastS(it) }
        finish()
    }
}