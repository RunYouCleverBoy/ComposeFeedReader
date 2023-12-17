package com.playgrounds.daggerkspplayground.api

interface RssApi {
    data class RssFeed(val title: String, val items: List<RssItem>)
    data class RssItem(val title: String, val link: String, val description: String)
    suspend fun getFeed(url: String): Result<RssFeed>
}