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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.text.toSpannable
import com.playgrounds.daggerkspplayground.R
import com.playgrounds.daggerkspplayground.repos.FeedRepo
import com.playgrounds.daggerkspplayground.ui.utils.detectDirection

@Composable
fun FeedScreen(
    isLoading: Boolean,
    tabs: List<TabDescriptor>,
    selectedTabIndex: Int = 0,
    feedData: List<FeedRepo.FeedItem>,
    onTabSelected: (TabDescriptor) -> Unit,
    onItemClicked: (FeedRepo.FeedItem) -> Unit,
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
fun FeedCell(feedItem: FeedRepo.FeedItem, onItemClicked: (FeedRepo.FeedItem) -> Unit) {
    val direction: LayoutDirection by remember { derivedStateOf { feedItem.title.detectDirection } }
    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked(feedItem)
            }) {
            Text(text = feedItem.title, modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium.copy(textDirection = TextDirection.Content))
            Text(text = feedItem.description.toString(), modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.bodyMedium.copy(textDirection = TextDirection.Content))
        }
    }
}


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun FeedCellPreview() {
    FeedCell(
        feedItem = FeedRepo.FeedItem("Title", "Link", "ולהלן הכותרות".toSpannable()),
        onItemClicked = {}
    )
}


@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun FeedScreenPreview() {
    FeedScreen(
        isLoading = false,
        tabs = listOf(TabDescriptor("Tab 1", R.string.sports), TabDescriptor("Tab 2", R.string.news)),
        feedData = listOf(
            FeedRepo.FeedItem("Title 1", "Link 1", "Description 1".toSpannable()),
            FeedRepo.FeedItem("עוד יום", "Link 2", "הפינק פלויד לא יופיעו על החומה בירושלים".toSpannable()),
            FeedRepo.FeedItem("Title 3", "Link 3", "Description 3".toSpannable()),
        ),
        onTabSelected = {},
        onItemClicked = {},
    )
}