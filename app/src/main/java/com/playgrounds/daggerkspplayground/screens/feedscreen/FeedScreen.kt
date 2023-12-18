package com.playgrounds.daggerkspplayground.screens.feedscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.text.toSpannable
import com.playgrounds.daggerkspplayground.R
import com.playgrounds.daggerkspplayground.repos.FeedRepo

@Composable
fun FeedScreen(
    isLoading: Boolean,
    tabs: List<TabDescriptor>,
    selectedTabIndex: Int = 0,
    feedData: List<FeedRepo.RssItem>,
    onTabSelected: (TabDescriptor) -> Unit,
    onItemClicked: (FeedRepo.RssItem) -> Unit,
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEach { tab ->
                    Tab(
                        selected = false,
                        onClick = { onTabSelected(tab) },
                        text = { Text(text = stringResource(id = tab.nameRes)) }
                    )
                }
            }
            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(Modifier.padding(16.dp)) {
                    items(feedData.size) { i ->
                        FeedCell(feedData[i], onItemClicked)
                    }
                }
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
fun FeedCell(rssFeed: FeedRepo.RssItem, onItemClicked: (FeedRepo.RssItem) -> Unit) {
    Column(modifier = Modifier.clickable {
        onItemClicked(rssFeed)
    }) {
        Text(text = rssFeed.title, style = TextStyle(textDirection = TextDirection.Content))
        Text(text = rssFeed.description.toString(), style = TextStyle(textDirection = TextDirection.Content))
    }
}


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen(
        isLoading = false,
        tabs = listOf(TabDescriptor("Tab 1", R.string.sports), TabDescriptor("Tab 2", R.string.news)),
        feedData = listOf(
            FeedRepo.RssItem("Title 1", "Link 1", "Description 1".toSpannable()),
            FeedRepo.RssItem("Title 2", "Link 2", "Description 2".toSpannable()),
            FeedRepo.RssItem("Title 3", "Link 3", "Description 3".toSpannable()),
        ),
        onTabSelected = {},
        onItemClicked = {},
    )
}