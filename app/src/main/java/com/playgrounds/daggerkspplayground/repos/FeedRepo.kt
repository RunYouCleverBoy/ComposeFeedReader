package com.playgrounds.daggerkspplayground.repos

import android.text.Spanned
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface FeedRepo {
    data class RssFeed(val title: String, val items: List<RssItem>, val timeStamp: Long)
    data class RssItem(val title: String, val link: String, val description: Spanned)

    val stateFlow: StateFlow<Map<String, RssFeed>>

    suspend fun fetchFeed(url: String, expiration: Duration = 5.seconds): Result<RssFeed>
}