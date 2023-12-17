package com.playgrounds.daggerkspplayground.mainscreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.playgrounds.daggerkspplayground.screens.feedscreen.FeedAction
import com.playgrounds.daggerkspplayground.screens.feedscreen.FeedEvent
import com.playgrounds.daggerkspplayground.screens.feedscreen.FeedScreen
import com.playgrounds.daggerkspplayground.screens.feedscreen.FeedViewModel
import com.playgrounds.daggerkspplayground.screens.primaryscreen.PrimaryAction
import com.playgrounds.daggerkspplayground.screens.primaryscreen.PrimaryEvent
import com.playgrounds.daggerkspplayground.screens.primaryscreen.PrimaryScreen
import com.playgrounds.daggerkspplayground.screens.primaryscreen.PrimaryViewModel

@Composable
fun MainNavigation(navController: NavHostController, onActivityRequest: (MainAction) -> Unit) {
    NavHost(navController = navController, startDestination = "main") {
        composable("tabs") {
            val viewModel: FeedViewModel = hiltViewModel()
            val state by viewModel.stateFlow.collectAsState()
            LaunchedEffect(key1 = Unit) {
                viewModel.actionFlow.collect{
                    when (it) {
                        is FeedAction.OpenUrl -> onActivityRequest(MainAction.OpenUrl(it.url))
                    }
                }
            }
            FeedScreen(
                isLoading = state.isLoading,
                tabs = state.tabs,
                selectedTabIndex = state.selectedTabIndex,
                feedData = state.items,
                onTabSelected = { viewModel.dispatchEvent(FeedEvent.OnTabOpened(it.id)) }
            ) { viewModel.dispatchEvent(FeedEvent.OnItemClicked(it)) }
        }
        composable("main") {
            val viewModel: PrimaryViewModel = hiltViewModel()
            LaunchedEffect(key1 = Unit) {
                viewModel.actionFlow.collect{
                    when (it) {
                        is PrimaryAction.NavigateTo -> navController.navigate(it.path)
                    }
                }
            }
            PrimaryScreen(
                lastSurfData = viewModel.stateFlow.collectAsState().value.lastSurfData,
                onSurfDataClicked = { viewModel.dispatchEvent(PrimaryEvent.OnSurfDataClicked) }
            )
        }
    }
}

sealed class MainAction {
    data class OpenUrl(val url: String): MainAction()
}

