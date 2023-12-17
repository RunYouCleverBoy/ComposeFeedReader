package com.playgrounds.daggerkspplayground.repos

import androidx.core.text.HtmlCompat
import com.playgrounds.daggerkspplayground.api.RssApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Singleton
class FeedRepoImpl @Inject constructor(
    private val rssApi: RssApi
): FeedRepo {
    private val _data = MutableStateFlow(emptyMap<String, FeedRepo.RssFeed>())
    override val stateFlow: StateFlow<Map<String, FeedRepo.RssFeed>> = _data

    override suspend fun fetchFeed(url: String, expiration: Duration): Result<FeedRepo.RssFeed> {
        val cachedFeed = _data.value[url]?.takeIf { it.age() < expiration }
        if (cachedFeed != null) {
            return Result.success(cachedFeed)
        }

        val result = rssApi.getFeed(url)
        return if (result.isSuccess) {
            val rssFeed = result.getOrThrow()
            val rssItems = rssFeed.items.map { rssItem ->
                FeedRepo.RssItem(
                    title = rssItem.title,
                    link = rssItem.link,
                    description = HtmlCompat.fromHtml(rssItem.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                )
            }
            val feed = FeedRepo.RssFeed(rssFeed.title, rssItems, System.currentTimeMillis())
            _data.update { it + (url to feed) }
            Result.success(feed)
        } else {
            Result.failure(result.exceptionOrNull() ?: Exception("Error fetching RSS feed"))
        }
    }

    private fun FeedRepo.RssFeed.age(): Duration {
        return (System.currentTimeMillis() - timeStamp).milliseconds
    }
}