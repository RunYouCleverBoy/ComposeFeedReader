package com.playgrounds.daggerkspplayground.screens.feedscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column (modifier = Modifier.fillMaxSize()){
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEach { tab ->
                        Tab (
                            selected = false,
                            onClick = { onTabSelected(tab) },
                            text = { Text(text = stringResource(id = tab.nameRes)) }
                        )
                    }
                }
                LazyColumn {
                    items(feedData.size) { i ->
                        FeedCell(feedData[i], onItemClicked)
                    }
                }
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
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
