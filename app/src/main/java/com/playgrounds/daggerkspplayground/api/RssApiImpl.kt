package com.playgrounds.daggerkspplayground.api

import com.prof18.rssparser.RssParser
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import javax.inject.Inject


class RssApiImpl @Inject constructor(
    private val rssParser: RssParser,
    private val ktorClient: HttpClient
) : RssApi {
    override suspend fun getFeed(url: String): Result<RssApi.RssFeed> {
        val response: HttpResponse = ktorClient.get(url)
        return if (response.status.isSuccess()) {
            val bodyStr = response.bodyAsText()
            val rssFeed = rssParser.parse(bodyStr)
            val rssItems = rssFeed.items.map { rssItem ->
                RssApi.RssItem(
                    title = rssItem.title ?: "",
                    link = rssItem.link ?: "",
                    description = rssItem.description ?: ""
                )
            }
            Result.success(RssApi.RssFeed(rssFeed.title ?: "", rssItems))
        } else {
            Result.failure(Exception("Error fetching RSS feed"))
        }
    }
}