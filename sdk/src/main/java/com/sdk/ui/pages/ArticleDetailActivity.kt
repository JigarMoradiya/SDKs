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
import com.sdk.data.toChip
import com.sdk.databinding.ActivityArticleDetailsBinding
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


class ArticleDetailActivity : AppCompatActivity(), ApiResponsesListener {
    private lateinit var binding: ActivityArticleDetailsBinding
    private var articleId : String? = null
    private var articleDetails: Article? = null
    companion object {
        fun getInstance(context: Context?, articleId : String) {
            Intent(context, ArticleDetailActivity::class.java).apply {
                putExtra(Constants.ARTICLE_ID,articleId)
                context?.startActivity(this)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initListener()
        setEditor()
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
                relHeader.setBackgroundColor(getColorWithAlpha(alpha,ContextCompat.getColor(this@ArticleDetailActivity,R.color.color_primary_dark)))
                txtTitle1.setTextColor(getColorWithAlpha(alpha,ContextCompat.getColor(this@ArticleDetailActivity,R.color.white)))

                if (alpha == 0.0F) {
                    // View is within the visible window
                    imgBack.setBackgroundResource(R.drawable.bg_back_alpha)
                    relLike1.setBackgroundResource(R.drawable.bg_back_alpha)
                    relSave1.setBackgroundResource(R.drawable.bg_back_alpha)
                } else {
                    // View is not within the visible window
                    imgBack.setBackgroundColor(ContextCompat.getColor(this@ArticleDetailActivity,R.color.transparent))
                    relLike1.setBackgroundColor(ContextCompat.getColor(this@ArticleDetailActivity,R.color.transparent))
                    relSave1.setBackgroundColor(ContextCompat.getColor(this@ArticleDetailActivity,R.color.transparent))
                }
            }
        }

    }

    private fun initView() {
        articleId = intent.getStringExtra(Constants.ARTICLE_ID)
        articleId?.let { CallApi.getArticleById(it,this, "getArticleById") }
    }
    private fun setEditor() {
        with(binding){
            editor.setEditorFontColor(ContextCompat.getColor(this@ArticleDetailActivity, R.color.white))
            editor.settings.allowFileAccess = true
            editor.setInputEnabled(false)
            editor.setEditorBackgroundColor(ContextCompat.getColor(this@ArticleDetailActivity,R.color.color_primary_dark))
        }
    }
    private fun setArticleData() {
        with(binding){
            nestedScrollView.show()
            relHeader.show()
            articleDetails?.let {
                data = it
                it.cover_image?.text_hex_code?.let { color->
                    tvDate.setTextColor(Color.parseColor(color))
                    tvTitle.setTextColor(Color.parseColor(color))
                }
                getString(R.string.combine_two_strings_by_dot,
                    DateFunctions.convertMilliSecondsToDate(it.created_at, DateFunctions.MMM_dd_yyyy),
                    DateFunctions.displayAudioDuration(it.duration.toLong()).plus(" read")
                ).also {
                    tvDate.text = it
                }

                editor.html = it.content

                it.sentiments?.let {
                    val list = ArrayList<Chip>()
                    for (element in it) {
                        list.add(element.toChip())
                    }
                    CustomFunctions.setCategoryChips(this@ArticleDetailActivity, list, binding.chipGroup, removeTagClickListner = object : RemoveTagClickListner {
                            override fun onRemoveClicked(chip: Chip)  = Unit

                            override fun onItemClicked(chip: Chip) {
//                                moveToExploreToSentimentFullScreen(chip.toSentiment())
                            }
                        }
                    )
                }
            }
        }

    }
    override fun success(message: String?, apiName: String?, content: JsonObject?, view: View?) {
        binding.progressBar.hide()
        if (content?.has("data") == true) {
            articleDetails = Gson().fromJson(content.get("data").asJsonObject,Article::class.java)
            articleDetails?.fetched_at = System.currentTimeMillis()
            setArticleData()
        }
    }

    override fun error(message: String?, apiName: String?) {
        binding.progressBar.hide()
        message?.let { toastS(it) }
        finish()
    }
}