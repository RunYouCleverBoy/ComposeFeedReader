package com.playgrounds.daggerkspplayground.screens.feedscreen

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.playgrounds.daggerkspplayground.repos.FeedRepo
import com.playgrounds.daggerkspplayground.repos.HistoryRepo
import com.playgrounds.daggerkspplayground.repos.TabRepo
import com.playgrounds.daggerkspplayground.templates.MVIViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepo: FeedRepo,
    private val tabsRepo: TabRepo,
    private val historyRepo: HistoryRepo
) : MVIViewModel<FeedState, FeedEvent, FeedAction>(FeedState()) {
    private var feedsPollJob: Job? = null

    init {
        viewModelScope.launch {
            tabsRepo.tabsFlow.collect { tabs ->
                mutableStateFlow.update { state ->
                    state.copy(tabs = tabs.map { tab ->
                        TabDescriptor(
                            tab.id,
                            tab.titleRes)
                    }, selectedTabIndex = 0)
                }
            }
        }
        viewModelScope.launch {
            dispatchEvent(FeedEvent.OnTabOpened(tabsRepo.tabsFlow.value.first().id))
        }
    }
    override fun dispatchEvent(event: FeedEvent) {
        when (event) {
            is FeedEvent.OnItemClicked -> openLink(event)
            is FeedEvent.OnTabOpened -> onTabOpened(event)
        }
    }

    private fun openLink(event: FeedEvent.OnItemClicked) {
        historyRepo.setLastSurfedName(event.item.title)
        emitAction(FeedAction.OpenUrl(event.item.link))
    }

    private fun onTabOpened(event: FeedEvent.OnTabOpened) {
        startPollingTab(event.tabId)
        mutableStateFlow.update { state ->
            state.copy(selectedTabIndex = state.tabs.indexOfFirst { it.id == event.tabId }
                .takeIf { it >= 0 } ?: 0)
        }
    }

    private fun startPollingTab(tabId: String) {
        feedsPollJob?.takeIf { it.isActive }?.cancel()
        feedsPollJob = null
        feedsPollJob = viewModelScope.launch {
            val tab = tabsRepo.tabsFlow.value.find { it.id == tabId } ?: return@launch
            var loadingCount = 0
            for (url in tab.urls) {
                launch {
                    while (true) {
                        loadingCount++
                        mutableStateFlow.update { it.copy(isLoading = true) }
                        feedRepo.fetchFeed(url, 5.seconds)
                        loadingCount--
                        mutableStateFlow.update { it.copy(isLoading = loadingCount > 0) }
                        delay(3.seconds)
                    }
                }
            }
            feedRepo.stateFlow.collect { data ->
                val items = tab.urls.flatMap { data[it]?.items ?: emptyList() }
                mutableStateFlow.update { it.copy(items = items, isLoading = loadingCount > 0) }
            }
        }
    }
}

data class FeedState(
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val tabs: List<TabDescriptor> = emptyList(), val items: List<FeedRepo.RssItem> = emptyList()
)

data class TabDescriptor(val id: String, @StringRes val nameRes: Int)

sealed class FeedEvent {
    data class OnItemClicked(val item: FeedRepo.RssItem) : FeedEvent()
    data class OnTabOpened(val tabId: String) : FeedEvent()
}

sealed class FeedAction {
    data class OpenUrl(val url: String) : FeedAction()
}