package com.playgrounds.daggerkspplayground.repos

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.StateFlow

interface TabRepo {
    data class Tab(val id: String, @StringRes val titleRes: Int, val urls: List<String>)
    val tabsFlow: StateFlow<List<Tab>>

    suspend fun addTab(tab: Tab)
}