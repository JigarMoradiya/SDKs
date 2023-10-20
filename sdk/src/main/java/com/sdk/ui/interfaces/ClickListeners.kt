package com.sdk.ui.interfaces

import com.sdk.data.Article
import com.sdk.data.HealingClip
import com.sdk.data.Podcast

interface PodcastClickListener {
    fun onPodcastClicked(position: Int, podcast: Podcast)
}
interface ArticleClickListener {
    fun onArticleClicked(position: Int, article: Article)
}
interface ClipClickListener {
    fun onClipClicked(position: Int, clip: HealingClip)
}