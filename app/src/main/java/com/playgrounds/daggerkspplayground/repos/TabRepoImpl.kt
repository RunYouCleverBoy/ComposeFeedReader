package com.playgrounds.daggerkspplayground.repos

import com.playgrounds.daggerkspplayground.R
import com.playgrounds.daggerkspplayground.repos.TabRepo.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TabRepoImpl @Inject constructor() : TabRepo {
    private val _tabsFlow = MutableStateFlow<List<Tab>>(emptyList())
    override val tabsFlow: StateFlow<List<Tab>> = _tabsFlow

    init {
        _tabsFlow.update { tabs ->
            tabs + listOf(
                Tab("1", R.string.news, listOf("https://www.ynet.co.il/Integration/StoryRss2.xml")),
                Tab("2", R.string.breaking_news, listOf("https://www.ynet.co.il/Integration/StoryRss1854.xml")),
                Tab(
                    "3", R.string.sports, listOf(
                        "https://www.ynet.co.il/Integration/StoryRss3.xml",
                        "https://www.ynet.co.il/Integration/StoryRss57.xml"
                    )
                ),
            )
        }
    }

    override suspend fun addTab(tab: Tab) {
        _tabsFlow.update { tabs ->
            tabs + tab
        }
    }
}