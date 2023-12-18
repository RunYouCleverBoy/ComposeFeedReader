package com.playgrounds.daggerkspplayground.repos

import android.text.Spanned
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface FeedRepo {
    data class Feed(val title: String, val items: List<FeedItem>, val timeStamp: Long)
    data class FeedItem(val title: String, val link: String, val description: Spanned)

    val stateFlow: StateFlow<Map<String, Feed>>

    suspend fun fetchFeed(url: String, expiration: Duration = 5.seconds): Result<Feed>
}