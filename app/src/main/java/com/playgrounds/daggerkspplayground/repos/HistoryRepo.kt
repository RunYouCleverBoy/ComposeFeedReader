package com.playgrounds.daggerkspplayground.repos

import kotlinx.coroutines.flow.StateFlow

interface HistoryRepo {
    data class HistoryItem(val lastSurfedName: String)
    val historyItems: StateFlow<HistoryItem>
    fun setLastSurfedName(name: String)
}