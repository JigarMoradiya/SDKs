package com.connectedminds.sdks.ui.view.home.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.connectedminds.sdks.databinding.FragmentResultBinding
import com.connectedminds.sdks.ui.view.base.BaseFragment
import com.connectedminds.sdks.utils.AppUtils
import com.connectedminds.sdks.utils.extensions.hide
import com.connectedminds.sdks.utils.extensions.show
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.sdk.data.Article
import com.sdk.data.HealingClip
import com.sdk.data.Podcast
import com.sdk.network.ApiResponsesListener
import com.sdk.network.CallApi
import com.sdk.ui.interfaces.ArticleClickListener
import com.sdk.ui.interfaces.ClipClickListener
import com.sdk.ui.interfaces.PodcastClickListener
import com.sdk.ui.pages.ArticleDetailActivity
import com.sdk.ui.pages.PodcastDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.HashMap

@AndroidEntryPoint
class ResultFragment : BaseFragment(), ApiResponsesListener, PodcastClickListener,
    ArticleClickListener, ClipClickListener {
    private lateinit var binding: FragmentResultBinding
    private var id : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id = arguments?.getString("id")
    }
    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentResultBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    private fun initViews() {
        when (id) {
            "login" -> {
                CallApi.login("sahil003","Test@123",this@ResultFragment, "login")
            }
            "getTrendingFeed" -> {
                CallApi.getTrendingFeed(listOf("article","podcast","clip"),listener = this@ResultFragment, apiName = "getTrendingFeed")
            }
            "getAllPodcast" -> {
                CallApi.getAllPodcast(this@ResultFragment, "getAllPodcast")
            }
            "getPodcastById" -> {
                CallApi.getPodcastById("34592998-7b54-4e3d-a558-ba59f172e0c0",this@ResultFragment, "getPodcastById")
            }
            "getPodcastByEmotionId" -> {
                val param: MutableMap<String?, Any?> = HashMap()
                param["emotion_id"] = "1c6dd213-3a0a-4993-b882-8a6b5688bcd9"
                CallApi.getAllPodcastByEmotionId(param,this@ResultFragment, "getPodcastByEmotionId")
            }
            "getPodcastByEmotionId_view" -> {
                val param: MutableMap<String?, Any?> = HashMap()
                param["emotion_id"] = "1c6dd213-3a0a-4993-b882-8a6b5688bcd9"
                CallApi.getAllPodcastByEmotionId(param,this@ResultFragment, "getPodcastByEmotionId",this@ResultFragment, isViewNeed = true)
            }
            "getAllArticle" -> {
                CallApi.getAllArticle(this@ResultFragment, "getAllArticle")
            }
            "getArticleById" -> {
                CallApi.getArticleById("0244167c-2f1c-401e-808c-9db33cbfbd2a",this@ResultFragment, "getArticleById")
            }
            "getArticleByEmotionId" -> {
                val param: MutableMap<String?, Any?> = HashMap()
                param["emotion_id"] = "1c6dd213-3a0a-4993-b882-8a6b5688bcd9"
                CallApi.getAllArticleByEmotionId(param,this@ResultFragment, "getArticleByEmotionId")
            }
            "getArticleByEmotionId_view" -> {
                val param: MutableMap<String?, Any?> = HashMap()
                param["emotion_id"] = "1c6dd213-3a0a-4993-b882-8a6b5688bcd9"
                CallApi.getAllArticleByEmotionId(param,this@ResultFragment, "getArticleByEmotionId",this@ResultFragment, isViewNeed = true)
            }
            "getAllClip" -> {
                CallApi.getAllClip(this@ResultFragment, "getAllClip")
            }
            "getClipById" -> {
                CallApi.getClipById("5303705d-1e85-470c-87a0-d1ba3fc83671",this@ResultFragment, "getClipById")
            }
            "getClipByEmotionId" -> {
                val param: MutableMap<String?, Any?> = HashMap()
                param["emotion_id"] = "1c6dd213-3a0a-4993-b882-8a6b5688bcd9"
                CallApi.getAllClipByEmotionId(param,this@ResultFragment, "getClipByEmotionId")
            }
            "getClipByEmotionId_view" -> {
                val param: MutableMap<String?, Any?> = HashMap()
                param["emotion_id"] = "1c6dd213-3a0a-4993-b882-8a6b5688bcd9"
                CallApi.getAllClipByEmotionId(param,this@ResultFragment, "getClipByEmotionId",this@ResultFragment, isViewNeed = true)
            }
        }

        /*
        binding.btnFeedEmotions.onClick {
            CallApi.getFeedEmotions("article",this@SplashFragment, "getFeedEmotions")
        }*/

    }

    override fun success(message: String?, apiName : String?, content: JsonObject?,view: View?) {
        Log.e("jigarLogs","success apiName = "+ apiName)
        Log.e("jigarLogs","success content = "+ Gson().toJson(content))

        binding.progressBar.hide()
        if (view != null){
            binding.linearView.removeAllViews()
            binding.linearView.addView(view)
            binding.txtJson.hide()
        }else{
            binding.txtJson.show()
            binding.txtJson.text = AppUtils.formatString(content.toString())
        }
    }

    override fun error(message: String?, apiName : String?) {
        binding.progressBar.hide()
        Log.e("jigarLogs","error apiName = "+ apiName)
        Log.e("jigarLogs","error message = "+ message)
        binding.txtJson.text = AppUtils.formatString(message?:"")
    }

    override fun onPodcastClicked(position: Int, podcast: Podcast) {
        Log.e("jigarLogs","position = "+position)
        Log.e("jigarLogs","podcast = "+Gson().toJson(podcast))
        podcast.id?.let { PodcastDetailActivity.getInstance(requireContext(), it) }
    }

    override fun onArticleClicked(position: Int, article: Article) {
        Log.e("jigarLogs","position = "+position)
        Log.e("jigarLogs","article = "+Gson().toJson(article))
        article.article_id?.let { ArticleDetailActivity.getInstance(requireContext(), it) }
    }

    override fun onClipClicked(position: Int, clip: HealingClip) {
        Log.e("jigarLogs","position = "+position)
        Log.e("jigarLogs","clip = "+Gson().toJson(clip))
    }


//    override fun success(response: ApiResponse?,isSuccess: Boolean) {
//        Log.e("jigarLogs","response isSuccess = "+ isSuccess)
//        Log.e("jigarLogs","response data = "+ Gson().toJson(response))
//    }
//
//    override fun error(response: ApiResponse?) {
//        Log.e("jigarLogs","response error = "+ response)
//    }
}